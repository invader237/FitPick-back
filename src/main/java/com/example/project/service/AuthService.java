package com.example.project.service;

import com.example.project.modele.Role;
import com.example.project.modele.User;
import com.example.project.repository.RoleRepository;
import com.example.project.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Inscrit un nouvel utilisateur dans le système.
     *
     * @param email     L'adresse email de l'utilisateur
     * @param password  Le mot de passe brut de l'utilisateur
     * @param firstName Le prénom de l'utilisateur
     * @param lastName  Le nom de l'utilisateur
     * @return L'utilisateur enregistré
     */
    public User registerUser(String email, String password, String firstName, String lastName) {
        // Vérifier si l'utilisateur existe déjà
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        // Récupérer le rôle USER par défaut
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Default role ROLE_USER not found"));

        // Créer et configurer un nouvel utilisateur
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password)); // Hachage sécurisé du mot de passe
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRoles(Collections.singleton(userRole));

        // Sauvegarder l'utilisateur dans la base
        return userRepository.save(user);
    }

    /**
     * Authentifie un utilisateur en vérifiant son email et son mot de passe.
     *
     * @param email       L'adresse email de l'utilisateur
     * @param rawPassword Le mot de passe brut saisi par l'utilisateur
     * @return L'utilisateur authentifié
     */
    public User authenticateUser(String email, String rawPassword) {
        // Rechercher l'utilisateur par email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        // Vérifier si le mot de passe correspond
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        return user;
    }

    /**
     * Change le mot de passe d'un utilisateur.
     *
     * @param email       L'adresse email de l'utilisateur
     * @param oldPassword L'ancien mot de passe de l'utilisateur
     * @param newPassword Le nouveau mot de passe de l'utilisateur
     */
    public void changePassword(String email, String oldPassword, String newPassword) {
        // Trouver l'utilisateur par email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Vérifier si l'ancien mot de passe correspond
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        // Mettre à jour avec le nouveau mot de passe hashé
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }
    
}
