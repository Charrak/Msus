import { useEffect, useMemo, useState } from 'react'
import { NEWS, PRODUCTS, TIKTOK_CREATORS, HASHTAGS, QUOTES, DISCO, QUIZ, OFFICIAL } from './data'

const storeUrl = (p) => (p.store === 'UK' ? OFFICIAL.storeUK : p.store === 'LINKTREE' ? OFFICIAL.linktree : OFFICIAL.storeUS)

function useLS(key, initial) {
  const [v, setV] = useState(() => {
    try { const s = localStorage.getItem(key); return s ? JSON.parse(s) : initial } catch { return initial }
  })
  useEffect(() => { try { localStorage.setItem(key, JSON.stringify(v)) } catch {} }, [key, v])
  return [v, setV]
}

function useCountdown() {
  const [now, setNow] = useState(new Date())
  useEffect(() => { const t = setInterval(() => setNow(new Date()), 1000); return () => clearInterval(t) }, [])
  const yr = now.getFullYear()
  let target = new Date(yr, 11, 18) // 18. Dezember – Billies Geburtstag (geb. 18.12.2001)
  if (target < now) target = new Date(yr + 1, 11, 18)
  const d = target - now
  return {
    days: Math.floor(d / 864e5),
    hours: Math.floor(d / 36e5) % 24,
    mins: Math.floor(d / 6e4) % 60,
    secs: Math.floor(d / 1e3) % 60,
    age: target.getFullYear() - 2001,
  }
}

const Blohsh = ({ size = 26 }) => (
  <svg width={size} height={size} viewBox="0 0 60 60" aria-hidden="true" className="blohsh-svg">
    <circle cx="34" cy="10" r="7" fill="currentColor" />
    <path d="M30 18 L24 38" stroke="currentColor" strokeWidth="7" strokeLinecap="round" />
    <path d="M28 24 L40 30 M27 25 L15 33" stroke="currentColor" strokeWidth="6" strokeLinecap="round" />
    <path d="M24 38 L14 52 M24 38 L33 51" stroke="currentColor" strokeWidth="7" strokeLinecap="round" />
  </svg>
)

export default function App() {
  const [tab, setTab] = useState('start')
  const cd = useCountdown()
  const go = (t) => { setTab(t); window.scrollTo({ top: 0, behavior: 'smooth' }) }

  return (
    <div className="app">
      <header className="topbar">
        <div className="brand" onClick={() => go('start')}>
          <span className="logo"><Blohsh size={28} /></span>
          <span>BLOHSH <em>Hub</em></span>
        </div>
        <nav className="topnav">
          {[['start', '🏠 Start'], ['news', '📰 News'], ['konzerte', '🎤 Konzerte'], ['shop', '🛍️ Fanartikel'], ['tiktok', '🎵 TikTok']].map(([id, label]) => (
            <button key={id} className={tab === id ? 'active' : ''} onClick={() => go(id)}>{label}</button>
          ))}
        </nav>
        <a className="pill-link" href={OFFICIAL.tiktok} target="_blank" rel="noreferrer">@billieeilish ↗</a>
      </header>

      {tab === 'start' && <Home go={go} cd={cd} />}
      {tab === 'news' && <News />}
      {tab === 'konzerte' && <Konzerte cd={cd} />}
      {tab === 'shop' && <Shop />}
      {tab === 'tiktok' && <TikTok />}

      <footer>
        <p><b>BLOHSH Hub</b> ist eine inoffizielle Fan-App von Fans für Fans – nicht mit Billie Eilish, Interscope oder Dark Room affiliiert. Alle Marken/Logos gehören ihren Inhabern.</p>
        <p>Tickets & Merch bitte <b>nur über offizielle Shops</b> kaufen (siehe verlinkte Stores). News-Stand: 30.10.2026 · Gerüchte sind gekennzeichnet.</p>
        <p className="foot-links">
          <a href={OFFICIAL.website} target="_blank" rel="noreferrer">billieeilish.com</a> ·
          <a href={OFFICIAL.linktree} target="_blank" rel="noreferrer">Linktree</a> ·
          <a href={OFFICIAL.instagram} target="_blank" rel="noreferrer">Instagram</a> ·
          <a href={OFFICIAL.youtube} target="_blank" rel="noreferrer">YouTube</a> ·
          <a href={OFFICIAL.spotify} target="_blank" rel="noreferrer">Spotify</a>
        </p>
      </footer>

      <nav className="bottomnav">
        {[['start', '🏠', 'Start'], ['news', '📰', 'News'], ['konzerte', '🎤', 'Konzerte'], ['shop', '🛍️', 'Shop'], ['tiktok', '🎵', 'TikTok']].map(([id, icon, label]) => (
          <button key={id} className={tab === id ? 'active' : ''} onClick={() => go(id)}><span>{icon}</span>{label}</button>
        ))}
      </nav>
    </div>
  )
}

