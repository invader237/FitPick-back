package com.example.project.service.Authentification;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Service pour la gestion des emails.
 */
@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Envoie un email de réinitialisation de mot de passe.
     *
     * @param to        Adresse email du destinataire.
     * @param resetLink Lien de réinitialisation dynamique.
     */
    public void sendResetPasswordEmail(String to, String resetLink) {
        // Validation des entrées
        if (to == null || to.isEmpty()) {
            throw new IllegalArgumentException("L'adresse email du destinataire ne peut pas être vide.");
        }
        if (resetLink == null || resetLink.isEmpty()) {
            throw new IllegalArgumentException("Le lien de réinitialisation ne peut pas être vide.");
        }

        try {
            // Charger le template HTML
            String htmlContent = loadEmailTemplate("templates/email/reset-password.html")
                    .replace("{{reset_link}}", resetLink);

            // Construire et envoyer l'email
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("fitpick.reset@gmail.com");
            helper.setTo(to);
            helper.setSubject("Réinitialisation de votre mot de passe - FitPick");
            helper.setText(htmlContent, true);

            mailSender.send(message);
            System.out.println("Email envoyé avec succès à : " + to);
        } catch (Exception e) {
            // Gérer les exceptions inattendues lors de l'envoi
            throw new RuntimeException("Erreur lors de l'envoi de l'email : " + e.getMessage(), e);
        }
    }

    /**
     * Charge un template HTML depuis le répertoire des ressources.
     *
     * @param templatePath Chemin du template HTML relatif à resources/.
     * @return Contenu du template sous forme de String.
     * @throws RuntimeException Si le fichier template est introuvable.
     */
    private String loadEmailTemplate(String templatePath) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(templatePath)) {
            if (inputStream == null) {
                throw new RuntimeException("Template non trouvé : " + templatePath);
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du chargement du template : " + e.getMessage(), e);
        }
    }
}
