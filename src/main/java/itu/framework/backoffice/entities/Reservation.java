package itu.framework.backoffice.entities;

import itu.framework.backoffice.dtos.ReservationDTO;
import legacy.annotations.*;
import legacy.query.Comparator;
import legacy.query.FilterSet;
import legacy.schema.BaseEntity;
import legacy.strategy.GeneratedAfterPersistence;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "reservation")
public class Reservation extends BaseEntity {
    @Id
    @Column
    @Generated(strategy = GeneratedAfterPersistence.class)
    Integer id;

    @Column(name = "nb_passager")
    Integer nbPassager;

    @Column(name = "id_client")
    String idClient;

    @Column(name = "id_hotel")
    @ForeignKey(mappedBy = "hotel", entity = Lieux.class)
    Integer idHotel;

    @Column(name = "date_heure_arrivee")
    LocalDateTime dateHeureArrivee;

    @Column(name = "temps_attente_max")
    Integer tempsAttenteMax; // en minutes

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNbPassager() {
        return nbPassager;
    }

    public void setNbPassager(Integer nbPassager) {
        this.nbPassager = nbPassager;
    }

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public Integer getIdHotel() {
        return idHotel;
    }

    public void setIdHotel(Integer idHotel) {
        this.idHotel = idHotel;
    }

    public LocalDateTime getDateHeureArrivee() {
        return dateHeureArrivee;
    }

    public void setDateHeureArrivee(LocalDateTime dateHeureArrivee) {
        this.dateHeureArrivee = dateHeureArrivee;
    }

    public Integer getTempsAttenteMax() {
        return tempsAttenteMax;
    }

    public void setTempsAttenteMax(Integer tempsAttenteMax) {
        this.tempsAttenteMax = tempsAttenteMax;
    }

    /**
     * Récupère le temps d'attente maximum effectif
     * Si pas défini, utilise la constante par défaut
     * @return Temps d'attente en minutes
     */
    public Integer getTempsAttenteMaxEffectif() {
        if (tempsAttenteMax != null) {
            return tempsAttenteMax;
        }
        return Constants.Config.getDefaultWaitTime();
    }

    public ReservationDTO toDto() throws Exception {
        ReservationDTO dto =  new ReservationDTO();
        dto.setId_client(this.getIdClient());
        dto.setNb_passager(this.getNbPassager());
        dto.setNom_hotel(((Lieux) this.getForeignKey("id_hotel")).getNom());
        dto.setDate_reservation(this.getDateHeureArrivee().toString());
        dto.setTempsAttenteMax(this.getTempsAttenteMax());
        return dto;
    }

    public static List<ReservationDTO> findByDate(LocalDate date) throws Exception {
        FilterSet filters = new FilterSet();
        if(date != null) {
            LocalDateTime endOfTheDay = date.atStartOfDay().plusHours(24);
            LocalDateTime startOfTheDay = date.atStartOfDay();
            filters.add("date_heure_arrivee", Comparator.LESS_THAN, endOfTheDay);
            filters.add("date_heure_arrivee", Comparator.GREATER_THAN, startOfTheDay);
        }
        List<Reservation> reservations = Reservation.filter(Reservation.class, filters);
        List<ReservationDTO> dtos = new ArrayList<>();
        for(Reservation reservation: reservations) {
            dtos.add(reservation.toDto());
        }
        return dtos;
    }

    public static List<Reservation> findUnassignedByDate(LocalDate date) throws Exception {
        String sql = "SELECT r.*\n" +
                "FROM reservation r LEFT JOIN\n" +
                "(SELECT tr.*\n" +
                "FROM trajet t JOIN trajet_reservation tr\n" +
                "                   ON t.id = tr.id_trajet\n" +
                "WHERE t.date_trajet < ?) AS trajet_before_date\n" +
                "ON trajet_before_date.id_reservation = r.id\n" +
                "WHERE trajet_before_date.id IS NULL;";
        Object[] params = { date };
        return Reservation.fetch(Reservation.class, sql, params);
    }
}
