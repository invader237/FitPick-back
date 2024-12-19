package com.example.project.controller.Authentification;

import com.example.project.config.Authentification.JwtUtil;
import com.example.project.dto.Authentification.ChangePasswordDTO;
import com.example.project.dto.Authentification.LoginRequestDTO;
import com.example.project.dto.Authentification.LoginResponseDTO;
import com.example.project.dto.Authentification.RegisterRequestDTO;
import com.example.project.model.Authentification.User;
import com.example.project.service.Authentification.AuthService;
import com.example.project.service.Authentification.EmailService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.stream.Collectors;

/**
 * Contrôleur pour la gestion de l'authentification des utilisateurs.
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private final AuthService authService;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;

    /**
     * Constructeur pour injecter les services nécessaires.
     *
     * @param authService Service pour l'authentification.
     * @param emailService Service pour la gestion des emails.
     * @param jwtUtil Utilitaire pour les opérations JWT.
     */
    public AuthController(AuthService authService, EmailService emailService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.emailService = emailService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Enregistre un nouvel utilisateur.
     *
     * @param request DTO contenant les informations d'inscription.
     * @return Réponse HTTP avec un message de succès ou d'erreur.
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequestDTO request) {
        try {
            authService.registerUser(
                request.getEmail(),
                request.getPassword(),
                request.getFirstName(),
                request.getLastName()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body("Utilisateur enregistré avec succès.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Authentifie un utilisateur et génère un token JWT.
     *
     * @param request DTO contenant les informations de connexion.
     * @return Réponse HTTP avec un token JWT ou un message d'erreur.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        try {
            User user = authService.authenticateUser(request.getEmail(), request.getPassword());
            String token = authService.generateAuthToken(
                user.getEmail(),
                user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toList())
            );
            return ResponseEntity.ok(new LoginResponseDTO(token));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponseDTO(null));
        }
    }

    /**
     * Récupère les informations de l'utilisateur connecté.
     *
     * @return Réponse HTTP avec les informations de l'utilisateur ou une erreur.
     */
    @GetMapping("/me")
    public ResponseEntity<User> getUserInfo() {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = authService.getUserByEmail(email);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    /**
     * Change le mot de passe de l'utilisateur connecté.
     *
     * @param changePasswordDTO DTO contenant l'ancien et le nouveau mot de passe.
     * @return Réponse HTTP avec un message de succès ou d'erreur.
     */
    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            authService.changePassword(
                email,
                changePasswordDTO.getOldPassword(),
                changePasswordDTO.getNewPassword()
            );
            return ResponseEntity.ok("Mot de passe changé avec succès.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Envoie un email de réinitialisation de mot de passe.
     *
     * @param email Adresse email de l'utilisateur.
     * @return Réponse HTTP avec un message de succès ou d'erreur.
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        try {
            if (!authService.emailExists(email)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cet email n'existe pas.");
            }
            if (!authService.canRequestReset(email)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Veuillez attendre avant de demander un autre lien.");
            }
            String resetToken = authService.generateResetToken(email);
            String resetLink = "http://localhost:3000/reset-password?token=" + resetToken;
            emailService.sendResetPasswordEmail(email, resetLink);
            return ResponseEntity.ok("Un email de réinitialisation a été envoyé.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * Réinitialise le mot de passe avec un token JWT.
     *
     * @param token Token JWT pour réinitialiser le mot de passe.
     * @param newPassword Nouveau mot de passe.
     * @return Réponse HTTP avec un message de succès ou d'erreur.
     */
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        try {
            if (jwtUtil.isTokenExpired(token)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token invalide ou expiré.");
            }
            authService.resetPassword(token, newPassword);
            return ResponseEntity.ok("Mot de passe réinitialisé avec succès.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
