package com.example.project.repository.Authentification;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.project.model.Authentification.User;

import java.util.Optional;

/**
 * Repository pour gérer les opérations sur l'entité User.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Recherche un utilisateur par son email.
     *
     * @param email Email de l'utilisateur.
     * @return Un Optional contenant l'utilisateur s'il existe.
     */
    Optional<User> findByEmail(String email);

    /**
     * Vérifie si un utilisateur existe déjà avec un email donné.
     *
     * @param email Email de l'utilisateur.
     * @return true si l'email existe, sinon false.
     */
    boolean existsByEmail(String email);
}
