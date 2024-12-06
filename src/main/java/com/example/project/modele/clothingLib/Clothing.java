package com.example.project.modele.clothingLib;

import jakarta.persistence.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "clothes")
public class Clothing {
    /**
     * Represents a clothing item.
     *
     * Attributes:
     * - `clo_id`: Unique identifier for the clothing item.
     * - `clo_lib`: The name of the clothing item.
     * - `tags`: A list of associated tags.
     * - `userId`: The ID of the user who owns this clothing item.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clo_id;

    @Column(name = "clo_lib", nullable = false, length = 255)
    private String clo_lib;

    @ManyToMany
    @JoinTable(
        name = "clothe_tags",
        joinColumns = @JoinColumn(name = "clo_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @JsonIgnore
    private List<Tag> tags;

    @Column(name = "user_id")
    private Long userId;

    public Clothing() {
    }

    public Long getClo_id() {
        return clo_id;
    }

    public String getClo_lib() {
        return clo_lib;
    }

    public void setClo_lib(String clo_lib) {
        this.clo_lib = clo_lib;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Clothing{" +
                "clo_id=" + clo_id +
                ", clo_lib='" + clo_lib + '\'' +
                ", tags=" + tags +
                ", userId=" + userId +
                '}';
    }
}
