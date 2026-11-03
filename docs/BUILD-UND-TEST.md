# Build und Test

## Tatsächlich ausgeführter Build und Gerätetest

Die ausgelieferte APK wurde am 3. November 2026 in GitHub Actions auf `ubuntu-latest` gebaut, auf einem Android-Emulator installiert und mit einem Instrumentation-Test geprüft. Die Ergebnisse liegen im Repository.

| Nachweis | Datei / Lauf |
| --- | --- |
| Debug-APK | `apk/EinfachStart91.apk` |
| Erfolgreicher Build-Lauf | `41723808804` |
| Erfolgreiche Emulator-Läufe | `41728410475`, Finalcheck `41730536087` |
| Badging, Manifest, Hash, Berechtigungen | `build-report/apk-info.txt`, `badging.txt`, `manifest.txt` |
| Installations- und Instrumentationsprotokoll | `test-report/emulator-test.txt`, `emulator-test-output.txt`, `emulator-run.log`, `sdk-install.log` |

Der Build-Workflow hat `assembleDebug` ausgeführt und anschließend geprüft:

- `android.intent.action.MAIN` und `android.intent.category.HOME` sind im Manifest vorhanden,
- **keine** `android.permission.INTERNET` ist in der APK enthalten,
- die APK ist 24.587.577 Byte groß; SHA-256 siehe `build-report/apk-info.txt`.

Der Emulator-Workflow hat `assembleDebug` und `assembleAndroidTest` ausgeführt. Danach:

- Android 15, API 35, `google_apis`, x86_64, AVD mit `pixel_8`-Geräteprofil und KVM,
- `libpulse0` für den Emulator installiert, `ANDROID_AVD_HOME` auf `$HOME/.android/avd` fixiert,
- Emulator direkt gestartet und `sys.boot_completed=1` abgewartet,
- App-APK und Test-APK mit `adb install -r` installiert – jeweils `Success`,
- `LauncherInstrumentedTest` mit `adb shell am instrument` ausgeführt: **`OK (1 test)`**,
- geprüft, dass Titel, „MEINE LIEBSTEN“, TELEFON, NACHRICHTEN und die Notfalltaste sichtbar sind.

## Lokal mit Android Studio bauen

1. **Android Studio Ladybug oder neuer** installieren.
2. JDK 17 einstellen.
3. Im SDK Manager installieren: **Android SDK Platform 35**, **Build-Tools 35.0.0**, optional einen Emulator.
4. Projekt öffnen; Gradle synchronisieren.
5. **Build → Generate App Bundles / APKs → Generate APKs** oder:

```bash
./gradlew assembleDebug
./gradlew assembleAndroidTest
```

6. Ergebnis: `app/build/outputs/apk/debug/app-debug.apk`.

## Lokal auf Emulator testen

```bash
./gradlew connectedDebugAndroidTest
```

Alternativ manuell:

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
adb install -r app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk
adb shell am instrument -w -r -e debug false \
  de.einfachstart91.launcher.test/androidx.test.runner.AndroidJUnitRunner
```

## Versionen

| Komponente | Version |
| --- | --- |
| Android Gradle Plugin | 8.7.3 |
| Gradle Wrapper | 8.9 |
| Kotlin | 2.0.21 |
| Compose Compiler Plugin | 2.0.21 |
| Compose BOM | 2024.12.01 |
| Activity Compose | 1.9.3 |
| Core KTX | 1.13.1 |
| compileSdk / targetSdk | 35 |
| minSdk | 29 |

## Signatur

`debug` und `release` verwenden in diesem Auslieferungsprojekt denselben lokalen Debug-Schlüssel, damit `apk/EinfachStart91.apk` sofort installierbar ist. Für eine Produktionsverteilung einen eigenen Keystore hinterlegen und die Release-Signatur in `app/build.gradle.kts` konfigurieren.

## Troubleshooting

- **„SDK location not found“:** `local.properties` mit `sdk.dir=/pfad/zum/Android/sdk` anlegen oder `ANDROID_HOME` setzen.
- **Gradle-Download schlägt fehl:** `gradle/wrapper/gradle-wrapper.properties` zeigt auf `services.gradle.org`; im Firmennetz ggf. Proxy oder lokale Distribution verwenden.
- **Emulator startet nicht:** Auf Linux KVM/Hardwarebeschleunigung prüfen. Der CI-Workflow installiert `libpulse0`, setzt `ANDROID_AVD_HOME` und startet `$ANDROID_HOME/emulator/emulator` direkt.
- **Test findet Text nicht:** Schriftgrößen-Faktor oder `fontScale` prüfen; der Test sucht exakt `EINFACHSTART 91` und `NOTFALL – HILFE RUFEN`.
