package itu.framework.backoffice.entities;

import legacy.annotations.Column;
import legacy.annotations.Entity;
import legacy.annotations.Id;
import legacy.query.Comparator;
import legacy.query.FilterSet;
import legacy.schema.BaseEntity;

import java.util.List;


@Entity(tableName = "hotel")
public class Lieux extends BaseEntity {
    public Lieux() {
        super();
    }

    @Id
    @Column
    private Long id;

    @Column
    private String nom;

    @Column
    private String code;

    @Column(name="aeroport")
    private Boolean aeroport;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public static Lieux findAeroport() throws Exception {
        FilterSet filterSet = new FilterSet();
        filterSet.add("aeroport", Comparator.EQUALS, true);
        List<Lieux> lieux = Lieux.filter(Lieux.class, filterSet);
        if (!lieux.isEmpty()) {
            return lieux.get(0);
        }
        return null;
    }
}
