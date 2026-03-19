BEGIN;

TRUNCATE TABLE trajet_reservation RESTART IDENTITY CASCADE;
TRUNCATE TABLE trajet RESTART IDENTITY CASCADE;
TRUNCATE TABLE reservation RESTART IDENTITY CASCADE;
TRUNCATE TABLE vehicule RESTART IDENTITY CASCADE;
TRUNCATE TABLE distance RESTART IDENTITY CASCADE;
TRUNCATE TABLE hotel RESTART IDENTITY CASCADE;

-- 1. Setup Hotels and Distances
INSERT INTO public.hotel (id, nom, code, aeroport) VALUES
(1, 'Aéroport Ivato', 'AERO1', true),
(2, 'Test Hotel', 'HOT01', false);

INSERT INTO public.distance (code_from, code_to, distance_km) VALUES
('AERO1', 'HOT01', 10.00);

-- 2. Setup Vehicles
-- Two small vehicles to force a split for a large group
-- Vehicle 1: Capacity 4
INSERT INTO public.vehicule (id, ref, nbr_place, type_carburant, vitesse_moyenne) VALUES
(1, 'SMALL_1', 4, 'ES', 50.00),
(2, 'SMALL_2', 4, 'ES', 50.00);

-- 3. Setup Split Reservation
-- Reservation for 7 people. 
-- Expectation: 
-- - Vehicle 1 takes 4 passengers
-- - Vehicle 2 takes 3 passengers
INSERT INTO public.reservation (id, nb_passager, id_client, id_hotel, date_heure_arrivee, temps_attente_max) VALUES
(1, 7, 'CLIENT_SPLIT', 2, '2026-06-01 10:00:00', 60);

SELECT setval('public.hotel_id_seq', (SELECT COALESCE(MAX(id),0) FROM public.hotel));
SELECT setval('public.distance_id_seq', (SELECT COALESCE(MAX(id),0) FROM public.distance));
SELECT setval('public.vehicule_id_seq', (SELECT COALESCE(MAX(id),0) FROM public.vehicule));
SELECT setval('public.reservation_id_seq', (SELECT COALESCE(MAX(id),0) FROM public.reservation));

COMMIT;

-- ============================================================
-- VERIFICATION QUERIES (Run these AFTER running the assignment process)
-- ============================================================

/*
-- 1. Check if the reservation was split into multiple trajets
SELECT 
    r.id AS reservation_id,
    r.nb_passager AS total_passengers,
    COUNT(tr.id_trajet) AS number_of_vehicles_used,
    SUM(tr.nbr_passager) AS assigned_passengers
FROM 
    reservation r
JOIN 
    trajet_reservation tr ON r.id = tr.id_reservation
WHERE 
    r.id = 1
GROUP BY 
    r.id;
-- Expected Result: number_of_vehicles_used should be > 1 (likely 2), assigned_passengers should be 7.

-- 2. Detailed view of the split
SELECT 
    t.id AS trajet_id,
    v.ref AS vehicle_ref,
    v.nbr_place AS vehicle_capacity,
    tr.nbr_passager AS passengers_in_this_vehicle
FROM 
    trajet_reservation tr
JOIN 
    trajet t ON tr.id_trajet = t.id
JOIN 
    vehicule v ON t.id_vehicule = v.id
WHERE 
    tr.id_reservation = 1;
-- Expected Result: 
-- Row 1: Vehicle X with 4 passengers
-- Row 2: Vehicle Y with 3 passengers
*/
