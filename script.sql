insert into public.hotel (code, nom, aeroport) values 
('HOT1', 'Hotel 1', false),
('HOT2', 'Hotel 2', false),
('AERO1', 'Aeroport', true);

insert into public.distance (code_from, code_to, distance_km)
values ('AERO1', 'HOT1', 90),
('AERO1', 'HOT2', 35),
('HOT1', 'HOT2', 60);

INSERt into public.vehicule (ref,nbr_place, type_carburant, heure_dispo, vitesse_moyenne) 
values 
('V1', 5, 'D', '09:00', 50),
('V2', 5, 'ES', '09:00', 50),
('V3', 12, 'D', '08:00', 50),
('V4',9, 'D', '09:00', 50),
('V5', 12, 'ES', '13:00', 50);

insert into public.reservation (id_client, nb_passager, date_heure_arrivee, id_hotel, temps_attente_max)
values 
('CL1', 7, '2026-03-19 09:00:00', 1, 30),
('CL2', 20, '2026-03-19 08:00:00', 2, 30),
('CL3', 3, '2026-03-19 09:10:00', 1, 30),
('CL4', 10, '2026-03-19 09:15:00', 1, 30),
('CL5', 5, '2026-03-19 09:20:00', 1, 30),
('CL6', 12, '2026-03-19 13:30:00', 1, 30);