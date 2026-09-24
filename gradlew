#!/bin/sh
set -e
APP_HOME=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
GRADLE_VERSION=8.7
GRADLE_HOME="$HOME/.gradle/wrapper/dists/gradle-$GRADLE_VERSION-bin"
GRADLE_DIR="$GRADLE_HOME/gradle-$GRADLE_VERSION"
GRADLE_ZIP="$GRADLE_HOME/gradle-$GRADLE_VERSION-bin.zip"
mkdir -p "$GRADLE_HOME"
if [ ! -x "$GRADLE_DIR/bin/gradle" ]; then
  if [ ! -f "$GRADLE_ZIP" ]; then
    curl -fsSL "https://services.gradle.org/distributions/gradle-$GRADLE_VERSION-bin.zip" -o "$GRADLE_ZIP"
  fi
  rm -rf "$GRADLE_HOME/gradle-$GRADLE_VERSION"
  unzip -q "$GRADLE_ZIP" -d "$GRADLE_HOME"
fi
exec "$GRADLE_DIR/bin/gradle" "$@"
