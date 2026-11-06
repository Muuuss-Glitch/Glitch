# 03 — Schema database

> ⏸ **Bozza parcheggiata fino a S5** (06/11/2026): scritta prima dell'[analisi del dominio](00a-analisi-dominio.md). Lo schema va rifatto dopo il nuovo modello concettuale (S4); restano validi i principi sul tempo (UTC + fuso IANA) e sul salvataggio offline.

## Motore proposto

**PostgreSQL** (da confermare, vedi [08-rischi-decisioni-aperte.md](08-rischi-decisioni-aperte.md)) per il backend. Motivazioni: tipo nativo `TIMESTAMPTZ`, supporto `jsonb`, ecosistema maturo per backend proprietari, buon supporto per indici su intervalli temporali.

**Nota tecnica importante**: `TIMESTAMPTZ` normalizza internamente in UTC, ma questo **non basta**: va comunque salvata separatamente la colonna con l'ID del fuso orario IANA, altrimenti si perde l'informazione su "in che fuso orario locale è stato creato/percepito questo evento" — indispensabile sia per ricostruire correttamente l'ora locale mostrata all'utente sia per le future metriche di produttività per fascia oraria. Questo è il vincolo non negoziabile di [01-requisiti.md](01-requisiti.md) applicato a livello di colonne.

## DDL indicativo

```sql
CREATE TABLE users (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email               TEXT NOT NULL UNIQUE,
    display_name        TEXT NOT NULL,
    default_timezone    TEXT NOT NULL, -- IANA tz id, es. 'Europe/Rome'
    created_at          TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE goals (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id     UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    titolo      TEXT NOT NULL,
    descrizione TEXT,
    stato       TEXT NOT NULL DEFAULT 'active', -- active | achieved | abandoned
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE categories (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id     UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    nome        TEXT NOT NULL,
    colore      TEXT
);

CREATE TABLE recurrence_rules (
    id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    rrule        TEXT NOT NULL,         -- RFC 5545, es. 'FREQ=WEEKLY;BYDAY=TU,TH;UNTIL=20261114'
    dtstart_utc  TIMESTAMPTZ NOT NULL,
    dtstart_tz   TEXT NOT NULL,         -- IANA tz id
    exdates      JSONB NOT NULL DEFAULT '[]'
);

CREATE TABLE tasks (
    id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id                 UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    goal_id                 UUID REFERENCES goals(id) ON DELETE SET NULL,
    category_id             UUID REFERENCES categories(id) ON DELETE SET NULL,
    recurrence_rule_id      UUID REFERENCES recurrence_rules(id) ON DELETE SET NULL, -- vedi decisione aperta 02
    titolo                  TEXT NOT NULL,
    descrizione             TEXT,
    effort_estimate_minutes INTEGER NOT NULL CHECK (effort_estimate_minutes > 0),
    status                  TEXT NOT NULL DEFAULT 'backlog', -- backlog | scheduled | completed | missed
    created_at              TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at              TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE time_blocks (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    task_id             UUID NOT NULL REFERENCES tasks(id) ON DELETE CASCADE,
    recurrence_rule_id  UUID REFERENCES recurrence_rules(id) ON DELETE SET NULL, -- vedi decisione aperta 02
    split_group_id      UUID, -- NULL se il Task non è stato spezzato
    start_utc           TIMESTAMPTZ NOT NULL,
    start_tz            TEXT NOT NULL, -- IANA tz id
    end_utc             TIMESTAMPTZ NOT NULL,
    end_tz              TEXT NOT NULL, -- IANA tz id
    status              TEXT NOT NULL DEFAULT 'planned', -- planned | completed | missed | rescheduled
    created_at          TIMESTAMPTZ NOT NULL DEFAULT now(),
    CHECK (end_utc > start_utc)
);

CREATE TABLE external_calendar_accounts (
    id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id                 UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    provider                TEXT NOT NULL, -- 'google' | 'apple_caldav'
    account_email           TEXT NOT NULL,
    oauth_token_encrypted   TEXT NOT NULL,
    sync_delta_token        TEXT,
    connected_at            TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE external_events (
    id                              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id                         UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    external_calendar_account_id    UUID NOT NULL REFERENCES external_calendar_accounts(id) ON DELETE CASCADE,
    external_uid                    TEXT NOT NULL, -- UID nel provider esterno
    titolo                          TEXT NOT NULL,
    start_utc                       TIMESTAMPTZ NOT NULL,
    start_tz                        TEXT NOT NULL,
    end_utc                         TIMESTAMPTZ NOT NULL,
    end_tz                          TEXT NOT NULL,
    raw_rrule                       TEXT, -- RRULE così come ricevuta dal provider, non reinterpretata
    UNIQUE (external_calendar_account_id, external_uid)
);

CREATE TABLE sync_logs (
    id                              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    external_calendar_account_id    UUID NOT NULL REFERENCES external_calendar_accounts(id) ON DELETE CASCADE,
    direzione                       TEXT NOT NULL, -- 'import' | 'export'
    esito                           TEXT NOT NULL, -- 'success' | 'error' | 'partial'
    eseguito_at                     TIMESTAMPTZ NOT NULL DEFAULT now(),
    dettaglio                       JSONB
);

CREATE TABLE gamification_events (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    time_block_id   UUID NOT NULL REFERENCES time_blocks(id) ON DELETE CASCADE,
    tipo_evento     TEXT NOT NULL, -- 'planning_ahead' | 'on_time_completion' | 'repeated_reschedule_warning'
    payload         JSONB NOT NULL DEFAULT '{}',
    emesso_at       TIMESTAMPTZ NOT NULL DEFAULT now()
);
```

