package com.example.project.config.Authentification;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration de la sécurité de l'application.
 * Gère les autorisations, la gestion des sessions et l'intégration des filtres JWT.
 */
@Configuration
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    /**
     * Constructeur pour injecter le filtre JWT.
     *
     * @param jwtAuthFilter Filtre personnalisé pour valider les tokens JWT.
     */
    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    /**
     * Configure la chaîne de sécurité Spring.
     *
     * @param http L'objet {@link HttpSecurity} permettant de configurer la sécurité.
     * @return Une instance de {@link SecurityFilterChain}.
     * @throws Exception Si une erreur de configuration survient.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Désactiver CSRF car nous utilisons des tokens JWT
            .csrf(csrf -> csrf.disable())
    
            // Gestion des autorisations
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/public/**", "/api/auth/**", "/api/weather/**").permitAll() // Endpoints publics
                .anyRequest().authenticated() // Toute autre requête doit être authentifiée
            )
    
            // Gestion des exceptions
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(authenticationEntryPoint()) // Gestion des accès non autorisés
            )
    
            // Gestion des sessions : Stateless car nous utilisons des tokens JWT
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    
            // Ajout du filtre JWT avant le filtre d'authentification standard
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
    
        return http.build();
    }
    

    /**
     * Gestionnaire des réponses en cas d'accès non autorisé.
     *
     * @return Une implémentation d'{@link AuthenticationEntryPoint}.
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Accès non autorisé : Token requis ou invalide\"}");
        };
    }
}
