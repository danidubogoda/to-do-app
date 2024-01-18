CREATE TABLE IF NOT EXISTS task(
   id          SERIAL PRIMARY KEY,
   description VARCHAR(200) NOT NULL,
   status      BOOLEAN      NOT NULL,
   email VARCHAR(100) NOT NULL
);