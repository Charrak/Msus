# EinfachStart 91

**Ein ruhiger, sehr groß bedienbarer Android-Launcher für einen 91-jährigen Menschen – entwickelt für das Google Pixel 8.**

EinfachStart 91 ersetzt den normalen Startbildschirm. Alles Wichtige ist mit einer Berührung erreichbar: große Uhr, Datum, Akkustand, bis zu acht Lieblingskontakte mit Foto, sechs große Funktionstasten und eine deutlich sichtbare rote Notfalltaste. Es gibt keine Werbung, keine In-App-Käufe, keine Datensammlung und keine verschachtelten Menüs.

---

## Fertige APK

- [`apk/EinfachStart91.apk`](apk/EinfachStart91.apk) – nach dem Build-Workflow direkt installierbar (debug-signiert).
- Build-Nachweis: [`build-report/apk-info.txt`](build-report/apk-info.txt)
- Emulator-Test: [`test-report/emulator-test.txt`](test-report/emulator-test.txt)

Die APK wird in GitHub Actions mit Android Gradle Plugin 8.7.3, Kotlin 2.0.21, Jetpack Compose und `compileSdk 35` tatsächlich gebaut. Anschließend wird sie auf einem Android-14-Emulator (API 34, x86_64, Pixel-Profil) installiert und mit einem Instrumentation-Test geprüft.

---

## Kurzüberblick

| Bereich | Umsetzung |
| --- | --- |
| Startbildschirm | `MAIN` + `HOME` + `DEFAULT` + `LAUNCHER` im Manifest; `singleTask`; nach dem Entsperren automatisch verfügbar |
| Oben | Große Uhr, deutsches Datum mit Wochentag, Akku in Prozent mit Symbol, optionaler lokaler Wetterhinweis |
| Kontakte | Bis 8 Favoriten mit rundem Foto, vollständigem Namen und großem Telefonsymbol; direkter Anruf |
| Große Tasten | TELEFON, NACHRICHTEN, WHATSAPP, FOTOS, KAMERA, HILFE |
| Notfall | Rote Taste → deutliche Bestätigungsseite → Notfallkontakt anrufen oder optional 112; **kein automatischer Anruf** |
| Verwaltung | PIN-geschützt: Kontakte, Fotos, Reihenfolge, Favoriten, Notfall-/Hilfekontakt, Schriftgröße, Funktionen, Gerät |
| Bedienung | Mindestens 80 dp Touchflächen, sehr große Schrift, hoher Kontrast, heller Hintergrund, großer Zurück-Button auf jedem Bildschirm |
| Datenschutz | **Keine `INTERNET`-Berechtigung**, keine Analyse, kein Backup, Kontakte und Fotos nur im app-eigenen Speicher |

---

## Dokumentation

1. [Installation](docs/INSTALLATION.md)
2. [Als Standard-Launcher festlegen](docs/STANDARD-LAUNCHER.md)
3. [Kontakte und PIN einrichten](docs/KONTAKTE-EINRICHTEN.md)
4. [Tägliche Bedienung](docs/BEDIENUNG.md)
5. [Ursprünglichen Pixel-Startbildschirm wiederherstellen](docs/ZURUECK-ZUM-PIXEL-LAUNCHER.md)
6. [Sicherheit und Datenschutz](docs/SICHERHEIT-DATENSCHUTZ.md)
7. [Build und Test](docs/BUILD-UND-TEST.md)

---

## Technik

- **Sprache:** Kotlin
- **UI:** Jetpack Compose (Material 3), Compose Compiler 2.0.21
- **Min. Android:** API 29 (Android 10)
- **Ziel:** API 35 (Android 15), getestet auf API 34
- **Speicherung:** `SharedPreferences` mit JSON + `StateFlow`; Kontaktfotos als JPEG in `filesDir/contact_photos`
- **PIN:** zufälliges Salt + SHA-256-Prüfsumme, nie im Klartext
- **Integration:** Android-Intents für Telefon, Anrufliste, SMS, WhatsApp, Galerie, Kamera und Systemeinstellungen
- **Kein Root**, kein Device-Admin, kein Accessibility-Service, keine Systemumgehung

---

## Projektstruktur

```text
app/src/main/java/de/einfachstart91/launcher/
├── MainActivity.kt                 # Launcher-Activity, HOME-Intent, Berechtigungen
├── data/                            # Kontakte, Einstellungen, PIN, lokale Speicherung
├── navigation/                      # Einfacher Bildschirm-Stapel ohne versteckte Gesten
├── ui/
│   ├── AppRoot.kt                   # Navigation, BackHandler, globale Zustände
│   ├── components/                  # Große Tasten, Kopfzeile, Avatare, Dialoge
│   ├── screens/                     # Start, Notfall, Verwaltung, Kontakteditor, PIN
│   └── theme/                       # Farben und Schriftgrößen-Faktor
└── util/                            # Uhr, Akku, WLAN, Intents, Bildladen
app/src/main/res/                    # Vector-Icons, adaptive Launcher-Icons, Manifest-Ressourcen
app/src/androidTest/                 # Compose-UI-Test für den Emulator
.github/workflows/                   # APK-Build und Emulator-Test
docs/                                # Deutsche Anleitungen
apk/                                 # Ausgelieferte APK
```

---

## Hinweis zur Signatur

Die mitgelieferte APK ist bewusst mit dem lokalen Debug-Schlüssel signiert, damit sie ohne weitere Einrichtung installierbar ist. Für eine Veröffentlichung im Play Store oder eine firmeninterne Verteilung sollte ein eigener, geheimer Release-Schlüssel in `app/build.gradle.kts` konfiguriert werden. Der Quellcode und die Debug-APK enthalten keine Geheimnisse.

## Bewusst einfache Entscheidungen

- **Wetter:** kein Online-Dienst. Angehörige können optional einen kurzen Text lokal hinterlegen, damit keine Daten das Gerät verlassen.
- **SMS / Anrufliste:** Die App öffnet die dafür zuständige System-App per Intent. Sie liest keine Nachrichten oder Anrufprotokolle in eine eigene Datenbank.
- **Zurück:** Der große Zurück-Button führt auf Unterseiten eine Ebene zurück. Auf dem Startbildschirm zeigt er eine große Bestätigung an, statt die App zu verlassen; die System-Zurück-Taste wird dort ebenfalls abgefangen.
- **Nach dem Neustart:** Android startet die festgelegte Standard-Home-App nach dem Entsperren selbst. Vor der ersten Entsperrung zeigt Android aus Sicherheitsgründen seinen eigenen Sperrbildschirm-Launcher.

---

Entwickelt als vollständiges, installierbares Android-Studio-Projekt. Keine Attrappe, kein Design-Demo.
