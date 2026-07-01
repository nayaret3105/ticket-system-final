-- =============================================================
-- init.sql — Inicialización de bases de datos
-- Se ejecuta automáticamente al iniciar el contenedor PostgreSQL
-- Cada microservicio gestiona su propio esquema con Flyway
-- =============================================================

CREATE DATABASE auth_db;
CREATE DATABASE event_db;
CREATE DATABASE sales_db;
