package itu.framework.backoffice.controllers;

import com.itu.framework.annotations.*;
import com.itu.framework.view.ModelView;
import itu.framework.backoffice.dtos.CreateReservation;
import itu.framework.backoffice.dtos.ReservationDTO;
import itu.framework.backoffice.entities.Hotel;
import itu.framework.backoffice.entities.Reservation;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller("/api/reservations")
public class ReservationController {
    @PostMapping("/save")
    public ModelView saveReservation(CreateReservation createReservation) throws Exception {
        try {
            Reservation reservation = new Reservation();
            reservation.setNbPassager(createReservation.getNbPassager());
            reservation.setIdClient(createReservation.getIdClient());
            reservation.setIdHotel(createReservation.getIdHotel());
            reservation.setDateHeureArrivee(LocalDateTime.parse(createReservation.getDateHeureArrivee()));
            reservation.save();
            String message = URLEncoder.encode(
                    "Réservation créée avec succès!",
                    StandardCharsets.UTF_8.toString()
            );
            return ModelView.redirect(
                    "/api/reservations/form?message=" + message
            );
        }catch (Exception e) {
            return ModelView.redirect("/error?error-message="+e.getMessage()+"&&link=/api/reservations/form");
        }
    }

    @GetMapping("/form")
    public ModelView showReservationForm(@RequestParam("message") String message) throws Exception {
        List<Hotel> hotelList = Hotel.findAll(Hotel.class);
        List<Reservation> reservations = Reservation.findAll(Reservation.class);
        List<ReservationDTO> reservationDTOList = new java.util.ArrayList<>();
        for (Reservation reservation : reservations) {
            reservationDTOList.add(reservation.toDto());
        }
        ModelView formView = new ModelView("reservation/reservation-form");
        formView.addObject("hotels", hotelList);
        formView.addObject("reservations", reservationDTOList);
        if(message != null && !message.isEmpty()) {
            formView.addObject("message", message);
        }
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
