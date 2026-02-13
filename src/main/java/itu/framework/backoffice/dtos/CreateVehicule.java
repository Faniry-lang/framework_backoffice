package itu.framework.backoffice.dtos;

public class CreateVehicule {
    Integer id;
    String ref;
    Integer nbrPlace;
    String typeCarburant;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
