package com.example.project.dto.outfitLib;

import com.example.project.dto.clothingLib.ClothingDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public class OutfitDisplay {
    private Long id;
    private String name;
    private List<String> cloImageUrlList;

    // Constructeurs, getters et setters
    public OutfitDisplay(Long id, String name, List<String> cloImageUrlList) {
        this.id = id;
        this.name = name;
        this.cloImageUrlList = cloImageUrlList;
    }

    public OutfitDisplay(String name, List<String> cloImageUrlList) {
        this.name = name;
        this.cloImageUrlList = cloImageUrlList;
    }

    public OutfitDisplay() {
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

    public List<String> getCloImageUrlList() {
        return cloImageUrlList;
    }

    public void setCloImageUrlList(List<String> cloImageUrlList) {
        this.cloImageUrlList = cloImageUrlList;
    }

}

