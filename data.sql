INSERT INTO hotel (nom, code, aeroport) VALUES
('Colbert', 'COL', false),
('Novotel', 'NOV', false),
('Ibis', 'IBI', false),
('Aeroport Ivato', 'TNR', true);

INSERT INTO vehicule (ref, nbr_place, type_carburant, vitesse_moyenne) VALUES
('V-001', 4, 'ES', 30),
('V-002', 6, 'D', 30),
('V-003', 6, 'D', 30);

INSERT INTO distance (code_from, code_to, distance_km) VALUES
('TNR', 'COL', 12.0),
('TNR', 'NOV', 12.0),
('TNR', 'IBI', 20.0),
('COL', 'NOV', 5.0),
('COL', 'IBI', 15.0),
('NOV', 'IBI', 10.0);

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
