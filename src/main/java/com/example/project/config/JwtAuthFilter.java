package com.example.project.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws java.io.IOException, jakarta.servlet.ServletException {

        String requestPath = request.getRequestURI();
        System.out.println("Requête entrante vers: " + requestPath);

        // Vérification des routes publiques
        if (isPublicRoute(requestPath)) {
            System.out.println("Route publique, filtre ignoré: " + requestPath);
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("Authorization Header manquant ou invalide.");
            filterChain.doFilter(request, response); // Continuer sans authentification
            return;
        }

        String token = authHeader.substring(7); // Extraire le token après "Bearer "

        if (!jwtUtil.validateToken(token)) {
            System.out.println("JWT invalide.");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // Répondre avec un statut HTTP 403
            return;
        }

        String username = jwtUtil.extractUsername(token);
        List<String> roles = jwtUtil.extractAuthorities(token);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            List<SimpleGrantedAuthority> authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authToken);
            System.out.println("Utilisateur authentifié: " + username + ", Rôles: " + roles);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Vérifie si une route est publique (sans besoin de JWT).
     *
     * @param requestPath Le chemin de la requête.
     * @return true si la route est publique, sinon false.
     */
    private boolean isPublicRoute(String requestPath) {
        return requestPath.startsWith("/api/auth") || requestPath.startsWith("/api/public");
    }
    
}
