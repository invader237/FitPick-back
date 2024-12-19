package com.example.project.dto.clothingLib;

import java.util.List;

public class ClothingDTO {
    private Long id;
    private String name;
    private List<TagDTO> tags;

    // Constructor
    public ClothingDTO(Long id, String name, List<TagDTO> tags) {
        this.id = id;
        this.name = name;
        this.tags = tags;
    }

    // Getters and Setters
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

    public List<TagDTO> getTags() {
        return tags;
    }

    public void setTags(List<TagDTO> tags) {
        this.tags = tags;
    }
}
