CREATE TABLE usuarios (
    id         UUID          DEFAULT gen_random_uuid() PRIMARY KEY,
    username   VARCHAR(100)  NOT NULL UNIQUE,
    email      VARCHAR(255)  NOT NULL UNIQUE,
    password   VARCHAR(255)  NOT NULL,
    role       VARCHAR(50)   NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP     NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP     NOT NULL DEFAULT NOW()
);
