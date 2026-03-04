package itu.framework.backoffice.entities;

import legacy.annotations.*;
import legacy.query.Comparator;
import legacy.query.FilterSet;
import legacy.schema.BaseEntity;
import legacy.strategy.GeneratedAfterPersistence;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity(tableName = "trajet")
public class Trajet extends BaseEntity {
    @Id
    @Column
    @Generated(strategy = GeneratedAfterPersistence.class)
    private Integer id;

    @Column(name = "id_vehicule")
    @ForeignKey(mappedBy = "vehicule", entity = Vehicule.class)
    private Integer idVehicule;

    @Column(name = "date_trajet")
    private LocalDate dateTrajet;

    @Column(name = "heure_depart")
    private LocalDateTime heureDepart;

    @Column(name = "heure_arrivee")
    private LocalDateTime heureArrivee;

    @Column(name = "distance_totale")
    private BigDecimal distanceTotale;

    @Column(name = "ordre_visites")
    private String ordreVisites; // JSON ou CSV: "AERO01,HOT001,HOT002,AERO01"

    /**
     * Trouve les trajets pour une date donnée
     * 
     * @param date La date de recherche
     * @return Liste des trajets pour cette date
     */
    public static List<Trajet> findByDate(LocalDate date) throws Exception {
        FilterSet filters = new FilterSet();
        filters.add("date_trajet", Comparator.EQUALS, date);
        return Trajet.filter(Trajet.class, filters);
    }

    /**
     * Trouve un trajet par véhicule et date
     * 
     * @param idVehicule L'ID du véhicule
     * @param date       La date
     * @return Le trajet ou null si non trouvé
     */
    public static Trajet findByVehiculeAndDate(Integer idVehicule, LocalDate date) throws Exception {
        FilterSet filters = new FilterSet();
        filters.add("id_vehicule", Comparator.EQUALS, idVehicule);
        filters.add("date_trajet", Comparator.EQUALS, date);

        List<Trajet> trajets = Trajet.filter(Trajet.class, filters);

        if (trajets != null && !trajets.isEmpty()) {
            return trajets.get(0);
        }

        return null;
    }

    /**
     * Récupère les réservations associées à ce trajet
     * 
     * @return Array des liaisons trajet-réservation
     */
    public TrajetReservation[] getReservations() throws Exception {
        List<TrajetReservation> reservations = TrajetReservation.findByTrajet(this.id);
        return reservations.toArray(new TrajetReservation[0]);
    }

    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdVehicule() {
        return idVehicule;
    }

    public void setIdVehicule(Integer idVehicule) {
        this.idVehicule = idVehicule;
    }

    public LocalDate getDateTrajet() {
        return dateTrajet;
    }

    public void setDateTrajet(LocalDate dateTrajet) {
        this.dateTrajet = dateTrajet;
    }

    public LocalDateTime getHeureDepart() {
        return heureDepart;
    }

    public void setHeureDepart(LocalDateTime heureDepart) {
        this.heureDepart = heureDepart;
    }

    public LocalDateTime getHeureArrivee() {
        return heureArrivee;
    }

    public void setHeureArrivee(LocalDateTime heureArrivee) {
        this.heureArrivee = heureArrivee;
    }

    public BigDecimal getDistanceTotale() {
        return distanceTotale;
    }

    public void setDistanceTotale(BigDecimal distanceTotale) {
        this.distanceTotale = distanceTotale;
    }

    public String getOrdreVisites() {
        return ordreVisites;
    }

    public void setOrdreVisites(String ordreVisites) {
        this.ordreVisites = ordreVisites;
    }

    // public static List<Trajet> findByVehiculeAndDate(Integer idVehicule,
    // LocalDate date) throws Exception {
    // FilterSet filterSet = new FilterSet();
    // filterSet.add("id_vehicule", Comparator.EQUALS, idVehicule);
    // filterSet.add("date_trajet", Comparator.LESS_THAN_OR_EQUALS, date);
    // return Trajet.filter(Trajet.class, filterSet);
    // }

    // public List<Reservation> getReservations() throws Exception {
    // String sql = "SELECT r.* " +
    // "FROM reservation r JOIN trajet_reservation tr " +
    // "ON tr.id_reservation = r.id " +
    // "WHERE tr.id_trajet = ?";
    // Object[] params = { this.id };
    // return Reservation.fetch(Reservation.class, sql, params);
    // }
}
