#!/bin/bash

set -e

if [ -z "$1" ]; then
  echo "Error, use project-name as argument."
  exit 1
fi

PROJ=$(echo $1 | tr -d '/')

if [ ! -d "$PROJ" ]; then
  echo "Error: Argument is no example project name: $PROJ"
  exit 1
fi

type javac
type jar

#TMP_D=$(mktemp -d)

#cp -r "$PROJ" "$TMP_D"
cd "$PROJ"
javac -version
find . -name "*.java" -print0 | xargs -0 javac
#mkdir ../META_INF
#echo "Manifest-Version: 1.0" > ../META_INF/MANIFEST.MF
#echo "Main-Class: $PROJ" >> ../META_INF/MANIFEST.MF
find . -name "*.class" -print0 | xargs -0 jar -c --file="$PROJ".jar --main-class="$PROJ"
find . -name "*.class" -print0 | xargs -0 rm
echo "Jar created at $(pwd)/$PROJ.jar"

#cd "$PROJ"
#javac *.java

