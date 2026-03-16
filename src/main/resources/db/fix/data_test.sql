-- ============================================================
-- TEST DATA
-- Schema target: V6__full_schema_consolidated.sql
-- ============================================================

-- ------------------------------------------------------------
-- HOTELS / LIEUX
-- ------------------------------------------------------------
INSERT INTO hotel (id, nom, code, aeroport) VALUES
    (1, 'Hotel Colbert',     'HOT001', FALSE),
    (2, 'Novotel Ivato',     'HOT002', FALSE),
    (3, 'Ibis Ankorondrano', 'HOT003', FALSE),
    (4, 'Carlton Anosy',     'HOT004', FALSE),
    (5, 'Le Louvre',         'HOT005', FALSE),
    (6, 'Aeroport Ivato',    'AERO01', TRUE);

SELECT setval('hotel_id_seq', (SELECT MAX(id) FROM hotel));

-- ------------------------------------------------------------
-- VEHICULES
-- ------------------------------------------------------------
INSERT INTO vehicule (id, ref, nbr_place, type_carburant, vitesse_moyenne) VALUES
    (1, 'VH001', 8,  'D',  65.0),
    (2, 'VH002', 6,  'ES', 60.0),
    (3, 'VH003', 8,  'H',  55.0),
    (4, 'VH004', 4,  'EL', 50.0),
    (5, 'VH005', 12, 'D',  65.0);

SELECT setval('vehicule_id_seq', (SELECT MAX(id) FROM vehicule));

-- ------------------------------------------------------------
-- DISTANCES
-- V6 keeps UNIQUE(code_from, code_to): avoid duplicate pairs.
-- ------------------------------------------------------------
INSERT INTO distance (code_from, code_to, distance_km, created_at) VALUES
    ('AERO01', 'HOT001', 15.5, NOW()),
    ('AERO01', 'HOT002', 22.3, NOW()),
    ('AERO01', 'HOT003', 18.7, NOW()),
    ('AERO01', 'HOT004', 12.9, NOW()),
    ('AERO01', 'HOT005', 10.4, NOW()),
    ('HOT001', 'HOT002', 12.1, NOW()),
    ('HOT001', 'HOT003', 8.9, NOW()),
    ('HOT002', 'HOT003', 14.2, NOW()),
    ('HOT004', 'HOT005', 5.5, NOW()),
    ('HOT003', 'HOT004', 7.8, NOW());

-- ------------------------------------------------------------
-- CONSTANTS
-- Must follow V6 CHECK(type IN ('INT','DECIMAL','STRING','BOOLEAN')).
-- ------------------------------------------------------------
INSERT INTO constants (code, value, type, description, created_at, updated_at) VALUES
    ('DEFAULT_WAIT_TIME', '30',   'INT',     'Default max wait time in minutes',          NOW(), NOW()),
    ('MAX_WAIT_TIME',     '60',   'INT',     'Absolute max wait time in minutes',         NOW(), NOW()),
    ('MIN_WAIT_TIME',     '10',   'INT',     'Minimum wait time in minutes',              NOW(), NOW()),
    ('SPEED_DIESEL',      '65.0', 'DECIMAL', 'Average speed Diesel km/h',                 NOW(), NOW()),
    ('SPEED_ESSENCE',     '60.0', 'DECIMAL', 'Average speed Essence km/h',                NOW(), NOW()),
    ('SPEED_HYBRIDE',     '55.0', 'DECIMAL', 'Average speed Hybride km/h',                NOW(), NOW()),
    ('SPEED_ELECTRIQUE',  '50.0', 'DECIMAL', 'Average speed Electrique km/h',             NOW(), NOW()),
    ('SPEED_DEFAULT',     '60.0', 'DECIMAL', 'Default average speed km/h',                NOW(), NOW()),
    ('MAX_PASSENGERS_PER_VEHICLE', '8', 'INT', 'Max passengers per vehicle',              NOW(), NOW()),
    ('PLANNING_TIME_BUFFER',      '15', 'INT', 'Planning buffer in minutes',              NOW(), NOW()),
    ('AEROPORT_STOP_TIME',        '10', 'INT', 'Stop time at airport in minutes',         NOW(), NOW()),
    ('HOTEL_STOP_TIME',           '5',  'INT', 'Stop time per hotel in minutes',          NOW(), NOW()),
    ('FUEL_PRIORITY_D',           '1',  'INT', 'Fuel priority Diesel',                    NOW(), NOW()),
    ('FUEL_PRIORITY_ES',          '2',  'INT', 'Fuel priority Essence',                   NOW(), NOW()),
    ('FUEL_PRIORITY_H',           '3',  'INT', 'Fuel priority Hybride',                   NOW(), NOW()),
    ('FUEL_PRIORITY_EL',          '4',  'INT', 'Fuel priority Electrique',                NOW(), NOW());

-- ------------------------------------------------------------
-- TOKENS
-- token_client is removed, use token table only.
-- ------------------------------------------------------------
INSERT INTO token (token, date_expiration, created_at) VALUES
    ('test-token-123456',  NOW() + INTERVAL '30 days', NOW()),
    ('admin-token-789012', NOW() + INTERVAL '90 days', NOW()),
    ('dev-token-345678',   NOW() + INTERVAL '15 days', NOW());

-- ------------------------------------------------------------
-- RESERVATIONS
-- V6 keeps id_client as VARCHAR(5), so values must be <= 5 chars.
-- ------------------------------------------------------------
INSERT INTO reservation (nb_passager, id_client, id_hotel, date_heure_arrivee, temps_attente_max) VALUES
    (2, 'C0001', 1, NOW() + INTERVAL '2 hours',          30),
    (4, 'C0002', 3, NOW() + INTERVAL '3 hours',          45),
    (1, 'C0003', 6, NOW() + INTERVAL '4 hours',          30),
    (3, 'C0004', 2, NOW() + INTERVAL '1 day',            30),
    (6, 'C0005', 4, NOW() + INTERVAL '1 day 2 hours',    60),
    (2, 'C0006', 5, NOW() + INTERVAL '1 day 5 hours',    30),
    (5, 'C0007', 1, NOW() + INTERVAL '2 days',           45),
    (3, 'C0008', 5, NOW() + INTERVAL '2 days 3 hours',   30),
    (4, 'C0009', 2, NOW() - INTERVAL '2 days',           30);

-- ------------------------------------------------------------
-- TRAJETS
-- ------------------------------------------------------------
INSERT INTO trajet (id_vehicule, date_trajet, heure_depart, heure_arrivee, distance_totale, ordre_visites) VALUES
    (1, CURRENT_DATE,     NOW() + INTERVAL '1 hour',           NOW() + INTERVAL '2 hours 20 minutes', 34.2, 'AERO01,HOT001,HOT003'),
    (2, CURRENT_DATE,     NOW() + INTERVAL '2 hours',          NOW() + INTERVAL '3 hours 10 minutes', 22.3, 'AERO01,HOT002'),
    (3, CURRENT_DATE + 1, NOW() + INTERVAL '1 day 1 hour',     NOW() + INTERVAL '1 day 3 hours',      27.4, 'AERO01,HOT004,HOT005');

-- ------------------------------------------------------------
-- TRAJET_RESERVATION
-- V6 has UNIQUE(id_trajet, id_reservation)
-- ------------------------------------------------------------
INSERT INTO trajet_reservation (id_trajet, id_reservation, ordre_visite) VALUES
    (1, 1, 1),
    (1, 2, 2),
    (2, 4, 1),
    (3, 5, 1),
    (3, 6, 2);
