// Diskografie-Daten (Quellen: Wikipedia-Diskografie, offizielle Stores, Discogs – Stand 30.10.2026)
// Lyrics-Hinweis: Songtexte sind urheberrechtlich geschützt. Hier stehen nur 1-zeilige Kurz-Zitate
// („Hook") mit eigener Übersetzung + Link zum lizenzierten Volltext (Genius).

export const FORMATS_META = {
  LP: { icon: '📀', label: 'LP / Vinyl' },
  CD: { icon: '💿', label: 'CD' },
  Kassette: { icon: '📼', label: 'Kassette' },
  Digital: { icon: '🎧', label: 'Digital / Streaming' },
  Box: { icon: '📦', label: 'Box Set' },
}

export const RELEASES = [
  {
    id: 'live-hmhas-2026', type: 'Live-Album', emoji: '🎤', color: '#3d7bff',
    title: 'HIT ME HARD AND SOFT: THE TOUR (LIVE)',
    date: '2026-05-01', dateLabel: '1. Mai 2026', labelName: 'Dark Room / Interscope',
    length: '27 Tracks (3 LPs)', recorded: 'Live aufgenommen 19.–23. Juli 2025 auf der ausverkauften Welttournee',
    info: 'Das Live-Album zum 3D-Konzertfilm: streng limitierte Einmal-Pressung auf 3 LPs aus 100 % recyceltem, einzigartig gefärbtem Vinyl – mit Lentikular-Beilage (Bild wechselt je nach Blickwinkel) und vielen Live-Fotos. Keine CD/Kassette erschienen – echtes Sammlerstück!',
    formats: [
      { type: 'LP', detail: '3×LP, 100 % recyceltes Vinyl, limitiert, Lentikular-Insert' },
      { type: 'Digital', detail: '„INTRO“ als Single vorab (24. Apr 2026)' },
    ],
    tracks: ['INTRO', 'CHIHIRO', 'LUNCH', 'NDA / THEREFORE I AM', 'WILDFLOWER', 'WHEN THE PARTY’S OVER', 'THE DINER', 'ILOMILO', 'BAD GUY', 'THE GREATEST', 'YOUR POWER', 'SKINNY', 'TV', 'BITTERSUITE', 'BURY A FRIEND', 'OXYTOCIN', 'GUESS', 'EVERYTHING I WANTED', 'BLUE', 'LOVELY', 'IDONTWANNABEYOUANYMORE', 'OCEAN EYES', 'L’AMOUR DE MA VIE / OVER NOW', 'WHAT WAS I MADE FOR?', 'HAPPIER THAN EVER', 'BIRDS OF A FEATHER', 'BLUE OUTRO'],
  },
  {
    id: 'hmhas', type: 'Studioalbum', emoji: '🚪', color: '#3d7bff',
    title: 'HIT ME HARD AND SOFT',
    date: '2024-05-17', dateLabel: '17. Mai 2024', labelName: 'Dark Room / Interscope',
    length: '43:45 Min · 10 Tracks', recorded: 'Aufgenommen Okt 2022 – Feb 2024 in Finneas’ Homestudio (L.A.)',
    info: 'Album Nr. 3 – ganz bewusst ohne Vorab-Singles veröffentlicht: Billie wollte, dass man es „am Stück“ hört („hit me hard AND soft“). Später kamen Sondereditionen dazu: „Isolated Vocals“-LP (29. Nov 2024) und eine japanische Anniversary-CD mit Live-Akustik-Bonus (1. Aug 2025).',
    formats: [
      { type: 'LP', detail: 'Standard- & farbige Pressungen, u. a. „Isolated Vocals“-Edition' },
      { type: 'CD', detail: 'Standard-CD + japanische Anniversary-CD (2025, Bonus-Tracks)' },
      { type: 'Kassette', detail: 'Standard-Kassette zum Release' },
      { type: 'Digital', detail: 'Streaming/Download, dazu Slowed+Reverb- & Speed-Up-Editionen' },
      { type: 'Box', detail: 'Box-Sets über den offiziellen Store' },
    ],
    tracks: ['Skinny', 'Lunch', 'Chihiro', 'Birds of a Feather', 'Wildflower', 'The Greatest', 'L’Amour de Ma Vie', 'The Diner', 'Bittersuite', 'Blue'],
  },
  {
    id: 'guitar-songs', type: 'EP', emoji: '🎸', color: '#c9a86a',
    title: 'Guitar Songs',
    date: '2022-07-21', dateLabel: '21. Juli 2022', labelName: 'Dark Room / Interscope',
    length: '8 Min · 2 Tracks', recorded: 'Überraschungs-Release ohne große Ankündigung',
    info: 'Zwei ruhige Gitarren-Songs als Überraschung zwischendurch: „TV“ spielte Billie vorher schon live auf Tour, „The 30th“ schrieb sie über den Autounfall eines Freundes. Nur digital erschienen – keine LP, CD oder Kassette.',
    formats: [{ type: 'Digital', detail: 'Nur Streaming / Download' }],
    tracks: ['TV', 'The 30th'],
  },
  {
    id: 'hte', type: 'Studioalbum', emoji: '🌅', color: '#d9c8a9',
    title: 'Happier Than Ever',
    date: '2021-07-30', dateLabel: '30. Juli 2021', labelName: 'Dark Room / Interscope',
    length: '56 Min · 16 Tracks', recorded: 'Während der Pandemie im Homestudio aufgenommen',
    info: 'Album Nr. 2 – reifer, jazziger, ehrlicher: Es geht um Ruhm, Macht-Gefälle, Herzschmerz und Selbstbefreiung. Der Titelsong startet als Ballade und explodiert zur Rock-Hymne. Nr. 1 in den USA, UK und vielen weiteren Ländern.',
    formats: [
      { type: 'LP', detail: 'Doppel-LP, viele farbige Varianten (u. a. Indie-Exklusives)' },
      { type: 'CD', detail: 'Standard-CD' },
      { type: 'Kassette', detail: 'Kassetten-Edition zum Release' },
      { type: 'Digital', detail: 'Streaming / Download' },
      { type: 'Box', detail: 'Box-Set-Editionen' },
    ],
    tracks: ['Getting Older', 'I Didn’t Change My Number', 'Billie Bossa Nova', 'my future', 'Oxytocin', 'GOLDWING', 'Lost Cause', 'Halley’s Comet', 'Not My Responsibility', 'OverHeated', 'Everybody Dies', 'Your Power', 'NDA', 'Therefore I Am', 'Happier Than Ever', 'Male Fantasy'],
  },
  {
    id: 'third-man', type: 'Live-Album', emoji: '🎙️', color: '#b8f53d',
    title: 'Live at Third Man Records',
    date: '2019-12-06', dateLabel: '6. Dez 2019', labelName: 'Third Man Records (TMR-660)',
    length: '10 Akustik-Live-Tracks', recorded: 'Akustik-Set im „Blue Room“ von Jack Whites Third Man Records, Nashville',
    info: 'Sammlerstück! Das Akustik-Livealbum gab es zuerst nur auf Vinyl in den Third-Man-Läden in Nashville & Detroit (Lime-Green- und Blue/Black-Split-Pressungen). Am 29. Aug 2020 folgte eine blaue Repress zum Record Store Day. Keine CD, keine Kassette.',
    formats: [{ type: 'LP', detail: 'Nur Vinyl, limitiert (grün, blau/schwarz, RSD-blau)' }],
    tracks: ['all the good girls go to hell', 'ocean eyes', 'bad guy', 'idontwannabeyouanymore', 'bury a friend', 'come out and play', 'COPYCAT', 'ilomilo', 'bellyache', 'when the party’s over'],
  },
  {
    id: 'wwafawdwg', type: 'Studioalbum', emoji: '🛏️', color: '#9bd1ff',
    title: 'WHEN WE ALL FALL ASLEEP, WHERE DO WE GO?',
    date: '2019-03-29', dateLabel: '29. März 2019', labelName: 'Dark Room / Interscope',
    length: '42:55 Min · 14 Tracks', recorded: 'Komplett im Kinderzimmer-/Homestudio mit Finneas produziert',
    info: 'Das Debütalbum – ein weltweites Phänomen: Nr. 1 in 15 Ländern, Grammy für „Album of the Year“. Dunkler Pop über Albträume, Schlaflosigkeit und das Monster unterm Bett – inklusive des Mega-Hits „bad guy“ (Diamond in den USA).',
    formats: [
      { type: 'LP', detail: 'Standard-LP + diverse farbige Pressungen' },
      { type: 'CD', detail: 'Standard-CD' },
      { type: 'Kassette', detail: 'Kassette (u. a. gelb/orange Shell)' },
      { type: 'Digital', detail: 'Streaming / Download' },
      { type: 'Box', detail: 'Box-Set-Editionen' },
    ],
    tracks: ['!!!!!!!!', 'bad guy', 'xanny', 'you should see me in a crown', 'all the good girls go to hell', 'wish you were gay', 'when the party’s over', '8', 'my strange addiction', 'bury a friend', 'ilomilo', 'listen before i go', 'i love you', 'goodbye'],
  },
  {
    id: 'dsm', type: 'EP', emoji: '🪜', color: '#ff6b6b',
    title: 'dont smile at me',
    date: '2017-08-11', dateLabel: '11. Aug 2017', labelName: 'Dark Room / Interscope',
    length: '26 Min · 8 Tracks (+1)', recorded: 'Debüt-EP, mit Finneas zuhause aufgenommen',
    info: 'Hier fing alles an: Die Debüt-EP mit „ocean eyes“ erschien zuerst nur digital. Wegen des Erfolgs folgten Vinyl (10. Nov 2017, limitiert rot) und eine gelb/rote Kassetten-Edition (Reissue 2020). Am 22. Dez 2017 kam digital „&burn“ (mit Vince Staples) als 9. Track dazu.',
    formats: [
      { type: 'LP', detail: '12"-LP, u. a. rote & schwarze Pressungen (Store: Farbe zufällig!)' },
      { type: 'CD', detail: 'CD-Edition (u. a. Japan Complete Edition)' },
      { type: 'Kassette', detail: 'Limited Cassette, gelb/rot (Urban Outfitters, Reissue 2020)' },
      { type: 'Digital', detail: 'Original nur digital, später Expanded Edition' },
    ],
    tracks: ['COPYCAT', 'idontwannabeyouanymore', 'my boy', 'watch', 'party favor', 'bellyache', 'ocean eyes', 'hostage', '&burn (Reissue-Bonus)'],
  },
]

