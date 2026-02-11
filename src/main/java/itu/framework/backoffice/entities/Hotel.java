package itu.framework.backoffice.entities;

import legacy.annotations.Column;
import legacy.annotations.Entity;
import legacy.annotations.Id;
import legacy.schema.BaseEntity;

@Entity(tableName = "hotel")
public class Hotel extends BaseEntity {
    @Id
    @Column
    Integer id;

    @Column(name = "nom")
    String nom;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}

