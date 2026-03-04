ALTER TABLE vehicule ADD COLUMN vitesse_moyenne DECIMAL(5,2) DEFAULT 60.0;
ALTER TABLE hotel ADD COLUMN code VARCHAR(10) UNIQUE;
ALTER TABLE hotel ADD COLUMN aeroport BOOLEAN DEFAULT FALSE;
CREATE INDEX code_index ON hotel(code);

CREATE TABLE distance (
    id SERIAL PRIMARY KEY,
    code_from VARCHAR(10),
    code_to VARCHAR(10),
    distance_km DECIMAL(10, 2)
);

CREATE INDEX distance_from_index
ON distance(code_from);

CREATE INDEX distance_to_index
ON distance(code_to);

ALTER TABLE reservation ADD COLUMN temps_attente_max INT DEFAULT 30;

CREATE TABLE trajet (
    id SERIAL PRIMARY KEY ,
    id_vehicule INT NOT NULL REFERENCES vehicule(id),
    date_trajet DATE,
    heure_depart TIMESTAMP,
    heure_arrivee TIMESTAMP,
    distance_totale DECIMAL(10, 2),
    ordre_visites TEXT
);

CREATE INDEX trajet_vehicule_index
ON trajet(id_vehicule);

CREATE INDEX trajet_date_index
ON trajet(date_trajet);

CREATE TABLE trajet_reservation (
    id SERIAL PRIMARY KEY ,
    id_trajet INT REFERENCES trajet(id),
    id_reservation INT REFERENCES reservation(id),
    ordre_visite INT
);

CREATE INDEX trajet_res_index
ON trajet_reservation(id_reservation);

CREATE INDEX trajet_index
ON trajet_reservation(id_trajet);

UPDATE hotel SET code = 'HOT001' WHERE nom = 'Carlton';

INSERT INTO hotel (nom, code, aeroport) VALUES
('Ivato', 'HOT002', true);

INSERT INTO distance (code_from, code_to, distance_km) VALUES
('HOT001', 'HOT002', 273);

SELECT * FROM reservation
WHERE id NOT IN
(SELECT tr.id_reservation
FROM trajet t JOIN trajet_reservation tr
                   ON t.id = tr.id_trajet
WHERE t.date_trajet <= CURRENT_DATE)