# Test-Bericht

Erzeugt im erfolgreichen GitHub-Actions-Lauf `41728410475` auf Android 15 / API 35, `google_apis`, x86_64, Pixel-8-Geräteprofil.

- `emulator-test.txt`: zusammengefasster Bericht mit Job-Status, Installations- und Testergebnis.
- `emulator-test-output.txt`: unveränderte Ausgabe von `adb shell am instrument`.
- `emulator-run.log`: AVD-Anlage, Boot, `adb install`, Instrumentation.
- `gradle-build.log`: Ausgabe von `assembleDebug` und `assembleAndroidTest`.

Ergebnis: beide APKs installiert (`Success`), `OK (1 test)`.
