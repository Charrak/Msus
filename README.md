# 💚 BLOHSH Hub – Billie Eilish Fan-App

Eine inoffizielle deutschsprachige Fan-Web-App (React + Vite, als PWA installierbar) rund um Billie Eilish.

## Features
- **🏠 Start:** Hero, Glastonbury-2027-Gerücht, Geburtstags-Countdown (18. Dez.), Songzeilen-Generator, Diskografie, **Fan-Quiz** (5 Fragen)
- **💿 Musik:** Komplette Diskografie (3 Studioalben, 2 EPs, 2 Live-Alben inkl. 2026er 3×LP) mit Erscheinungsdaten, Tracklists und Format-Details zu **LP/CD/Kassette/Digital/Box**, Format-Filter, „Meine Sammlung“-Tracker (LocalStorage), Singles-Zeitleiste, **Lyrics-Hilfe**: Hook-Kurz-Zitate mit deutscher Übersetzung, Song-Erklärungen, Kern-Vokabeln, Volltext-Links (Genius) + **Übersetzer-Tool** (MyMemory-API, DeepL/Google-Fallbacks). Komplette Songtexte werden aus Urheberrechtsgründen nicht reproduziert.
- **📰 News:** Aktuelle, quellenverlinkte News (Stand 30.10.2026) mit Suche & Kategorie-Filter – Gerüchte sind gekennzeichnet
- **🎤 Konzerte:** Ehrlicher Tour-Status (aktuell keine bestätigte Tour), offizielle Ticket-Links (Ticketmaster/Live Nation DE), Ticket-Alarm (lokal gespeichert), 3D-Konzertfilm
- **🛍️ Fanartikel:** Produkte mit Links **nur in die offiziellen Stores** (UK/US), Wunschliste mit ❤️ (LocalStorage), Blohsh-Fake-Check
- **🎵 TikTok Fan-Videos:** Featured Creator **[@blohsh.obsessed](https://www.tiktok.com/@blohsh.obsessed?_r=1&_t=ZG-9AzSFQoX9pA)**, weitere Fan-Accounts, Hashtag-Entdecken, optionaler TikTok-Embed, eigenes Community-Verzeichnis (Account vorschlagen)

## Start
```bash
npm install
npm run dev     # http://localhost:5173
npm run build   # Produktions-Build
```

## Hinweise
- Keine offizielle App – nicht mit Billie Eilish / Interscope / Dark Room affiliiert.
- Wunschliste, Ticket-Alarm & Community-Accounts werden nur lokal im Browser gespeichert (kein Backend/Versand).
- Held: Konzertfoto (Unsplash, via Bildersuche). Keine Bildrechte an Billie-Fotos nötig – die App nutzt keine offiziellen Pressefotos.

## 📱 APK bauen (Android-App)

Die App ist per [Capacitor](https://capacitorjs.com) als echte Android-App verpackbar
(App-ID `de.fanapp.blohshhub`, Android-Projekt im Ordner `android/`).

### Option A: Automatisch per GitHub Actions (empfohlen, kein Android Studio nötig)
Workflow: [`.github/workflows/android-apk.yml`](.github/workflows/android-apk.yml)

1. Auf GitHub ins Repo → Tab **Actions** → **„📱 Android APK bauen“** → **Run workflow**
2. Build-Typ **debug** wählen (für Tests sofort installierbar) → starten
3. Nach wenigen Minuten: Lauf öffnen → unten bei **Artifacts** die Datei
   **`BLOHSH-Hub-APK-debug`** herunterladen → enthält `BLOHSH-Hub-debug.apk`
4. APK aufs Handy kopieren und installieren (ggf. „Unbekannte Apps installieren“ erlauben)

Per Git-Tag (`git tag v1.0.0 && git push --tags`) wird zusätzlich automatisch gebaut
und die APK an eine GitHub-Release angehängt.

### Release-APK (signiert, z. B. zum Weitergeben)
Einmalig einen Keystore erstellen (Passwörter sicher aufbewahren!):
```bash
keytool -genkeypair -v -keystore release.keystore -alias blohsh -keyalg RSA -keysize 2048 -validity 10000
base64 -w0 release.keystore   # Ausgabe kopieren
```
Dann im Repo unter **Settings → Secrets and variables → Actions** anlegen:
`ANDROID_KEYSTORE_BASE64`, `ANDROID_KEY_ALIAS`, `ANDROID_KEY_PASSWORD`, `ANDROID_STORE_PASSWORD`.
Danach den Workflow mit Build-Typ **release** starten. Die `release.keystore`-Datei
**niemals** ins Repo hochladen (ist per `.gitignore` gesperrt).

### Option B: Lokal bauen (braucht JDK 17+ und Android SDK / Android Studio)
```bash
npm run apk:debug     # baut Web-App, synct Capacitor und erstellt die Debug-APK
# Ergebnis: android/app/build/outputs/apk/debug/app-debug.apk
npm run android:open  # Projekt in Android Studio öffnen
```
