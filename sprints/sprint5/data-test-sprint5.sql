-- Données de test pour Sprint 5
-- Objectif : tester le comportement de l'assignation (temps d'attente, grouping, véhicules occupés)

-- Véhicules
insert into vehicule (ref, nbr_place, vitesse_moyenne, type_carburant) values
('V-1', 12, 60, 'D'),
('V-2', 5, 55, 'ES'),
('V-3', 5, 50, 'D'),
('V-4', 12, 65, 'EL'),
('V-5', 8, 60, 'ES');

-- Lieux / Hôtels (Aéroport + hôtels destinataires)
insert into hotel (nom, code, aeroport) values
('Aéroport Ivato', 'AERO01', true),
('Hotel Central', 'HOT001', false),
('Hotel Ocean', 'HOT002', false),
('Hotel Mountain', 'HOT003', false);

-- Distances (bidirectionnelles attendues par la logique)
insert into distance (code_from, code_to, distance_km) values
('AERO01', 'HOT001', 30.00),
('AERO01', 'HOT002', 50.00),
('AERO01', 'HOT003', 80.00),
('HOT001', 'HOT002', 25.00),
('HOT001', 'HOT003', 60.00),
('HOT002', 'HOT003', 40.00);

-- Réservations (même date) : construire des fenêtres d'arrivée serrées
-- Format: (nb_passager, id_client, id_hotel, date_heure_arrivee, temps_attente_max)
-- Scénario principal (date 2026-03-12) : plusieurs vols rapprochés le matin
insert into reservation (nb_passager, id_client, id_hotel, date_heure_arrivee, temps_attente_max) values
(2, 'C1', 2, '2026-03-12 08:20:00', 30), -- vol A 08:20, attente 30min => fenêtre jusqu'à 08:50
(1, 'C2', 2, '2026-03-12 08:25:00', 10), -- vol B 08:25, attente 10min => fenêtre jusqu'à 08:35
(3, 'C3', 3, '2026-03-12 08:30:00', 20), -- vol C 08:30, attente 20min => fenêtre jusqu'à 08:50
(4, 'C4', 4, '2026-03-12 08:35:00', 30), -- vol D 08:35, attente 30min
(5, 'C5', 3, '2026-03-12 08:45:00', 15), -- vol E 08:45
(2, 'C6', 2, '2026-03-12 09:00:00', 30), -- vol F 09:00 (devrait aller sur une autre fenêtre)
(5, 'C7', 4, '2026-03-12 08:50:00', 15), -- vol G 08:50
(6, 'C8', 3, '2026-03-12 16:00:00', 30); -- vol tardif pour vérifier ré-utilisation possible

-- Données supplémentaires pour tester limitation de capacité
insert into reservation (nb_passager, id_client, id_hotel, date_heure_arrivee, temps_attente_max) values
(11, 'C9', 2, '2026-03-12 08:20:00', 30), -- très grand groupe (11 pax) à 08:20 (doit aller dans véhicule 12 places)
(7, 'C10', 2, '2026-03-12 08:22:00', 30); -- 7 pax à 08:22

-- Trajets existants (simulent véhicules occupés avant 08:00-09:00)
-- Véhicule V-2 (id = 2) est occupé et arrive seulement à 08:25
insert into trajet (id_vehicule, date_trajet, heure_depart, heure_arrivee, distance_totale, ordre_visites) values
(2, '2026-03-12', '2026-03-12 07:30:00', '2026-03-12 08:25:00', 60.00, 'AERO01,HOT001,AERO01'),
(3, '2026-03-12', '2026-03-12 07:00:00', '2026-03-12 07:50:00', 40.00, 'AERO01,HOT002,AERO01'),
(5, '2026-03-12', '2026-03-12 08:10:00', '2026-03-12 08:40:00', 45.00, 'AERO01,HOT003,AERO01');

-- Remarques / scénarios à vérifier manuellement après import :
-- 1) La réservation C1 (08:20, attente 30) : fenêtre 08:20-08:50. V-2 est libre à 08:25 => il peut être candidat et départ pourra être 08:25 (subtilité demandée).
-- 2) Groupement : toutes les réservations dont l'heure est dans [08:20, 08:50] doivent être regroupées si la capacité le permet (V-1 ou V-4 12 places peuvent prendre plusieurs demandes).
-- 3) Les réservations hors fenêtre (ex : 09:00) resteront non assignées pour cette fenêtre et seront traitées dans l'itération suivante.
-- 4) Limitation par capacité : C9 (11 pax) doit être affectée à un véhicule >=11 places (V-1 ou V-4), pas aux 5 places.
-- 5) Véhicules déjà occupés (V-5 occupé 08:10-08:40) ne doivent pas être utilisés pour trajets chevauchant leur intervalle.

-- Fin du fichier de test

-- Résultats:
framework_backoffice=# select * from trajet;
id | id_vehicule | date_trajet |    heure_depart     |    heure_arrivee    | distance_totale |    ordre_visites
----+-------------+-------------+---------------------+---------------------+-----------------+----------------------
  1 |           2 | 2026-03-12  | 2026-03-12 07:30:00 | 2026-03-12 08:25:00 |           60.00 | AERO01,HOT001,AERO01
  2 |           3 | 2026-03-12  | 2026-03-12 07:00:00 | 2026-03-12 07:50:00 |           40.00 | AERO01,HOT002,AERO01
  3 |           5 | 2026-03-12  | 2026-03-12 08:10:00 | 2026-03-12 08:40:00 |           45.00 | AERO01,HOT003,AERO01
  4 |           1 | 2026-03-12  | 2026-03-12 08:25:00 | 2026-03-12 09:25:00 |           60.00 | AERO01,HOT001,AERO01
  5 |           5 | 2026-03-12  | 2026-03-12 08:40:00 | 2026-03-12 09:40:00 |           60.00 | AERO01,HOT001,AERO01
  6 |           5 | 2026-03-12  | 2026-03-12 16:00:00 | 2026-03-12 17:40:00 |          100.00 | AERO01,HOT002,AERO01
  7 |           3 | 2026-03-12  | 2026-03-12 08:50:00 | 2026-03-12 10:50:00 |          100.00 | AERO01,HOT002,AERO01
(7 rows)


framework_backoffice=# select * from trajet_reservation;
id | id_trajet | id_reservation | ordre_visite
----+-----------+----------------+--------------
  1 |         4 |              9 |            1
  2 |         4 |              2 |            2
  3 |         5 |             10 |            1
  4 |         6 |              8 |            1
  5 |         7 |              5 |            1
(5 rows)
