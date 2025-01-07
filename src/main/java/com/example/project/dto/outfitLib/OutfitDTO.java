package com.example.project.dto.outfitLib;

import com.example.project.dto.clothingLib.ClothingDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public class OutfitDTO {
    private Long id;
    private String name;
    private List<Long> clothingList; // Doit être une List<Long>

    // Constructeurs, getters et setters
    public OutfitDTO(Long id, String name, List<Long> clothingList) {
        this.id = id;
        this.name = name;
        this.clothingList = clothingList;
    }

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

    public List<Long> getClothingList() {
        return clothingList;
    }

    public void setClothingList(List<Long> clothingList) {
        this.clothingList = clothingList;
    }
}

