package itu.framework.backoffice.entities;

import legacy.annotations.Column;
import legacy.annotations.Entity;
import legacy.annotations.Id;
import legacy.schema.BaseEntity;

@Entity(tableName = "vehicule")
public class Vehicule extends BaseEntity {
    public Vehicule() {
        super();
    }

    @Id
    @Column
    private Integer id;

    @Column
    private String ref;

    @Column(name = "nbr_place")
    private Integer nbrPlace;

    @Column(name = "type_carburant")
    private String typeCarburant;

    @Column(name = "vitesse_moyenne")
    private Double vitesseMoyenne;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public Integer getNbrPlace() {
        return nbrPlace;
    }

    public void setNbrPlace(Integer nbrPlace) {
        this.nbrPlace = nbrPlace;
    }

    public String getTypeCarburant() {
        return typeCarburant;
    }

    public void setTypeCarburant(String typeCarburant) {
        this.typeCarburant = typeCarburant;
    }

    public Double getVitesseMoyenne() {
        return vitesseMoyenne;
    }

    public void setVitesseMoyenne(Double vitesseMoyenne) {
        this.vitesseMoyenne = vitesseMoyenne;
    }

}
