CREATE TABLE case_entry (
    id           UUID PRIMARY KEY,
    client_name  VARCHAR(255) NOT NULL,
    description  TEXT,
    status       VARCHAR(20)  NOT NULL,
    deadline     TIMESTAMPTZ  NOT NULL,
    created_at   TIMESTAMPTZ  NOT NULL,
    updated_at   TIMESTAMPTZ  NOT NULL
);

CREATE INDEX idx_case_entry_status_deadline ON case_entry (status, deadline);
