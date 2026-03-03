-- Migration V5__03032026.sql
-- Corrections des migrations précédentes et ajout d'une table de constantes
-- Date: 03/03/2026

-- ========================================
-- 1. CRÉATION DE LA TABLE DE CONSTANTES
-- ========================================
CREATE TABLE constants (
    id SERIAL PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL,
    value VARCHAR(255) NOT NULL,
    type VARCHAR(20) NOT NULL, -- 'INT', 'DECIMAL', 'STRING', 'BOOLEAN'
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Index pour optimiser les recherches par code
CREATE INDEX idx_constants_code ON constants(code);

-- ========================================
-- 2. CORRECTIONS DU SCRIPT V3
-- ========================================

-- Corriger la table distance (noms de colonnes)
-- V3 utilisait 'from_' et 'to_', on standardise avec 'code_from' et 'code_to'
DROP TABLE IF EXISTS distance CASCADE;

CREATE TABLE distance (
    id SERIAL PRIMARY KEY,
    code_from VARCHAR(10) NOT NULL,
    code_to VARCHAR(10) NOT NULL,
    distance_km DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(code_from, code_to)
);

-- Index pour optimiser les recherches de distance
CREATE INDEX idx_distance_codes ON distance(code_from, code_to);

-- Corriger la table hotel
-- V3 avait 'aeroport_parent_id', on standardise avec 'aeroport' (boolean)
ALTER TABLE hotel DROP COLUMN IF EXISTS aeroport_parent_id CASCADE;
ALTER TABLE hotel ADD COLUMN IF NOT EXISTS aeroport BOOLEAN DEFAULT FALSE;

-- ========================================
-- 3. AJOUTS MANQUANTS DES TÂCHES PRÉCÉDENTES
-- ========================================

-- Ajouter la colonne vitesse_moyenne aux véhicules (sera gérée par constantes)
ALTER TABLE vehicule ADD COLUMN IF NOT EXISTS vitesse_moyenne DECIMAL(5,2);

-- Ajouter temps_attente_max aux réservations (sera gérée par constantes)
ALTER TABLE reservation ADD COLUMN IF NOT EXISTS temps_attente_max INTEGER;

-- Créer table trajet si elle n'existe pas
CREATE TABLE IF NOT EXISTS trajet (
    id SERIAL PRIMARY KEY,
    id_vehicule INTEGER NOT NULL,
    date_trajet DATE NOT NULL,
    heure_depart TIMESTAMP,
    heure_arrivee TIMESTAMP,
    distance_totale DECIMAL(10,2),
    ordre_visites TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_vehicule) REFERENCES vehicule(id) ON DELETE CASCADE,
    INDEX idx_trajet_vehicule_date (id_vehicule, date_trajet)
);

-- Créer table trajet_reservation si elle n'existe pas
CREATE TABLE IF NOT EXISTS trajet_reservation (
    id SERIAL PRIMARY KEY,
    id_trajet INTEGER NOT NULL,
    id_reservation INTEGER NOT NULL,
    ordre_visite INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_trajet) REFERENCES trajet(id) ON DELETE CASCADE,
    FOREIGN KEY (id_reservation) REFERENCES reservation(id) ON DELETE CASCADE,
    UNIQUE(id_trajet, id_reservation),
    INDEX idx_trajet_reservation_trajet (id_trajet),
    INDEX idx_trajet_reservation_reservation (id_reservation)
);

-- Standardiser la table token (V2 utilisait token_client)
-- Supprimer l'ancienne table si elle existe et créer la nouvelle
DROP TABLE IF EXISTS token_client CASCADE;