/* ---------------- START ---------------- */
function Home({ go, cd }) {
  const [qi, setQi] = useState(0)
  const quote = QUOTES[qi % QUOTES.length]
  return (
    <main>
      <section className="hero" style={{ backgroundImage: `url(/images/hero.jpg)` }}>
        <div className="hero-inner">
          <span className="kicker">💚 DEINE Billie Eilish FAN-APP</span>
          <h1>Welcome to the<br /><span className="neon">BLOHSH HUB</span></h1>
          <p>News, Konzerte & Ticket-Alarm, offizieller Merch und die besten TikTok Fan-Videos – alles an einem Ort. <i>Hit me hard and soft.</i></p>
          <div className="hero-cta">
            <button className="btn primary" onClick={() => go('news')}>📰 Aktuelle News</button>
            <button className="btn ghost" onClick={() => go('tiktok')}>🎵 TikTok Fan-Videos</button>
          </div>
          <div className="hero-stats">
            <div><b>9×</b><span>Grammy-Gewinnerin</span></div>
            <div><b>2×</b><span>Oscar (Bond & Barbie)</span></div>
            <div><b>3</b><span>Studioalben</span></div>
            <div><b>100 Mio.+</b><span>Instagram-Fans</span></div>
          </div>
        </div>
      </section>

      <section className="grid-2">
        <div className="card highlight">
          <span className="tag rumor">🎪 GERÜCHT · JUNI 2027</span>
          <h3>Glastonbury-Comeback?</h3>
          <p>Britische Medien berichten: Billie soll 2027 wieder die Pyramid Stage headlinen. Noch nicht offiziell – aber wir halten dich auf dem Laufenden.</p>
          <button className="link-btn" onClick={() => go('konzerte')}>Zu Konzerten & Ticket-Alarm →</button>
        </div>
        <div className="card">
          <span className="tag">🎂 FAN-COUNTDOWN</span>
          <h3>Billies {cd.age}. Geburtstag</h3>
          <p className="countdown"><b>{cd.days}</b>T : <b>{String(cd.hours).padStart(2, '0')}</b>Std : <b>{String(cd.mins).padStart(2, '0')}</b>Min : <b>{String(cd.secs).padStart(2, '0')}</b>Sek</p>
          <p>Am 18. Dezember feiert Billie Geburtstag – Fan-Projekte auf TikTok starten schon jetzt unter #billieeilish 🎂</p>
        </div>
      </section>

      <section className="card quote-card">
        <span className="tag">🎧 SONGZEILE DES MOMENTS</span>
        <blockquote>{quote.text}</blockquote>
        <small>{quote.sub}</small>
        <button className="btn small" onClick={() => setQi(qi + 1)}>🔀 Nächste Zeile</button>
      </section>

      <section>
        <h2 className="section-title">💿 Die Alben <a href={OFFICIAL.spotify} target="_blank" rel="noreferrer" className="more">Auf Spotify hören ↗</a></h2>
        <div className="disco">
          {DISCO.map((a) => (
            <div className="album" key={a.title} style={{ '--ac': a.color }}>
              <span className="year">{a.year}</span><h4>{a.title}</h4><p>{a.hits}</p>
            </div>
          ))}
        </div>
      </section>

      <section>
        <h2 className="section-title">📰 Top-News <button className="more btn-link" onClick={() => go('news')}>Alle News →</button></h2>
        <div className="news-list">{NEWS.slice(0, 3).map((n) => <NewsCard key={n.id} n={n} />)}</div>
      </section>

      <Quiz />
    </main>
  )
}

