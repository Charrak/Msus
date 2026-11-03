# Build-Bericht

Erzeugt im erfolgreichen GitHub-Actions-Lauf `41723808804`:

- `apk-info.txt`: Datum, Commit, APK-Größe, SHA-256, Berechtigungen, Launcher-Prüfung.
- `badging.txt`: vollständige Ausgabe von `aapt dump badging`.
- `manifest.txt`: vollständige Ausgabe von `aapt dump xmltree ... AndroidManifest.xml`.

Geprüft wurden `MAIN` + `HOME` im Manifest, das Fehlen von `android.permission.INTERNET` und die bewusst deklarierte `android.permission.READ_CONTACTS` für die Kontaktauswahl.
