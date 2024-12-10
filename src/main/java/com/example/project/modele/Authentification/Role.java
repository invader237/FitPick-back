package com.example.project.modele.Authentification;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "roles") // La table correspondante dans la base de données
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id") // Correspond au préfixe ajouté à la colonne
    private Long id;

    @Column(name = "role_name", nullable = false, unique = true) // Ajout explicite pour le préfixe
    private String name;

    // Constructeurs
    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Override equals et hashCode pour une bonne gestion des collections (comme Set)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(name, role.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    // Override toString pour le débogage
    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
