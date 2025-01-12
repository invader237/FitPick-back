package com.example.project.controller;

import com.example.project.model.Authentification.User;
import com.example.project.service.Profile.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Contrôleur REST pour la gestion des profils utilisateur.
 */
@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;

    /**
     * Constructeur pour injecter le service de gestion des profils.
     *
     * @param profileService Service pour gérer les profils.
     */
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    /**
     * Récupère le profil d'un utilisateur par son email.
     *
     * @param email L'email de l'utilisateur.
     * @return Les informations de l'utilisateur.
     */
    @GetMapping("/{email}")
    public ResponseEntity<User> getProfile(@PathVariable("email") String email) {
        User user = profileService.getProfileByEmail(email);
        return ResponseEntity.ok(user);
    }

    /**
     * Met à jour les informations du profil d'un utilisateur.
     *
     * @param email         L'email de l'utilisateur.
     * @param updatedProfile Les nouvelles informations du profil.
     * @return Les informations mises à jour de l'utilisateur.
     */
    @PutMapping("/{email}/update")
    public ResponseEntity<User> updateProfile(@PathVariable("email") String email, @RequestBody User updatedProfile) {
        User user = profileService.updateProfile(email, updatedProfile);
        return ResponseEntity.ok(user);
    }

    /**
     * Met à jour l'avatar d'un utilisateur.
     *
     * @param email      L'email de l'utilisateur.
     * @param avatarFile Le fichier de l'avatar.
     * @return L'URL de l'avatar mis à jour.
     * @throws IOException Si une erreur survient lors du téléchargement du fichier.
     */
    @PostMapping("/{email}/update-avatar")
    public ResponseEntity<String> updateAvatar(@PathVariable("email") String email, @RequestParam("file") MultipartFile avatarFile) throws IOException {
        String avatarUrl = profileService.updateAvatar(email, avatarFile);
        return ResponseEntity.ok(avatarUrl);
    }
}
