-- Utilisation de la base créée automatiquement
USE lakonta;

-- Table users
CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT NOT NULL AUTO_INCREMENT,
                                     name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
    );

-- Exemple de données initiales
INSERT INTO users (name) VALUES ('Alice'), ('Bob');
