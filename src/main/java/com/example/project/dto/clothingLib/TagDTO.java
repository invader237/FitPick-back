package com.example.project.dto.clothingLib;

import com.example.project.model.clothingLib.Tag;

/**
 * Data Transfer Object for Tag.
 */
public class TagDTO {
    private Long tagId;
    private String tagLib;
    private Integer tagTemperatureScore;
    private Integer tagWindScore;
    private Integer tagRainScore;

    public TagDTO() {
    }

    public TagDTO(Long tagId, String tagLib, Integer tagTemperatureScore, Integer tagWindScore, Integer tagRainScore) {
        this.tagId = tagId;
        this.tagLib = tagLib;
        this.tagTemperatureScore = tagTemperatureScore;
        this.tagWindScore = tagWindScore;
        this.tagRainScore = tagRainScore;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public String getTagLib() {
        return tagLib;
    }

    public void setTagLib(String tagLib) {
        this.tagLib = tagLib;
    }

    public Integer getTagTemperatureScore() {
        return tagTemperatureScore;
    }

    public void setTagTemperatureScore(Integer tagTemperatureScore) {
        this.tagTemperatureScore = tagTemperatureScore;
    }

    public Integer getTagWindScore() {
        return tagWindScore;
    }

    public void setTagWindScore(Integer tagWindScore) {
        this.tagWindScore = tagWindScore;
    }

    public Integer getTagRainScore() {
        return tagRainScore;
    }

    public void setTagRainScore(Integer tagRainScore) {
        this.tagRainScore = tagRainScore;
    }

    /**
     * Utility method to map a Tag entity to a TagDTO.
     *
     * @param tag The Tag entity to map.
     * @return The mapped TagDTO.
     */
    public static TagDTO mapToTagDTO(Tag tag) {
        if (tag == null) {
            throw new IllegalArgumentException("Tag cannot be null");
        }
        return new TagDTO(
                tag.getTag_id(),
                tag.getTag_lib(),
                tag.getTag_temperature_score(),
                tag.getTag_wind_score(),
                tag.getTag_rain_score()
        );
    }
}
