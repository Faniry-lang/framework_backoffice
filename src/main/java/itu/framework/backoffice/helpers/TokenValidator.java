package itu.framework.backoffice.helpers;

import itu.framework.backoffice.entities.Token;

/**
 * Helper pour la validation des tokens d'authentification
 */
public class TokenValidator {

    /**
     * Valide un token d'API
     * @param tokenValue Le token à valider
     * @return true si le token est valide, false sinon
     */
    public static boolean validateApiToken(String tokenValue) {
        try {
            return Token.isValidToken(tokenValue);
        } catch (Exception e) {
            // En cas d'erreur, considérer le token comme invalide
            System.err.println("Erreur lors de la validation du token: " + e.getMessage());
            return false;
        }
    }

    /**
     * Extrait le token depuis l'en-tête Authorization
     * Format attendu: "Bearer <token>"
     * @param authorizationHeader L'en-tête Authorization
     * @return Le token ou null si format invalide
     */
    public static String extractTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.isEmpty()) {
            return null;
        }

        if (authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // Retirer "Bearer "
        }

        return null;
    }

    /**
     * Vérifie si une requête est autorisée avec le token fourni
     * @param authorizationHeader L'en-tête Authorization de la requête
     * @return true si autorisé, false sinon
     */
    public static boolean isAuthorized(String authorizationHeader) {
        String token = extractTokenFromHeader(authorizationHeader);
        if (token == null) {
            return false;
        }

        return validateApiToken(token);
    }
}
