#!/usr/bin/env bash
pcap_filename="$1"
output_path="$2"

mkdir -p plugins/collectors/tshark/parsed
java -cp plugins/parsers/tshark NetworkDataParser ${pcap_filename} ${output_path}