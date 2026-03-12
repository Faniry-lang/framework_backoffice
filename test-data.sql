BEGIN;

-- =====================================
-- HOTELS
-- =====================================
INSERT INTO hotel (nom, code, libelle) VALUES ('Colbert', 'COL', 'hotel');
INSERT INTO hotel (nom, code, libelle) VALUES ('Novotel', 'NOV', 'hotel');
INSERT INTO hotel (nom, code, libelle) VALUES ('Ibis', 'IBI', 'hotel');
INSERT INTO hotel (nom, code, libelle) VALUES ('Aeroport Ivato', 'TNR', 'aeroport');

-- =====================================
-- PARAM (vitesse moyenne 30 km/h, temps attente 30 min)
-- =====================================
INSERT INTO param (vitesse_moyenne, temps_attente) VALUES (30, 30);

-- =====================================
-- VEHICULES
-- =====================================
INSERT INTO vehicule (reference, place, type_carburant) VALUES
('V-001', 4, 'ES'),
('V-002', 4, 'D'),
('V-003', 6, 'D');

-- =====================================
-- DISTANCES
-- =====================================
INSERT INTO distance (id_hotel_from, id_hotel_to, distance_value) VALUES (4, 1, 12.0);
INSERT INTO distance (id_hotel_from, id_hotel_to, distance_value) VALUES (4, 2, 12.0);
INSERT INTO distance (id_hotel_from, id_hotel_to, distance_value) VALUES (4, 3, 20.0);

INSERT INTO distance (id_hotel_from, id_hotel_to, distance_value) VALUES (1, 2, 5.0);
INSERT INTO distance (id_hotel_from, id_hotel_to, distance_value) VALUES (1, 3, 15.0);
INSERT INTO distance (id_hotel_from, id_hotel_to, distance_value) VALUES (2, 3, 10.0);

-- =====================================
-- RESERVATIONS
-- =====================================
INSERT INTO reservation (id_client, nb_passager, date_heure_arrivee, id_hotel)
VALUES
    ('C001', 3, '2026-03-10 08:00:00', 3), -- G1
    ('C002', 2, '2026-03-10 08:00:00', 1), -- G1
    ('C003', 1, '2026-03-10 08:00:00', 2), -- G1

    ('C004', 4, '2026-03-10 10:00:00', 1), -- alone

    ('C005', 7, '2026-03-10 12:00:00', 3), -- no assign, no > vehicule.p > 7

    ('C006', 2, '2026-03-10 14:00:00', 1), -- G2
    ('C007', 1, '2026-03-10 14:00:00', 2), -- G2

    ('C008', 2, '2026-03-10 16:00:00', 3), -- alone
    ('C009', 6, '2026-03-10 16:05:00', 3), -- alone
    ('C010', 4, '2026-03-10 16:10:00', 3), -- alone
    ('C011', 1, '2026-03-10 16:15:00', 3); -- no assign, all are occupied

COMMIT;
