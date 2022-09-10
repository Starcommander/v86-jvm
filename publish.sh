#!/bin/bash

set -e

type scp
type zstd

SSH_URL="paul@185.164.4.149"
SSH_CMD="ssh $SSH_URL"

APP_PATH="/var/www/html/v86/"
BUILD_DIR="v86/tools/docker/exec/mount/v86/"

if [ -f "tiny-core/v86state.bin" ]; then
  zstd tiny-core/v86state.bin
  rm tiny-core/v86state.bin
elif [ ! -f "tiny-core/v86state.bin.zst" ]; then
  echo "No v86state.bin state file, place it under ./tiny-core/v86state.bin"
fi

echo "Packing data..."
tar -czf /tmp/v86.tgz tiny-core/ "$BUILD_DIR"
scp /tmp/v86.tgz $SSH_URL:/tmp/v86.tgz
$SSH_CMD tar -xf /tmp/v86.tgz -C /tmp
$SSH_CMD rm -r "$APP_PATH"*
$SSH_CMD mv /tmp/tiny-core "$APP_PATH/images"
$SSH_CMD mv "/tmp/$BUILD_DIR"* "$APP_PATH"
echo "Finish."
