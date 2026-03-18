-- Converted data from test-nyavo.sql to the schema used in data-test-assignment.sql
BEGIN;
TRUNCATE TABLE trajet_reservation RESTART IDENTITY CASCADE;
TRUNCATE TABLE trajet RESTART IDENTITY CASCADE;
TRUNCATE TABLE reservation RESTART IDENTITY CASCADE;
TRUNCATE TABLE vehicule RESTART IDENTITY CASCADE;
TRUNCATE TABLE distance RESTART IDENTITY CASCADE;
TRUNCATE TABLE hotel RESTART IDENTITY CASCADE;

-- Hotels (places converted)
INSERT INTO public.hotel (id, nom, code, aeroport) VALUES
(1, 'Ivato Aeroport', 'TNR', true),
(2, 'Colbert', 'COLB', false),
(3, 'Novotel', 'NOVO', false),
(4, 'Ibis', 'IBIS', false),
(5, 'Lokanga', 'LOKA', false);

-- Distances (converted using codes)
INSERT INTO public.distance (id, code_from, code_to, distance_km) VALUES
(1, 'TNR', 'COLB', 18.00),
(2, 'TNR', 'NOVO', 20.50),
(3, 'TNR', 'IBIS', 21.00),
(4, 'TNR', 'LOKA', 23.00),
(5, 'COLB', 'NOVO', 2.50),
(6, 'COLB', 'IBIS', 3.00),
(7, 'COLB', 'LOKA', 5.00),
(8, 'NOVO', 'IBIS', 1.50),
(9, 'NOVO', 'LOKA', 3.50),
(10, 'IBIS', 'LOKA', 2.50);

-- Vehicles mapped to vehicule table (ids chosen sequentially)
INSERT INTO public.vehicule (id, ref, nbr_place, type_carburant, vitesse_moyenne) VALUES
(1, 'MV-001', 10, 'D', 30.00),
(2, 'MV-002', 5, 'D', 30.00),
(3, 'MV-003', 5, 'D', 30.00),
(4, 'MV-004', 12, 'D', 30.00),
(5, 'MV-005', 8, 'ES', 30.00),
(6, 'MV-006', 8, 'D', 30.00);

-- Reservations converted (ids preserved where present)
-- Using client_info as id_client (varchar), place_id -> id_hotel
INSERT INTO public.reservation (id, nb_passager, id_client, id_hotel, date_heure_arrivee, temps_attente_max) VALUES
(101, 2, 'G1A', 3, '2035-05-20 10:00:00', 30),
(102, 1, 'G1B', 2, '2035-05-20 10:00:00', 30),
(103, 4, 'G1C', 3, '2035-05-20 10:00:00', 30),
(104, 3, 'G1D', 5, '2035-05-20 10:00:00', 30),
(105, 5, 'G1E', 4, '2035-05-20 10:00:00', 30),
(106, 2, 'T101', 3, '2036-03-13 08:00:00', 30),
(107, 8, 'T102', 4, '2036-03-13 08:10:00', 30),
(108, 5, 'T103', 2, '2036-03-13 08:20:00', 30),
(109, 3, 'T104', 5, '2036-03-13 08:25:00', 30),
(110, 4, 'T105', 3, '2036-03-13 08:28:00', 30),
(111, 6, 'T106', 4, '2036-03-13 08:35:00', 30),
(112, 2, 'T107', 2, '2036-03-13 08:50:00', 30),
(113, 7, 'T108', 5, '2036-03-13 09:00:00', 30),
(114, 1, 'T109', 3, '2036-03-13 09:05:00', 30),
(115, 5, 'T110', 4, '2036-03-13 09:25:00', 30),
(116, 10, 'T111', 3, '2036-03-13 10:00:00', 30),
(117, 12, 'T112', 2, '2036-03-13 10:10:00', 30),
(118, 5, 'T113', 2, '2036-03-13 11:30:00', 30),
(119, 5, 'T114', 3, '2036-03-13 11:45:00', 30),
(120, 8, 'T115', 5, '2036-03-13 13:00:00', 30),
(121, 8, 'T116', 4, '2036-03-13 15:00:00', 30);

SELECT setval('public.hotel_id_seq', (SELECT COALESCE(MAX(id),0) FROM public.hotel));
SELECT setval('public.distance_id_seq', (SELECT COALESCE(MAX(id),0) FROM public.distance));
SELECT setval('public.vehicule_id_seq', (SELECT COALESCE(MAX(id),0) FROM public.vehicule));
SELECT setval('public.reservation_id_seq', (SELECT COALESCE(MAX(id),0) FROM public.reservation));

COMMIT;
