-- Table hotel
CREATE TABLE hotel (
                       id INT PRIMARY KEY NOT NULL,
                       nom VARCHAR
);

-- Table reservation
CREATE TABLE reservation (
                             id INT PRIMARY KEY NOT NULL,
                             nb_passager INT,
                             id_client VARCHAR(5),
                             id_hotel INT,
                            date_reservation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                             CONSTRAINT fk_reservation_hotel
                                 FOREIGN KEY (id_hotel)
                                     REFERENCES hotel(id)
                                     ON UPDATE CASCADE
                                     ON DELETE RESTRICT
);