# Atomic Calendar

Modulo "calendario smart" della piattaforma Glitch — il **fulcro centrale** del Progetto Atomic: trasformare obiettivi in un percorso costante tramite timeblocking + to-do list dinamica, su cui in futuro si aggancerà lo strato di gamification/edutainment (evoluzione di `Glitch mark1`).

Questa cartella contiene **solo documentazione di design**, prodotta seguendo un percorso in stile universitario: prima i modelli, poi lo schema del database, poi la logica di business, poi la sincronizzazione esterna, infine UX/UI. Nessun codice applicativo è ancora presente.

## Indice

Leggere in quest'ordine:

1. [01 — Requisiti](docs/01-requisiti.md)
2. [02 — Modello concettuale (ER)](docs/02-modello-concettuale.md)
3. [03 — Schema database](docs/03-schema-database.md)
4. [04 — Logica di business](docs/04-logica-di-business.md)
5. [05 — Sync calendari esterni (Google / Apple)](docs/05-sync-calendari-esterni.md)
6. [06 — UX/UI](docs/06-ux-ui.md)
7. [07 — Roadmap e milestone](docs/07-roadmap-milestone.md)
8. [08 — Rischi e decisioni aperte](docs/08-rischi-decisioni-aperte.md)

## Stack target (deciso)

- **Client**: Flutter, cross-platform (iOS, Android, Web, Desktop) — un solo codebase.
- **Backend**: proprietario (non solo Firebase/Supabase), con database relazionale.
- **Sync esterna**: Google Calendar API + Apple Calendar (CalDAV/EventKit).
- **Offline-first**: storage locale su dispositivo con sync incrementale verso il backend.

## Vincoli tecnici non negoziabili

1. Ogni evento salva **sempre** un timestamp assoluto in **UTC** + l'ID del fuso orario **IANA** (es. `Europe/Rome`). Mai "ora locale + data" come dato primario.
2. Le ricorrenze si salvano come stringa **RRULE** (RFC 5545), mai esplose in record separati. Si "srotolano" solo a runtime per renderizzare una vista.

Dettagli e motivazioni in [01-requisiti.md](docs/01-requisiti.md).
