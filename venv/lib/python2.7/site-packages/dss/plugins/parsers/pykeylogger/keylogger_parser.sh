#!/usr/bin/env bash
filename="$1"
output_path="$2"
clicks_dir="$3"

mkdir -p plugins/collectors/pykeylogger/parsed
java -cp plugins/parsers/pykeylogger KeysToJSON ${filename} ${output_path}
java -cp plugins/parsers/pykeylogger ClicksToJSON ${clicks_dir} ${output_path}