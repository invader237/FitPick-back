package com.example.project.controller.Authentification;

import com.example.project.config.Authentification.JwtUtil;
import com.example.project.dto.Authentification.ChangePasswordDTO;
import com.example.project.dto.Authentification.LoginRequestDTO;
import com.example.project.dto.Authentification.LoginResponseDTO;
import com.example.project.dto.Authentification.RegisterRequestDTO;
import com.example.project.modele.Authentification.Role;
import com.example.project.modele.Authentification.User;
import com.example.project.service.Authentification.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000") // Autoriser uniquement localhost:3000
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Endpoint pour l'inscription.
     *
     * @param request DTO contenant les informations nécessaires pour enregistrer un utilisateur
     * @return Réponse HTTP indiquant le succès ou l'échec de l'inscription
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
            return ResponseEntity.ok("User registered successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Endpoint pour la connexion.
     *
     * @param request DTO contenant les informations nécessaires pour l'authentification
     * @return Réponse HTTP contenant le JWT en cas de succès ou une erreur en cas d'échec
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        try {
            // Authentifier l'utilisateur
            User user = authService.authenticateUser(request.getEmail(), request.getPassword());

            // Convertir Set<Role> en List<String> pour les rôles de l'utilisateur
            List<String> roles = user.getRoles().stream()
                    .map(Role::getName)
                    .collect(Collectors.toList());

            // Générer le JWT avec les rôles
            String token = jwtUtil.generateToken(user.getEmail(), roles);

            return ResponseEntity.ok(new LoginResponseDTO(token));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new LoginResponseDTO(null));
        }
    }

    /**
     * Endpoint pour récupérer les informations de l'utilisateur connecté.
     *
     * @return Informations de l'utilisateur connecté
     */
    @GetMapping("/me")
    public ResponseEntity<User> getUserInfo() {
        try {
            // Récupérer l'utilisateur connecté via le token
            String email = SecurityContextHolder.getContext().getAuthentication().getName();

            User user = authService.getUserByEmail(email);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Endpoint pour changer le mot de passe de l'utilisateur connecté.
     *
     * @param changePasswordDTO DTO contenant l'ancien et le nouveau mot de passe
     * @return Réponse HTTP indiquant le succès ou l'échec de l'opération
     */
    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();

            authService.changePassword(
                email,
                changePasswordDTO.getOldPassword(),
                changePasswordDTO.getNewPassword()
            );
            return ResponseEntity.ok("Mot de passe changé avec succès");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
