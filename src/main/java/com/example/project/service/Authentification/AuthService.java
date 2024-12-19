package com.example.project.service.Authentification;

import com.example.project.model.Authentification.Role;
import com.example.project.model.Authentification.User;
import com.example.project.repository.Authentification.RoleRepository;
import com.example.project.repository.Authentification.UserRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Service pour gérer l'authentification et les opérations liées aux utilisateurs.
 * Inclut l'enregistrement, la gestion des mots de passe et la gestion des tokens JWT.
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private Key key;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration.auth}")
    private long jwtExpirationAuth;

    @Value("${jwt.expiration.reset}")
    private long jwtExpirationReset;

    @Value("${security.server.salt}")
    private String serverSalt;

    private final Map<String, Long> resetRequestTimestamps = new ConcurrentHashMap<>();
    private final Set<String> usedTokens = new CopyOnWriteArraySet<>();

    public AuthService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @PostConstruct
    public void init() {
        byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Vérifie si un utilisateur peut demander une réinitialisation de mot de passe.
     *
     * @param email Email de l'utilisateur.
     * @return True si la demande est permise, false sinon.
     */
    public boolean canRequestReset(String email) {
        long currentTime = System.currentTimeMillis();
        if (resetRequestTimestamps.containsKey(email)) {
            long lastRequestTime = resetRequestTimestamps.get(email);
            if (currentTime - lastRequestTime < 30000) { // 30 secondes de cooldown
                return false;
            }
        }
        resetRequestTimestamps.put(email, currentTime);
        return true;
    }

    /**
     * Génère un token JWT pour la réinitialisation de mot de passe.
     *
     * @param email Email de l'utilisateur.
     * @return Token JWT généré.
     */
    public String generateResetToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationReset))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Réinitialise le mot de passe avec un token JWT.
     *
     * @param token       Token JWT.
     * @param newPassword Nouveau mot de passe (haché en SHA256 côté client).
     */
    public void resetPassword(String token, String newPassword) {
        String email = extractEmailFromToken(token);

        if (!validateToken(token)) {
            throw new RuntimeException("Le lien de réinitialisation a expiré ou est invalide.");
        }

        User user = getUserByEmail(email);

        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new IllegalArgumentException("Le nouveau mot de passe ne peut pas être identique à l'ancien.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        markTokenAsUsed(token);
    }

    /**
     * Valide un token JWT.
     *
     * @param token Token JWT.
     * @return True si le token est valide, false sinon.
     */
    private boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);

            if (usedTokens.contains(token)) {
                throw new RuntimeException("Le token a déjà été utilisé.");
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Marque un token comme utilisé.
     *
     * @param token Token JWT.
     */
    private void markTokenAsUsed(String token) {
        usedTokens.add(token);
    }

    /**
     * Nettoie les tokens utilisés (exécuté toutes les heures).
     */
    @Scheduled(fixedRate = 3600000) // Toutes les heures
    public void cleanUsedTokens() {
        usedTokens.clear();
    }

    /**
     * Extrait l'email depuis un token JWT.
     *
     * @param token Token JWT.
     * @return Email extrait du token.
     */
    private String extractEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Vérifie si un utilisateur existe par email.
     *
     * @param email Email à vérifier.
     * @return True si l'utilisateur existe, sinon false.
     */
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email.toLowerCase());
    }

    /**
     * Récupère un utilisateur par email.
     *
     * @param email Email de l'utilisateur.
     * @return Utilisateur trouvé.
     */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec cet email."));
    }

    /**
     * Enregistre un nouvel utilisateur avec le rôle par défaut "ROLE_USER".
     *
     * @param email        Email de l'utilisateur.
     * @param hashedPassword Mot de passe haché en SHA256.
     * @param firstName    Prénom.
     * @param lastName     Nom.
     * @return Utilisateur enregistré.
     */
    public User registerUser(String email, String hashedPassword, String firstName, String lastName) {
        if (emailExists(email)) {
            throw new RuntimeException("Cet email est déjà utilisé.");
        }

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Le rôle par défaut ROLE_USER est introuvable."));

        String finalHash = passwordEncoder.encode(hashedPassword);

        User user = new User();
        user.setEmail(email.toLowerCase());
        user.setPassword(finalHash);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRoles(Collections.singleton(userRole));

        return userRepository.save(user);
    }

    /**
     * Authentifie un utilisateur avec email et mot de passe.
     *
     * @param email         Email de l'utilisateur.
     * @param hashedPassword Mot de passe haché en SHA256.
     * @return Utilisateur authentifié.
     */
    public User authenticateUser(String email, String hashedPassword) {
        User user = getUserByEmail(email);

        if (!passwordEncoder.matches(hashedPassword, user.getPassword())) {
            throw new RuntimeException("Email ou mot de passe invalide.");
        }

        return user;
    }

    /**
     * Change le mot de passe d'un utilisateur.
     *
     * @param email       Email de l'utilisateur.
     * @param oldPassword Ancien mot de passe haché en SHA256.
     * @param newPassword Nouveau mot de passe haché en SHA256.
     */
    public void changePassword(String email, String oldPassword, String newPassword) {
        User user = getUserByEmail(email);

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("L'ancien mot de passe est incorrect.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    /**
     * Génère un token JWT d'authentification.
     *
     * @param email Email de l'utilisateur.
     * @param roles Rôles de l'utilisateur.
     * @return Token JWT généré.
     */
    public String generateAuthToken(String email, List<String> roles) {
        return Jwts.builder()
                .setSubject(email)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationAuth))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
