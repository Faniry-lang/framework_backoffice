-- Table hotel
CREATE TABLE hotel (
                       id SERIAL PRIMARY KEY ,
                       nom VARCHAR
);

-- Table reservation
CREATE TABLE reservation (
                             id SERIAL PRIMARY KEY ,
                             nb_passager INT,
                             id_client VARCHAR(5),
                             id_hotel INT,
                            date_heure_arrivee TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             CONSTRAINT fk_reservation_hotel
                                 FOREIGN KEY (id_hotel)
                                     REFERENCES hotel(id)
                                     ON UPDATE CASCADE
                                     ON DELETE RESTRICT
);

