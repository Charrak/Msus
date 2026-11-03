# Test-Bericht

Emulator-Tests auf Android 15 / API 35, `google_apis`, x86_64, Pixel-8-Geräteprofil.

- `emulator-test.txt`: zusammengefasster Bericht mit Job-Status, Installations- und Testergebnis.
- `emulator-test-output.txt`: unveränderte Ausgabe von `adb shell am instrument`.
- `emulator-run.log`: AVD-Anlage, Boot, `adb install`, Instrumentation.
- `gradle-build.log`: Ausgabe von `assembleDebug` und `assembleAndroidTest`.
- `sdk-install.log`: Ausgabe der SDK-/System-Image-Installation.

Getestet werden:

1. `startbildschirmZeigtAlleWichtigenElemente` – Titel, „MEINE LIEBSTEN“, TELEFON, NACHRICHTEN, KONTAKTE und Notfalltaste.
2. `kontaktSucheZeigtAlleKontakteAn` – die **KONTAKTE**-Taste öffnet die durchsuchbare Kontaktliste mit dem Feld „ALLE KONTAKTE DURCHSUCHEN“.

Für die Tests werden `CALL_PHONE` und `READ_CONTACTS` per `GrantPermissionRule` erteilt.
