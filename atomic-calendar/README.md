# Atomic Calendar

Modulo "calendario smart" della piattaforma Glitch — il **fulcro centrale** del Progetto Atomic: trasformare obiettivi in un percorso costante tramite timeblocking + to-do list dinamica, su cui in futuro si aggancerà lo strato di gamification/edutainment (evoluzione di `Glitch mark1`).

Questa cartella contiene **solo documentazione di design**, prodotta con un metodo in stile universitario: dominio → dizionario dei dati → requisiti → modello concettuale → modello logico → logica → UX/UI → codice. Nessun codice applicativo è ancora presente.

**Per riprendere il lavoro, parti da [STATO.md](STATO.md)**: dice dove siamo e cosa fare nella prossima sessione.

## Indice

Documenti attivi:

- [STATO.md](STATO.md) — stato del progetto e prossima sessione
- [00a — Analisi del dominio](docs/00a-analisi-dominio.md)
- [00b — Dizionario dei dati](docs/00b-dizionario-dati.md)
- [Registro delle decisioni](docs/decisioni.md)
- [07 — Roadmap per sessioni](docs/07-roadmap-milestone.md)

Bozze scritte prima dell'analisi del dominio, da riprendere al loro turno (vedi roadmap):

- [01 — Requisiti](docs/01-requisiti.md)
- [02 — Modello concettuale (ER)](docs/02-modello-concettuale.md)
- [03 — Schema database](docs/03-schema-database.md)
- [04 — Logica di business](docs/04-logica-di-business.md)
- [05 — Sync calendari esterni (Google / Apple)](docs/05-sync-calendari-esterni.md)
- [06 — UX/UI](docs/06-ux-ui.md)
- [08 — Rischi e decisioni aperte](docs/08-rischi-decisioni-aperte.md)

## Stack target (deciso)

- **Client**: Flutter, cross-platform (iOS, Android, Web, Desktop) — un solo codebase.
- **Backend**: proprietario (non solo Firebase/Supabase), con database relazionale.
- **Sync esterna**: Google Calendar API + Apple Calendar (CalDAV/EventKit).
- **Offline-first**: storage locale su dispositivo con sync incrementale verso il backend.

## Vincoli tecnici non negoziabili

1. Ogni evento salva **sempre** un timestamp assoluto in **UTC** + l'ID del fuso orario **IANA** (es. `Europe/Rome`). Mai "ora locale + data" come dato primario.
2. Le ricorrenze si salvano come stringa **RRULE** (RFC 5545), mai esplose in record separati. Si "srotolano" solo a runtime per renderizzare una vista.

Dettagli e motivazioni in [01-requisiti.md](docs/01-requisiti.md).
