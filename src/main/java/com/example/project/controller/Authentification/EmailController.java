package com.example.project.controller.Authentification;

import com.example.project.service.Authentification.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

/**
 * Contrôleur pour la gestion des emails.
 * Fournit des endpoints pour tester l'envoi d'emails et gérer d'autres fonctionnalités liées.
 */
@RestController
@RequestMapping("/api/email")
public class EmailController {

    private final EmailService emailService;

    /**
     * Constructeur avec injection de dépendance.
     *
     * @param emailService Service pour l'envoi d'emails.
     */
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * Endpoint pour tester l'envoi d'un email.
     *
     * @param to Adresse email du destinataire.
     * @return Une réponse HTTP indiquant le succès ou l'échec de l'envoi.
     */
    @PostMapping("/send-test")
    public ResponseEntity<String> sendTestEmail(
            @RequestParam @NotEmpty @Email String to) {
        try {
            // Lien fictif pour le test
            String resetLink = "http://localhost:3000/reset-password?token=exampleToken123";

            // Envoi de l'email via le service
            emailService.sendResetPasswordEmail(to, resetLink);

            return ResponseEntity.ok("Email envoyé avec succès à : " + to);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'envoi de l'email : " + e.getMessage());
        }
    }
}
