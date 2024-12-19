package com.example.project.service.Authentification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;

import jakarta.mail.internet.MimeMessage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private JavaMailSender mailSender;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendResetPasswordEmail_Success() throws Exception {
        // Données
        String to = "test@example.com";
        String resetLink = "http://localhost:3000/reset-password?token=exampleToken123";

        // Mock du comportement du mailSender
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Exécution
        emailService.sendResetPasswordEmail(to, resetLink);

        // Vérification
        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    void testSendResetPasswordEmail_EmptyRecipient() {
        // Données
        String to = ""; // Destinataire vide
        String resetLink = "http://localhost:3000/reset-password?token=exampleToken123";

        // Test
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                emailService.sendResetPasswordEmail(to, resetLink)
        );

        // Vérifications
        assertEquals("L'adresse email du destinataire ne peut pas être vide.", exception.getMessage());
    }

    @Test
    void testSendResetPasswordEmail_EmptyResetLink() {
        // Données
        String to = "test@example.com";
        String resetLink = ""; // Lien vide

        // Test
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                emailService.sendResetPasswordEmail(to, resetLink)
        );

        // Vérifications
        assertEquals("Le lien de réinitialisation ne peut pas être vide.", exception.getMessage());
    }

    @Test
    void testSendResetPasswordEmail_ExceptionThrown() throws Exception {
        // Données
        String to = "test@example.com";
        String resetLink = "http://localhost:3000/reset-password?token=exampleToken123";

        // Mock du comportement du mailSender pour générer une exception
        when(mailSender.createMimeMessage()).thenThrow(new RuntimeException("Erreur simulée"));

        // Test
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                emailService.sendResetPasswordEmail(to, resetLink)
        );

        // Vérifications
        assertTrue(exception.getMessage().contains("Erreur lors de l'envoi de l'email"));
    }
}
