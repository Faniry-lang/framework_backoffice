package itu.framework.backoffice.entities;

import legacy.annotations.*;
import legacy.schema.BaseEntity;
import legacy.strategy.GeneratedAfterPersistence;

@Entity(tableName = "vehicule")
public class Vehicule extends BaseEntity {
    @Id
    @Column
    @Generated(strategy = GeneratedAfterPersistence.class)
    Integer id;

    @Column
    String ref;

    @Column(name = "nbr_place")
    Integer nbrPlace;

    @Column(name = "type_carburant")
    String typeCarburant;

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
}
