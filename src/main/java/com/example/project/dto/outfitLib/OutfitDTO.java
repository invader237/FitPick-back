package com.example.project.dto.outfitLib; 

import com.example.project.dto.clothingLib.ClothingDTO;

import java.util.List;

public class OutfitDTO {
    private Long id;
    private String name;
    private List<ClothingDTO> clothingList;

    // Constructor
    public OutfitDTO(Long id, String name, List<ClothingDTO> clothingList) {
        this.id = id;
        this.name = name;
        this.clothingList = clothingList;
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

    public List<ClothingDTO> getClothingList() {
        return clothingList;
    }

    public void setClothingList(List<ClothingDTO> clothingList) {
        this.clothingList = clothingList;
    }

}

