package itu.framework.backoffice.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TripTiming {
    private final LocalDateTime heureDepart;
    private final LocalDateTime heureArrivee;
    private final BigDecimal distanceTotale;

    public TripTiming(LocalDateTime heureDepart, LocalDateTime heureArrivee, BigDecimal distanceTotale) {
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

    public BigDecimal getDistanceTotale() {
        return distanceTotale;
    }
}
