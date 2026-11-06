# 05 — Sync calendari esterni (Google / Apple)

> ⏸ **Bozza parcheggiata** (06/11/2026): la sincronizzazione dettagliata è fuori dal confine della v1 ([D-007](decisioni.md)) e si riprende dopo la prima fetta verticale. Il materiale resta valido come base.

## Google Calendar

- **Autenticazione**: OAuth2, scope minimo necessario (`calendar.events` in lettura/scrittura, non l'intero account Google se evitabile).
- **Sync incrementale**: Google espone nativamente un `syncToken` per ogni calendario — dopo una prima sincronizzazione completa, le chiamate successive richiedono solo i cambiamenti dal token precedente. Il token va persistito in `external_calendar_accounts.sync_delta_token` (vedi [03-schema-database.md](03-schema-database.md)).
- **Mapping ricorrenze**: Google Calendar supporta nativamente RRULE (RFC 5545), quindi il mapping con `recurrence_rules.rrule` è **diretto**, senza conversioni con perdita di informazione.
- **Gestione errori**: token scaduto → refresh silenzioso; token revocato dall'utente → l'account passa in stato "da ricollegare", segnalato in UI, senza bloccare il resto dell'app.

## Apple Calendar (iPhone / iCloud)

Due strade possibili, da confrontare:

| Opzione | Pro | Contro |
|---------|-----|--------|
| **EventKit** (framework nativo iOS/macOS) | Integrazione perfetta con il calendario di sistema iOS, nessuna gestione credenziali separata | **Non è headless**: funziona solo con codice eseguito sul dispositivo iOS stesso, non è utilizzabile da un backend o da un client web/Android per accedere al calendario Apple di un utente |
| **CalDAV su iCloud** (RFC 4791) | Cross-platform, utilizzabile da backend/web/qualsiasi client, coerente con l'architettura multi-dispositivo | Richiede app-specific password (o OAuth dove disponibile) generata dall'utente su iCloud; protocollo più verboso di una REST API moderna |

**Raccomandazione**: **CalDAV** come canale principale di sincronizzazione (garantisce coerenza multi-dispositivo, requisito RNF-02/RF-09), con **EventKit** eventualmente usato solo come ottimizzazione locale sul client iOS nativo (es. per leggere il calendario di sistema più rapidamente quando l'app gira su iPhone), mai come unico canale.

## Mapping campi

| Atomic Calendar | Google Event | iCalendar VEVENT (CalDAV) |
|---|---|---|
| `external_uid` | `id` | `UID` |
| `start_utc` + `start_tz` | `start.dateTime` + `start.timeZone` | `DTSTART` (con `TZID`) |
| `end_utc` + `end_tz` | `end.dateTime` + `end.timeZone` | `DTEND` (con `TZID`) |
| `raw_rrule` | `recurrence[]` | `RRULE` |
| `titolo` | `summary` | `SUMMARY` |
| stato locale (`planned`/`completed`/...) | non mappato 1:1 — Google non ha un concetto di "completato" per eventi normali | `STATUS` (`CONFIRMED`/`CANCELLED`), semantica diversa da `TimeBlockStatus` |

Nota: lo stato `completed`/`missed` di un Time Block è un concetto **specifico di Atomic Calendar** (legato alla to-do list dinamica), non ha equivalente diretto nei provider esterni. Gli eventi esportati verso Google/Apple mostrano solo la collocazione temporale; l'esecuzione/completamento resta tracciata lato Atomic Calendar.

## Conflitti a due vie

- Un evento modificato sia in Atomic Calendar sia nel provider esterno tra due sync viene rilevato tramite confronto di `updated_at` (locale) e `etag`/`updated` (provider).
- Politica di default (da validare, vedi [08-rischi-decisioni-aperte.md](08-rischi-decisioni-aperte.md)): l'ultima modifica vince (last-write-wins), con log dell'evento in `sync_logs` per permettere audit e, in una fase successiva, un'opzione di merge manuale.

## Privacy

- Richiedere solo gli scope OAuth strettamente necessari alla gestione calendario (mai contatti, email, drive, ecc. se non richiesti da altre funzionalità esplicite).
- I token (`oauth_token_encrypted`) vanno cifrati at-rest nel backend, mai salvati in chiaro.
- L'utente deve poter revocare il collegamento a un account esterno in qualsiasi momento, con effetto immediato sulla sync (non solo sospensione lato UI).
