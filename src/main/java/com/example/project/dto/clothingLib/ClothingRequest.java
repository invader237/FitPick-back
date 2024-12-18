package com.example.project.dto.clothingLib;

import java.util.List; 


public class ClothingRequest {
    /**
     * Represents a request to create a new clothing item.
     *
     * Attributes:
     * - `name`: The name of the clothing item.
     * - `userId`: The ID of the user who owns the clothing item.
     * - `tagIds`: A list of tag IDs associated with the clothing item.
     */

    private String name;
    private Long userId;
    private List<Long> tagIds;

    public ClothingRequest() {
    }

    public ClothingRequest(String name, Long userId, List<Long> tagIds) {
        this.name = name;
        this.userId = userId;
        this.tagIds = tagIds;
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
}
