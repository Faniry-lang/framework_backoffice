package itu.framework.backoffice.models;

import java.time.LocalDateTime;

public class TripTiming {
    private final LocalDateTime heureDepart;
    private final LocalDateTime heureArrivee;
    private final Double distanceTotale;

    public TripTiming(LocalDateTime heureDepart, LocalDateTime heureArrivee, Double distanceTotale) {
        this.heureDepart = heureDepart;
        this.heureArrivee = heureArrivee;
        this.distanceTotale = distanceTotale;
    }

    public LocalDateTime getHeureDepart() {
        return heureDepart;
    }

    public LocalDateTime getHeureArrivee() {
        return heureArrivee;
    }

    public Double getDistanceTotale() {
        return distanceTotale;
    }
}
