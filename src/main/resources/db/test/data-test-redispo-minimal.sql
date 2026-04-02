-- Minimal test data for return-vehicle behavior
-- Vehicle returns at 08:00 and should pick up an earlier unassigned reservation and a same-time reservation

TRUNCATE TABLE trajet_reservation CASCADE;
TRUNCATE TABLE trajet CASCADE;
TRUNCATE TABLE reservation CASCADE;
TRUNCATE TABLE vehicule CASCADE;
TRUNCATE TABLE hotel CASCADE;
TRUNCATE TABLE distance CASCADE;

-- Hotels (airport + one hotel)
INSERT INTO hotel (id, nom, code, aeroport) VALUES (1, 'Aéroport Ivato', 'AERO1', TRUE);
INSERT INTO hotel (id, nom, code, aeroport) VALUES (2, 'Hotel Test', 'HOT01', FALSE);

-- Distances (both directions for safety)
INSERT INTO distance (code_from, code_to, distance_km) VALUES ('AERO1','HOT01',10.0);
INSERT INTO distance (code_from, code_to, distance_km) VALUES ('HOT01','AERO1',10.0);

-- Vehicle (capacity 4)
INSERT INTO vehicule (id, ref, nbr_place, type_carburant, vitesse_moyenne) VALUES (1, 'MV-001', 4, 'D', 60.0);

-- Existing trajet that makes vehicle 1 return at 08:00
INSERT INTO trajet (id, id_vehicule, date_trajet, heure_depart, heure_arrivee, distance_totale, ordre_visites)
VALUES (1, 1, '2026-03-20', '2026-03-20 07:00:00', '2026-03-20 08:00:00', 20.0, 'AERO1,HOT01,AERO1');

-- Reservations: one earlier (07:50) and one at same-time (08:00) — together fill the vehicle
INSERT INTO reservation (id, nb_passager, id_client, id_hotel, date_heure_arrivee, temps_attente_max)
VALUES (101, 2, 5001, 2, '2026-03-20 07:50:00', 30);

INSERT INTO reservation (id, nb_passager, id_client, id_hotel, date_heure_arrivee, temps_attente_max)
VALUES (102, 2, 5002, 2, '2026-03-20 08:00:00', 30);

COMMIT;
