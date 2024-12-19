package com.example.project.config.Authentification;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/**
 * Utilitaire pour la gestion des tokens JWT.
 * Fournit des méthodes pour générer, valider et extraire les données des tokens.
 */
@Component
public class JwtUtil {

    private final Key key; // Clé secrète pour la signature des tokens
    private final long authExpiration; // Durée d'expiration des tokens d'authentification
    private final long resetExpiration; // Durée d'expiration des tokens de réinitialisation

    /**
     * Constructeur pour injecter les propriétés JWT depuis application.properties.
     *
     * @param jwtSecret       La clé secrète utilisée pour signer les tokens.
     * @param authExpiration  Durée d'expiration des tokens d'authentification (en ms).
     * @param resetExpiration Durée d'expiration des tokens de réinitialisation (en ms).
     */
    public JwtUtil(
            @Value("${jwt.secret}") String jwtSecret,
            @Value("${jwt.expiration.auth}") long authExpiration,
            @Value("${jwt.expiration.reset}") long resetExpiration) {

        if (jwtSecret == null || jwtSecret.isEmpty()) {
            throw new IllegalArgumentException("JWT_SECRET n'est pas défini dans les variables d'environnement");
        }

        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtSecret));
        this.authExpiration = authExpiration;
        this.resetExpiration = resetExpiration;
    }

    /**
     * Génère un token JWT pour l'authentification.
     *
     * @param username Nom d'utilisateur ou email.
     * @param roles    Liste des rôles de l'utilisateur.
     * @return Token JWT signé.
     */
    public String generateAuthToken(String username, List<String> roles) {
        return generateToken(username, roles, authExpiration);
    }

    /**
     * Génère un token JWT pour la réinitialisation du mot de passe.
     *
     * @param username Nom d'utilisateur ou email.
     * @return Token JWT signé pour la réinitialisation.
     */
    public String generateResetToken(String username) {
        return generateToken(username, null, resetExpiration);
    }

    /**
     * Valide un token JWT.
     *
     * @param token Le token JWT à valider.
     * @return Vrai si le token est valide, sinon faux.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Extrait le nom d'utilisateur depuis un token JWT.
     *
     * @param token Le token JWT.
     * @return Nom d'utilisateur extrait du token.
     */
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * Extrait les autorités (rôles) depuis un token JWT.
     *
     * @param token Le token JWT.
     * @return La liste des rôles ou autorités extraites.
     */
    @SuppressWarnings("unchecked")
    public List<String> extractAuthorities(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("roles", List.class);
    }

    /**
     * Vérifie si le token JWT est expiré.
     *
     * @param token Le token JWT.
     * @return Vrai si le token est expiré, sinon faux.
     */
    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    /**
     * Génère un token JWT avec les informations fournies.
     *
     * @param username    Nom d'utilisateur ou email.
     * @param roles       Liste des rôles (facultatif).
     * @param expiration  Durée d'expiration en millisecondes.
     * @return Token JWT signé.
     */
    private String generateToken(String username, List<String> roles, long expiration) {
        JwtBuilder builder = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, SignatureAlgorithm.HS256);

        if (roles != null) {
            builder.claim("roles", roles);
        }

        return builder.compact();
    }

    /**
     * Extrait toutes les informations (claims) contenues dans un token JWT.
     *
     * @param token Le token JWT.
     * @return Les claims extraites du token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
