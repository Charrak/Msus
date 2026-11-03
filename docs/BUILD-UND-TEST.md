# Build und Test

## Was tatsächlich gebaut und geprüft wird

Die Sandbox dieses Auftrags hat keinen direkten Zugriff auf `dl.google.com`, Maven Central oder einen lokalen Android-Emulator. Deshalb wird die APK im **GitHub-Actions-Runner** des Repositories gebaut. Der Runner hat Java, das Android-SDK und Internetzugriff auf die Maven-Repositories. Er schreibt die fertige APK und die Prüfberichte automatisch auf denselben Branch zurück.

|Nachweis | Datei |
| --- | --- |
| Gebaute APK | `apk/EinfachStart91.apk` |
| Badging, Manifest, Hash, Berechtigungen | `build-report/apk-info.txt`, `badging.txt`, `manifest.txt` |
| Emulator-Test | `test-report/emulator-test.txt` |

Der Build-Workflow prüft automatisiert:

- `android.intent.action.MAIN` und `android.intent.category.HOME` im Manifest,
- **keine** `android.permission.INTERNET`,
- erfolgreiche `assembleDebug`.

Der Emulator-Workflow:

- baut `assembleDebug` und `assembleAndroidTest`,
- startet einen Android-14-Emulator (API 34, `google_apis`, `x86_64`, Pixel-8-Profil) mit KVM,
- installiert beide APKs per `adb`,
- führt `LauncherInstrumentedTest` aus,
- prüft, dass Titel, „MEINE LIEBSTEN“, TELEFON, NACHRICHTEN und die Notfalltaste sichtbar sind,
- schreibt die Instrumentation-Ausgabe in `test-report/emulator-test.txt`.

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
- **Emulator startet nicht:** KVM/Linux bzw. Hardwarebeschleunigung prüfen; im CI übernimmt `reactivecircus/android-emulator-runner` die Einrichtung.
- **Test findet Text nicht:** Schriftgrößen-Faktor oder `fontScale` prüfen; der Test sucht exakt `EINFACHSTART 91` und `NOTFALL – HILFE RUFEN`.
