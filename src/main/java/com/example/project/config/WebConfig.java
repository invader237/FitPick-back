package com.example.project.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration pour gérer les requêtes Cross-Origin (CORS).
 * Permet de définir les origines, méthodes et en-têtes autorisés.
 */
@Configuration
public class WebConfig {

    @Value("${cors.allowed.origins:http://localhost:3000}")
    private String[] allowedOrigins;

    @Value("${cors.allowed.methods:GET,POST,PUT,DELETE}")
    private String[] allowedMethods;

    @Value("${cors.allowed.headers:*}")
    private String[] allowedHeaders;

    @Value("${cors.allow.credentials:true}")
    private boolean allowCredentials;

    /**
     * Configure les règles CORS pour l'application.
     *
     * @return Une instance de {@link WebMvcConfigurer}.
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(allowedOrigins)
                        .allowedMethods(allowedMethods)
                        .allowedHeaders(allowedHeaders)
                        .allowCredentials(allowCredentials);
            }
        };
    }
}
