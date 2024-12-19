package com.example.project.repository.Authentification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.project.model.Authentification.Role;

import java.util.Optional;

/**
 * Repository pour gérer les opérations sur l'entité Role.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Recherche un rôle par son nom.
     *
     * @param name Nom du rôle.
     * @return Un Optional contenant le rôle s'il existe.
     */
    Optional<Role> findByName(String name);
}
