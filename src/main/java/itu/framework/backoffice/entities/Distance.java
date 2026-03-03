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

@Entity(tableName = "distance")
public class Distance extends BaseEntity {
    @Id
    @Column
    @Generated(strategy = GeneratedAfterPersistence.class)
    private Integer id;

    @Column(name = "code_from")
    private String codeFrom;

    @Column(name = "code_to")
    private String codeTo;

    @Column(name = "distance_km")
    private Double distanceKm;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Cache pour éviter les requêtes répétées
    private static Map<String, Double> distanceCache = new HashMap<>();

    /**
     * Récupère la distance entre deux lieux
     * @param codeFrom Code du lieu de départ
     * @param codeTo Code du lieu d'arrivée
     * @return Distance en km, ou null si non trouvée
     */
    public static Double getDistance(String codeFrom, String codeTo) throws Exception {
        String cacheKey = codeFrom + ":" + codeTo;

        // Vérifier le cache
        if (distanceCache.containsKey(cacheKey)) {
            return distanceCache.get(cacheKey);
        }

        FilterSet filters = new FilterSet();
        filters.add("code_from", Comparator.EQUALS, codeFrom);
        filters.add("code_to", Comparator.EQUALS, codeTo);

        List<Distance> distances = Distance.filter(Distance.class, filters);

        if (distances != null && !distances.isEmpty()) {
            Distance distance = distances.get(0);
            Double distanceValue = distance.getDistanceKm();
            distanceCache.put(cacheKey, distanceValue);
            return distanceValue;
        }

        return null;
    }

    /**
     * Récupère la distance avec une valeur par défaut si non trouvée
     * @param codeFrom Code du lieu de départ
     * @param codeTo Code du lieu d'arrivée
     * @param defaultDistance Distance par défaut
     * @return Distance en km
     */
    public static Double getDistanceOrDefault(String codeFrom, String codeTo, Double defaultDistance) {
        try {
            Double distance = getDistance(codeFrom, codeTo);
            return distance != null ? distance : defaultDistance;
        } catch (Exception e) {
            return defaultDistance;
        }
    }

    /**
     * Calcule le temps de trajet en minutes
     * @param codeFrom Code du lieu de départ
     * @param codeTo Code du lieu d'arrivée
     * @param vitesseMoyenne Vitesse moyenne en km/h
     * @return Temps en minutes, ou null si distance non trouvée
     */
    public static Integer calculateTravelTime(String codeFrom, String codeTo, Double vitesseMoyenne) {
        try {
            Double distance = getDistance(codeFrom, codeTo);
            if (distance != null && vitesseMoyenne != null && vitesseMoyenne > 0) {
                // Temps = Distance / Vitesse * 60 (pour convertir en minutes)
                return (int) Math.ceil((distance / vitesseMoyenne) * 60);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Trouve toutes les distances depuis un lieu
     * @param codeFrom Code du lieu de départ
     * @return Liste des distances
     */
    public static List<Distance> getDistancesFrom(String codeFrom) throws Exception {
        FilterSet filters = new FilterSet();
        filters.add("code_from", Comparator.EQUALS, codeFrom);

        return Distance.filter(Distance.class, filters);
    }

    /**
     * Trouve toutes les distances vers un lieu
     * @param codeTo Code du lieu d'arrivée
     * @return Liste des distances
     */
    public static List<Distance> getDistancesTo(String codeTo) throws Exception {
        FilterSet filters = new FilterSet();
        filters.add("code_to", Comparator.EQUALS, codeTo);

        return Distance.filter(Distance.class, filters);
    }

    /**
     * Vide le cache des distances
     */
    public static void clearCache() {
        distanceCache.clear();
    }

    /**
     * Ajoute ou met à jour une distance
     * @param codeFrom Code du lieu de départ
     * @param codeTo Code du lieu d'arrivée
     * @param distanceKm Distance en kilomètres
     * @return L'entité Distance créée/mise à jour
     */
    public static Distance setDistance(String codeFrom, String codeTo, Double distanceKm) throws Exception {
        // Chercher si existe déjà
        FilterSet filters = new FilterSet();
        filters.add("code_from", Comparator.EQUALS, codeFrom);
        filters.add("code_to", Comparator.EQUALS, codeTo);

        List<Distance> existing = Distance.filter(Distance.class, filters);

        Distance distance;
        if (existing != null && !existing.isEmpty()) {
            // Mettre à jour
            distance = existing.get(0);
            distance.setDistanceKm(distanceKm);
            distance.update();
        } else {
            // Créer nouveau
            distance = new Distance();
            distance.setCodeFrom(codeFrom);
            distance.setCodeTo(codeTo);
            distance.setDistanceKm(distanceKm);
            distance.setCreatedAt(LocalDateTime.now());
            distance.save();
        }

        // Vider le cache pour cette entrée
        String cacheKey = codeFrom + ":" + codeTo;
        distanceCache.remove(cacheKey);

        return distance;
    }

    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodeFrom() {
        return codeFrom;
    }

    public void setCodeFrom(String codeFrom) {
        this.codeFrom = codeFrom;
    }

    public String getCodeTo() {
        return codeTo;
    }

    public void setCodeTo(String codeTo) {
        this.codeTo = codeTo;
    }

    public Double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(Double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
