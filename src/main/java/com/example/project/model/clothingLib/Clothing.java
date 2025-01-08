package com.example.project.model.clothingLib;

import jakarta.persistence.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Represents a clothing item.
 *
 * Attributes:
 * - `clo_id`: Unique identifier for the clothing item.
 * - `clo_lib`: The name of the clothing item.
 * - `tags`: A list of associated tags.
 * - `userId`: The ID of the user who owns this clothing item.
 * - `cloImageUrl`: URL of the image associated with the clothing item.
 */
@Entity
@Table(name = "clothes")
public class Clothing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clo_id;

    @Column(name = "clo_lib", nullable = false, length = 255)
    private String clo_lib;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "clothe_tags",
        joinColumns = @JoinColumn(name = "clo_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @JsonIgnore
    private List<Tag> tags;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "clo_image_url")
    private String cloImageUrl;

    /**
     * Default constructor.
     */
    public Clothing() {
    }

    /**
     * Constructor with all attributes.
     *
     * @param clo_lib the name of the clothing
     * @param userId the ID of the user who owns the clothing
     * @param cloImageUrl the URL of the clothing image
     * @param tags the list of tags associated with the clothing
     */
    public Clothing(String clo_lib, Long userId, String cloImageUrl, List<Tag> tags) {
        this.clo_lib = clo_lib;
        this.userId = userId;
        this.cloImageUrl = cloImageUrl;
        this.tags = tags;
    }

    // Getters and setters
    public Long getClo_id() {
        return clo_id;
    }

    public void setClo_id(Long clo_id) {
        this.clo_id = clo_id;
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

    public String getCloImageUrl() {
        return cloImageUrl;
    }

    public void setCloImageUrl(String cloImageUrl) {
        this.cloImageUrl = cloImageUrl;
    }

    @Override
    public String toString() {
        return "Clothing{" +
                "clo_id=" + clo_id +
                ", clo_lib='" + clo_lib + '\'' +
                ", tags=" + tags +
                ", userId=" + userId +
                ", cloImageUrl='" + cloImageUrl + '\'' +
                '}';
    }
}
