# 07 — Roadmap e milestone

Fasi di progettazione, ciascuna con **criterio di uscita esplicito** (non stime a giorni/settimane: si chiude una milestone quando il criterio è soddisfatto, non a scadenza).

## M0 — Requisiti
**Documento**: [01-requisiti.md](01-requisiti.md)
**Criterio di uscita**: i requisiti funzionali/non funzionali sono approvati dall'utente e i due vincoli tecnici non negoziabili (UTC+IANA tz sempre; RRULE mai esplose) sono confermati senza ambiguità.

## M1 — Modello concettuale
**Documento**: [02-modello-concettuale.md](02-modello-concettuale.md)
**Criterio di uscita**: il diagramma ER è validato con l'utente e la decisione aperta "dove agganciare `RecurrenceRule` (Task o TimeBlock)" è presa e registrata in [08-rischi-decisioni-aperte.md](08-rischi-decisioni-aperte.md).

## M2 — Schema database
**Documento**: [03-schema-database.md](03-schema-database.md)
**Criterio di uscita**: il DDL è rivisto e approvato, il motore DB (Postgres o alternativa) è confermato, e lo schema locale offline-first (Drift o alternativa) è abbozzato a livello di principi.

## M3 — Logica di business
**Documento**: [04-logica-di-business.md](04-logica-di-business.md)
**Criterio di uscita**: la state machine di Task/Time Block e l'algoritmo di split sono validati con almeno un walkthrough concreto (esempio numerico end-to-end), e il contratto degli eventi verso la gamification è definito e ritenuto sufficiente per il futuro consumer.

## M4 — Sync calendari esterni
**Documento**: [05-sync-calendari-esterni.md](05-sync-calendari-esterni.md)
**Criterio di uscita**: la scelta EventKit vs CalDAV per Apple è presa, gli scope OAuth per Google/Apple sono definiti, e la policy di conflitto a due vie è approvata.

## M5 — UX/UI
**Documento**: [06-ux-ui.md](06-ux-ui.md)
**Criterio di uscita**: i flow principali e i wireframe (testuali/Mermaid) delle due viste sono approvati e verificati come coerenti con il modello dati definito in M1-M2 (nessuna interazione UX richiede un dato che lo schema non prevede).

## M6 — MVP implementativo (fuori scope di questa fase)
Da aprire **solo su richiesta esplicita** dell'utente, come piano di implementazione separato, una volta chiuse M0–M5. Non fa parte del lavoro di questa sessione di design.

## Stato attuale

Tutti i documenti M0–M5 sono stati scritti in prima bozza in questa sessione. Restano da fare le **validazioni esplicite** con l'utente (in particolare la decisione su `RecurrenceRule` in M1, il motore DB in M2, e le scelte di sync in M4) prima di considerare ciascuna milestone formalmente chiusa — vedi [08-rischi-decisioni-aperte.md](08-rischi-decisioni-aperte.md) per l'elenco completo.
