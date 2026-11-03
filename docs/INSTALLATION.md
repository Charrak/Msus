# Installation auf dem Google Pixel 8

## Voraussetzungen

- Google Pixel 8 mit Android 10 oder neuer (empfohlen: Android 14/15).
- Die Datei `apk/EinfachStart91.apk`.
- Kein Root-Zugriff nötig.
- Etwa 30 MB freier Speicher (APK: 24.587.577 Byte).

## Weg 1: Installation direkt auf dem Gerät

1. `EinfachStart91.apk` auf das Pixel 8 kopieren (USB, Download oder Dateiübertragung).
2. Datei im **Dateimanager** antippen.
3. Falls Android fragt: **„Unbekannte Apps installieren“** für den Dateimanager bzw. Browser einmal erlauben.
4. **Installieren** antippen.
5. **Öffnen** antippen oder anschließend die Home-Taste drücken.

## Weg 2: Installation mit adb

Auf einem Computer mit Android-SDK-Plattform-Tools:

```bash
adb devices
adb install -r apk/EinfachStart91.apk
```

## Erste Schritte nach der Installation

1. Beim ersten Start kann Android nach der Berechtigung **„Telefonanrufe tätigen“** fragen. Bitte erlauben: Dann genügt ein Tippen auf einen Kontakt für den direkten Anruf. Ohne Berechtigung öffnet sich stattdessen die Wählscheibe mit bereits eingetragener Nummer.
2. EinfachStart 91 [als Standard-Launcher festlegen](STANDARD-LAUNCHER.md).
3. Zahnrad oben rechts antippen, eine **PIN erstellen** und die [Kontakte einrichten](KONTAKTE-EINRICHTEN.md).

## Wenn die Installation abgelehnt wird

- Prüfen, ob genügend Speicher frei ist.
- Prüfen, ob die APK vollständig übertragen wurde (Größe laut `build-report/apk-info.txt`).
- Die APK ist debug-signiert; Android akzeptiert sie wie jede andere selbst installierte App.
- Eine bereits installierte Version mit gleichem Paketnamen wird mit `adb install -r` aktualisiert.

## Berechtigungen

| Berechtigung | Zweck |
| --- | --- |
| `CALL_PHONE` | Direkter Anruf nach einem Tap auf einen Kontakt oder die Notfallbestätigung |
| `ACCESS_NETWORK_STATE` / `ACCESS_WIFI_STATE` | Anzeige des WLAN-Gerätestatus in der Verwaltung |

Es wird **keine** `INTERNET`-, Kontakt-, SMS-, Kamera- oder Speicherberechtigung angefordert. Fotos werden über die System-Galerie ausgewählt; die App erhält dabei nur temporären Zugriff auf das eine ausgewählte Bild und kopiert es in ihren eigenen privaten Speicher.
