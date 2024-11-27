package com.example.project.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long expiration = 3600000; // 1 heure en millisecondes

    // Générer un token avec des rôles
    public String generateToken(String username, List<String> roles) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles) 
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    // Extraire le nom d'utilisateur du token
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // Extraire les rôles/autorités du token
    @SuppressWarnings("unchecked")
    public List<String> extractAuthorities(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("roles", List.class); 
    }

    // Valider un token
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    // Extraire tous les claims
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
