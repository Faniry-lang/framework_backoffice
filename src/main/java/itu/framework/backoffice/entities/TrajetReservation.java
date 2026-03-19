package itu.framework.backoffice.entities;

import legacy.annotations.*;
import legacy.query.Comparator;
import legacy.query.FilterSet;
import legacy.schema.BaseEntity;
import legacy.strategy.GeneratedAfterPersistence;

import java.util.List;
import java.util.ArrayList;

@Entity(tableName = "trajet_reservation")
public class TrajetReservation extends BaseEntity {
    @Id
    @Column
    @Generated(strategy = GeneratedAfterPersistence.class)
    private Integer id;

    @Column(name = "id_trajet")
    @ForeignKey(mappedBy = "trajet", entity = Trajet.class)
    private Integer idTrajet;

    @Column(name = "id_reservation")
    @ForeignKey(mappedBy = "reservation", entity = Reservation.class)
    private Integer idReservation;

    @Column(name = "nbr_passager")
    private Integer nbrPassager;

    @Column(name = "ordre_visite")
    private Integer ordreVisite; // 1er arrêt, 2e arrêt, etc

    public Integer getNbrPassager() {
        return nbrPassager;
    }

    public void setNbrPassager(Integer nbrPassager) {
        this.nbrPassager = nbrPassager;
    }

    public static List<TrajetReservation> findByTrajet(Integer idTrajet) throws Exception {
        if (idTrajet == null) {
            return new ArrayList<>();
        }

        FilterSet filters = new FilterSet();
        filters.add("id_trajet", Comparator.EQUALS, idTrajet);

        List<TrajetReservation> result = TrajetReservation.filter(TrajetReservation.class, filters);
        return result != null ? result : new ArrayList<>();
    }

    public static List<TrajetReservation> findByReservation(Integer idReservation) throws Exception {
        if (idReservation == null) {
            return new ArrayList<>();
        }

        FilterSet filters = new FilterSet();
        filters.add("id_reservation", Comparator.EQUALS, idReservation);

        List<TrajetReservation> result = TrajetReservation.filter(TrajetReservation.class, filters);
        return result != null ? result : new ArrayList<>();
    }

    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdTrajet() {
        return idTrajet;
    }

    public void setIdTrajet(Integer idTrajet) {
        this.idTrajet = idTrajet;
    }

    public Integer getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(Integer idReservation) {
        this.idReservation = idReservation;
    }

    public Integer getOrdreVisite() {
        return ordreVisite;
    }

    public void setOrdreVisite(Integer ordreVisite) {
        this.ordreVisite = ordreVisite;
    }

    // public static List<TrajetReservation> findByTrajet(Integer idTrajet) throws
    // Exception {
    // return TrajetReservation.findBy("id_trajet", idTrajet,
    // TrajetReservation.class);
    // }

    // public static List<TrajetReservation> findByReservation(Integer
    // idReservation) throws Exception {
    // return TrajetReservation.findBy("id_reservation", idReservation,
    // TrajetReservation.class);
    // }

}
