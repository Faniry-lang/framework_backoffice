package itu.framework.backoffice.entities;

import legacy.annotations.Column;
import legacy.annotations.Entity;
import legacy.annotations.Id;
import legacy.query.Comparator;
import legacy.query.FilterSet;
import legacy.schema.BaseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

    // @Column(name = "vitesse_moyenne")
    // Double vitesseMoyenne;

    /**
     * Récupère la vitesse moyenne du véhicule
     * Si pas définie, utilise la constante selon le type de carburant
     * 
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
     * 
     * @return Priorité (1=highest, 4=lowest)
     */
    public Integer getPriorite() {
        return Constants.Config.getFuelPriority(typeCarburant);
    }

    /**
     * Vérifie si le véhicule peut transporter le nombre de passagers demandé
     * 
     * @param nbPassagers Nombre de passagers
     * @return true si possible
     */
    public boolean canTransport(Integer nbPassagers) {
        return nbPassagers != null && nbrPlace != null && nbPassagers <= nbrPlace;
    }

    /**
     * Calcule le temps de trajet entre deux lieux pour ce véhicule
     * 
     * @param codeFrom Code du lieu de départ
     * @param codeTo   Code du lieu d'arrivée
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

    public List<Trajet> findTrajets(LocalDate date) throws Exception {
        FilterSet filterSet = new FilterSet();
        filterSet.add("date_trajet", Comparator.EQUALS, date);
        filterSet.add("id_vehicule", Comparator.EQUALS, this.id);
        return Trajet.filter(Trajet.class, filterSet);
    }

    public boolean estOccupe(List<Trajet> trajets, LocalDateTime dateTime) throws Exception {
        for(Trajet trajet : trajets) {
            if(
                    dateTime.isAfter(trajet.getHeureDepart()) &&
                            dateTime.isBefore(trajet.getHeureArrivee())
            ) {
                return true;
            }
        }
        return false;
    }

    public boolean estOccupe(LocalDate date, LocalDateTime dateTime) throws Exception {
        List<Trajet> trajetList = findTrajets(date);
        return estOccupe(trajetList, dateTime);
    }

    @Override
    public String toString() {
        return "{id:"+this.id+", ref: "+this.ref+" }";
    }
}
