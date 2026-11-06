# 00b — Dizionario dei dati

Le entità del dominio, estratte dall'[analisi del dominio](00a-analisi-dominio.md). Per ognuna: definizione, attributi principali, vincoli ed esempio reale dal "100 days challenge".

**Stato**: prima versione (sessione S1, 06/11/2026). Gli attributi sono una **prima bozza**: si completano e si validano nella sessione S2. Le domande ancora aperte sono in fondo.

**Convenzioni**:
- i nomi delle entità sono al singolare e con la maiuscola;
- ogni istante di tempo si intende salvato come **UTC + fuso orario IANA** (vincolo non negoziabile, vedi [01-requisiti.md](01-requisiti.md));
- ogni ricorrenza si intende salvata come **RRULE** e mai esplosa in copie.

## Mappa rapida

| Entità | In una riga |
|---|---|
| Giocatore | chi usa il sistema |
| Area | il perché di un obiettivo |
| Obiettivo | un traguardo misurabile (Progetto, Abitudine o Contatore) |
| Attività | l'unità più piccola che si spunta |
| Blocco | un intervallo di calendario che contiene attività |
| Routine | un blocco ricorrente con attività fisse |
| Registrazione | la prova di ciò che è stato fatto |
| Rinvio | lo spostamento di un'attività o di un blocco |
| Regola | quando X, se Y, allora Z |
| Ricompensa | un premio da sbloccare |
| Sfida | un periodo con obiettivi |
| Fase | una parte della sfida, con o senza gioco |

---

## Giocatore

**Definizione**: la persona che definisce obiettivi, pianifica, esegue e registra.

| Attributo | Significato |
|---|---|
| nome | come si fa chiamare |
| fuso orario predefinito | usato quando un istante non ne indica uno |
| preferenze | aree, regole e punti personalizzati |

**Vincoli**: ogni altra entità appartiene a un solo giocatore. Nessun dato è condiviso tra giocatori nella v1.
**Esempio**: il primo giocatore è l'autore del progetto.

## Area

**Definizione**: un grande ambito della vita a cui un obiettivo appartiene. Risponde al *perché*.

| Attributo | Significato |
|---|---|
| nome | es. "Salute fisica" |
| ordine | la posizione nell'elenco (1–7) |
| colore | per l'interfaccia |

**Vincoli**: ogni obiettivo ha al massimo un'area. Esistono 7 aree predefinite.
**Esempio**: 1 Salute fisica, 2 Salute mentale ed emotiva, 3 Relazioni, 4 Lavoro studio e realizzazione, 5 Sicurezza economica, 6 Significato e crescita, 7 Ambiente e comunità.

## Obiettivo

**Definizione**: un traguardo misurabile che il giocatore vuole raggiungere. È la **generalizzazione** di tre tipi (Progetto, Abitudine, Contatore), che condividono questi attributi:

| Attributo | Significato |
|---|---|
| titolo | es. "Superare 3 esami" |
| area | a quale area appartiene |
| tipo | Progetto, Abitudine o Contatore |
| modalità | Spinta, Manutenzione o Parcheggio |
| misura | il criterio di riuscita (SMART) |
| priorità | Alta, Media, Bassa |
| difficoltà | Alta, Media, Bassa (quella percepita) |
| punti | il valore al raggiungimento |
| scadenza | entro quando (facoltativa) |
| fonti | studi o materiali che ne motivano il valore |
| note | testo libero |

**Vincoli**:
- la misura è obbligatoria: un obiettivo senza misura non è SMART;
- solo gli obiettivi di tipo **Progetto** generano attività da mettere in calendario;
- la modalità può cambiare nel tempo (es. dopo gli esami, "Assistente IA" passa da Parcheggio a Spinta).

Ogni tipo aggiunge qualcosa di proprio:

| Tipo | Attributi in più | Esempio |
|---|---|---|
| **Progetto** | stima complessiva (facoltativa) | "Superare 3 esami", "Skill tree di calisthenics" |
| **Abitudine** | minimo (valore e unità), frequenza (es. ogni giorno) | "Acqua: 2 L al giorno", "10k passi" |
| **Contatore** | traguardo (valore e unità), valore attuale (calcolato) | "7 libri", "20 ricette", "Elo 400" |

## Attività

