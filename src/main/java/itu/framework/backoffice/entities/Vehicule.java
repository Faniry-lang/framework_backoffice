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

    @Column(name = "vitesse_moyenne")
    Double vitesseMoyenne;

    /**
     * Récupère la vitesse moyenne du véhicule
     * Si pas définie, utilise la constante selon le type de carburant
     * @return Vitesse moyenne en km/h
     */
    public Double getVitesseMoyenneEffective() {
        if (vitesseMoyenne != null) {
            return vitesseMoyenne;
        }
        // Utiliser les constantes
        return Constants.Config.getSpeedForFuel(typeCarburant);
    }

    /**
     * Récupère la priorité du véhicule selon son carburant
     * @return Priorité (1=highest, 4=lowest)
     */
    public Integer getPriorite() {
        return Constants.Config.getFuelPriority(typeCarburant);
    }

    /**
     * Vérifie si le véhicule peut transporter le nombre de passagers demandé
     * @param nbPassagers Nombre de passagers
     * @return true si possible
     */
    public boolean canTransport(Integer nbPassagers) {
        return nbPassagers != null && nbrPlace != null && nbPassagers <= nbrPlace;
    }

    /**
     * Calcule le temps de trajet entre deux lieux pour ce véhicule
     * @param codeFrom Code du lieu de départ
     * @param codeTo Code du lieu d'arrivée
     * @return Temps en minutes, ou null si impossible à calculer
     */
    public Integer calculateTravelTime(String codeFrom, String codeTo) {
        return Distance.calculateTravelTime(codeFrom, codeTo, getVitesseMoyenneEffective());
    }

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
