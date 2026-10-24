CREATE TABLE IF NOT EXISTS musical_instruments (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    brand VARCHAR(120) NOT NULL,
    instrument_type VARCHAR(80) NOT NULL,
    material VARCHAR(120) NOT NULL,
    price NUMERIC(12, 2) NOT NULL CHECK (price >= 0),
    quantity INTEGER NOT NULL CHECK (quantity >= 0)
);
