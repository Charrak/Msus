import { useEffect, useRef, useState } from 'react'
import { RELEASES, SINGLES, SONGS, FORMATS_META, geniusUrl, ytUrl, spotifySearch } from './musicData'
import { OFFICIAL } from './data'

function useLS(key, initial) {
  const [v, setV] = useState(() => { try { const s = localStorage.getItem(key); return s ? JSON.parse(s) : initial } catch { return initial } })
  useEffect(() => { try { localStorage.setItem(key, JSON.stringify(v)) } catch {} }, [key, v])
  return [v, setV]
}
const norm = (s) => s.toLowerCase().replace(/[^a-z0-9]/g, '')
const LANGS = [['en', '🇬🇧 Englisch'], ['de', '🇩🇪 Deutsch'], ['es', '🇪🇸 Spanisch'], ['fr', '🇫🇷 Französisch'], ['it', '🇮🇹 Italienisch'], ['tr', '🇹🇷 Türkisch']]

export default function Music() {
  const [typeFilter, setTypeFilter] = useState('Alle')
  const [formatFilter, setFormatFilter] = useState('Alle Formate')
  const [openId, setOpenId] = useState('hmhas')
  const [collection, setCollection] = useLS('blohsh-collection', {})
  const [songId, setSongId] = useState('boaf')
  const [showDE, setShowDE] = useState(false)
  const [songQ, setSongQ] = useState('')
  // Übersetzer-State (wird auch von Songs befüllt)
  const [tText, setTText] = useState('')
  const [tFrom, setTFrom] = useState('en'), [tTo, setTTo] = useState('de')
  const [tResult, setTResult] = useState(''), [tLoading, setTLoading] = useState(false), [tError, setTError] = useState('')
  const lyricsRef = useRef(null), translatorRef = useRef(null)

  const types = ['Alle', 'Studioalbum', 'EP', 'Live-Album']
  const formats = ['Alle Formate', 'LP', 'CD', 'Kassette', 'Digital', 'Box']
  const releases = RELEASES.filter((r) =>
    (typeFilter === 'Alle' || r.type === typeFilter) &&
    (formatFilter === 'Alle Formate' || r.formats.some((f) => f.type === formatFilter)))

  const ownedCount = Object.values(collection).filter((arr) => arr?.length).length
  const ownedFormats = Object.values(collection).reduce((n, arr) => n + (arr?.length || 0), 0)
  const toggleOwn = (rid, fmt) => {
    const cur = collection[rid] || []
    setCollection({ ...collection, [rid]: cur.includes(fmt) ? cur.filter((f) => f !== fmt) : [...cur, fmt] })
  }

  const song = SONGS.find((s) => s.id === songId) || SONGS[0]
  const songList = SONGS.filter((s) => s.title.toLowerCase().includes(songQ.toLowerCase()) || s.about.toLowerCase().includes(songQ.toLowerCase()))
  const pickTrack = (title) => {
    const found = SONGS.find((s) => norm(s.title) === norm(title) || norm(s.title).startsWith(norm(title).slice(0, 8)))
    if (found) { setSongId(found.id); setShowDE(false); lyricsRef.current?.scrollIntoView({ behavior: 'smooth' }) }
    else window.open(geniusUrl(title), '_blank')
  }

  const translate = async () => {
    const text = tText.trim().slice(0, 500)
    if (!text) return
    setTLoading(true); setTError(''); setTResult('')
    try {
      const res = await fetch(`https://api.mymemory.translated.net/get?q=${encodeURIComponent(text)}&langpair=${tFrom}|${tTo}`)
      const data = await res.json()
      const out = data?.responseData?.translatedText
      if (out) setTResult(out); else throw new Error('leer')
    } catch { setTError('Die Online-Übersetzung ist gerade nicht erreichbar – nutze unten DeepL/Google (öffnet mit deinem Text).') }
    setTLoading(false)
  }
  const sendToTranslator = (text) => {
    setTText(text); setTFrom('en'); setTTo('de'); setTResult(''); setTError('')
    translatorRef.current?.scrollIntoView({ behavior: 'smooth' })
  }

  return (
    <main>
      <div className="pagehead">
        <span className="kicker">💿 MUSIK · ALBEN · LYRICS</span>
        <h1>Lieder, Alben & Sammlerstücke</h1>
        <p>Komplette Diskografie mit Erscheinungsdaten & Formaten (LP, CD, Kassette …), Song-Infos und Lyrics-Hilfe mit Übersetzer. Markiere mit <b>„Hab ich!“</b>, was schon in deinem Regal steht. 📀</p>
      </div>

      <div className="card collection-bar">
        <div><span className="tag">🗄️ MEINE SAMMLUNG</span>
          <p className="countdown"><b>{ownedCount}</b>/{RELEASES.length} Releases · <b>{ownedFormats}</b> Formate</p>
          <div className="quiz-progress"><i style={{ width: `${(ownedCount / RELEASES.length) * 100}%` }} /></div>
        </div>
        <p>{ownedCount === 0 ? 'Noch leer – tippe unten bei einem Album auf „Hab ich“ (LP/CD/Kassette).' :
          ownedCount >= RELEASES.length ? '🏆 Komplett-Sammler:in! Respekt.' : 'Wird doch! Fehlendes findest du in den offiziellen Stores. 💚'}</p>
        <a className="btn small" href={OFFICIAL.storeUK} target="_blank" rel="noreferrer">Im offiziellen Store stöbern ↗</a>
      </div>

      <h2 className="section-title">📀 Alben, EPs & Live-Releases</h2>
      <div className="chips">{types.map((t) => <button key={t} className={typeFilter === t ? 'chip active' : 'chip'} onClick={() => setTypeFilter(t)}>{t}</button>)}</div>
      <div className="chips">{formats.map((f) => <button key={f} className={formatFilter === f ? 'chip active' : 'chip'} onClick={() => setFormatFilter(f)}>{f === 'Alle Formate' ? f : `${FORMATS_META[f].icon} ${FORMATS_META[f].label}`}</button>)}</div>

      <div className="releases">
        {releases.map((r) => {
          const open = openId === r.id, owned = collection[r.id] || []
          return (
            <article className="card release" key={r.id} style={{ '--ac': r.color }}>
              <button className="release-head" onClick={() => setOpenId(open ? null : r.id)}>
                <span className="cover">{r.emoji}</span>
                <span className="rh-text"><span className="tag">{r.type.toUpperCase()} · {r.dateLabel}</span>
                  <b>{r.title}</b><small>{r.length} · {r.labelName}</small>
                  <span className="fmt-row">{r.formats.map((f) => <i key={f.type}>{FORMATS_META[f.type].icon} {FORMATS_META[f.type].label}</i>)}</span>
                </span><span className="chev">{open ? '▾' : '▸'}</span>
              </button>
              {open && (
                <div className="release-body">
                  <p>{r.info}</p><p className="tiny">🎛️ {r.recorded}</p>
                  <h5>💽 Erschienen als:</h5>
                  <ul className="fmt-list">{r.formats.map((f) => (
                    <li key={f.type}><b>{FORMATS_META[f.type].icon} {FORMATS_META[f.type].label}</b> – {f.detail}
                      {['LP', 'CD', 'Kassette', 'Box'].includes(f.type) && (
                        <button className={`own-btn ${owned.includes(f.type) ? 'owned' : ''}`} onClick={() => toggleOwn(r.id, f.type)}>{owned.includes(f.type) ? '✅ Hab ich!' : '＋ Hab ich?'}</button>)}
                    </li>))}
                  </ul>
                  <h5>🎵 Tracklist <span className="tiny">(Tipp einen Song an für Infos & Lyrics-Hilfe)</span></h5>
                  <ol className="tracks">{r.tracks.map((t) => (
                    <li key={t}><button onClick={() => pickTrack(t)}>{t}</button>
                      <a href={geniusUrl(t)} target="_blank" rel="noreferrer" title="Songtext auf Genius">Lyrics ↗</a></li>))}
                  </ol>
                  <div className="btn-row">
                    <a className="btn small primary" href={spotifySearch('Billie Eilish ' + r.title)} target="_blank" rel="noreferrer">🎧 Auf Spotify hören</a>
                    <a className="btn small" href={ytUrl(r.title + ' full album')} target="_blank" rel="noreferrer">▶️ YouTube</a>
                    <a className="btn small" href={OFFICIAL.storeUK} target="_blank" rel="noreferrer">🛍️ Physisch kaufen</a>
                  </div>
                </div>
              )}
            </article>
          )
        })}
        {!releases.length && <p className="empty">😶 Für diese Filter-Kombi gibt’s kein Release.</p>}
      </div>

      <div className="card">
        <span className="tag">🌟 WICHTIGE SINGLES & RARITÄTEN</span>
        <div className="singles">{SINGLES.map((s) => (
          <div className="single-row" key={s.title}><b className="yr">{s.year}</b>
            <button className="st" onClick={() => pickTrack(s.title)}>{s.title}</button><span>{s.note}</span>
            <a href={geniusUrl(s.title)} target="_blank" rel="noreferrer">Lyrics ↗</a></div>))}
        </div>
        <p className="tiny">Quelle der Diskografie-Daten: <a href="https://en.wikipedia.org/wiki/Billie_Eilish_discography" target="_blank" rel="noreferrer">Wikipedia – Billie Eilish discography ↗</a> & offizielle Stores/Discogs.</p>
      </div>

      {/* ---------------- LYRICS ---------------- */}
      <section ref={lyricsRef} className="lyrics-sec">
        <h2 className="section-title">✍️ Songtexte verstehen & übersetzen</h2>
        <div className="card notice">
          <p>⚖️ <b>Ehrlich & legal:</b> Komplette Songtexte sind urheberrechtlich geschützt – deshalb zeigen wir hier pro Song nur die <b>wichtigste Hook-Zeile als Kurz-Zitat</b> mit Übersetzung, eine Inhalts-Erklärung auf Deutsch und Kern-Vokabeln. Den <b>lizenzierten Volltext</b> findest du per Knopfdruck bei Genius. Mit dem Übersetzer unten kannst du beliebige kurze Zeilen selbst übersetzen.</p>
        </div>
        <input className="search" placeholder="🔎 Song suchen … (z. B. feather, Chihiro, Bond)" value={songQ} onChange={(e) => setSongQ(e.target.value)} />
        <div className="chips song-chips">
          {songList.map((s) => <button key={s.id} className={songId === s.id ? 'chip active' : 'chip'} onClick={() => { setSongId(s.id); setShowDE(false) }}>{s.title}</button>)}
          {!songList.length && <p className="empty">Kein Song gefunden – versuch’s mit einem anderen Wort.</p>}
        </div>

        <div className="card song-detail">
          <span className="tag">🎵 {song.from}</span>
          <h3>{song.title}</h3>
          <div className="hook-box">
            <small>DIE HOOK (KURZ-ZITAT)</small>
            <blockquote>„{song.hook}“</blockquote>
            {showDE ? <p className="hook-de">🇩🇪 {song.de}</p> : null}
            <div className="btn-row">
              <button className="btn small primary" onClick={() => setShowDE(!showDE)}>{showDE ? 'Übersetzung ausblenden' : '🇩🇪 Übersetzung zeigen'}</button>
              <button className="btn small" onClick={() => sendToTranslator(song.hook)}>🌐 Im Übersetzer öffnen</button>
            </div>
          </div>
          <h5>💬 Um was geht’s im Song?</h5><p>{song.about}</p>
          <h5>📚 Kern-Vokabeln</h5>
          <div className="vocab">{song.vocab.map(([en, de]) => (
            <button key={en} onClick={() => sendToTranslator(en)} title="Im Übersetzer öffnen"><b>{en}</b><span>{de}</span></button>))}
          </div>
          <div className="btn-row">
            <a className="btn small primary" href={geniusUrl(song.title)} target="_blank" rel="noreferrer">📜 Kompletten Songtext bei Genius ↗</a>
            <a className="btn small" href={ytUrl(song.title)} target="_blank" rel="noreferrer">▶️ Video / Audio (YouTube)</a>
            <a className="btn small" href={spotifySearch('Billie Eilish ' + song.title)} target="_blank" rel="noreferrer">🎧 Spotify</a>
            <a className="btn small" href={`https://www.songtexte.com/search?q=${encodeURIComponent('Billie Eilish ' + song.title)}`} target="_blank" rel="noreferrer">🇩🇪 Deutsche Übersetzung suchen ↗</a>
          </div>
        </div>
      </section>

      {/* ---------------- ÜBERSETZER ---------------- */}
      <section ref={translatorRef}>
        <h2 className="section-title">🌐 Lyrics-Übersetzer</h2>
        <div className="card translator">
          <p>Füge eine Songzeile (oder ein Wort) ein und lass sie übersetzen – z. B. aus einem Songtext, den du gerade mitliest. Max. 500 Zeichen pro Durchgang.</p>
          <div className="lang-row">
            <select value={tFrom} onChange={(e) => setTFrom(e.target.value)}>{LANGS.map(([v, l]) => <option key={v} value={v}>{l}</option>)}</select>
            <button className="btn small" onClick={() => { setTFrom(tTo); setTTo(tFrom); setTText(tResult || tText); setTResult('') }}>⇄ Tauschen</button>
            <select value={tTo} onChange={(e) => setTTo(e.target.value)}>{LANGS.map(([v, l]) => <option key={v} value={v}>{l}</option>)}</select>
          </div>
          <textarea rows={4} maxLength={500} placeholder={'z. B.: We should stick together …'} value={tText} onChange={(e) => setTText(e.target.value)} />
          <small className="tiny">{tText.length}/500 Zeichen</small>
          <div className="btn-row">
            <button className="btn primary" onClick={translate} disabled={tLoading || !tText.trim()}>{tLoading ? '⏳ Übersetze …' : '🌐 Übersetzen'}</button>
            {SONGS.slice(11, 15).map((s) => <button key={s.id} className="chip" onClick={() => sendToTranslator(s.hook)}>„{s.hook.slice(0, 22)}…“</button>)}
          </div>
          {tResult && <div className="result"><small>ÜBERSETZUNG</small><p>{tResult}</p></div>}
          {tError && <p className="error">⚠️ {tError}</p>}
          <div className="btn-row">
            <a className="btn small" href={`https://www.deepl.com/translator#${tFrom}/${tTo}/${encodeURIComponent(tText)}`} target="_blank" rel="noreferrer">In DeepL öffnen ↗</a>
            <a className="btn small" href={`https://translate.google.com/?sl=${tFrom}&tl=${tTo}&text=${encodeURIComponent(tText)}&op=translate`} target="_blank" rel="noreferrer">In Google Übersetzer öffnen ↗</a>
          </div>
          <p className="tiny">Maschinelle Übersetzung (MyMemory) – bei Slang & Wortspielen (Billie liebt beides!) am besten mit der Song-Erklärung oben abgleichen.</p>
        </div>
      </section>
    </main>
  )
}
