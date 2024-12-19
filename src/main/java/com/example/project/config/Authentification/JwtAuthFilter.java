package com.example.project.config.Authentification;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Filtre personnalisé pour valider les tokens JWT dans chaque requête HTTP.
 * Ce filtre est exécuté une seule fois par requête.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    /**
     * Constructeur pour injecter les dépendances nécessaires.
     *
     * @param jwtUtil Utilitaire pour gérer les opérations liées aux tokens JWT.
     */
    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Valide le token JWT dans l'en-tête Authorization, extrait les informations,
     * et configure le contexte de sécurité de Spring.
     *
     * @param request     La requête HTTP entrante.
     * @param response    La réponse HTTP sortante.
     * @param filterChain La chaîne de filtres à exécuter.
     * @throws IOException      Si une erreur d'E/S se produit.
     * @throws ServletException Si une erreur de servlet se produit.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        String authHeader = request.getHeader("Authorization");

        // Vérifie si l'en-tête Authorization est absent ou mal formé
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extraction du token JWT après "Bearer "
        String token = authHeader.substring(7);

        // Validation du token
        if (!jwtUtil.validateToken(token)) {
            sendErrorResponse(response, "Invalid or expired token", HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // Extraction des informations utilisateur
        String username = jwtUtil.extractUsername(token);
        List<String> roles = jwtUtil.extractAuthorities(token);

        // Configuration du contexte de sécurité
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            setSecurityContext(username, roles);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Configure le contexte de sécurité de Spring avec l'utilisateur et ses rôles.
     *
     * @param username Nom d'utilisateur extrait du token JWT.
     * @param roles    Liste des rôles extraits du token JWT.
     */
    private void setSecurityContext(String username, List<String> roles) {
        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(username, null, authorities);

        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    /**
     * Envoie une réponse JSON standardisée en cas d'erreur.
     *
     * @param response La réponse HTTP.
     * @param message  Message d'erreur à envoyer.
     * @param status   Code de statut HTTP.
     * @throws IOException Si une erreur d'E/S se produit.
     */
    private void sendErrorResponse(HttpServletResponse response, String message, int status) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}
