#!/usr/bin/env bash
clicks_dir="$1"
output_path="$2"

mkdir -p plugins/collectors/manualscreenshot/parsed
java -cp plugins/parsers/manualscreenshot ClicksToJSON ${clicks_dir} ${output_path}