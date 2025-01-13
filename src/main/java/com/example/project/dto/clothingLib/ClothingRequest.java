package com.example.project.dto.clothingLib;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class ClothingRequest {

    @NotEmpty(message = "Le nom est obligatoire.")
    private String name;

    @NotNull(message = "L'ID utilisateur est obligatoire.")
    private Long userId;

    @NotEmpty(message = "Au moins un tag est requis.")
    private List<Long> tagIds;

    private String imageUrl; // Champ pour l'URL de l'image

    public ClothingRequest() {
    }

    public ClothingRequest(String name, Long userId, List<Long> tagIds, String imageUrl) {
        this.name = name;
        this.userId = userId;
        this.tagIds = tagIds;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Long> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<Long> tagIds) {
        this.tagIds = tagIds;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "ClothingRequest{" +
                "name='" + name + '\'' +
                ", userId=" + userId +
                ", tagIds=" + tagIds +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