function Quiz() {
  const [step, setStep] = useState(0), [score, setScore] = useState(0), [picked, setPicked] = useState(null)
  const done = step >= QUIZ.length
  const pick = (i) => { if (picked !== null) return; setPicked(i); if (i === QUIZ[step].correct) setScore(score + 1) }
  const next = () => { setPicked(null); setStep(step + 1) }
  const restart = () => { setStep(0); setScore(0); setPicked(null) }
  return (
    <section className="card quiz">
      <span className="tag">🧠 FAN-QUIZ</span>
      {!done ? (
        <>
          <div className="quiz-progress"><i style={{ width: `${(step / QUIZ.length) * 100}%` }} /></div>
          <h3>{step + 1}. {QUIZ[step].q}</h3>
          <div className="quiz-opts">
            {QUIZ[step].options.map((o, i) => (
              <button key={o} disabled={picked !== null} onClick={() => pick(i)}
                className={picked !== null ? (i === QUIZ[step].correct ? 'right' : i === picked ? 'wrong' : '') : ''}>{o}</button>
            ))}
          </div>
          {picked !== null && <><p className="fact">💡 {QUIZ[step].fact}</p><button className="btn primary small" onClick={next}>{step === QUIZ.length - 1 ? 'Ergebnis zeigen' : 'Weiter →'}</button></>}
        </>
      ) : (
        <>
          <h3>{score === QUIZ.length ? '🏆 BLOHSH-LEGEND!' : score >= 3 ? '💚 Echter Fan!' : '🌱 Fan in Ausbildung'}</h3>
          <p>Du hast <b>{score} von {QUIZ.length}</b> richtig. {score === QUIZ.length ? 'Billie wäre stolz. 🖤' : 'Nochmal versuchen und die Legende werden!'}</p>
          <button className="btn primary small" onClick={restart}>🔁 Nochmal spielen</button>
        </>
      )}
    </section>
  )
}

/* ---------------- NEWS ---------------- */
function NewsCard({ n }) {
  return (
    <a className="news-card" href={n.url} target="_blank" rel="noreferrer">
      <div className="news-emoji">{n.emoji}</div>
      <div>
        <div className="news-meta"><span className={`tag ${n.tag === 'GERÜCHT' ? 'rumor' : ''}`}>{n.tag}</span><small>{n.dateLabel} · {n.cat} · Quelle: {n.source}</small></div>
        <h3>{n.title}</h3><p>{n.text}</p><span className="read">Artikel lesen ↗</span>
      </div>
    </a>
  )
}
function News() {
  const cats = ['Alle', ...new Set(NEWS.map((n) => n.cat))]
  const [cat, setCat] = useState('Alle'), [q, setQ] = useState('')
  const list = NEWS.filter((n) => (cat === 'Alle' || n.cat === cat) && (n.title + n.text).toLowerCase().includes(q.toLowerCase()))
  return (
    <main>
      <div className="pagehead"><span className="kicker">📰 NEWS</span><h1>Was gibt’s Neues bei Billie?</h1><p>Handverlesene Updates mit Quellen. Gerüchte sind als <b>GERÜCHT</b> gekennzeichnet – Stand 30.10.2026.</p></div>
      <input className="search" placeholder="🔎 News durchsuchen … (z. B. Film, Glastonbury, Haare)" value={q} onChange={(e) => setQ(e.target.value)} />
      <div className="chips">{cats.map((c) => <button key={c} className={cat === c ? 'chip active' : 'chip'} onClick={() => setCat(c)}>{c}</button>)}</div>
      <div className="news-list">{list.length ? list.map((n) => <NewsCard key={n.id} n={n} />) : <p className="empty">😶 Keine Treffer – versuch ein anderes Stichwort.</p>}</div>
    </main>
  )
}

