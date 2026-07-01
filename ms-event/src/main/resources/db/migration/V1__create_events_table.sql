CREATE TABLE IF NOT EXISTS events (
    id           UUID          DEFAULT gen_random_uuid() PRIMARY KEY,
    name         VARCHAR(150)  NOT NULL,
    description  TEXT,
    event_date   TIMESTAMP     NOT NULL,
    location     VARCHAR(200)  NOT NULL,
    capacity     INTEGER       NOT NULL,
    ticket_price NUMERIC(10,2) NOT NULL,
    created_at   TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP
);
