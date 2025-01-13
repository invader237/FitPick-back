package com.example.project.dto.clothingLib;

import java.util.List;

public class ClothingDTO {
    private Long cloId;
    private String cloLib;
    private List<TagDTO> tags; // Utilisation de TagDTO pour ne pas exposer l'entité directement
    private String cloImageUrl;

    public ClothingDTO(Long cloId, String cloLib, List<TagDTO> tags, String cloImageUrl) {
        this.cloId = cloId;
        this.cloLib = cloLib;
        this.tags = tags;
        this.cloImageUrl = cloImageUrl;
    }

    public Long getCloId() {
        return cloId;
    }

    public void setCloId(Long cloId) {
        this.cloId = cloId;
    }

    public String getCloLib() {
        return cloLib;
    }

    public void setCloLib(String cloLib) {
        this.cloLib = cloLib;
    }

    public List<TagDTO> getTags() {
        return tags;
    }

    public void setTags(List<TagDTO> tags) {
        this.tags = tags;
    }

    public String getCloImageUrl() {
        return cloImageUrl;
    }

    public void setCloImageUrl(String cloImageUrl) {
        this.cloImageUrl = cloImageUrl;
    }
}