/* ---------------- KONZERTE ---------------- */
function Konzerte({ cd }) {
  const [alarm, setAlarm] = useLS('blohsh-ticketalarm', null)
  const [form, setForm] = useState({ name: '', city: '', email: '' })
  const submit = (e) => { e.preventDefault(); if (form.email.includes('@')) setAlarm({ ...form, at: new Date().toISOString() }) }
  return (
    <main>
      <div className="pagehead"><span className="kicker">🎤 KONZERTE & TICKETS</span><h1>Billie live erleben</h1></div>

      <div className="card warn">
        <h3>📍 Aktueller Stand (30.10.2026)</h3>
        <p>Die <b>„Hit Me Hard and Soft: The Tour“</b> ist abgeschlossen – für 2026/27 gibt es aktuell <b>keine offiziell bestätigte neue Welttournee</b>. Im Netz kursieren viele Fake-Ticketseiten: Kaufe <b>nur</b> über billieeilish.com, Ticketmaster oder Live Nation!</p>
        <div className="btn-row">
          <a className="btn primary" href={OFFICIAL.website} target="_blank" rel="noreferrer">🌐 Offizielle Website</a>
          <a className="btn" href={OFFICIAL.ticketmasterDE} target="_blank" rel="noreferrer">🎟️ Ticketmaster DE</a>
          <a className="btn" href={OFFICIAL.livenationDE} target="_blank" rel="noreferrer">🎟️ Live Nation DE</a>
        </div>
      </div>

      <div className="concerts">
        <div className="card concert rumor-card">
          <span className="tag rumor">🎪 GERÜCHT – NICHT BESTÄTIGT</span>
          <div className="c-date"><b>JUN</b><span>2027</span></div>
          <h3>Glastonbury Festival · Pyramid Stage</h3><p>📍 Worthy Farm, Pilton, England (UK) · laut Medienberichten Headliner-Slot neben Harry Styles & Sam Fender. Offizielle Bestätigung steht aus.</p>
          <a className="link-btn" href="https://www.glastonburyfestivals.co.uk" target="_blank" rel="noreferrer">Offizielle Festival-Website ↗</a>
        </div>
        <div className="card concert">
          <span className="tag">🍿 JETZT ZUHAUSE SCHAUEN</span>
          <div className="c-date"><b>3D</b><span>FILM</span></div>
          <h3>Hit Me Hard and Soft: The Tour (Live in 3D)</h3><p>Der Konzertfilm von Billie & James Cameron – die ganze Tour-Energie für dein Wohnzimmer.</p>
          <a className="link-btn" href={OFFICIAL.linktree} target="_blank" rel="noreferrer">Über Linktree ansehen ↗</a>
        </div>
        <div className="card concert past">
          <span className="tag muted">✔ ABGESCHLOSSEN</span>
          <div className="c-date"><b>’24/25</b><span>TOUR</span></div>
          <h3>Hit Me Hard and Soft: The Tour</h3><p>Welttournee zum dritten Album – u. a. Hannover, Berlin und Köln. Fan-Mitschnitte findest du im TikTok-Bereich!</p>
        </div>
      </div>

      <div className="grid-2">
        <div className="card">
          <span className="tag">🔔 TICKET-ALARM</span><h3>Keinen Vorverkauf verpassen</h3>
          {alarm ? (
            <><p>✅ <b>{alarm.name || 'Du'}</b>, du bist vorgemerkt{alarm.city ? ` für Termine nahe ${alarm.city}` : ''}! Wir „melden“ uns an <b>{alarm.email}</b>, sobald offizielle Termine stehen.*</p>
            <button className="btn small" onClick={() => setAlarm(null)}>Alarm löschen</button>
            <p className="tiny">*Demo-Funktion dieser Fan-App: Die Daten bleiben nur lokal auf deinem Gerät – trage dich für echte Alarme zusätzlich bei Ticketmaster/Live Nation ein.</p></>
          ) : (
            <form onSubmit={submit} className="alarm-form">
              <input placeholder="Dein Name" value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} />
              <input placeholder="Lieblings-Konzertstadt (z. B. Köln)" value={form.city} onChange={(e) => setForm({ ...form, city: e.target.value })} />
              <input type="email" required placeholder="E-Mail für den Alarm" value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} />
              <button className="btn primary" type="submit">🔔 Ticket-Alarm aktivieren</button>
              <p className="tiny">Wird nur lokal auf deinem Gerät gespeichert (kein Versand). Für echte Vorverkaufs-Mails: Ticketmaster/Live Nation.</p>
            </form>
          )}
        </div>
        <div className="card">
          <span className="tag">🎂 COUNTDOWN</span><h3>Billies {cd.age}. Geburtstag – 18. Dezember</h3>
          <p className="countdown big"><b>{cd.days}</b>T <b>{String(cd.hours).padStart(2, '0')}</b>:<b>{String(cd.mins).padStart(2, '0')}</b>:<b>{String(cd.secs).padStart(2, '0')}</b></p>
          <p>Plane dein Fan-Projekt: Edit, Cover oder Blohsh-DIY – und poste es mit #billieeilish 💚</p>
        </div>
      </div>
    </main>
  )
}

