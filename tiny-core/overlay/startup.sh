#!/bin/sh

TTYDEV="/dev/ttyS0"
MNT_DIR="/mnt/sda1"

#start_in()
#{
#  local state=""
#  cat $TTYDEV | while read line
#  do
#    local is_new_state=$(echo "$line" | grep "^<.*>\$") # Starts with < and ends with >
#    if [ "$line" = "<end>" ]; then
#      state=""
#      continue
#    elif [ ! -z "$is_new_state" ]; then
#      state=$line
#      continue
#    fi
#    if [ "$state" = "<execute>" ]; then
#      #$line | sed -e 's#^\\#\\\\#g' | sed -e 's#^<#\\<#g' > $TTYDEV # Replacing \ and <
#      $line > $TTYDEV
#    fi
#  done
#}

if [ -z "$1" ]; then
  echo "Use as argument: up|run"
elif [ "$1" = "up" ]; then
  tce-load -wil Xorg-7.7-lib.tcz
  sudo ln -s "$MNT_DIR/data/jre/bin/java" /usr/bin/java
#alias java="$MNT_DIR/data/jre/bin/java"
#sudo cp -r "$MNT_DIR/data/tc/usr/local/lib"/* "$MNT_DIR/data/jre/lib/"

  sudo sh -c "echo 1 > /proc/sys/fs/pipe-max-size" # Disable buffering of pipe, used in loop of start_in().
kill $(pidof wbar) # Exit win-bar as not necessary.
sudo ifconfig eth0 down # Disable eth0 as we enable only if needed
clear
elif [ "$1" = "run" ]; then
#start_in &
#cd java && java Startup &
cd java && java Progress
else
  echo "Use as argument: up|run"
fi
