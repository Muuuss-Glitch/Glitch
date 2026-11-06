# Registro delle decisioni

Ogni scelta importante si scrive qui in poche righe, così non si riapre una discussione già chiusa. Le decisioni non si cancellano: se una cambia, se ne aggiunge una nuova che la sostituisce.

**Formato**: contesto (perché serviva decidere), decisione, alternative scartate, conseguenze.

---

## D-001 — Stack tecnologico
**Data**: 20/09/2026
**Contesto**: l'app deve funzionare su tutti i dispositivi ed essere compatibile con Google Calendar e Apple Calendar.
**Decisione**: client Flutter (un solo codice per iOS, Android, Web, Desktop) con backend proprio.
**Scartate**: web app installabile (PWA), solo nativo iOS.
**Conseguenze**: serve un database sul server e uno sul dispositivo, con sincronizzazione tra i due.

## D-002 — Dove vive il progetto
**Data**: 20/09/2026
**Decisione**: cartella `atomic-calendar/` nel repository Glitch, accanto a `Glitch mark1/` che resta intatto.

## D-003 — Prima la progettazione, poi il codice
**Data**: 20/09/2026
**Decisione**: nessun codice applicativo finché la progettazione non è chiusa. Si producono solo documenti e diagrammi.

## D-004 — Due vincoli sul tempo
**Data**: 20/09/2026 (dal Notion "Progetto Atomic")
**Decisione**: ogni istante si salva come UTC + fuso orario IANA; ogni ricorrenza si salva come RRULE (RFC 5545) e mai esplosa in copie.
**Conseguenze**: valgono in ogni documento successivo, senza eccezioni.

## D-005 — Ordine del metodo
**Data**: 06/11/2026
**Contesto**: a settembre schema del database, sincronizzazione e UX sono stati scritti prima di aver analizzato il dominio. L'analisi del "100 days challenge" ha poi mostrato concetti che il modello non prevedeva (abitudini, contatori, ricompense, blocchi con più attività).
**Decisione**: si procede in quest'ordine: dominio → dizionario dei dati → requisiti → modello concettuale → modello logico → logica → UX/UI → codice. I documenti da 02 a 06 restano come **bozze parcheggiate** e si riprendono al loro turno.
**Scartate**: continuare dal punto in cui ci si era fermati (chiudere la decisione sulla ricorrenza e M1).
**Conseguenze**: nuova roadmap per sessioni in [07-roadmap-milestone.md](07-roadmap-milestone.md).

## D-006 — Per chi è la v1
**Data**: 06/11/2026
**Decisione**: il prodotto è pensato per altri giocatori; l'autore è il primo tester e il caso di studio.
**Scartate**: solo per l'autore; community già nella v1.
**Conseguenze**: regole, aree e punti sono personalizzabili per giocatore; i dati sulla salute vanno trattati come dati sensibili (GDPR).

## D-007 — Confine del dominio della v1
**Data**: 06/11/2026
**Decisione**: calendario + i quattro tipi di obiettivo (Progetto, Abitudine, Contatore, Ricompensa), con regole e punti come concetti.
**Scartate**: solo calendario (troppo stretto, si rifarebbe tutto); tutto Glitch (troppo largo).
**Conseguenze**: il motore di calcolo dei punti, la tokenomics, l'assistente IA e i video restano fuori.

## D-008 — Cos'è un blocco
**Data**: 06/11/2026
**Decisione**: un blocco è un contenitore di una o più attività elementari; si completa con la soglia "x su y" (regola 1).
**Scartate**: un blocco = una sola attività (modello di settembre).
**Conseguenze**: cambia il modello del documento 02 (relazione blocco → attività da 1:1 a 1:N); la routine diventa un blocco ricorrente con attività fisse.

## D-009 — Osservabili della tesi
**Data**: 06/11/2026
**Decisione**: A = tasso di completamento, B = accuratezza della stima (durata reale / stimata), C = rinvii.
**Conseguenze**: ogni attività deve avere una durata stimata e una reale; i rinvii si registrano come entità; si registra anche in Fase 0.
