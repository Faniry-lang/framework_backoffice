package itu.framework.backoffice.entities;

import legacy.annotations.Column;
import legacy.annotations.Entity;
import legacy.annotations.ForeignKey;
import legacy.annotations.Id;
import legacy.schema.BaseEntity;

import java.util.List;

@Entity(tableName = "trajet_reservation")
public class TrajetReservation extends BaseEntity {
    public TrajetReservation() {
        super();
    }

    @Id
    @Column
    private Long id;

    @Column(name = "id_trajet")
    @ForeignKey(mappedBy = "trajet", entity = Trajet.class)
    private Integer idTrajet;

    @Column(name = "id_reservation")
    @ForeignKey(mappedBy = "reservation", entity = Reservation.class)
    private Integer idReservation;

    @Column(name = "ordre_visite")
    private Integer ordreVisite;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public static List<TrajetReservation> findByTrajet(Integer idTrajet) throws Exception {
        return TrajetReservation.findBy("id_trajet", idTrajet, TrajetReservation.class);
    }

    public static List<TrajetReservation> findByReservation(Integer idReservation) throws Exception {
        return TrajetReservation.findBy("id_reservation", idReservation, TrajetReservation.class);
    }

}