/* ---------------- SHOP ---------------- */
function Shop() {
  const [favs, setFavs] = useLS('blohsh-wishlist', [])
  const [cat, setCat] = useState('Alle'), [onlyFav, setOnlyFav] = useState(false)
  const cats = ['Alle', ...new Set(PRODUCTS.map((p) => p.cat))]
  const list = PRODUCTS.filter((p) => (cat === 'Alle' || p.cat === cat) && (!onlyFav || favs.includes(p.id)))
  const toggle = (id) => setFavs(favs.includes(id) ? favs.filter((f) => f !== id) : [...favs, id])
  return (
    <main>
      <div className="pagehead"><span className="kicker">🛍️ FANARTIKEL</span><h1>Merch – nur offiziell!</h1>
        <p>Alle Produkte verlinken in die <b>offiziellen Stores</b>. Tippe ❤️ für deine Wunschliste (wird auf deinem Gerät gespeichert).</p></div>
      <div className="btn-row">
        <a className="btn primary" href={OFFICIAL.storeUK} target="_blank" rel="noreferrer">🇬🇧 Official UK/EU Store ↗</a>
        <a className="btn" href={OFFICIAL.storeUS} target="_blank" rel="noreferrer">🇺🇸 Official US Store ↗</a>
        <button className={`btn ${onlyFav ? 'primary' : ''}`} onClick={() => setOnlyFav(!onlyFav)}>❤️ Wunschliste ({favs.length})</button>
      </div>
      <div className="chips">{cats.map((c) => <button key={c} className={cat === c ? 'chip active' : 'chip'} onClick={() => setCat(c)}>{c}</button>)}</div>
      <div className="products">
        {list.map((p) => (
          <div className="product" key={p.id}>
            <button className="heart" aria-label="Auf Wunschliste" onClick={() => toggle(p.id)}>{favs.includes(p.id) ? '❤️' : '🤍'}</button>
            <div className="p-emoji">{p.emoji}</div><span className="tag">{p.cat}</span>
            <h4>{p.name}</h4><p>{p.desc}</p><span className="price">{p.price}</span>
            <a className="btn primary small block" href={storeUrl(p)} target="_blank" rel="noreferrer">Im offiziellen Store ansehen ↗</a>
          </div>
        ))}
        {!list.length && <p className="empty">🤍 Noch nichts hier – tippe bei einem Artikel aufs Herz.</p>}
      </div>
      <div className="card warn">
        <h3>🛡️ Fake-Check: Blohsh-Ketten</h3>
        <p>Gerade Blohsh-Necklaces werden oft gefälscht. Bevor du gebraucht kaufst: Die TikTok-Creatorin <b>@blohsh.obsessed</b> zeigt in Detail-Videos, woran du echte Ketten erkennst.</p>
        <a className="btn" href="https://www.tiktok.com/@blohsh.obsessed?_r=1&_t=ZG-9AzSFQoX9pA" target="_blank" rel="noreferrer">🎬 Fake-Check-Videos ansehen ↗</a>
      </div>
    </main>
  )
}

