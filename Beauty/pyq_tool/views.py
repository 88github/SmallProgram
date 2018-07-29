# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.shortcuts import render
from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt
from .models import Tempinfo
from dss.Serializer import serializer
from django.views.generic import ListView
from dss.Mixin import MultipleJsonResponseMixin
from PIL import Image,ImageDraw,ImageFont
import datetime
import os

# Create your views here.
# 获取模板列表
class GetTempList(MultipleJsonResponseMixin,ListView):
    model = Tempinfo
    queryset = Tempinfo.objects.all()
    paginate_by = 2

@csrf_exempt
def get_temp_detail(request):
    if request.method == 'POST':
        id = request.POST.get('tid', '')
        if id is not '':
            try:
                detail = Tempinfo.objects.get(id=id)
                detail = serializer(detail, datetime_format='string')
                return JsonResponse({'sucess': True, 'data': detail})
            except Exception as e:
                return JsonResponse({'success': False, 'data': str(e)})
        else:
            return JsonResponse({'success': False, 'data': '没有数据'})

@csrf_exempt
def generate_photo(request):
    if request.method == 'POST':
        id = request.POST.get('tid', '')
        content = request.POST.get('content', '')
        if id != '' and content != '':
            try:
                temp = Tempinfo.objects.get(id=id)
                fontpath = os.path.join(BASE_DIR, 'media/pyq_font/{font}.ttf'.format(font=temp.font))
                ttfont = ImageFont.truetype(fontpath, int(temp.fontsize))
                # 图片大小
                imgsize = temp.imgsize.split(",")
                try:
                    bg = Image.new("RGB", (int(imgsize[0]), int(imgsize[1])))
                except Exception as e:
                    return JsonResponse({'success': False, 'data': '图片大小出错：' + str(e)})
                im = Image.open(temp.img)
                draw2 = Image.blend(bg, im, 1.0)
                draw = ImageDraw.Draw(draw2)
                try:
                    textplace = temp.textplace.split(",")
                    textcolor = temp.textcolor.split(",")
                    draw.text((int(textplace[0]), int(textplace[1])), content,
                              fill=(int(textcolor[0]), int(textcolor[1]), int(textcolor[2])), font=ttfont)
                except Exception as e:
                    return JsonResponse({'success': False, 'data': '文字颜色位置出错：' + str(e)})
                if temp.text2 != '':
                    text2place = temp.text2place.split(",")
                    draw.text((int(text2place[0]), int(text2place[1])),
                              datetime.date.strftime(datetime.date.today(), "%Y-%m-%d"),
                              fill=(int(textcolor[0]), int(textcolor[1]), int(textcolor[2])),
                              font=ttfont)
                filename = str(datetime.datetime.today()).replace(':', '-').replace(' ', '-').replace('.', '')
                photoname = os.path.join(BASE_DIR, 'media/pyq_photo/{0}.jpg'.format(filename))
                draw2.save(photoname)
                return JsonResponse({'success': True, 'data': 'media/pyq_photo/{0}.jpg'.format(filename)})
            except Exception as e:
                return JsonResponse({'success': False, 'data': str(e)})
        else:
            return JsonResponse({'success': False, 'data': '不能为空'})