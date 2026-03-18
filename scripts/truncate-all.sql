-- Script de TRUNCATE pour la base (s'inspire de dmp-17-03-2026.sql)
-- Ce script supprime toutes les données des tables listées et réinitialise les séquences.
-- Utiliser avec précaution (détruit les données).

-- Tables détectées dans le dump : distance, hotel, ordre_depart, reservation, token_client, trajet, trajet_reservation, vehicule

BEGIN;

-- TRUNCATE en cascade pour respecter les FK et remettre les séquences à zéro
TRUNCATE TABLE public.trajet_reservation,
               public.trajet,
               public.ordre_depart,
               public.reservation,
               public.vehicule,
               public.distance,
               public.hotel,
               public.token_client
RESTART IDENTITY CASCADE;

COMMIT;

-- Exemple d'exécution (adapté à votre environnement) :
-- psql -h <host> -U <user> -d <database> -f scripts/truncate-all.sql

-- Remarques :
-- - Le mot-clé CASCADE assure que les contraintes FK sont prises en compte et que les tables filles sont vidées.
-- - RESTART IDENTITY remet les séquences (id) à leur valeur initiale afin d'éviter des conflits d'IDs après réinsertion.
-- - Si vous préférez supprimer sélectivement (sans cascade), exécutez les TRUNCATE dans l'ordre enfant -> parent manuellement.
-- - Sauvegardez vos données avant d'exécuter ce script en production.