/* ---------------- TIKTOK ---------------- */
function TikTok() {
  const [community, setCommunity] = useLS('blohsh-community-tiktok', [])
  const [handle, setHandle] = useState(''), [desc, setDesc] = useState('')
  const [embedOn, setEmbedOn] = useState(false)

  useEffect(() => {
    if (!embedOn) return
    const s = document.createElement('script'); s.src = 'https://www.tiktok.com/embed.js'; s.async = true
    document.body.appendChild(s); return () => s.remove()
  }, [embedOn])

  const add = (e) => {
    e.preventDefault()
    const h = handle.trim().replace(/^@/, '').replace(/[^a-zA-Z0-9._]/g, '')
    if (!h) return
    setCommunity([{ handle: h, desc: desc.trim() || 'Community-Empfehlung', url: `https://www.tiktok.com/@${h}` }, ...community])
    setHandle(''); setDesc('')
  }
  const all = [...TIKTOK_CREATORS, ...community]
  return (
    <main>
      <div className="pagehead"><span className="kicker">🎵 TIKTOK FAN-VIDEOS</span><h1>Die Fan-Creator-Community</h1>
        <p>Von Blohsh-Fake-Checks über Konzert-Gänsehaut bis zu Unboxings – hier sind Accounts, die jeder Fan kennen sollte. Alle Karten öffnen TikTok in einem neuen Tab.</p></div>

      <a className="official-tt" href={OFFICIAL.tiktok} target="_blank" rel="noreferrer">
        <Blohsh size={40} /><div><b>@billieeilish</b><span>✔ Billies offizieller TikTok-Account – hier startet alles</span></div><span className="arrow">↗</span>
      </a>

      <h2 className="section-title">🔥 Fan-Accounts & Video-Tipps</h2>
      <div className="creators">
        {all.map((c) => (
          <a key={c.handle + c.desc} className={`creator ${c.featured ? 'featured' : ''}`} href={c.url} target="_blank" rel="noreferrer">
            <span className={`tag ${c.featured ? 'rumor' : ''}`}>{c.badge || '💚 COMMUNITY'}</span>
            <div className="avatar">{c.handle.slice(0, 1).toUpperCase()}</div>
            <h4>@{c.handle}</h4>{c.name && <small>{c.name}</small>}<p>{c.desc}</p>
            {c.tags && <div className="mini-tags">{c.tags.map((t) => <i key={t}>{t}</i>)}</div>}
            <span className="btn primary small">🎬 Videos ansehen ↗</span>
          </a>
        ))}
      </div>

      <h2 className="section-title">🏷️ Hashtags entdecken</h2>
      <div className="chips">
        {HASHTAGS.map((h) => <a key={h} className="chip" href={`https://www.tiktok.com/tag/${h}`} target="_blank" rel="noreferrer">#{h} ↗</a>)}
        <a className="chip" href="https://www.tiktok.com/discover/billie-blosh" target="_blank" rel="noreferrer">🔍 „Billie Blosh“ Trends ↗</a>
        <a className="chip" href="https://www.tiktok.com/discover/the-blohsh-necklace-explained-by-billie-eilish" target="_blank" rel="noreferrer">🔍 „Blohsh Necklace Explained“ ↗</a>
      </div>

      <div className="grid-2">
        <div className="card">
          <span className="tag">📲 LIVE-EMBED</span><h3>@blohsh.obsessed direkt hier schauen</h3>
          {!embedOn ? (
            <><p>Tippe auf „Embed laden“, um das TikTok-Profil direkt in der App zu sehen (lädt Inhalte von tiktok.com).</p>
            <button className="btn primary small" onClick={() => setEmbedOn(true)}>▶️ TikTok-Embed laden</button></>
          ) : (
            <div className="embed-wrap">
              <blockquote className="tiktok-embed" cite="https://www.tiktok.com/@blohsh.obsessed" data-unique-id="blohsh.obsessed" data-embed-type="creator" style={{ maxWidth: '100%', minWidth: 0 }}>
                <section><a target="_blank" rel="noreferrer" href="https://www.tiktok.com/@blohsh.obsessed"> @blohsh.obsessed auf TikTok ansehen</a></section>
              </blockquote>
              <p className="tiny">Falls hier nichts lädt (Browser-/TikTok-Blocker), nutze einfach die Karten-Links oben.</p>
            </div>
          )}
        </div>
        <div className="card">
          <span className="tag">➕ DEIN ACCOUNT</span><h3>Fan-Account vorschlagen</h3>
          <p>Dein eigener Billie-Fanaccount oder ein Geheimtipp? Trag ihn ein – er erscheint sofort in der Community-Liste (gespeichert auf deinem Gerät).</p>
          <form onSubmit={add} className="alarm-form">
            <input placeholder="@dein_tiktok_name" value={handle} onChange={(e) => setHandle(e.target.value)} required />
            <input placeholder="Was gibt’s dort? (z. B. Edits, Covers …)" value={desc} onChange={(e) => setDesc(e.target.value)} />
            <button className="btn primary" type="submit">💚 Zur Liste hinzufügen</button>
          </form>
        </div>
      </div>
    </main>
  )
}
