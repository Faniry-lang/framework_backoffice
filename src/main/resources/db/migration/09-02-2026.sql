truncate table reservation restart identity cascade ;
truncate table hotel restart identity cascade ;


insert into hotel ( nom ) values ( 'Colbert' );
insert into hotel ( nom ) values ( 'Novotel' );
insert into hotel ( nom ) values ( 'Ibis' );
insert into hotel ( nom ) values ( 'Lokanga' );

insert into reservation (
    nb_passager,
    id_client,
    id_hotel,
    date_heure_arrivee
) values ( 11,
           '4631',
           3,
           '2026-02-05 00:01:00' );

insert into reservation (
    nb_passager,
    id_client,
    id_hotel,
    date_heure_arrivee
) values ( 1,
           '4394',
           3,
           '2026-02-05 23:55:00' );

insert into reservation (
    nb_passager,
    id_client,
    id_hotel,
    date_heure_arrivee
) values ( 2,
           '8054',
           1,
           '2026-02-09 10:17:00' );

insert into reservation (
    nb_passager,
    id_client,
    id_hotel,
    date_heure_arrivee
) values ( 4,
           '1432',
           2,
           '2026-02-01 15:25:00' );

insert into reservation (
    nb_passager,
    id_client,
    id_hotel,
    date_heure_arrivee
) values ( 4,
           '7861',
           1,
           '2026-01-28 07:11:00' );

insert into reservation (
    nb_passager,
    id_client,
    id_hotel,
    date_heure_arrivee
) values ( 5,
           '3308',
           1,
           '2026-01-28 07:45:00' );

insert into reservation (
    nb_passager,
    id_client,
    id_hotel,
    date_heure_arrivee
) values ( 13,
           '4484',
           2,
           '2026-02-28 08:25:00' );

insert into reservation (
    nb_passager,
    id_client,
    id_hotel,
    date_heure_arrivee
) values ( 8,
           '9687',
           2,
           '2026-02-28 13:00:00' );

insert into reservation (
    nb_passager,
    id_client,
    id_hotel,
    date_heure_arrivee
) values ( 7,
           '6302',
           1,
           '2026-02-15 13:00:00' );

insert into reservation (
    nb_passager,
    id_client,
    id_hotel,
    date_heure_arrivee
) values ( 1,
           '8640',
           4,
           '2026-02-18 22:55:00' );