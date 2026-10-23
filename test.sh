#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "$0")" && pwd)"
OUT="$ROOT/build/tests"
mkdir -p "$OUT"
javac -source 8 -target 8 -d "$OUT" \
  "$ROOT/src/com/jeremykenedy/canopydrift/CanopyOptions.java" \
  "$ROOT/src/com/jeremykenedy/canopydrift/SettingsValues.java" \
  "$ROOT/tests/CanopyOptionsTest.java"
java -ea -cp "$OUT" com.jeremykenedy.canopydrift.CanopyOptionsTest
javac -source 8 -target 8 -cp "$OUT" -d "$OUT" "$ROOT/tests/ClockVisibilitySettingsTest.java"
java -ea -cp "$OUT" com.jeremykenedy.canopydrift.ClockVisibilitySettingsTest
python3 -m unittest -v tests.test_installer
