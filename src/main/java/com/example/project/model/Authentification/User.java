package com.example.project.model.Authentification;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Entité représentant un utilisateur.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "email") // Comparaison basée sur l'email
@ToString(exclude = "roles") // Exclut les rôles pour éviter les boucles infinies
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_email", nullable = false, unique = true)
    private String email;

    @Column(name = "user_password", nullable = false)
    private String password;

    @Column(name = "user_first_name")
    private String firstName;

    @Column(name = "user_last_name")
    private String lastName;

    @Column(name = "user_avatar_url") // Ajout du champ avatar
    private String avatar;

    @Column(name = "user_created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "user_updated_at")
    private LocalDateTime updatedAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Getter explicite pour l'ID.
     * 
     * @return l'ID de l'utilisateur.
     */
    public Long getId() {
        return id;
    }

    /**
     * Getter explicite pour l'email.
     * 
     * @return l'email de l'utilisateur.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter explicite pour l'email.
     * 
     * @param email Nouvel email.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter explicite pour le mot de passe.
     * 
     * @return le mot de passe.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter explicite pour le mot de passe.
     * 
     * @param password Nouveau mot de passe.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Getter explicite pour le prénom.
     * 
     * @return le prénom de l'utilisateur.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Setter explicite pour le prénom.
     * 
     * @param firstName Nouveau prénom.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Getter explicite pour le nom de famille.
     * 
     * @return le nom de famille de l'utilisateur.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Setter explicite pour le nom de famille.
     * 
     * @param lastName Nouveau nom de famille.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Getter explicite pour l'avatar.
     * 
     * @return l'URL de l'avatar de l'utilisateur.
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * Setter explicite pour l'avatar.
     * 
     * @param avatar Nouvel URL de l'avatar.
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    /**
     * Getter explicite pour les rôles.
     * 
     * @return les rôles de l'utilisateur.
     */
    public Set<Role> getRoles() {
        return roles;
    }

    /**
     * Setter explicite pour les rôles.
     * 
     * @param roles Nouveaux rôles.
     */
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
