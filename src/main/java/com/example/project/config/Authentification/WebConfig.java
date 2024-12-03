package com.example.project.config.Authentification;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class WebConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:3000");  // Frontend local
        config.addAllowedMethod("*");  // Autorise toutes les méthodes
        config.addAllowedHeader("*");  // Autorise tous les headers
        config.setAllowCredentials(true); // Permet l'envoi de cookies entre le frontend et le backend

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);  // Applique la configuration CORS à toutes les routes

        return new CorsFilter(source);
    }
}
