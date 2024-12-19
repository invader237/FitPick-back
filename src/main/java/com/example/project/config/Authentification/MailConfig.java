package com.example.project.config.Authentification;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.beans.factory.annotation.Value;

/**
 * Configuration pour le système d'envoi d'e-mails via Spring Boot.
 * Les propriétés SMTP sont chargées directement depuis application.properties.
 */
@Configuration
public class MailConfig {

    /**
     * Configure et retourne le {@link JavaMailSender}.
     * Spring Boot utilise automatiquement les propriétés définies dans application.properties.
     *
     * @param host       Hôte SMTP configuré.
     * @param port       Port SMTP utilisé.
     * @param username   Nom d'utilisateur SMTP.
     * @param password   Mot de passe ou clé API SMTP.
     * @return Une instance configurée de {@link JavaMailSender}.
     */
    @Bean
    public JavaMailSender javaMailSender(
            @Value("${spring.mail.host}") String host,
            @Value("${spring.mail.port}") int port,
            @Value("${spring.mail.username}") String username,
            @Value("${spring.mail.password}") String password) {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        return mailSender;
    }
}
