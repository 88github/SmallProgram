# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models

# Create your models here.
class Tempinfo(models.Model):
    name = models.CharField(max_length=20,verbose_name="模板名称")
    font = models.CharField(max_length=5,verbose_name='字体代码')
    fontsize = models.IntegerField(verbose_name="字体大小")
    img = models.ImageField(upload_to='pyq_tool',verbose_name="模板图片")
    imgsize = models.CharField(max_length=10,verbose_name='图片大小')
    icon = models.ImageField(upload_to='pyq_tool',verbose_name="图标")
    hint = models.CharField(max_length=10,verbose_name="输入提示")
    textcolor = models.CharField(max_length=15,verbose_name='文本颜色')
    textplace = models.CharField(max_length=10,verbose_name='文本位置')
    text2 = models.CharField(max_length=15,verbose_name='日期文本',null=True,blank=True)
    text2hint = models.CharField(max_length=10,verbose_name="文本2提示",null=True,blank=True)
    text2place = models.CharField(max_length=10,verbose_name='日期位置',null=True,blank=True)

    def __str__(self):
        return self.name

    class Meta:
        verbose_name = '票圈神器'
        verbose_name_plural = verbose_name