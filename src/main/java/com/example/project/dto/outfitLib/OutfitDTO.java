package com.example.project.dto.outfitLib;

import com.example.project.dto.clothingLib.ClothingDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public class OutfitDTO {

    private Long id;

    @NotBlank(message = "Le nom de la tenue est obligatoire.")
    private String name;

    @NotNull(message = "La liste des vêtements ne peut pas être nulle.")
    @Size(min = 4, max = 4, message = "Une tenue doit contenir exactement 4 vêtements.")
    private List<ClothingDTO> clothingList;

    public OutfitDTO() {
    }

    public OutfitDTO(Long id, String name, List<ClothingDTO> clothingList) {
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

    public List<ClothingDTO> getClothingList() {
        return clothingList;
    }

    public void setClothingList(List<ClothingDTO> clothingList) {
        this.clothingList = clothingList;
    }
}
