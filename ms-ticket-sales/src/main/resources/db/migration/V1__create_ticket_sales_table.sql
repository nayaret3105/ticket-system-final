CREATE TABLE IF NOT EXISTS ticket_sales (
    id           UUID          PRIMARY KEY DEFAULT gen_random_uuid(),
    event_id     UUID          NOT NULL,
    buyer        VARCHAR(150)  NOT NULL,
    ticket_count INTEGER       NOT NULL,
    total        NUMERIC(12,2) NOT NULL,
    sale_date    TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP
);
