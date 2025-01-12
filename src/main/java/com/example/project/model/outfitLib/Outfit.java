package com.example.project.model.outfitLib;

import com.example.project.model.clothingLib.Clothing;

import jakarta.persistence.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "outfits")
public class Outfit {
    /**
     * Represents an outfit.
     *
     * Attributes:
     * - `out_id`: Unique identifier for the outfit.
     * - `out_lib`: The name of the outfit.
     * - `clothes`: A list of clothing items that make up the outfit.
     * - `userId`: The ID of the user who owns this outfit.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fit_id;

    @Column(name = "fit_lib", nullable = false, length = 255)
    private String fit_lib;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "outfit_clothes",
        joinColumns = @JoinColumn(name = "fit_id"),
        inverseJoinColumns = @JoinColumn(name = "clo_id")
    )
    @JsonIgnore
    private List<Clothing> clothes;

    @Column(name = "user_id")
    private Long userId;

    public Outfit() {
    }

    public Long getFit_id() {
        return fit_id;
    }

    public void setFit_id(Long fit_id) {
        this.fit_id = fit_id;
    }

    public String getFit_lib() {
        return fit_lib;
    }

    public void setFit_lib(String fit_lib) {
        this.fit_lib = fit_lib;

    }

    public List<Clothing> getClothes() {
        return clothes;
    }

    public void setClothes(List<Clothing> clothes) {
        this.clothes = clothes;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
