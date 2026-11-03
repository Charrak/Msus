# Sicherheit und Datenschutz

## Keine Daten nach außen

Die App deklariert **keine `android.permission.INTERNET`**. Damit kann sie schon technisch keine Kontakte, Fotos oder Nutzungsdaten an einen Server senden. Es gibt:

- keine Analyse-/Tracking-Bibliothek,
- keine Werbung,
- keine In-App-Käufe,
- keine Cloud-Synchronisation,
- keine Weitergabe von Kontaktdaten.

`android:allowBackup="false"` und `data_extraction_rules.xml` verhindern zusätzlich, dass Kontakte über Android-Backup oder Geräteübertragung das Gerät verlassen. `usesCleartextTraffic` ist deaktiviert.

## Lokale Speicherung

| Daten | Ort |
| --- | --- |
| Kontakte, Reihenfolge, Favoriten, PIN-Prüfsumme, Einstellungen | `SharedPreferences` im privaten App-Speicher |
| Kontaktfotos | `filesDir/contact_photos/<id>.jpg`, verkleinert auf maximal 512 px |
| Wetterhinweis | nur lokaler Freitext, kein Wetterdienst |

Andere Apps haben ohne Root keinen Zugriff auf diese Dateien.

## PIN

- Beim ersten Öffnen der Verwaltung wird eine 4–8-stellige PIN erstellt.
- Gespeichert werden ein zufälliges 16-Byte-Salt und ein SHA-256-Hash, **nicht** die PIN selbst.
- Die Prüfung erfolgt mit `MessageDigest.isEqual`.
- Die PIN schützt alle Änderungen am Startbildschirm: Kontakte, Reihenfolge, Schriftgröße, Notfallkontakt und Funktionen.

## Anrufe und externe Apps

- `CALL_PHONE` wird nur für direkte Anrufe nach einer bewussten Berührung verwendet.
- Fehlt die Berechtigung, öffnet die App ersatzweise die Wählscheibe mit vorausgefüllter Nummer.
- WhatsApp, Galerie, Kamera, SMS, Anrufliste und Systemeinstellungen werden ausschließlich über normale Android-Intents geöffnet.
- Die Notfallseite wählt **nichts automatisch**; 112 und der Notfallkontakt erfordern jeweils einen eigenen Tap.

## Keine Systemumgehung

- Kein Root, kein Device-Administrator, kein Accessibility-Service, kein Overlay, kein Boot-Receiver, keine versteckten Dienste.
- Die Registrierung als Standard-Launcher erfolgt über die offizielle `HOME`-Kategorie und `RoleManager.ROLE_HOME`.
- Die System-Zurück-Taste wird nur innerhalb der eigenen Compose-App abgefangen, um unbeabsichtigtes Verlassen des Startbildschirms zu verhindern.

## Barrierefreiheit

- Alle Bedientasten haben `contentDescription` bzw. einen sichtbaren deutschen Text.
- Sehr große Schrift, drei Schriftgrößen-Stufen und hohe Kontraste.
- Keine zeitkritischen Aktionen, keine Gesten, keine doppelten Taps, keine Animationen.
