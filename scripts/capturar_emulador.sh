#!/usr/bin/env bash
set -euo pipefail
# O Gradle desinstala os APKs no fim da suíte e apaga a pasta externa do app.
# Executa novamente apenas o cenário de evidências por adb e coleta antes da limpeza.
adb install -r app/build/outputs/apk/debug/app-debug.apk
adb install -r app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk
adb shell am instrument -w -r -e class carreiras.com.github.todolist.ui.EvidenciasExclusaoTest carreiras.com.github.todolist.test/androidx.test.runner.AndroidJUnitRunner | tee /tmp/cp5-evidencias-teste.log
python3 - <<'CHECK'
from pathlib import Path
log = Path('/tmp/cp5-evidencias-teste.log').read_text()
assert 'OK (1 test)' in log, 'O teste de evidências não confirmou sucesso.'
assert 'FAILURES!!!' not in log, 'O teste de evidências falhou.'
CHECK
mkdir -p docs/images/exclusao
adb pull /sdcard/Android/data/carreiras.com.github.todolist/files/evidencias/. docs/images/exclusao/