export const SINGLES = [
  { year: '2016', title: 'Ocean Eyes', note: 'Debüt-Single (2015 schon auf SoundCloud viral)' },
  { year: '2017', title: 'Bellyache', note: 'Single aus „dont smile at me“' },
  { year: '2018', title: 'lovely (mit Khalid)', note: 'Soundtrack-Single, später auch als 7"-Vinyl' },
  { year: '2018', title: 'when the party’s over', note: 'Vorab-Single zum Debütalbum' },
  { year: '2019', title: 'bury a friend', note: 'Single, 30. Jan 2019' },
  { year: '2019', title: 'bad guy', note: 'Welt-Hit, u. a. als gelbe Kassetten-Single' },
  { year: '2019', title: 'everything i wanted', note: 'Standalone-Single; 2020 als einseitige 12"-Vinyl mit Ätzung' },
  { year: '2020', title: 'No Time to Die', note: 'James-Bond-Titelsong (Oscar!); 7"-Vinyl zum Record Store Day 2021' },
  { year: '2020', title: 'my future', note: 'Erste Single der Happier-Than-Ever-Ära' },
  { year: '2021', title: 'Your Power', note: 'Akustik-Single, 29. Apr 2021' },
  { year: '2023', title: 'What Was I Made For?', note: 'Aus dem „Barbie“-Film (Oscar + Grammy), auch als 7"-Vinyl' },
  { year: '2024', title: 'LUNCH', note: 'Lead-Single zu HIT ME HARD AND SOFT' },
  { year: '2024', title: 'BIRDS OF A FEATHER', note: 'Single & Dauerbrenner in den Charts' },
  { year: '2024', title: 'Guess (Charli XCX feat. Billie Eilish)', note: 'Remix-Feature, 1. Aug 2024' },
  { year: '2025', title: 'WILDFLOWER', note: 'Späte Single aus HMHAS' },
  { year: '2026', title: 'INTRO (Hit Me Hard and Soft Tour)', note: 'Live-Intro als Single vor dem Live-Album, 24. Apr 2026' },
]

