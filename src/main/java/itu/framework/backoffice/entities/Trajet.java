package itu.framework.backoffice.entities;

import legacy.annotations.Column;
import legacy.annotations.Entity;
import legacy.annotations.ForeignKey;
import legacy.annotations.Id;
import legacy.query.Comparator;
import legacy.query.FilterSet;
import legacy.schema.BaseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

@Entity(tableName = "trajet")
public class Trajet extends BaseEntity {
    public Trajet() {
        super();
    }

    @Id
    @Column
    private Long id;

    @Column(name = "id_vehicule")
    @ForeignKey(entity = Vehicule.class, mappedBy = "vehicule")
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
    private String ordreVisites;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public static List<Trajet> findByVehiculeAndDate(Integer idVehicule, LocalDate date) throws Exception {
        FilterSet filterSet = new FilterSet();
        filterSet.add("id_vehicule", Comparator.EQUALS, idVehicule);
        filterSet.add("date_trajet", Comparator.LESS_THAN_OR_EQUALS, date);
        return Trajet.filter(Trajet.class, filterSet);
    }

    public List<Reservation> getReservations() throws Exception {
        String sql = "SELECT r.* " +
                "FROM reservation r JOIN trajet_reservation tr " +
                "ON tr.id_reservation = r.id " +
                "WHERE tr.id_trajet = ?";
        Object[] params = { this.id };
        return Reservation.fetch(Reservation.class, sql, params);
    }
}
