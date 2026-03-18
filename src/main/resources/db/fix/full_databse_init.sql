-- V6__full_schema_consolidated.sql
-- Consolidation of V1, V2, V3 and V5 into one final schema.
-- WARNING: this script recreates the domain tables and resets data.

BEGIN;

-- ------------------------------------------------------------
-- 1) DROP IN REVERSE DEPENDENCY ORDER
-- ------------------------------------------------------------
DROP TABLE IF EXISTS trajet_reservation CASCADE;
DROP TABLE IF EXISTS trajet CASCADE;
DROP TABLE IF EXISTS reservation CASCADE;
DROP TABLE IF EXISTS token CASCADE;
DROP TABLE IF EXISTS token_client CASCADE;
DROP TABLE IF EXISTS distance CASCADE;
DROP TABLE IF EXISTS constants CASCADE;
DROP TABLE IF EXISTS vehicule CASCADE;
DROP TABLE IF EXISTS hotel CASCADE;

-- ------------------------------------------------------------
-- 2) CORE TABLES
-- ------------------------------------------------------------
CREATE TABLE hotel (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    code VARCHAR(10) UNIQUE NOT NULL,
    aeroport BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE vehicule (
    id SERIAL PRIMARY KEY,
    ref VARCHAR(10) NOT NULL,
    nbr_place INT NOT NULL,
    type_carburant VARCHAR(50) NOT NULL,
    vitesse_moyenne DECIMAL(5,2),
    CONSTRAINT check_vehicule_carburant CHECK (type_carburant IN ('D', 'ES', 'H', 'EL'))
);

CREATE TABLE reservation (
    id SERIAL PRIMARY KEY,
    nb_passager INT NOT NULL,
    id_client VARCHAR(5),
    id_hotel INT NOT NULL,
    date_heure_arrivee TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    temps_attente_max INT DEFAULT 30,
    CONSTRAINT fk_reservation_hotel
        FOREIGN KEY (id_hotel)
        REFERENCES hotel(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE distance (
    id SERIAL PRIMARY KEY,
    code_from VARCHAR(10) NOT NULL,
    code_to VARCHAR(10) NOT NULL,
    distance_km DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_distance_codes UNIQUE (code_from, code_to)
);

CREATE TABLE trajet (
    id SERIAL PRIMARY KEY,
    id_vehicule INT NOT NULL,
    date_trajet DATE NOT NULL,
    heure_depart TIMESTAMP,
    heure_arrivee TIMESTAMP,
    distance_totale DECIMAL(10,2),
    ordre_visites TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_trajet_vehicule
        FOREIGN KEY (id_vehicule)
        REFERENCES vehicule(id)
        ON DELETE CASCADE
);

CREATE TABLE trajet_reservation (
    id SERIAL PRIMARY KEY,
    id_trajet INT NOT NULL,
    id_reservation INT NOT NULL,
    ordre_visite INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_trajet_reservation_trajet
        FOREIGN KEY (id_trajet)
        REFERENCES trajet(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_trajet_reservation_reservation
        FOREIGN KEY (id_reservation)
        REFERENCES reservation(id)
        ON DELETE CASCADE,
    CONSTRAINT uq_trajet_reservation UNIQUE (id_trajet, id_reservation)
);

CREATE TABLE token (
    id SERIAL PRIMARY KEY,
    token VARCHAR(255) UNIQUE NOT NULL,
    date_expiration TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE constants (
    id SERIAL PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL,
    value VARCHAR(255) NOT NULL,
    type VARCHAR(20) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT check_constants_type CHECK (type IN ('INT', 'DECIMAL', 'STRING', 'BOOLEAN'))
);

-- ------------------------------------------------------------
-- 3) INDEXES
-- ------------------------------------------------------------
CREATE INDEX idx_hotel_code ON hotel(code);
CREATE INDEX idx_vehicule_ref ON vehicule(ref);
CREATE INDEX idx_reservation_hotel ON reservation(id_hotel);
CREATE INDEX idx_reservation_arrivee ON reservation(date_heure_arrivee);
CREATE INDEX idx_distance_codes ON distance(code_from, code_to);
CREATE INDEX idx_trajet_vehicule_date ON trajet(id_vehicule, date_trajet);
CREATE INDEX idx_trajet_reservation_trajet ON trajet_reservation(id_trajet);
CREATE INDEX idx_trajet_reservation_reservation ON trajet_reservation(id_reservation);
CREATE INDEX idx_token_value ON token(token);
CREATE INDEX idx_token_expiration ON token(date_expiration);
CREATE INDEX idx_constants_code ON constants(code);

-- ------------------------------------------------------------
-- 4) DEFAULT BUSINESS CONSTANTS
-- ------------------------------------------------------------
INSERT INTO constants (code, value, type, description) VALUES
    ('DEFAULT_WAIT_TIME', '30', 'INT', 'Temps d''attente maximum par defaut en minutes'),
    ('MAX_WAIT_TIME', '60', 'INT', 'Temps d''attente maximum absolu en minutes'),
    ('MIN_WAIT_TIME', '10', 'INT', 'Temps d''attente minimum en minutes'),
    ('SPEED_DIESEL', '65.0', 'DECIMAL', 'Vitesse moyenne vehicule Diesel en km/h'),
    ('SPEED_ESSENCE', '60.0', 'DECIMAL', 'Vitesse moyenne vehicule Essence en km/h'),
    ('SPEED_HYBRIDE', '55.0', 'DECIMAL', 'Vitesse moyenne vehicule Hybride en km/h'),
    ('SPEED_ELECTRIQUE', '50.0', 'DECIMAL', 'Vitesse moyenne vehicule electrique en km/h'),
    ('SPEED_DEFAULT', '60.0', 'DECIMAL', 'Vitesse moyenne par defaut en km/h'),
    ('MAX_PASSENGERS_PER_VEHICLE', '8', 'INT', 'Nombre maximum de passagers par vehicule'),
    ('PLANNING_TIME_BUFFER', '15', 'INT', 'Marge de temps en minutes entre les trajets'),
    ('AEROPORT_STOP_TIME', '10', 'INT', 'Temps d''arrêt a l''aeroport en minutes'),
    ('HOTEL_STOP_TIME', '5', 'INT', 'Temps d''arrêt par hôtel en minutes'),
    ('FUEL_PRIORITY_D', '1', 'INT', 'Priorite carburant Diesel (1=highest)'),
    ('FUEL_PRIORITY_ES', '2', 'INT', 'Priorite carburant Essence'),
    ('FUEL_PRIORITY_H', '3', 'INT', 'Priorite carburant Hybride'),
    ('FUEL_PRIORITY_EL', '4', 'INT', 'Priorite carburant electrique (4=lowest)')
ON CONFLICT (code) DO NOTHING;

COMMIT;