#!/bin/sh

TTYDEV="/dev/ttyS0"
MNT_DIR="/mnt/sda1"

start_in()
{
  local state=""
  cat $TTYDEV | while read line
  do
    local is_new_state=$(echo "$line" | grep "^<.*>\$") # Starts with < and ends with >
    if [ "$line" = "<end>" ]; then
      state=""
      continue
    elif [ ! -z "$is_new_state" ]; then
      state=$line
      continue
    fi
    if [ "$state" = "<execute>" ]; then
      #$line | sed -e 's#^\\#\\\\#g' | sed -e 's#^<#\\<#g' > $TTYDEV # Replacing \ and <
      $line > $TTYDEV
    fi
  done
}

alias java="$MNT_DIR/data/jre/bin/java"
#sudo cp -r "$MNT_DIR/data/tc/usr/local/lib"/* "$MNT_DIR/data/jre/lib/"

sudo sh -c "echo 1 > /proc/sys/fs/pipe-max-size" # Disable buffering of pipe, used in loop of start_in().
start_in &
