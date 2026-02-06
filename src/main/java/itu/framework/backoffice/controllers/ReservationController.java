package itu.framework.backoffice.controllers;

import com.itu.framework.annotations.Controller;
import com.itu.framework.annotations.GetMapping;
import com.itu.framework.annotations.PostMapping;
import com.itu.framework.annotations.RequestParam;
import com.itu.framework.view.ModelView;
import itu.framework.backoffice.dtos.CreateReservation;
import itu.framework.backoffice.entities.Hotel;
import itu.framework.backoffice.entities.Reservation;
import legacy.query.QueryManager;

import java.util.List;

@Controller("/api/reservation")
public class ReservationController {
    @PostMapping("/save")
    public ModelView saveReservation(CreateReservation createReservation) {
        Reservation reservation = new Reservation();
        reservation.setNbPassager(createReservation.getNbPassager());
        reservation.setIdClient(createReservation.getIdClient());
        reservation.setIdHotel(createReservation.getIdHotel());
        reservation.setDateHeureArrivee(createReservation.getDateHeureArrivee());
        try {
           reservation.save();
           ModelView successView = new ModelView("sucess");
           successView.addObject("reservation", reservation);
           return successView;
        } catch (Exception e) {
            ModelView errorView = new ModelView("error");
            errorView.addObject("error-message", e.getMessage());
            return errorView;
        }
    }

    @GetMapping("/form")
    public ModelView showReservationForm() throws Exception {
        List<Hotel> hotelList = Hotel.findAll(Hotel.class, QueryManager.get_instance());
        ModelView formView = new ModelView("reservation-form");
        formView.addObject("hotels", hotelList);
        return formView;
    }
}
