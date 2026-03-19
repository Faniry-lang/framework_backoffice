-- =====================================
-- DATA FROM GOOGLE SHEET CONVERTED
-- =====================================

BEGIN;

-- =====================================
-- HOTEL (places)
-- =====================================
INSERT INTO hotel (nom, code, aeroport) VALUES
                                            ('Aeroport', 'APT', true),
                                            ('Ibis', 'IBIS', false),
                                            ('Novotel', 'NOVO', false);

-- =====================================
-- PARAM (settings)
-- =====================================
-- =====================================
-- VEHICULES (simplifié)
-- =====================================
INSERT INTO vehicule (ref, nbr_place, type_carburant, vitesse_moyenne, heure_dispo) VALUES
                                                                           ('V1', 7, 'D', 30, '00:00'),
                                                                           ('V3', 5, 'D', 30, '13:00'),
                                                                           ('V2', 3, 'ES', 30, '00:00');

-- =====================================
-- DISTANCES
-- =====================================

-- Aéroport -> hôtels
INSERT INTO distance (code_from, code_to, distance_km) VALUES
                                                           ('APT', 'IBIS', 75.00), -- Aeroport -> Ibis
                                                           ('APT', 'NOVO', 60.00); -- Aeroport -> Novotel

-- Entre hôtels
INSERT INTO distance (code_from, code_to, distance_km) VALUES
    ('IBIS', 'NOVO', 43.00); -- Ibis -> Novotel

-- =====================================
-- RESERVATIONS
-- Date fixée au 2001-01-01 / Heures conservées
-- =====================================

INSERT INTO reservation (id_client, nb_passager, date_heure_arrivee, id_hotel, temps_attente_max) VALUES
                                                                                                      ('R1', 2, '2001-01-01 09:00:00', 2, 30),
                                                                                                      ('R3', 5, '2001-01-01 09:18:00', 2, 30),
                                                                                                      ('R4', 9, '2001-01-01 09:20:00', 3, 30),
                                                                                                      ('R2', 4, '2001-01-01 09:59:00', 3, 30);

COMMIT;