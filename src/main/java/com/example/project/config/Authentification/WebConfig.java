package com.example.project.config.Authentification;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class WebConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*"); // Remplace par des domaines spécifiques en production
        config.addAllowedMethod("*"); // Autorise toutes les méthodes HTTP
        config.addAllowedHeader("*"); // Autorise tous les en-têtes
        config.setAllowCredentials(true); // Autorise l'envoi de cookies et d'informations d'identité

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // Applique la configuration à toutes les routes

        return new CorsFilter(source);
    }
}