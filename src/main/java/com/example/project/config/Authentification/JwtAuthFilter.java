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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Filtre personnalisé pour valider les tokens JWT dans chaque requête HTTP.
 * Ce filtre s'assure que chaque requête est correctement authentifiée avant d'être traitée.
 * <p>
 * Il extrait et valide le token JWT, puis configure le contexte de sécurité de Spring si l'authentification réussit.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);

    /**
     * Constructeur pour injecter l'instance de {@link JwtUtil}.
     *
     * @param jwtUtil Utilitaire pour la gestion des tokens JWT.
     */
    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Méthode principale du filtre, exécutée pour chaque requête HTTP.
     * Valide le token JWT dans l'en-tête Authorization, extrait les informations utilisateur,
     * et configure le contexte de sécurité de Spring.
     *
     * @param request     La requête HTTP entrante.
     * @param response    La réponse HTTP sortante.
     * @param filterChain La chaîne de filtres à exécuter.
     * @throws IOException      En cas d'erreur d'entrée/sortie.
     * @throws ServletException En cas d'erreur liée à la servlet.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        // Récupération de l'en-tête Authorization
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.info("L'en-tête Authorization est absent ou mal formé. Filtre ignoré.");
            filterChain.doFilter(request, response);
            return;
        }

        // Extraction du token JWT après "Bearer "
        String token = authHeader.substring(7);
        log.debug("Token JWT extrait : {}", token);

        try {
            // Validation du token
            if (!jwtUtil.validateToken(token)) {
                log.warn("Le token est invalide ou expiré.");
                sendErrorResponse(response, "Token invalide ou expiré", HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            // Extraction des informations utilisateur
            String username = jwtUtil.extractUsername(token);
            List<String> roles = jwtUtil.extractAuthorities(token);

            log.debug("Nom d'utilisateur extrait : {}", username);
            log.debug("Rôles extraits : {}", roles);

            // Configuration du contexte de sécurité si l'utilisateur est valide
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                setSecurityContext(username, roles);
                log.info("Contexte de sécurité configuré pour : {}", username);
            }
        } catch (Exception e) {
            log.error("Erreur lors du traitement du token JWT : {}", e.getMessage(), e);
            sendErrorResponse(response, "Erreur lors de la validation du token", HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // Continuer la chaîne de filtres
        filterChain.doFilter(request, response);
    }

    /**
     * Configure le contexte de sécurité de Spring avec les informations utilisateur.
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
     * Envoie une réponse JSON en cas d'échec de l'authentification.
     *
     * @param response   La réponse HTTP.
     * @param message    Message d'erreur à inclure dans la réponse.
     * @param statusCode Code de statut HTTP à retourner.
     * @throws IOException En cas d'erreur d'entrée/sortie.
     */
    private void sendErrorResponse(HttpServletResponse response, String message, int statusCode) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
        log.info("Réponse d'erreur envoyée avec le statut {} : {}", statusCode, message);
    }
}
