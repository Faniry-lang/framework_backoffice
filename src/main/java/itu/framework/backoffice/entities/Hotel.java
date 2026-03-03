package itu.framework.backoffice.entities;

import legacy.annotations.Column;
import legacy.annotations.Entity;
import legacy.annotations.Id;
import legacy.schema.BaseEntity;
import legacy.query.Comparator;
import legacy.query.FilterSet;

import java.util.List;

@Entity(tableName = "hotel")
public class Hotel extends BaseEntity {
    @Id
    @Column
    Integer id;

    @Column(name = "nom")
    String nom;

    @Column(name = "code")
    String code;

    @Column(name = "aeroport")
    Boolean aeroport;

    /**
     * Trouve un hôtel par son code
     * @param code Le code de l'hôtel (ex: HOT001, AERO01)
     * @return L'hôtel ou null si non trouvé
     */
    public static Hotel findByCode(String code) throws Exception {
        FilterSet filters = new FilterSet();
        filters.add("code", Comparator.EQUALS, code);

        List<Hotel> hotels = Hotel.filter(Hotel.class, filters);

        if (hotels != null && !hotels.isEmpty()) {
            return hotels.get(0);
        }

        return null;
    }

    /**
     * Trouve tous les aéroports
     * @return Liste des aéroports
     */
    public static List<Hotel> findAeroports() throws Exception {
        FilterSet filters = new FilterSet();
        filters.add("aeroport", Comparator.EQUALS, true);

        return Hotel.filter(Hotel.class, filters);
    }

    /**
     * Trouve tous les hôtels (non aéroports)
     * @return Liste des hôtels
     */
    public static List<Hotel> findHotels() throws Exception {
        FilterSet filters = new FilterSet();
        filters.add("aeroport", Comparator.EQUALS, false);

        return Hotel.filter(Hotel.class, filters);
    }

    /**
     * Vérifie si c'est un aéroport
     * @return true si c'est un aéroport
     */
    public boolean isAeroport() {
        return aeroport != null && aeroport;
    }

    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getAeroport() {
        return aeroport;
    }

    public void setAeroport(Boolean aeroport) {
        this.aeroport = aeroport;
    }
}

