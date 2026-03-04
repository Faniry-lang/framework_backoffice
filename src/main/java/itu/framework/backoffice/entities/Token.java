package itu.framework.backoffice.entities;

import legacy.annotations.*;
import legacy.query.Comparator;
import legacy.query.FilterSet;
import legacy.schema.BaseEntity;
import legacy.strategy.GeneratedAfterPersistence;

import java.time.LocalDateTime;
import java.util.List;

@Entity(tableName = "token")
public class Token extends BaseEntity {
    @Id
    @Column
    @Generated(strategy = GeneratedAfterPersistence.class)
    private Integer id;

    @Column
    private String token;

    @Column(name = "date_expiration")
    private LocalDateTime dateExpiration;

    /**
     * Vérifie la validité d'un token
     * @param tokenValue Le token à vérifier
     * @return true si le token est valide et non expiré, false sinon
     */
    public static boolean isValidToken(String tokenValue) throws Exception {
        if (tokenValue == null || tokenValue.isEmpty()) {
            return false;
        }

        // Rechercher le token dans la base de données
        FilterSet filters = new FilterSet();
        filters.add("token", Comparator.EQUALS, tokenValue);

        List<Token> tokens = Token.filter(Token.class, filters);

        if (tokens == null || tokens.isEmpty()) {
            return false; // Token non trouvé
        }

        Token token = tokens.get(0);

        // Vérifier si le token n'est pas expiré
        LocalDateTime now = LocalDateTime.now();
        if (token.getDateExpiration() != null && token.getDateExpiration().isBefore(now)) {
            return false; // Token expiré
        }

        return true; // Token valide
    }

    /**
     * Trouve un token par sa valeur
     * @param tokenValue La valeur du token
     * @return L'entité Token ou null si non trouvé
     */
    public static Token findByToken(String tokenValue) throws Exception {
        FilterSet filters = new FilterSet();
        filters.add("token", Comparator.EQUALS, tokenValue);

        List<Token> tokens = Token.filter(Token.class, filters);

        if (tokens != null && !tokens.isEmpty()) {
            return tokens.get(0);
        }

        return null;
    }

    /**
     * Supprime les tokens expirés
     */
    public static void cleanExpiredTokens() throws Exception {
        FilterSet filters = new FilterSet();
        filters.add("date_expiration", Comparator.LESS_THAN, LocalDateTime.now());

        List<Token> expiredTokens = Token.filter(Token.class, filters);

        if (expiredTokens != null) {
            for (Token token : expiredTokens) {
                token.delete();
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(LocalDateTime dateExpiration) {
        this.dateExpiration = dateExpiration;
    }
}


