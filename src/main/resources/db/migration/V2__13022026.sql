-- Migration V2 - 13/02/2026
-- Création de la table vehicule

CREATE TABLE vehicule (
    id SERIAL PRIMARY KEY,
    ref VARCHAR(10),
    nbr_place INT,
    type_carburant VARCHAR(50)
);

-- Index pour optimiser les recherches par référence
CREATE INDEX idx_vehicule_ref ON vehicule(ref);
