package itu.framework.backoffice.entities;

import itu.framework.backoffice.dtos.ReservationDTO;
import legacy.annotations.Column;
import legacy.annotations.Entity;
import legacy.annotations.ForeignKey;
import legacy.annotations.Id;
import legacy.query.Comparator;
import legacy.query.Filter;
import legacy.query.QueryManager;
import legacy.schema.BaseEntity;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "reservation")
public class Reservation extends BaseEntity {
    @Id
    @Column
    Integer id;

    @Column(name = "nb_passager")
    Integer nbPassager;

    @Column(name = "id_client")
    String idClient;

    @Column(name = "id_hotel")
    @ForeignKey(mappedBy = "hotel", entity = Hotel.class)
    Integer idHotel;

    @Column(name = "date_heure_arrivee")
    LocalDateTime dateHeureArrivee;

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

    public ReservationDTO toDto() throws Exception {
        ReservationDTO dto =  new ReservationDTO();
        dto.setId_client(this.getIdClient());
        dto.setNb_passager(this.getNbPassager());
        dto.setNom_hotel(((Hotel) this.getForeignKey("id_hotel")).getNom());
        dto.setDate_reservation(this.getDateHeureArrivee());
        return dto;
    }

    public static List<ReservationDTO> findByDate(LocalDate date) throws Exception {
        List<Filter> filters = new ArrayList<>();
        if(date != null) {
            LocalDateTime endOfTheDay = date.atStartOfDay().plusHours(24);
            LocalDateTime startOfTheDay = date.atStartOfDay();
            filters.add(new Filter("date_heure_arrivee", Comparator.LESS_THAN, endOfTheDay));
            filters.add(new Filter("date_heure_arrivee", Comparator.GREATER_THAN, startOfTheDay));
        }
        List<Reservation> reservations = Reservation.filter(Reservation.class, QueryManager.get_instance(), filters.toArray(new Filter[0]));
        List<ReservationDTO> dtos = new ArrayList<>();
        for(Reservation reservation: reservations) {
            dtos.add(reservation.toDto());
        }
        return dtos;
    }
}
