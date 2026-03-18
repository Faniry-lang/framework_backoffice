-- ============================================================
-- TRUNCATE ALL TABLES
-- Ordre respectant les dépendances de clés étrangères
-- ============================================================

TRUNCATE TABLE
    distance,
hotel,
reservation,
trajet,
trajet_reservation,
vehicule;
RESTART IDENTITY CASCADE;

