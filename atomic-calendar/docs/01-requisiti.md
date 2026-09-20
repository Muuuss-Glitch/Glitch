# 01 — Requisiti

## Visione

> Dobbiamo realizzare un'app che consenta di trasformare obiettivi in un percorso costante, senza far perdere la concentrazione per via della mancanza di retention.

Il **calendario** è il perno centrale di tutto il sistema. La parte di edutainment / gamification / tokenomics (evoluzione futura del prototipo `Glitch mark1`) si baserà interamente su ciò che viene definito nel calendario e completato attraverso la to-do list dinamica.

Il calendario deve avere **due modalità**:

- **Timeblocking** (vista calendario classica): per **definire** le attività, con orari precisi.
- **To-do list** (vista "lista spuntabile"): per **testimoniare l'esecuzione** delle attività — qui entra in gioco la logica di completamento.

## Requisiti funzionali

| ID | Requisito |
|----|-----------|
| RF-01 | L'utente gestisce un backlog di **Task**: attività con obiettivo, categoria, stima di sforzo, ma **senza** data/ora fissa di esecuzione ("potenziale non espresso"). |
| RF-02 | L'utente può trascinare un Task dal backlog su uno slot del calendario, creando un **Time Block** (inizio/fine, riferimento al Task). |
| RF-03 | Se un Task non entra in un singolo slot libero, il sistema permette di **spezzarlo** in più Time Block più piccoli, anche su giorni diversi, la cui somma delle durate copre la stima di sforzo del Task. |
| RF-04 | Esistono due viste sincronizzate sugli stessi dati: vista **calendario/timeblocking** (griglia oraria) e vista **to-do list** (lista spuntabile per giorno). |
| RF-05 | Se un Time Block scade senza essere completato, il sistema **non** lo spunta automaticamente e **non** lo cancella: lo stato passa a "missed" e il Task torna nel backlog, oppure l'utente è costretto a ri-trascinarlo esplicitamente al giorno successivo ("attrito positivo"). |
| RF-06 | Il sistema espone eventi/hook (pianificazione anticipata, esecuzione puntuale, riprogrammazioni ripetute) verso un futuro motore di gamification, senza implementarne la logica di calcolo in questa fase. |
| RF-07 | Le attività ricorrenti si definiscono con una singola regola (RRULE), non con record duplicati. |
| RF-08 | L'utente collega uno o più account calendario esterni (Google Calendar, Apple/iCloud Calendar) e vede/gestisce una sincronizzazione bidirezionale con i propri eventi. |
| RF-09 | L'app funziona su più dispositivi dello stesso utente (mobile, web, desktop) con lo stesso stato sincronizzato. |
| RF-10 | L'app funziona **offline**: le modifiche fatte senza connessione si sincronizzano quando la connessione torna disponibile. |

## Requisiti non funzionali

| ID | Requisito |
|----|-----------|
| RNF-01 | **Offline-first**: nessuna azione base (creare/modificare/completare un Task o Time Block) richiede connessione immediata. |
| RNF-02 | **Cross-platform**: un solo codebase client (Flutter) per iOS, Android, Web, Desktop. |
| RNF-03 | **Backend proprietario**: non ci si appoggia esclusivamente a BaaS di terze parti (es. solo Firebase); il backend possiede e governa il modello dati primario. |
| RNF-04 | **Privacy/OAuth minimi**: le integrazioni con Google/Apple richiedono solo gli scope strettamente necessari alla gestione calendario. |
| RNF-05 | **Performance di rendering ricorrenze**: lo srotolamento di una RRULE per una vista (es. settimana corrente) deve essere rapido anche con serie ricorrenti pluriennali — non si pre-calcolano né si materializzano tutte le occorrenze nel DB. |
| RNF-06 | **Estendibilità verso la gamification**: il modello dati non deve richiedere modifiche strutturali quando in futuro si aggancerà il motore di reward/penalità. |

## Vincoli tecnici non negoziabili

Questi due vincoli sono stati identificati esplicitamente nella fase di analisi e vanno rispettati senza eccezioni in tutti i documenti successivi (modello dati, schema DB, logica):

1. **Tempo e fusi orari.** Non si salva mai "ora locale + data" come dato primario (es. non "Ora inizio: 10:00, Data: 14 Agosto"). Se l'utente viaggia, o scatta l'ora legale, un sistema così costruito smette di funzionare correttamente. Per ogni evento si salvano **sempre due cose**:
   - il timestamp esatto in **UTC** (Tempo Coordinato Universale);
   - l'**ID del fuso orario IANA** (es. `Europe/Rome`).

   È il client (la UI) a convertire l'UTC nell'ora locale dell'utente per disegnare la griglia. Questo è indispensabile anche per future metriche di produttività per fascia oraria (es. "l'utente X è più produttivo tra le 9 e le 11").

2. **Le ricorrenze non si esplodono.** Se un utente vuole un blocco di lettura ogni martedì e giovedì per tre mesi, il database **non** crea un record per ogni occorrenza. Si salva **un solo record** con una stringa di ricorrenza standard, es. `FREQ=WEEKLY;BYDAY=TU,TH;UNTIL=20261114` (protocollo **RRULE**, RFC 5545). È il motore applicativo che, al momento del caricamento di una vista, "srotola" la regola e genera visivamente i blocchi.

## Glossario

- **Task**: l'unità di lavoro nel backlog (il *cosa*). Ha un obiettivo, una categoria, una stima di sforzo, ma nessuna collocazione temporale fissa.
- **Time Block**: uno slot sulla griglia calendario (il *quando*). Ha un inizio e una fine in UTC + fuso orario IANA, e referenzia il Task che ospita.
- **Backlog**: l'insieme dei Task non ancora pianificati (stile inbox GTD).
- **Split**: l'operazione di suddivisione di un Task in più Time Block quando non esiste un singolo slot libero sufficiente.
- **RRULE**: stringa standard RFC 5545 che descrive una regola di ricorrenza.
- **Attrito positivo**: comportamento intenzionale del sistema che non completa/cancella automaticamente un'attività scaduta, ma costringe l'utente a una decisione esplicita (rimando o ri-pianificazione), per generare consapevolezza sull'uso del tempo.

## Fuori scope (in questa fase)

- Scrittura di codice applicativo (client, backend, migrazioni DB reali).
- Implementazione della logica di calcolo della gamification (punti, livelli, token) — si definiscono solo i punti di aggancio (hook).
- Supporto multi-tenant / team / calendari condivisi tra più utenti.
- Notifiche push (si segnala solo come dipendenza futura in [08-rischi-decisioni-aperte.md](08-rischi-decisioni-aperte.md)).
