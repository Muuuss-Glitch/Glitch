# STATO — la memoria del progetto

Questo file dice **dove siamo** e **cosa fare nella prossima sessione**. Si legge all'inizio di ogni sessione e si aggiorna alla fine.

**Ultimo aggiornamento**: 06/11/2026, fine sessione S1.

## Come si usa (protocollo di sessione)

**All'inizio**: scrivi a Claude *"Leggi `atomic-calendar/STATO.md` e partiamo con la prossima sessione."* Non serve rispiegare nulla.

**Durante**:
- una sessione ha **un solo obiettivo** e un criterio di uscita: si chiude quando il criterio è soddisfatto, non quando finisce il tempo;
- la durata sta nel budget "Video e Glitch" (circa 45 minuti al giorno): una sessione occupa uno o due di questi blocchi;
- ogni scelta importante finisce in [docs/decisioni.md](docs/decisioni.md);
- un'idea che non c'entra con l'obiettivo va nel **parcheggio idee** qui sotto, non si insegue subito.

**Alla fine**:
1. aggiorna questo file (stato dei documenti, prossima sessione);
2. commit e push: è il punto di salvataggio.

## Dove siamo

**Fase attuale**: analisi del dominio, conclusa in prima versione. Si passa al dizionario completo.

| Documento | Stato | Note |
|---|---|---|
| [00a — Analisi del dominio](docs/00a-analisi-dominio.md) | ✅ Prima versione | Le 6 domande hanno una risposta; c'è il glossario |
| [00b — Dizionario dei dati](docs/00b-dizionario-dati.md) | 🟡 Bozza | 12 entità definite; attributi da validare; 8 domande aperte |
| [Decisioni](docs/decisioni.md) | ✅ Aggiornato | D-001 … D-009 |
| [01 — Requisiti](docs/01-requisiti.md) | 🟡 Da rivedere in S3 | Scritto prima del dominio |
| [02 — Modello concettuale](docs/02-modello-concettuale.md) | ⏸ Parcheggiato fino a S4 | Va rifatto: blocco 1:N attività, tipi di obiettivo |
| [03 — Schema database](docs/03-schema-database.md) | ⏸ Parcheggiato fino a S5 | — |
| [04 — Logica di business](docs/04-logica-di-business.md) | ⏸ Parcheggiato fino a S6 | Allineare i rinvii alla regola 9 |
| [05 — Sync calendari esterni](docs/05-sync-calendari-esterni.md) | ⏸ Parcheggiato | Fuori dal confine della v1 (D-007) |
| [06 — UX/UI](docs/06-ux-ui.md) | ⏸ Parcheggiato fino a S7 | — |
| [07 — Roadmap](docs/07-roadmap-milestone.md) | ✅ Aggiornata | Roadmap per sessioni |
| [08 — Rischi e decisioni aperte](docs/08-rischi-decisioni-aperte.md) | 🟡 Da riassorbire | Le voci confluiranno nelle domande aperte del dizionario e nelle decisioni |

## Prossima sessione: S2 — Dizionario dei dati completo

**Obiettivo**: chiudere il dizionario dei dati.

**Criterio di uscita**:
- le 8 domande aperte (Q1–Q8 in fondo a [00b](docs/00b-dizionario-dati.md)) hanno una risposta, registrata in decisioni.md;
- ogni entità ha attributi definitivi (nome, significato, obbligatorio sì/no, valori ammessi);
- le 9 regole v0 sono riscritte nella forma "quando / se / allora".

**Da preparare prima** (10–15 minuti, anche su carta):
- leggi le domande Q1–Q8 e scrivi una risposta d'istinto per ognuna;
- su Notion, compila Priorità, Difficoltà e Punti nel database "Obiettivi 100 giorni": sono dati veri per la Fase 1, e serviranno per capire come funzionano i punti (Q5).

## Sessioni successive

Vedi [docs/07-roadmap-milestone.md](docs/07-roadmap-milestone.md).

## Parcheggio idee

Idee emerse che non appartengono alla sessione in corso. Si rivalutano quando arriva il loro turno.

- Lo skill tree di calisthenics è un grafo (una skill può avere due prerequisiti): potenziale modulo futuro e ottimo esercizio per l'esame di strutture dati.
- Sistema di note (taccuino tascabile, diario, braindump): fuori dalla v1.
- Assistente IA che impara dal giocatore: in parcheggio, candidato a sottoprogetto dopo gli esami.
