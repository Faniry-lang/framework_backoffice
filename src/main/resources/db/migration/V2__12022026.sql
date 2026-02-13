CREATE TABLE token_client (
    id SERIAL PRIMARY KEY,
    token TEXT,
    expiration_date TIMESTAMP
)