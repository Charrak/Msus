# APK

`EinfachStart91.apk` ist die am 3. November 2026 in GitHub Actions gebaute, debug-signierte und direkt installierbare Datei.

- Größe: 24.587.577 Byte
- SHA-256: `f639ceed6d6b852583e7c0310d310b83ad9b2ca01167c23fb351c4a66f9c95df`
- Paket: `de.einfachstart91.launcher`, Version 1.0, `minSdk 29`, `targetSdk 35`
- Erfolgreich per `adb install -r` auf Android 15 / API 35 im Pixel-8-Emulatorprofil installiert; Instrumentation-Test: `OK (1 test)`.

Nachweise: `../build-report/apk-info.txt` und `../test-report/emulator-test.txt`.

## Senioren Launcher (extern, v0.8.8)

`SeniorenLauncher-v0.8.8-debug.apk` ist die am 3. November 2026 in GitHub Actions gebaute, debug-signierte und direkt installierbare APK des öffentlichen Projekts [cma58/senioren-launcher](https://github.com/cma58/senioren-launcher).

- Größe: 32.152.834 Byte
- SHA-256: `74963bf7b065e1d7f594584ca93f06bd7815cf1802df6f19d69615a7af120235`
- Paket: `com.seniorenlauncher`, Version 0.8.8 (versionCode 15), `minSdk 26`, `targetSdk 36`, `compileSdk 36`
- Build-Commit: `eab0bf21a85f43b8b15ad8a7f99d90c64f6e4282` (Stand `main`)
- GitHub-Actions-Lauf: `41736409319` (Build erfolgreich in 5m 20s)
- Launcher-Nachweis: `MAIN` + `HOME` im Manifest gefunden
- Signatur: Debug-Signatur (wie im Build-Workflow des Projekts vorgesehen)

Wichtige Berechtigungen: SMS (Senden/Empfangen), Anrufe, Kontakte, Standort, Kamera, Kalender, Benachrichtigungen, Akku-/Stromsparfunktionen sowie `QUERY_ALL_PACKAGES`. Die App ist für die Fernverwaltung per SMS und Notfallfunktionen ausgelegt; die Berechtigungen sollten vor der Installation bewusst geprüft werden.

Nachweise: `../build-report/senioren-apk-info.txt`, `../build-report/senioren-badging.txt`, `../build-report/senioren-manifest.txt` und `../build-report/senioren-gradle-build.log`.
