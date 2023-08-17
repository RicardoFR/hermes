#!/bin/bash
WLAN=$1
sudo ip link set $WLAN down
sudo iw $WLAN set monitor none
sudo ip link set $WLAN up
# See Status
sudo iw dev
