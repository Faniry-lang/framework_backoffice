package itu.framework.backoffice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itu.framework.annotations.*;
import com.itu.framework.view.ModelView;
import itu.framework.backoffice.dtos.CreateReservation;
import itu.framework.backoffice.dtos.ReservationDTO;
import itu.framework.backoffice.entities.Hotel;
import itu.framework.backoffice.entities.Reservation;
import legacy.query.QueryManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller("/api/reservation")
public class ReservationController {
    @PostMapping("/save")
    public ModelView saveReservation(CreateReservation createReservation) throws Exception {
        List<Hotel> hotelList = Hotel.findAll(Hotel.class, QueryManager.get_instance());
        try {
            Reservation reservation = new Reservation();
            reservation.setNbPassager(createReservation.getNbPassager());
            reservation.setIdClient(createReservation.getIdClient());
            reservation.setIdHotel(createReservation.getIdHotel());
            reservation.setDateHeureArrivee(LocalDateTime.parse(createReservation.getDateHeureArrivee()));
            ModelView formView = new ModelView("reservation-form");
            formView.addObject("hotels", hotelList);
            formView.addObject("message", "Succès!");
            return formView;
        } catch (Exception e) {
            ModelView formView = new ModelView("reservation-form");
            formView.addObject("hotels", hotelList);
            formView.addObject("message", "Erreur: "+e.getMessage());
            return formView;
        }
    }

    @GetMapping("/form")
    public ModelView showReservationForm() throws Exception {
        List<Hotel> hotelList = Hotel.findAll(Hotel.class, QueryManager.get_instance());
        ModelView formView = new ModelView("reservation-form");
        formView.addObject("hotels", hotelList);
        return formView;
    }

    @GetMapping
    @Json
    public List<ReservationDTO> getReservationFilteredByDate(@RequestParam("date_reservation") String date) throws Exception {

        LocalDate dateObj = null;
        if(date != null) {
            dateObj = LocalDate.parse(date);
        }
        return Reservation.findByDate(dateObj);
    }
}
