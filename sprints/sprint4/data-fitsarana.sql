insert into vehicule (ref, nbr_place, vitesse_moyenne, type_carburant) values
('V-1', 12, 50, 'D'),
('V-2', 5,50,  'E'),
('V-3', 5, 50,'D'),
('V-4', 12, 50,'E');

insert into hotel (nom, code, aeroport) values
('Ivato', 'AEROPORT', true),
('Hotel1', 'HOTEL-1', false);


insert into reservation (nb_passager, id_client, id_hotel, date_heure_arrivee) values
(7, 'CLI1', 2, '2026-03-12T09:00:00'),
(11, 'CLI2', 2, '2026-03-12T09:00:00'),
(3, 'CLI3', 2, '2026-03-12T09:00:00'),
(1, 'CLI4', 2, '2026-03-12T09:00:00'),
(2, 'CLI5', 2, '2026-03-12T09:00:00'),
(20, 'CLI6', 2, '2026-03-12T09:00:00');

insert into distance (code_from, code_to, distance_km) values
('AEROPORT', 'HOTEL-1', 50);
