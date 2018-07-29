#!/usr/bin/env bash
pcap_filename="$1"
output_path="$2"

mkdir -p plugins/collectors/netscanner/parsed
java -cp plugins/parsers/tshark NetworkDataParser ${pcap_filename} ${output_path}