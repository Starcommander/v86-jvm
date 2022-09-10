#!/bin/bash

set -e

clone_patch_v86()
{
  git clone https://github.com/copy/v86.git
  echo "Apply changes"
  cd v86
#  git checkout cb1c3211e9d08679752ab72e38661ccecd29d5f2
  git apply ../v86.patch
  cd -
}

type git

if [ ! -d v86 ]; then
  clone_patch_v86
fi

cd v86/tools/docker/exec/
echo "Cleanup..."
mkdir -p mount
sudo rm -r -f mount/v86/
echo "Build Docker..."
./build.sh || true
echo "Build v86..."
sudo docker run -it --rm --mount type=bind,source=$(pwd)/mount,target=/fs_mount v86:alpine-3.14
echo "All build done"
