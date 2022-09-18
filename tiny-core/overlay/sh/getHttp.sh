#!/bin/sh

# Use args http://url/file.jar /tar/file.jar

sudo ifconfig eth0 up
sleep 1
/etc/init.d/dhcp.sh start

for i in 1 2 3 4 5 6 7 8 9 ; do
  wget -O "$2" "$1"
  if [ -f "$2" ]; then
    break
  else
    sleep 3
  fi
done
