-- Script creat in PostGreSQL pentru baza de date ISGBD_lab1. 
-- Ruleaza din PgAdmin prin query tool



-- Extensii
CREATE EXTENSION IF NOT EXISTS pg_prewarm;
CREATE EXTENSION IF NOT EXISTS pg_stat_statements;



-- Setari de sesiune sigure (per-session)
SET synchronous_commit = OFF;
SET commit_delay = 100000;  -- 100ms grouping
SET commit_siblings = 10;
SET maintenance_work_mem = '1.99GB';
SET work_mem = '1.99GB';


-- ==========================================================
-- TABELA users (partitie HASH pe user_id)
-- ==========================================================
CREATE TABLE IF NOT EXISTS users (
    user_id BIGSERIAL PRIMARY KEY,
    username TEXT NOT NULL,
    email TEXT NOT NULL,
    country_code VARCHAR(2),
    created_at TIMESTAMP DEFAULT NOW()
) PARTITION BY HASH (user_id);


DO $$
BEGIN
    FOR i IN 0..7 LOOP
        EXECUTE format(
            'CREATE TABLE IF NOT EXISTS users_p%s PARTITION OF users FOR VALUES WITH (MODULUS 8, REMAINDER %s);',
            i, i
        );
    END LOOP;
END $$;


-- ==========================================================
-- TABELA orders (partitie HASH pe user_id)
-- ==========================================================

CREATE TABLE IF NOT EXISTS orders (
    order_id BIGSERIAL,
    user_id BIGINT NOT NULL,
    order_total NUMERIC(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),
    status TEXT DEFAULT 'PENDING',
    PRIMARY KEY (user_id, order_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
) PARTITION BY HASH (user_id);


DO $$
BEGIN
    FOR i IN 0..7 LOOP
        EXECUTE format(
            'CREATE TABLE IF NOT EXISTS orders_p%s PARTITION OF orders FOR VALUES WITH (MODULUS 8, REMAINDER %s);',
            i, i
        );
    END LOOP;
END $$;



-- ==========================================================
-- INDEXI
-- ==========================================================
CREATE INDEX IF NOT EXISTS idx_orders_user_id ON orders (user_id);

ANALYZE;


SELECT * FROM users;
--SELECT * FROM orders;

-- SHOW log_directory;
-- SHOW log_filename;