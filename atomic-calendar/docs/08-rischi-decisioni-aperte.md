# 08 — Rischi e decisioni aperte

Elenco delle decisioni non ancora prese e dei rischi identificati durante la stesura dei documenti di design. Nessuna di queste blocca l'inizio del lavoro sulle singole milestone (vedi [07-roadmap-milestone.md](07-roadmap-milestone.md)): vanno validate **durante** l'esecuzione della milestone a cui si riferiscono, con la proposta più sensata già segnata come default nei documenti corrispondenti.

| # | Decisione/rischio | Dove impatta | Proposta di default (da validare) |
|---|---|---|---|
| 1 | Dove agganciare `RecurrenceRule`: su `Task` o su `TimeBlock`? | 02, 03 | Nessun default imposto: lo schema modella entrambi i collegamenti come possibili finché non si decide (M1). |
| 2 | Motore DB definitivo e hosting (Postgres self-managed vs managed, es. RDS/Cloud SQL/Supabase-solo-DB) | 03 | Postgres, hosting da confermare in M2. |
| 3 | Libreria Flutter per il calendario drag & drop | 04, 06 | Da valutare (es. Syncfusion — attenzione: licenza community con limiti — vs soluzione custom); non ancora scelta. |
| 4 | Libreria RRULE lato Dart e lato backend | 04 | Lato Dart: pacchetto `rrule` su pub.dev; lato backend dipende dal linguaggio scelto (non ancora deciso). |
| 5 | EventKit non è headless: se serve sync anche fuori da iOS nativo, CalDAV resta comunque necessario | 05 | CalDAV come canale principale, EventKit solo come ottimizzazione locale iOS (vedi 05). |
| 6 | Necessità reale dell'entità `ExternalEvent` separata da `TimeBlock` | 02, 03 | Sì, per non mescolare eventi "nativi" Atomic Calendar con eventi puramente a specchio da provider esterni; da confermare in M1. |
| 7 | Policy di conflict resolution definitiva (locale↔backend e Atomic↔provider esterni) | 04, 05 | v1: version/etag + last-write-wins, senza merge automatico; da validare in M3/M4. |
| 8 | Gestione override di singola occorrenza in una serie ricorrente (pattern iCalendar `RECURRENCE-ID`) | 04 | Da definire nel dettaglio durante M3 — impatta lo schema di `recurrence_rules`/`exdates`. |
| 9 | Infrastruttura notifiche push per l'attrito positivo (avviso quando un Time Block sta per scadere/è scaduto) | fuori scope DB/API attuale | Segnalata come dipendenza futura, non progettata in questa fase. |
| 10 | Privacy/GDPR sui dati calendario (dati potenzialmente sensibili: routine, abitudini, luoghi impliciti) | trasversale | Da affrontare quando si definisce l'infrastruttura di hosting/backend (M2/M6), non bloccante per il design attuale. |
| 11 | Backend proprietario: linguaggio/framework non ancora scelto | 03, 04, 05 | Nessun default: da decidere con l'utente, condiziona le librerie RRULE/CalDAV lato server (voce 4). |
| 12 | Overlap tra Time Block dello stesso utente: permesso o vietato? | 04 | Default: vietato: da confermare se servono eccezioni (es. eventi importati da calendari esterni che si sovrappongono a un Time Block nativo). |

## Come usare questa tabella

Ogni riga va chiusa (decisione presa, annotata, e propagata ai documenti impattati) prima di considerare formalmente conclusa la milestone corrispondente in [07-roadmap-milestone.md](07-roadmap-milestone.md). Le righe non ancora chiuse non impediscono di procedere con la stesura/iterazione degli altri documenti, purché la proposta di default resti esplicitamente marcata come "da validare".
