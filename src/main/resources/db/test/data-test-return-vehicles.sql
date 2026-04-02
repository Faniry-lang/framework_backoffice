-- Test data for return-vehicle grouping
-- Scenario: vehicles return during day and should try to fill with previous unassigned reservations
-- Default wait time assumed = 30 minutes (Constants.Config.getDefaultWaitTime)

-- Clean relevant tables (use with caution)
TRUNCATE TABLE trajet_reservation CASCADE;
TRUNCATE TABLE trajet CASCADE;
TRUNCATE TABLE reservation CASCADE;
TRUNCATE TABLE vehicule CASCADE;
TRUNCATE TABLE hotel CASCADE;
TRUNCATE TABLE distance CASCADE;

-- Insert hotels (including airport)
INSERT INTO hotel (id, nom, code, aeroport) VALUES (1, 'Aéroport Ivato', 'AERO1', TRUE);
INSERT INTO hotel (id, nom, code, aeroport) VALUES (2, 'Hotel Alpha', 'HOT01', FALSE);
INSERT INTO hotel (id, nom, code, aeroport) VALUES (3, 'Hotel Beta', 'HOT02', FALSE);
INSERT INTO hotel (id, nom, code, aeroport) VALUES (4, 'Hotel Gamma', 'HOT03', FALSE);

-- Insert distances (symmetric entries)
INSERT INTO distance (code_from, code_to, distance_km) VALUES ('AERO1','HOT01',12.5);
INSERT INTO distance (code_from, code_to, distance_km) VALUES ('AERO1','HOT02',20.0);
INSERT INTO distance (code_from, code_to, distance_km) VALUES ('AERO1','HOT03',8.0);
INSERT INTO distance (code_from, code_to, distance_km) VALUES ('HOT01','HOT02',15.0);
INSERT INTO distance (code_from, code_to, distance_km) VALUES ('HOT01','HOT03',10.0);
INSERT INTO distance (code_from, code_to, distance_km) VALUES ('HOT02','HOT03',18.0);

-- Insert vehicles
-- id, ref, nbr_place, type_carburant, vitesse_moyenne
INSERT INTO vehicule (id, ref, nbr_place, type_carburant, vitesse_moyenne, heure_dispo) VALUES (1,'MV-001',4,'D',60.0, '00:00');
INSERT INTO vehicule (id, ref, nbr_place, type_carburant, vitesse_moyenne, heure_dispo) VALUES (2,'MV-002',8,'ES',60.0, '00:00');
INSERT INTO vehicule (id, ref, nbr_place, type_carburant, vitesse_moyenne, heure_dispo) VALUES (3,'MV-003',12,'D',60.0, '00:00');

-- Existing trajet that makes vehicle 2 return at 08:50 (so it's busy before).
-- We'll create trajet so vehicle 2 is occupied from 07:30 to 08:50.
INSERT INTO trajet (id, id_vehicule, date_trajet, heure_depart, heure_arrivee, distance_totale, ordre_visites)
VALUES (100,2,'2026-03-20','2026-03-20 07:30:00','2026-03-20 08:50:00',25.0,'AERO1,HOT02,AERO1');

-- Reservations (unassigned) earlier and around the return time
-- Columns: id, nb_passager, id_client, id_hotel, date_heure_arrivee, temps_attente_max
-- A reservation at 07:50 (earlier) with 2 pax, within previous interval for vehicle 2
INSERT INTO reservation (id, nb_passager, id_client, id_hotel, date_heure_arrivee, temps_attente_max)
VALUES (201,2,1001,2,'2026-03-20 07:50:00',30);
-- A reservation at 08:00 with 3 pax
INSERT INTO reservation (id, nb_passager, id_client, id_hotel, date_heure_arrivee, temps_attente_max)
VALUES (202,3,1002,2,'2026-03-20 08:00:00',30);
-- A reservation at 08:10 with 4 pax
INSERT INTO reservation (id, nb_passager, id_client, id_hotel, date_heure_arrivee, temps_attente_max)
VALUES (203,4,1003,3,'2026-03-20 08:10:00',30);
-- A reservation at 08:20 with 6 pax
-- changed id_hotel from 1 (airport) to 4 (Hotel Gamma) because id_hotel must not be the airport
INSERT INTO reservation (id, nb_passager, id_client, id_hotel, date_heure_arrivee, temps_attente_max)
VALUES (204,6,1004,4,'2026-03-20 08:20:00',30);
-- A reservation at 09:00 later the day
INSERT INTO reservation (id, nb_passager, id_client, id_hotel, date_heure_arrivee, temps_attente_max)
VALUES (205,2,1005,3,'2026-03-20 09:00:00',30);

-- The idea:
-- Vehicle 2 returns at 08:50. When testing assignRemainingReservationsToReturnVehicles on date 2026-03-20,
-- it should try to fill vehicle 2 with previous unassigned reservations (201,202,203,204)
-- Reservations 201,202,203 have arrival times before 08:50 (07:50,08:00,08:10) and may still be within their wait windows.
-- If vehicle 2 can be filled (capacity 8), it should depart immediately at 08:50. If not filled, it should create an "waiting" interval [08:50,09:20].

COMMIT;