## Indici consigliati

```sql
CREATE INDEX idx_time_blocks_start_utc ON time_blocks (start_utc);
CREATE INDEX idx_time_blocks_task_id ON time_blocks (task_id);
CREATE INDEX idx_time_blocks_split_group ON time_blocks (split_group_id) WHERE split_group_id IS NOT NULL;
CREATE INDEX idx_tasks_status ON tasks (status);
CREATE INDEX idx_tasks_user_status ON tasks (user_id, status);
CREATE INDEX idx_external_events_time_range ON external_events (start_utc, end_utc);
```

`idx_time_blocks_start_utc` è l'indice più critico: quasi tutte le query di rendering vista (calendario per range di date) filtrano per intervallo su `start_utc`.

## Storage locale offline-first (Flutter)

Schema locale speculare, pensato per **Drift** (SQLite tipizzato per Dart/Flutter) o equivalente:

- Le stesse tabelle logiche (`tasks`, `time_blocks`, `recurrence_rules`, ...), con **stessi campi UTC + tz**, mai solo ora locale.
- Colonne aggiuntive di sync per ogni tabella sincronizzabile:
  - `local_id` (chiave primaria locale, generata offline) vs `remote_id` (assegnato dal backend dopo il primo push riuscito).
  - `dirty BOOLEAN`: riga modificata localmente e non ancora confermata dal backend.
  - `updated_at TIMESTAMPTZ`: usato per risoluzione conflitti (vedi [04-logica-di-business.md](04-logica-di-business.md)).
  - `deleted_at TIMESTAMPTZ NULL`: soft-delete, per poter propagare le cancellazioni durante la sync invece di perderle.
  - `version` / `etag`: usato per rilevare conflitti concorrenti tra client diversi dello stesso utente.
- La sync incrementale procede a **delta**: il client invia solo le righe con `dirty = true`, il backend risponde con le righe modificate dal server dall'ultimo `sync_token` noto (pattern simile al `syncToken` di Google Calendar, vedi [05-sync-calendari-esterni.md](05-sync-calendari-esterni.md)).

Questo schema locale va formalizzato in dettaglio (DDL Drift completo) solo quando si apre la milestone implementativa (M6): in questa fase resta a livello di principi, per non anticipare decisioni di libreria non ancora validate (vedi [08-rischi-decisioni-aperte.md](08-rischi-decisioni-aperte.md)).