// hook = kurzes, einzeiliges Zitat (max. ~9 Wörter) · de = eigene Übersetzung · about = Inhalts-Zusammenfassung in eigenen Worten
export const SONGS = [
  { id: 'ocean-eyes', title: 'ocean eyes', from: 'dont smile at me (2017)', hook: 'I’ve been watching you for some time', de: 'Ich beobachte dich schon eine ganze Weile.', about: 'Verträumte Ballade über eine erste, überwältigende Verliebtheit – die „Ozean-Augen“ des Gegenübers ziehen sie völlig in den Bann. Eigentlich für ihren Tanzunterricht von Finneas geschrieben, wurde der Song über Nacht ihr Durchbruch.', vocab: [['napalm skies', 'brennender Himmel'], ['careful creature', 'vorsichtiges Wesen'], ['fall for', 'sich verlieben']] },
  { id: 'idontwanna', title: 'idontwannabeyouanymore', from: 'dont smile at me (2017)', hook: 'If teardrops could be bottled', de: 'Wenn man Tränen in Flaschen füllen könnte …', about: 'Ein sehr ehrlicher Song über Selbstzweifel und das Gefühl, sich selbst nicht zu genügen. Sie singt quasi mit ihrem Spiegelbild – traurig, aber vielen Fans aus der Seele gesprochen.', vocab: [['teardrop', 'Träne'], ['bottled', 'in Flaschen gefüllt'], ['fall apart', 'auseinanderfallen / zerbrechen']] },
  { id: 'lovely', title: 'lovely (mit Khalid)', from: 'Single (2018)', hook: 'Isn’t it lovely, all alone?', de: 'Ist es nicht „wunderschön“, ganz allein zu sein?', about: 'Ironischer Titel: In Wahrheit geht es um Einsamkeit, Ängste und den Wunsch, aus dunklen Gedanken auszubrechen. Das Duett mit Khalid klingt zerbrechlich wie Glas – und wurde ein weltweiter Streaming-Gigant.', vocab: [['heart made of glass', 'Herz aus Glas'], ['mind of stone', 'Kopf aus Stein'], ['tear me to pieces', 'zerreiß mich in Stücke']] },
  { id: 'wtpo', title: 'when the party’s over', from: 'WHEN WE ALL FALL ASLEEP… (2019)', hook: 'I’ll only hurt you if you let me', de: 'Ich verletze dich nur, wenn du es zulässt.', about: 'Fast nur Klavier und Stimme: Es geht um einen Abschied am Telefon – sie bittet die andere Person, nicht mehr anzurufen, obwohl es beiden wehtut. Das Video mit der schwarzen „Tränen“-Flüssigkeit wurde ikonisch.', vocab: [['quiet', 'still / leise'], ['call me friend', 'nenn mich Freund'], ['hurt', 'verletzen']] },
  { id: 'bury', title: 'bury a friend', from: 'WHEN WE ALL FALL ASLEEP… (2019)', hook: 'What do you want from me?', de: 'Was willst du von mir?', about: 'Geschrieben aus der Sicht des „Monsters unterm Bett“ – also ihrer eigenen Ängste und Schlafprobleme. Ratternde Sounds (u. a. ein Zahnarztbohrer-Geräusch!) machen den Song zum Grusel-Herzschlag des Albums.', vocab: [['bury', 'begraben'], ['cannibal', 'Kannibale'], ['Step on the glass', 'Tritt aufs Glas']] },
  { id: 'badguy', title: 'bad guy', from: 'WHEN WE ALL FALL ASLEEP… (2019)', hook: 'I’m the bad guy, duh', de: 'Ich bin die Böse, na klar doch.', about: 'Verspielt-freche Abrechnung mit Jungs, die sich für superhart halten – Billie dreht den Spieß um. Fun Fact: Der Beat enthält ein Fußgänger-Ampel-Geräusch aus Sydney. Ihr erster Nr.-1-Hit in den USA.', vocab: [['tough guy', 'harter Kerl'], ['bruises', 'blaue Flecken'], ['duh', 'na klar / eh klar']] },
  { id: 'eiw', title: 'everything i wanted', from: 'Single (2019)', hook: 'I got everything I wanted', de: 'Ich bekam alles, was ich immer wollte.', about: 'Ein Albtraum über Ruhm und Druck – und darüber, dass ihr Bruder Finneas in jeder Version des Traums an ihrer Seite bleibt. Ein Song über Zusammenhalt, wenn der Erfolg sich schwer anfühlt.', vocab: [['dream', 'Traum'], ['scream', 'schreien'], ['nobody cried', 'niemand weinte']] },
  { id: 'nttd', title: 'No Time To Die', from: 'James-Bond-Film (2020)', hook: 'Now you’ll never see me cry', de: 'Jetzt wirst du mich niemals weinen sehen.', about: 'Der Bond-Titelsong über Verrat und kalte Entschlossenheit. Billie war mit 18 die jüngste Künstlerin, die je einen Bond-Song schrieb – und gewann dafür ihren ersten Oscar.', vocab: [['betray', 'verraten'], ['fool me once', 'täusche mich einmal …'], ['parasite', 'Parasit / Schmarotzer']] },
  { id: 'yourpower', title: 'Your Power', from: 'Happier Than Ever (2021)', hook: 'Try not to abuse your power', de: 'Versuch, deine Macht nicht zu missbrauchen.', about: 'Ruhige Akustik-Ballade mit ernster Botschaft: Sie warnt davor, ein Machtgefälle (z. B. Alter/Berühmtheit) in einer Beziehung auszunutzen. Im Video würgt sie sinnbildlich eine riesige Schlange – ein starkes Bild für Kontrolle.', vocab: [['abuse', 'missbrauchen'], ['power', 'Macht'], ['ruin', 'ruinieren / zerstören']] },
  { id: 'hte-song', title: 'Happier Than Ever', from: 'Happier Than Ever (2021)', hook: 'When I’m away from you, I’m happier than ever', de: 'Wenn ich weg von dir bin, bin ich glücklicher als je zuvor.', about: 'Zwei Songs in einem: Erst eine zarte Ballade, dann bricht der Song in eine laute Rock-Befreiung aus – der Moment, in dem sie eine toxische Beziehung endgültig hinter sich lässt. Live DER Mitbrüll-Moment jeder Show.', vocab: [['away from you', 'weg von dir'], ['happier than ever', 'glücklicher als je zuvor'], ['waste of time', 'Zeitverschwendung']] },
  { id: 'wwimf', title: 'What Was I Made For?', from: 'Barbie-Soundtrack (2023)', hook: 'What was I made for?', de: 'Wofür wurde ich eigentlich gemacht?', about: 'Für Barbies Sinnkrise im Film geschrieben, traf der Song auch Billies eigene Fragen an sich selbst. Flüsterleise und herzzerreißend – dafür gab es Oscar Nr. 2 und den Grammy „Song of the Year“.', vocab: [['made for', 'gemacht für'], ['float', 'schweben / treiben'], ['used to know', 'früher wusste ich …']] },
  { id: 'skinny', title: 'SKINNY', from: 'HIT ME HARD AND SOFT (2024)', hook: 'People say I look happy', de: 'Die Leute sagen, ich sähe glücklich aus.', about: 'Der Album-Opener rechnet mit Körper-Kommentaren und Erwartungen an ihr Aussehen ab – und stellt die Frage, ob „dünn sein“ wirklich irgendetwas über Glück aussagt. Streicher und Gitarre inklusive Gänsehaut.', vocab: [['skinny', 'sehr dünn'], ['look happy', 'glücklich aussehen'], ['the old me', 'mein altes Ich']] },
  { id: 'lunch', title: 'LUNCH', from: 'HIT ME HARD AND SOFT (2024)', hook: 'I could eat that girl for lunch', de: 'Ich könnte dieses Mädchen zum Mittag vernaschen.', about: 'Selbstbewusst, frech, tanzbar: Billie singt offen über ihr Verliebtsein in eine Frau – ein Befreiungsschlag, den viele Fans als ihr Coming-out in Songform feierten. Lead-Single des Albums.', vocab: [['lunch', 'Mittagessen'], ['craving', 'Verlangen / Heißhunger'], ['not a phase', 'keine Phase']] },
  { id: 'Chihiro', title: 'CHIHIRO', from: 'HIT ME HARD AND SOFT (2024)', hook: 'Open up the door', de: 'Mach die Tür auf.', about: 'Inspiriert vom Anime „Chihiros Reise ins Zauberland“: Es geht um Türen zwischen Menschen – jemanden zu verlieren, ihn nicht mehr zu erkennen und trotzdem nach Verbindung zu suchen. Hypnotischer Bass, verträumter Vibe.', vocab: [['open up', 'sich öffnen / aufmachen'], ['door', 'Tür'], ['stranger', 'Fremde*r']] },
  { id: 'boaf', title: 'BIRDS OF A FEATHER', from: 'HIT ME HARD AND SOFT (2024)', hook: 'We should stick together', de: 'Wir sollten für immer zusammenhalten.', about: 'Eine Liebeserklärung an eine Bindung „bis zum allerletzten Tag“ – süß und gleichzeitig ein bisschen düster, typisch Billie. Der Titel spielt mit dem Sprichwort „birds of a feather flock together“ (= Gleich und Gleich gesellt sich gern).', vocab: [['feather', 'Feder'], ['stick together', 'zusammenhalten'], ['till the day I die', 'bis zu meinem Todestag']] },
  { id: 'wildflower', title: 'WILDFLOWER', from: 'HIT ME HARD AND SOFT (2024)', hook: 'I see her in the back of my mind', de: 'Ich sehe sie immer noch im Hinterkopf.', about: 'Sie tröstet erst eine Freundin nach deren Trennung – und verliebt sich später selbst in deren Ex. Der Song kreist um Schuldgefühle und die Frage, ob sie damit eine Grenze überschritten hat.', vocab: [['wildflower', 'Wildblume'], ['crossed a line', 'eine Grenze überschritten'], ['back of my mind', 'im Hinterkopf']] },
]

export const geniusUrl = (title) =>
  `https://genius.com/search?q=${encodeURIComponent('Billie Eilish ' + title.replace(/\(.*\)/, '').trim())}`
export const ytUrl = (title) =>
  `https://www.youtube.com/results?search_query=${encodeURIComponent('Billie Eilish ' + title.replace(/\(.*\)/, '').trim() + ' official')}`
export const spotifySearch = (q) => `https://open.spotify.com/search/${encodeURIComponent(q)}`