**Definizione**: l'unità di lavoro più piccola, che si spunta come fatta o non fatta. Corrisponde all'"azione minima" del Notion: abbastanza semplice da non richiedere sforzo di volontà per iniziare.

| Attributo | Significato |
|---|---|
| titolo | es. "Flashcard capitolo 3" |
| obiettivo | il Progetto a cui contribuisce |
| durata stimata | quanto si prevede che duri (serve all'osservabile B) |
| durata reale | quanto è durata (serve all'osservabile B) |
| stato | da pianificare, pianificata, fatta, non fatta |

**Vincoli**:
- un'attività senza blocco è nel **backlog**;
- un'attività non fatta non viene spuntata né cancellata in automatico (attrito positivo).

**Esempio**: nel Progetto "Superare 3 esami", per il blocco di studio di ASD: appunti con immagini e collegamenti, flashcard, esercizi.

## Blocco

**Definizione**: un intervallo di tempo nel calendario che **contiene una o più attività**. È il *quando*; le attività sono il *cosa*.

| Attributo | Significato |
|---|---|
| inizio, fine | UTC + fuso orario IANA |
| attività contenute | elenco ordinato |
| soglia di completamento | x su y: quante attività servono perché il blocco valga come completato |
| esito | calcolato: percentuale di attività fatte |

**Vincoli**:
- regola 1: il blocco è completato se sono fatte almeno x attività su y; con x = y vale il 100%;
- la fine viene dopo l'inizio;
- due blocchi dello stesso giocatore di norma non si sovrappongono (da confermare).

**Esempio**: "Studio Geometria, 2 h" con 3 attività; soglia 2 su 3.

## Routine

**Definizione**: un blocco che si ripete con le stesse attività. Riunisce le attività fisse in un unico blocco (regola 6).

| Attributo | Significato |
|---|---|
| nome | es. "Routine del mattino" |
| attività modello | l'elenco che si ripete |
| ricorrenza | RRULE (es. ogni giorno) |

**Vincoli**: ogni ripetizione produce un blocco con le sue attività; modificare la routine non cambia i blocchi già passati.
**Esempio**: routine del mattino, cioè sistemare la stanza, sistemare la scrivania, skincare, prepararsi.

## Registrazione

**Definizione**: l'atto con cui il giocatore testimonia ciò che ha fatto. È la materia prima di tutti gli osservabili.

| Attributo | Significato |
|---|---|
| cosa | l'attività, l'abitudine o il contatore a cui si riferisce |
| quando | istante (UTC + fuso orario IANA) |
| forma | spunta (fatto o non fatto), valore (es. 1,5 L), incremento (es. +1 libro) |
| fonte | manuale o automatica (es. Apple Watch) |

**Vincoli**:
- si registra **anche in Fase 0**, quando il gioco è spento;
- una registrazione non si cancella: si corregge con una nuova, così la storia resta affidabile.

**Esempio**: "Acqua: 2 L" il 4/11; "Contatore del fumo: 6"; "+1 film: nota scritta".

## Rinvio

**Definizione**: lo spostamento di un'attività o di un blocco a un altro momento. È l'osservabile C.

| Attributo | Significato |
|---|---|
| cosa | l'attività o il blocco rinviato |
| da, a | il momento di prima e quello nuovo |
| quando | istante in cui è avvenuto il rinvio |
| motivo | facoltativo, testo breve |
| numero | quante volte è già stato rinviato |

**Vincoli** (regola 9, proposta da confermare): un'attività si rinvia al massimo una volta. Al secondo rinvio il giocatore deve scegliere tra farla, ridurla (scomporla) o toglierla.
**Esempio**: "Montare il video di settimana 1" rinviato da giovedì a sabato.

## Regola

**Definizione**: una meccanica scelta dal giocatore, nella forma **quando** succede qualcosa, **se** vale una condizione, **allora** c'è una conseguenza.

| Attributo | Significato |
|---|---|
| nome, descrizione | in parole semplici |
| famiglia | valutazione, composizione, pianificazione o vincolo (vedi sotto) |
| quando | l'evento che la fa scattare (es. fine di un blocco) |
| se | la condizione (es. x su y attività fatte) |
| allora | la conseguenza (es. +punti, aggiungi un'attività, chiedi una decisione) |
| attiva | in quali fasi vale |

**Vincoli**: in Fase 0 nessuna regola assegna punti; le regole sono personalizzabili per giocatore.

Le regole v0 si dividono in quattro famiglie:

| Famiglia | Cosa fa | Regole v0 |
|---|---|---|
| Valutazione | giudica ciò che è stato fatto | 1 (x su y), 3 (almeno un movimento al giorno), 4 (salute del personaggio) |
| Composizione | aggiunge attività prima o dopo altre | 2 (decompressione prima, movimento dopo), 6 (routine come blocco unico), 8 (serie dopo ogni task) |
| Pianificazione | organizza il tempo | 5 (pagina alternativa del piano a settimana), 7 (un giorno per pianificare) |
| Vincolo | limita un comportamento | 9 (al massimo un rinvio) |

## Ricompensa

**Definizione**: un premio da sbloccare. Non è un'attività e non si mette in calendario.

| Attributo | Significato |
|---|---|
| titolo | es. "Tatuaggio" |
| condizione di sblocco | un traguardo (es. i 3 esami) o un costo in punti |
| tappe | facoltative (es. l'armadio si sblocca a pezzi) |
| sbloccata il | istante dello sblocco |

**Esempio**: "Tatuaggio" (ricompensa del traguardo grosso), "Armadio" (da sbloccare con i punti, a tappe).

## Sfida

**Definizione**: un periodo con inizio e fine in cui il giocatore persegue un insieme di obiettivi.

| Attributo | Significato |
|---|---|
| nome | es. "100 days challenge" |
| inizio, fine | date |
| obiettivi | quelli che ne fanno parte |

**Esempio**: "100 days challenge", dal 26/10/2026 al 02/02/2027.

## Fase

**Definizione**: una parte della sfida con impostazioni di gioco proprie. Serve a confrontare il comportamento con e senza gioco.

| Attributo | Significato |
|---|---|
| nome | es. "Fase 0 — settimana senza gioco" |
| inizio, fine | date |
| gioco attivo | sì o no |
| note | es. "esami in spinta, tutto il resto al minimo" |

**Vincoli**: le fasi di una sfida non si sovrappongono.
**Esempio**: Fase 0 (26/10–01/11, gioco spento), Fase 1 (02/11–fine dicembre, gioco attivo), Fase 2 (gennaio–02/02, sessione d'esame).

---

## Cosa non è un'entità

- **Osservabili A, B, C**: si **calcolano** da registrazioni, attività e rinvii, non si salvano.
- **Pianificazione settimanale** e **chiusura serale**: sono momenti (blocchi o routine), non entità nuove.
- **Esito di un blocco** e **valore attuale di un contatore**: si calcolano.

## Domande aperte (da chiudere in S2)

| # | Domanda | Proposta di partenza |
|---|---|---|
| Q1 | Quando un'attività è troppo grande per un blocco, si **scompone** in attività più piccole, o la stessa attività può occupare più blocchi? | Si scompone: è coerente con l'"azione minima" del Notion e tiene semplice il calcolo x su y. |
| Q2 | Le 7 aree sono fisse o ogni giocatore può cambiarle? | 7 predefinite, personalizzabili. |
| Q3 | Rinvii: vale la regola 9 (al massimo uno) o l'avviso dopo 3 giorni di fila del documento 04? | Regola 9, perché è tua e più semplice. |
| Q4 | Un'abitudine può avere un orario (es. il sonno) o si spunta sempre fuori dal calendario? | Fuori dal calendario, con un promemoria facoltativo. |
| Q5 | I punti li sceglie il giocatore o si calcolano da priorità e difficoltà? | Li sceglie il giocatore, con un suggerimento calcolato. |
| Q6 | Ricompensa: tipo di obiettivo (come su Notion) o entità separata (come in questo dizionario)? | Entità separata: non si persegue, si sblocca. |
| Q7 | Come scrive le regole un giocatore che non programma: testo libero o modelli già pronti? | Modelli pronti "quando / se / allora" con valori modificabili. |
| Q8 | Dati sensibili (fumo, sonno, alimentazione): sono dati sulla salute, protetti in modo speciale dal GDPR se l'app è usata da altri. Cosa si raccoglie e con quale consenso? | Raccolta minima, consenso esplicito, tutto cancellabile. Da approfondire prima del modello logico. |
