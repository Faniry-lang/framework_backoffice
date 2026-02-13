import itu.framework.backoffice.entities.Reservation;
import itu.framework.backoffice.entities.TokenClient;

import java.time.LocalDateTime;
import java.util.UUID;

public class TestToken {
    public static void main(String[] args) {
        TokenClient tokenClient = new TokenClient();
        UUID uuid = UUID.randomUUID();
        tokenClient.setToken(uuid.toString());
        LocalDateTime now  = LocalDateTime.now().plusMinutes(10);
        tokenClient.setExpirationDate(now);
        try {
            System.out.println("sauvegarde du token...");
            tokenClient.save();
        } catch (Exception e) {
            System.err.println("erreur de sauvegarde du token:" + e.getMessage());
        }
//        boolean valid = TokenClient.isTokenValid("12test12", LocalDateTime.now());
//        if (valid) {
//            System.out.println("token valide");
//        } else {
//            System.out.println("token invalide");
//        }
//        Reservation reservation = new Reservation();
//        reservation.setNbPassager(2);
//        reservation.setIdClient("1");
//        reservation.setIdHotel(1);
//        reservation.setDateHeureArrivee(LocalDateTime.now().plusMinutes(10));
//        try {
//            reservation.save();
//            System.out.println("reservation saved with id: " + reservation.getId());
//        } catch (Exception e) {
//            System.err.println("error saving reservation: " + e.getMessage());
//        }
    }
}
