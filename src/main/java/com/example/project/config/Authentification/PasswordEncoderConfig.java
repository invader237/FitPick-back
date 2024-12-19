package com.example.project.config.Authentification;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration pour l'encodage des mots de passe.
 * Utilise BCrypt avec une force configurable pour assurer la sécurité des mots de passe.
 */
@Configuration
public class PasswordEncoderConfig {

    @Value("${security.password.encoder.strength:10}") // Force par défaut à 10
    private int strength;

    /**
     * Initialise le bean {@link PasswordEncoder} avec un encodage BCrypt.
     * La force de BCrypt est configurable via application.properties.
     *
     * @return Une instance de {@link BCryptPasswordEncoder}.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(strength);
    }
}
