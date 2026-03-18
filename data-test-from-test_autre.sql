-- Script de test adapté de test_autre.sql vers le schéma de dmp-17-03-2026.sql
-- Respecte les noms de colonnes et longueurs (code varchar(10), ref varchar(10), id_client varchar(5), type_carburant varchar(50))
BEGIN;

-- HOTELS (id, nom, code, aeroport)
INSERT INTO hotel (id, nom, code, aeroport) VALUES
    (1, 'Colbert', 'COL', false),
    (2, 'Novotel', 'NOV', false),
    (3, 'Ibis', 'IBI', false),
    (4, 'Aeroport Ivato', 'TNR', true);

-- VEHICULES (id, ref, nbr_place, type_carburant, vitesse_moyenne)
INSERT INTO vehicule (id, ref, nbr_place, type_carburant, vitesse_moyenne) VALUES
    (1, 'V-001', 3, 'ES', 30.00),
    (2, 'V-002', 3, 'D', 30.00),
    (3, 'V-003', 4, 'D', 30.00);

-- DISTANCES (id, code_from, code_to, distance_km)
-- On ajoute distances bidirectionnelles si besoin (ici entrées uniques suffisent pour l'exemple)
INSERT INTO distance (id, code_from, code_to, distance_km) VALUES
    (1, 'TNR', 'COL', 12.00),
    (2, 'TNR', 'NOV', 12.00),
    (3, 'TNR', 'IBI', 20.00),
    (4, 'COL', 'NOV', 5.00),
    (5, 'COL', 'IBI', 15.00),
    (6, 'NOV', 'IBI', 10.00);

-- RESERVATIONS (id, nb_passager, id_client, id_hotel, date_heure_arrivee, temps_attente_max)
-- Les IDs et dates sont alignés sur la version finale des mises à jour présentes dans test_autre.sql
INSERT INTO reservation (id, nb_passager, id_client, id_hotel, date_heure_arrivee, temps_attente_max) VALUES
    (1, 3, 'C001', 3, '2026-03-10 08:00:00', 30),
    (2, 2, 'C002', 1, '2026-03-10 08:10:00', 30),
    (3, 1, 'C003', 2, '2026-03-10 08:20:00', 30),
    (4, 4, 'C004', 1, '2026-03-10 10:00:00', 30),
    (5, 7, 'C005', 3, '2026-03-10 12:00:00', 30),
    (6, 2, 'C006', 1, '2026-03-10 12:30:00', 30),
    (7, 2, 'C007', 2, '2026-03-10 12:40:00', 30),
    (8, 2, 'C008', 1, '2026-03-10 13:00:00', 30),
    (9, 6, 'C009', 3, '2026-03-10 13:05:00', 30),
    (10, 4, 'C010', 3, '2026-03-10 13:10:00', 30),
    (11, 1, 'C011', 3, '2026-03-10 13:15:00', 30);

COMMIT;

-- Ajuster les séquences pour qu'elles suivent les IDs insérés (utile si on réutilise la DB après exécution)
SELECT setval('hotel_id_seq', 4, true);
SELECT setval('vehicule_id_seq', 3, true);
SELECT setval('distance_id_seq', 6, true);
SELECT setval('reservation_id_seq', 11, true);

-- Notes:
-- 1) Le schéma utilisé ici est celui du dump dmp-17-03-2026.sql (noms/longueurs : code varchar(10), ref varchar(10), id_client varchar(5), type_carburant varchar(50)).
-- 2) Les distances sont insérées avec des codes (code_from/code_to). Si votre application recherche distances par code, ces valeurs correspondent aux codes des hotels ci-dessus (TNR, COL, NOV, IBI).
-- 3) Si vous avez des contraintes d'unicité / clefs étrangères supplémentaires dans votre instance, exécutez ce script après avoir vidé ou sauvegardé les tables concernées.
-- 4) Je peux générer aussi des INSERTs pour les tables trajet / trajet_reservation si vous voulez tester la planification avec trajets existants.
