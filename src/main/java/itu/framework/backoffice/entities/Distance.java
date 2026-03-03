package itu.framework.backoffice.entities;

import legacy.annotations.Column;
import legacy.annotations.Entity;
import legacy.annotations.ForeignKey;
import legacy.annotations.Id;
import legacy.query.Comparator;
import legacy.query.FilterSet;
import legacy.schema.BaseEntity;

import java.math.BigDecimal;
import java.util.List;

@Entity(tableName = "distance")
public class Distance extends BaseEntity {
    public Distance() {
        super();
    }

    @Id
    @Column
    private Long id;

    @Column(name = "code_from")
    private String codeFrom;

    @Column(name = "code_to")
    private String codeTo;

    @Column(name = "distance_km")
    private BigDecimal distanceKm;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public BigDecimal getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(BigDecimal distanceKm) {
        this.distanceKm = distanceKm;
    }

    public static Distance getDistance(String from, String to) throws Exception {
        String sql = "SELECT * FROM distance WHERE " +
                "(code_from = ? AND code_to = ?) OR (code_from = ? AND code_to = ?)";
        Object[] params = { from, to, to, from };
        List<Distance> distanceList = Distance.fetch(Distance.class,
                sql, params);
        if(distanceList.size() > 0) {
            return distanceList.get(0);
        }
        return null;
    }
}
