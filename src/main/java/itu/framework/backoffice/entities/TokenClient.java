package itu.framework.backoffice.entities;

import legacy.annotations.Column;
import legacy.annotations.Entity;
import legacy.annotations.Id;
import legacy.query.Comparator;
import legacy.query.FilterSet;
import legacy.schema.BaseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "token_client")
public class TokenClient extends BaseEntity {
    public TokenClient() {
        super();
    }

    @Id
    @Column
    private Long id;

    @Column
    private String token;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public static boolean isTokenValid(String token, LocalDateTime now) {
        if (token == null || token.isEmpty()) {
            return false;
        }

        try {
            List<TokenClient> tokenClients = new ArrayList<>();
            FilterSet filterSet = new FilterSet();
            if (now != null ) {
                filterSet.add("token", Comparator.EQUALS, token);
                filterSet.add("expirationDate", Comparator.GREATER_THAN, now);
                tokenClients = TokenClient.filter(TokenClient.class, filterSet);
            }
            return !tokenClients.isEmpty();
        } catch (Exception e) {
            System.err.println("Erreur lors de la verification du token:" + e.getMessage());
            return false;
        }
    }

}
