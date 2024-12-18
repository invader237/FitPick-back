package com.example.project.modele.clothingLib;

import jakarta.persistence.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tags")
public class Tag {
    /**
     * Represents a tag that can be associated with clothing items.
     *
     * Attributes:
     * - `id` (tag_id): Unique identifier for the tag.
     * - `name` (tag_lib): Name or label of the tag.
     * - `temperatureScore` (tag_temperature_score): Score indicating the suitability of this tag for specific temperatures.
     * - `windScore` (tag_wind_score): Score indicating the suitability of this tag for windy conditions.
     * - `rainScore` (tag_rain_score): Score indicating the suitability of this tag for rainy conditions.
     * - `clothes`: List of clothing items associated with this tag (many-to-many relationship).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tag_id;

    @Column(name = "tag_lib", nullable = false, length = 255)
    private String tag_lib;

    @Column(name = "tag_temperature_score", nullable = false)
    private int tag_temperature_score;

    @Column(name = "tag_wind_score", nullable = false)
    private int tag_wind_score;

    @Column(name = "tag_rain_score", nullable = false)
    private int tag_rain_score;

    @ManyToMany(mappedBy = "tags")
    @JsonIgnore  
    private List<Clothing> clothes;

    public Tag() {
    }

    public Long getTag_id() {
        return tag_id;
    }

    public void setTag_id(Long tag_id) {
        this.tag_id = tag_id;
    }

    public String getTag_lib() {
        return tag_lib;
    }

    public void setTag_lib(String tag_lib) {
        this.tag_lib = tag_lib;
    }

    public int getTag_temperature_score() {
        return tag_temperature_score;
    }

    public void setTag_temperature_score(int tag_temperature_score) {
        this.tag_temperature_score = tag_temperature_score;
    }

    public int getTag_wind_score() {
        return tag_wind_score;
    }

    public void setTag_wind_score(int tag_wind_score) {
        this.tag_wind_score = tag_wind_score;
    }

    public int getTag_rain_score() {
        return tag_rain_score;
    }

    public void setTag_rain_score(int tag_rain_score) {
        this.tag_rain_score = tag_rain_score;
    }

    public List<Clothing> getClothes() {
        return clothes;
    }

    public void setClothes(List<Clothing> clothes) {
        this.clothes = clothes;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "tag_id=" + tag_id +
                ", tag_lib='" + tag_lib + '\'' +
                ", tag_temperature_score=" + tag_temperature_score +
                ", tag_wind_score=" + tag_wind_score +
                ", tag_rain_score=" + tag_rain_score +
                '}';
    }
}
