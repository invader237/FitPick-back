package com.example.project.service.clothingLib;

import com.example.project.dto.clothingLib.TagDTO;
import com.example.project.model.clothingLib.Tag;
import com.example.project.repository.clothingLib.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing Tag operations.
 */
@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    /**
     * Retrieve all tags available in the system and map them to TagDTO objects.
     *
     * @return A list of {@link TagDTO} representing all tags.
     */
    public List<TagDTO> getAllTags() {
        return tagRepository.findAll().stream()
                .map(TagDTO::mapToTagDTO) // Use the utility method from TagDTO to map entities
                .collect(Collectors.toList());
    }

    /**
     * Retrieve a specific tag by its ID and map it to a TagDTO.
     *
     * @param tagId The ID of the tag to retrieve.
     * @return The {@link TagDTO} object with the specified ID, or {@code null} if not found.
     */
    public TagDTO getTagById(Long tagId) {
        return tagRepository.findById(tagId)
                .map(TagDTO::mapToTagDTO) // Use the utility method to map the entity to DTO
                .orElse(null);
    }

    /**
     * Retrieve a list of tags by their IDs and map them to TagDTO objects.
     *
     * @param tagIds The list of tag IDs to retrieve.
     * @return A list of {@link TagDTO} objects corresponding to the specified IDs.
     * @throws IllegalArgumentException if one or more tags are not found.
     */
    public List<TagDTO> getTagsByIds(List<Long> tagIds) {
        List<Tag> tags = tagRepository.findAllById(tagIds);
        if (tags.size() != tagIds.size()) {
            throw new IllegalArgumentException("Some tags could not be found for the provided IDs: " + tagIds);
        }
        return tags.stream()
                .map(TagDTO::mapToTagDTO)
                .collect(Collectors.toList());
    }
}
