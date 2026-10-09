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
python3 -m unittest -v tests.test_installer
