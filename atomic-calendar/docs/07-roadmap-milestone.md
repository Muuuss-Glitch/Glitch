# 07 — Roadmap per sessioni

Ogni sessione ha **un obiettivo** e un **criterio di uscita** verificabile. Una sessione si chiude quando il criterio è soddisfatto; se non ci si riesce, la sessione era troppo grande e va spezzata in due. L'ordine segue la decisione [D-005](decisioni.md): dominio → dizionario → requisiti → modelli → logica → UX/UI → codice.

Lo stato aggiornato (quale sessione è la prossima) è sempre in [STATO.md](../STATO.md).

## Fase A — Capire il problema

### S1 — Analisi del dominio ✅ (06/11/2026)
**Prodotto**: [00a-analisi-dominio.md](00a-analisi-dominio.md), prima versione di [00b-dizionario-dati.md](00b-dizionario-dati.md), [decisioni.md](decisioni.md), [STATO.md](../STATO.md).
**Criterio di uscita**: le 6 domande (perché, chi, cosa, come, quando, dove) hanno una risposta approvata; esiste un glossario; le prime entità sono estratte.

### S2 — Dizionario dei dati completo
**Prodotto**: 00b in versione definitiva.
**Criterio di uscita**: domande Q1–Q8 chiuse e registrate; ogni entità ha attributi con significato, obbligatorietà e valori ammessi; le regole v0 sono scritte come "quando / se / allora".

### S3 — Requisiti e casi d'uso
**Prodotto**: 01-requisiti.md riscritto a partire dal dominio, con le storie utente ("come giocatore voglio… per…").
**Criterio di uscita**: ogni storia usa solo termini del glossario; ogni entità del dizionario compare in almeno una storia; è scelta la **prima fetta verticale** (le 3–5 storie minime da implementare per prime).

## Fase B — Modellare

### S4 — Modello concettuale
**Prodotto**: 02-modello-concettuale.md rifatto (diagramma ER).
**Criterio di uscita**: tutte le entità del dizionario sono nel diagramma con cardinalità motivate; è chiusa la scelta su dove vive la ricorrenza (proposta: sulla Routine).

### S5 — Modello logico (database)
**Prodotto**: 03-schema-database.md rifatto.
**Criterio di uscita**: ogni entità ha la sua tabella; ogni vincolo del dizionario è un vincolo dello schema oppure è assegnato esplicitamente alla logica; un esempio reale della settimana (blocchi, attività, registrazioni, rinvii) entra nello schema senza forzature.

### S6 — Logica di dominio
**Prodotto**: 04-logica-di-business.md rifatto (stati, regole, calcolo degli osservabili).
**Criterio di uscita**: diagrammi degli stati per attività e blocco; le regole v0 eseguite a mano su una giornata vera danno l'esito atteso; A, B e C sono calcolabili dai dati dello schema.

## Fase C — Progettare l'uso

### S7 — UX/UI della prima fetta
**Prodotto**: 06-ux-ui.md rifatto, solo per le storie della prima fetta.
**Criterio di uscita**: ogni schermata usa solo dati presenti nello schema; il percorso "pianifico un blocco → lo eseguo → lo registro" si percorre su carta senza buchi.

## Fase D — Costruire

### S8 — Preparazione del progetto
**Prodotto**: progetto Flutter vuoto, struttura delle cartelle, prima verifica automatica.
**Criterio di uscita**: l'app si avvia e la verifica automatica passa.

### S9 e seguenti — Prima fetta verticale, poi si allarga
Una sessione per storia della prima fetta, ognuna con dati salvati, interfaccia minima e test. Solo dopo si aggiungono sincronizzazione con calendari esterni (05), motore di gioco e il resto.

## Corrispondenza con la roadmap di settembre

| Prima (M0–M6) | Ora |
|---|---|
| M0 Requisiti | S1–S3 (dominio, dizionario, requisiti) |
| M1 Modello concettuale | S4 |
| M2 Schema database | S5 |
| M3 Logica di business | S6 |
| M4 Sync calendari esterni | dopo la prima fetta (fuori dal confine v1, D-007) |
| M5 UX/UI | S7 |
| M6 MVP | S8 e seguenti |
