package itu.framework.backoffice.entities;

import legacy.annotations.*;
import legacy.query.Comparator;
import legacy.query.FilterSet;
import legacy.schema.BaseEntity;
import legacy.strategy.GeneratedAfterPersistence;

import java.time.LocalDateTime;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Entity(tableName = "constants")
public class Constants extends BaseEntity {
    @Id
    @Column
    @Generated(strategy = GeneratedAfterPersistence.class)
    private Integer id;

    @Column
    private String code;

    @Column(name = "value")
    private String value;

    @Column
    private String type;

    @Column
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Cache statique pour éviter les requêtes répétées
    private static Map<String, Constants> cache = new HashMap<>();

    /**
     * Récupère une constante par son code
     * @param code Le code de la constante
     * @return L'entité Constants ou null si non trouvée
     */
    public static Constants getByCode(String code) throws Exception {
        // Vérifier le cache d'abord
        if (cache.containsKey(code)) {
            return cache.get(code);
        }

        FilterSet filters = new FilterSet();
        filters.add("code", Comparator.EQUALS, code);

        List<Constants> constants = Constants.filter(Constants.class, filters);

        if (constants != null && !constants.isEmpty()) {
            Constants constant = constants.get(0);
            cache.put(code, constant);
            return constant;
        }

        return null;
    }

    /**
     * Récupère la valeur d'une constante comme String
     * @param code Le code de la constante
     * @return La valeur ou null si non trouvée
     */
    public static String getValue(String code) throws Exception {
        Constants constant = getByCode(code);
        return constant != null ? constant.getValue() : null;
    }

    /**
     * Récupère la valeur d'une constante comme Integer
     * @param code Le code de la constante
     * @param defaultValue Valeur par défaut si non trouvée
     * @return La valeur convertie en Integer
     */
    public static Integer getIntValue(String code, Integer defaultValue) {
        try {
            String value = getValue(code);
            return value != null ? Integer.valueOf(value) : defaultValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Récupère la valeur d'une constante comme Double
     * @param code Le code de la constante
     * @param defaultValue Valeur par défaut si non trouvée
     * @return La valeur convertie en Double
     */
    public static Double getDoubleValue(String code, Double defaultValue) {
        try {
            String value = getValue(code);
            return value != null ? Double.valueOf(value) : defaultValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Récupère la valeur d'une constante comme Boolean
     * @param code Le code de la constante
     * @param defaultValue Valeur par défaut si non trouvée
     * @return La valeur convertie en Boolean
     */
    public static Boolean getBooleanValue(String code, Boolean defaultValue) {
        try {
            String value = getValue(code);
            return value != null ? Boolean.valueOf(value) : defaultValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Vide le cache (utile après modification des constantes)
     */
    public static void clearCache() {
        cache.clear();
    }

    /**
     * Met à jour une constante
     * @param code Le code de la constante
     * @param newValue La nouvelle valeur
     * @return true si mise à jour réussie
     */
    public static boolean updateConstant(String code, String newValue) {
        try {
            Constants constant = getByCode(code);
            if (constant != null) {
                constant.setValue(newValue);
                constant.setUpdatedAt(LocalDateTime.now());
                constant.update();
                // Vider le cache pour cette constante
                cache.remove(code);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Méthodes helper pour les constantes métier courantes
     */
    public static class Config {
        // Temps d'attente
        public static Integer getDefaultWaitTime() {
            return getIntValue("DEFAULT_WAIT_TIME", 30);
        }

        public static Integer getMaxWaitTime() {
            return getIntValue("MAX_WAIT_TIME", 60);
        }

        // Vitesses selon carburant
        public static Double getSpeedForFuel(String fuelType) {
            switch (fuelType) {
                case "D": return getDoubleValue("SPEED_DIESEL", 65.0);
                case "ES": return getDoubleValue("SPEED_ESSENCE", 60.0);
                case "H": return getDoubleValue("SPEED_HYBRIDE", 55.0);
                case "EL": return getDoubleValue("SPEED_ELECTRIQUE", 50.0);
                default: return getDoubleValue("SPEED_DEFAULT", 60.0);
            }
        }

        // Paramètres de planification
        public static Integer getMaxPassengersPerVehicle() {
            return getIntValue("MAX_PASSENGERS_PER_VEHICLE", 8);
        }

        public static Integer getPlanningTimeBuffer() {
            return getIntValue("PLANNING_TIME_BUFFER", 15);
        }

        public static Integer getAeroportStopTime() {
            return getIntValue("AEROPORT_STOP_TIME", 10);
        }

        public static Integer getHotelStopTime() {
            return getIntValue("HOTEL_STOP_TIME", 5);
        }

        // Priorités carburant
        public static Integer getFuelPriority(String fuelType) {
            switch (fuelType) {
                case "D": return getIntValue("FUEL_PRIORITY_D", 1);
                case "ES": return getIntValue("FUEL_PRIORITY_ES", 2);
                case "H": return getIntValue("FUEL_PRIORITY_H", 3);
                case "EL": return getIntValue("FUEL_PRIORITY_EL", 4);
                default: return 5; // Plus basse priorité
            }
        }
    }

    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getSmartWaitTime() throws Exception {
        List<Reservation> reservations = Reservation.findAll(Reservation.class);
        Integer smartWaitTime = 0;
        for(Reservation r: reservations) {
            smartWaitTime += r.getTempsAttenteMax();
        }
        if(reservations.size() > 0) {
            smartWaitTime = smartWaitTime / reservations.size();
        } else {
            smartWaitTime = 15;
        }
        return smartWaitTime;
    }

    public Integer getMaxWaitTime() throws Exception {
        List<Reservation> reservations = Reservation.findAll(Reservation.class);
        Integer maxWaitTime = 0;
        for(Reservation r : reservations) {
            if(r.getTempsAttenteMax() > maxWaitTime) {
                maxWaitTime = r.getTempsAttenteMax();
            }
        }
        return maxWaitTime > 30 ? 30 : maxWaitTime;
    }

    public boolean isSmartWaitingEnabled() {
        return true;
    }
}
