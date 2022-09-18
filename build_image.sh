#!/bin/bash

set -e

LINK_TCL="http://www.tinycorelinux.net/13.x/x86/release/TinyCore-current.iso"
LINK_JRE="https://cdn.azul.com/zulu/bin/zulu18.32.11-ca-jre18.0.2-linux_i686.zip"

OFFS=1048576 # 2048(bytes) * 512(sectorsize)
OVER="tiny-core/overlay"

get_data() # Args: relPath/tarDirName
{
  local cur_dir=$(pwd)
  local tmpd=$(mktemp -d)
  local tmp_jre=$(basename "$LINK_JRE")
  cd "$tmpd"
  if [ -f "/tmp/$tmp_jre" ]; then
    cp "/tmp/$tmp_jre" .
  else
    wget "$LINK_JRE"
    cp "$tmp_jre" "/tmp/$tmp_jre"
  fi
  unzip *.zip
  rm *.zip
  mv * "$cur_dir/$1/jre"
  cd "$cur_dir"
  rm -r "$tmpd"
}

type unzip
type wget
type /sbin/fdisk
type /sbin/mkfs.ext2

mkdir -p tiny-core
cd tiny-core
if [ ! -f "TinyCore-current.iso" ]; then
  wget "$LINK_TCL" --output-document=TinyCore-current.iso
fi

echo "Cleaning up..."
sudo umount tiny.mount || echo "Unmount not necessary."
echo -n "" > tiny.img

echo "Creating tiny.img..."
dd if=/dev/zero of=tiny.img bs=1024 count=$((1024 * 600)) #600MB
echo -e "o\nn\n\n\n\n\nw\n" | /sbin/fdisk tiny.img #New dos, new part.
echo "Formating partition..."
yes | /sbin/mkfs.ext2 -E "offset=$OFFS" tiny.img
mkdir -p tiny.mount
echo "Mounting image..."
sudo mount -o loop,offset=$OFFS,user tiny.img tiny.mount/
echo "Copy and get data..."
sudo cp -r ../$OVER/* tiny.mount/
sudo mkdir tiny.mount/data
sudo chown 1000.1000 tiny.mount/data
get_data tiny.mount/data
sudo chown 1000.1000 tiny.mount/*
sudo umount tiny.mount/
rmdir tiny.mount/
echo "Info: If size of tiny.img has changed, also update browser/main.js"
echo "Finish: tiny-core/tiny.img"
