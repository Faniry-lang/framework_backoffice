-- Data set to test AssignmentService grouping, vehicle selection and timing
BEGIN;
TRUNCATE TABLE trajet_reservation RESTART IDENTITY CASCADE;
TRUNCATE TABLE trajet RESTART IDENTITY CASCADE;
TRUNCATE TABLE reservation RESTART IDENTITY CASCADE;
TRUNCATE TABLE vehicule RESTART IDENTITY CASCADE;
TRUNCATE TABLE distance RESTART IDENTITY CASCADE;
TRUNCATE TABLE hotel RESTART IDENTITY CASCADE;

-- Hotels (including airport)
INSERT INTO public.hotel (id, nom, code, aeroport) VALUES
(1, 'Aéroport Ivato', 'AERO1', true),
(2, 'Hotel One', 'HOT01', false),
(3, 'Hotel Two', 'HOT02', false),
(4, 'Hotel Three', 'HOT03', false);

-- Distances (bidirectional entries)
INSERT INTO public.distance (id, code_from, code_to, distance_km) VALUES
(1, 'AERO1', 'HOT01', 15.00),
(2, 'AERO1', 'HOT02', 20.00),
(3, 'AERO1', 'HOT03', 25.00),
(4, 'HOT01', 'HOT02', 5.00),
(5, 'HOT02', 'HOT03', 7.00),
(6, 'HOT01', 'HOT03', 10.00);

-- Vehicles with varied capacities, fuel types and speeds
INSERT INTO public.vehicule (id, ref, nbr_place, type_carburant, vitesse_moyenne) VALUES
(1, 'VH01', 4, 'ES', 30.00),
(2, 'VH02', 6, 'D', 30.00),
(3, 'VH03', 8, 'ES', 30.00),
(4, 'VH04', 6, 'H', 30.00),
(5, 'VH05', 4, 'EL', 30.00);

-- Reservations designed to test grouping and reuse of vehicles
-- Morning cluster (08:00 - 09:00) should allow grouping into vehicles
INSERT INTO public.reservation (id, nb_passager, id_client, id_hotel, date_heure_arrivee, temps_attente_max) VALUES
(1, 2, 'C001', 2, '2026-03-20 08:00:00', 30),
(2, 2, 'C002', 3, '2026-03-20 08:10:00', 30),
(3, 1, 'C003', 4, '2026-03-20 08:15:00', 30),
(4, 3, 'C004', 2, '2026-03-20 08:20:00', 30),
(5, 4, 'C005', 3, '2026-03-20 08:25:00', 30),
(6, 2, 'C006', 4, '2026-03-20 09:00:00', 30),

-- Afternoon cluster (16:00 - 16:30) that should reuse vehicles if available
(7, 2, 'C007', 2, '2026-03-20 16:00:00', 30),
(8, 3, 'C008', 3, '2026-03-20 16:10:00', 30),
(9, 1, 'C009', 4, '2026-03-20 16:20:00', 30),
(10, 2, 'C010', 2, '2026-03-20 16:25:00', 30),
(11, 2, 'C011', 3, '2026-03-20 16:30:00', 30);

SELECT setval('public.hotel_id_seq', (SELECT COALESCE(MAX(id),0) FROM public.hotel));
SELECT setval('public.distance_id_seq', (SELECT COALESCE(MAX(id),0) FROM public.distance));
SELECT setval('public.vehicule_id_seq', (SELECT COALESCE(MAX(id),0) FROM public.vehicule));
SELECT setval('public.reservation_id_seq', (SELECT COALESCE(MAX(id),0) FROM public.reservation));

COMMIT;