CREATE TABLE IF NOT EXISTS token (
    id SERIAL PRIMARY KEY,
    token VARCHAR(255) UNIQUE NOT NULL,
    date_expiration TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Index pour optimiser les recherches par token
CREATE INDEX idx_token_value ON token(token);
CREATE INDEX idx_token_expiration ON token(date_expiration);

-- ========================================
-- 4. DONNÉES DE CONSTANTES PAR DÉFAUT
-- ========================================

-- Constantes pour les temps d'attente (en minutes)
INSERT INTO constants (code, value, type, description) VALUES
    ('DEFAULT_WAIT_TIME', '30', 'INT', 'Temps d''attente maximum par défaut en minutes'),
    ('MAX_WAIT_TIME', '60', 'INT', 'Temps d''attente maximum absolu en minutes'),
    ('MIN_WAIT_TIME', '10', 'INT', 'Temps d''attente minimum en minutes');

-- Constantes pour les vitesses moyennes (en km/h)
INSERT INTO constants (code, value, type, description) VALUES
    ('SPEED_DIESEL', '65.0', 'DECIMAL', 'Vitesse moyenne véhicule Diesel en km/h'),
    ('SPEED_ESSENCE', '60.0', 'DECIMAL', 'Vitesse moyenne véhicule Essence en km/h'),
    ('SPEED_HYBRIDE', '55.0', 'DECIMAL', 'Vitesse moyenne véhicule Hybride en km/h'),
    ('SPEED_ELECTRIQUE', '50.0', 'DECIMAL', 'Vitesse moyenne véhicule Électrique en km/h'),
    ('SPEED_DEFAULT', '60.0', 'DECIMAL', 'Vitesse moyenne par défaut en km/h');

-- Constantes pour l'algorithme de planification
INSERT INTO constants (code, value, type, description) VALUES
    ('MAX_PASSENGERS_PER_VEHICLE', '8', 'INT', 'Nombre maximum de passagers par véhicule'),
    ('PLANNING_TIME_BUFFER', '15', 'INT', 'Marge de temps en minutes entre les trajets'),
    ('AEROPORT_STOP_TIME', '10', 'INT', 'Temps d''arrêt à l''aéroport en minutes'),
    ('HOTEL_STOP_TIME', '5', 'INT', 'Temps d''arrêt par hôtel en minutes');

-- Constantes pour les priorités de carburant
INSERT INTO constants (code, value, type, description) VALUES
    ('FUEL_PRIORITY_D', '1', 'INT', 'Priorité carburant Diesel (1=highest)'),
    ('FUEL_PRIORITY_ES', '2', 'INT', 'Priorité carburant Essence'),
    ('FUEL_PRIORITY_H', '3', 'INT', 'Priorité carburant Hybride'),
    ('FUEL_PRIORITY_EL', '4', 'INT', 'Priorité carburant Électrique (4=lowest)');

-- ========================================
-- 5. MISE À JOUR DES DONNÉES EXISTANTES
-- ========================================

-- Mettre à jour les codes des hôtels existants
UPDATE hotel SET code = CONCAT('HOT', LPAD(id::text, 3, '0')) WHERE code IS NULL;

-- Ajouter l'aéroport principal
INSERT INTO hotel (nom, code, aeroport)
SELECT 'Aéroport Ivato', 'AERO01', TRUE
WHERE NOT EXISTS (SELECT 1 FROM hotel WHERE code = 'AERO01');

-- Mettre à jour les vitesses des véhicules selon les constantes
UPDATE vehicule SET vitesse_moyenne =
    CASE
        WHEN type_carburant = 'D' THEN (SELECT value::DECIMAL FROM constants WHERE code = 'SPEED_DIESEL')
        WHEN type_carburant = 'ES' THEN (SELECT value::DECIMAL FROM constants WHERE code = 'SPEED_ESSENCE')
        WHEN type_carburant = 'H' THEN (SELECT value::DECIMAL FROM constants WHERE code = 'SPEED_HYBRIDE')
        WHEN type_carburant = 'EL' THEN (SELECT value::DECIMAL FROM constants WHERE code = 'SPEED_ELECTRIQUE')
        ELSE (SELECT value::DECIMAL FROM constants WHERE code = 'SPEED_DEFAULT')
    END
WHERE vitesse_moyenne IS NULL;

-- Mettre à jour les temps d'attente des réservations
UPDATE reservation SET temps_attente_max = (SELECT value::INT FROM constants WHERE code = 'DEFAULT_WAIT_TIME')
WHERE temps_attente_max IS NULL;

-- ========================================
-- 6. DONNÉES D'EXEMPLE
-- ========================================

-- Exemples de distances entre lieux
INSERT INTO distance (code_from, code_to, distance_km) VALUES
    ('AERO01', 'HOT001', 15.5),
    ('AERO01', 'HOT002', 22.3),
    ('AERO01', 'HOT003', 18.7),
    ('HOT001', 'HOT002', 12.1),
    ('HOT001', 'HOT003', 8.9),
    ('HOT002', 'HOT003', 14.2),
    ('HOT001', 'AERO01', 15.5),  -- Trajets retour
    ('HOT002', 'AERO01', 22.3),
    ('HOT003', 'AERO01', 18.7),
    ('HOT002', 'HOT001', 12.1),
    ('HOT003', 'HOT001', 8.9),
    ('HOT003', 'HOT002', 14.2)
ON CONFLICT (code_from, code_to) DO NOTHING;

-- Tokens d'exemple pour les tests API
INSERT INTO token (token, date_expiration) VALUES
    ('test-token-123456', '2026-12-31 23:59:59'),
    ('admin-token-789012', '2026-12-31 23:59:59'),
    ('dev-token-345678', '2026-06-30 23:59:59')
ON CONFLICT (token) DO NOTHING;

-- ========================================
-- 7. VÉRIFICATIONS ET CONTRAINTES
-- ========================================

-- Vérifier que tous les hôtels ont un code
UPDATE hotel SET code = CONCAT('HOT', LPAD(id::text, 3, '0')) WHERE code IS NULL OR code = '';

-- Ajouter des contraintes de validation
ALTER TABLE constants ADD CONSTRAINT check_constants_type
    CHECK (type IN ('INT', 'DECIMAL', 'STRING', 'BOOLEAN'));

-- Ajouter contrainte sur les types de carburant
ALTER TABLE vehicule ADD CONSTRAINT check_vehicule_carburant
    CHECK (type_carburant IN ('D', 'ES', 'H', 'EL'));
