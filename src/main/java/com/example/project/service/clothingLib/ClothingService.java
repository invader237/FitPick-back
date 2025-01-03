package com.example.project.service.clothingLib;

import com.example.project.dto.clothingLib.ClothingDTO;
import com.example.project.dto.clothingLib.TagDTO;
import com.example.project.model.clothingLib.Clothing;
import com.example.project.model.clothingLib.Tag;
import com.example.project.repository.clothingLib.ClothingRepository;
import com.example.project.repository.clothingLib.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Validated
public class ClothingService {

    private static final Logger log = LoggerFactory.getLogger(ClothingService.class);

    @Autowired
    private ClothingRepository clothingRepository;

    @Autowired
    private TagRepository tagRepository;

    /**
     * Retrieves all clothing items for a specific user.
     *
     * @param userId the ID of the user
     * @return a list of ClothingDTO objects
     */
    public List<ClothingDTO> getClothingByUserId(@NotNull Long userId) {
        log.debug("Fetching clothing for user ID: {}", userId);
        return clothingRepository.findByUserId(userId).stream()
                .map(this::mapToClothingDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a specific clothing item by user ID and clothing ID.
     *
     * @param userId the ID of the user
     * @param cloId the ID of the clothing item
     * @return the Clothing object
     */
    public Clothing getClothingById(@NotNull Long userId, @NotNull Long cloId) {
        log.debug("Fetching clothing with userId={} and cloId={}", userId, cloId);
        return Optional.ofNullable(clothingRepository.findByUserIdAndCloId(userId, cloId))
                .orElseThrow(() -> {
                    log.warn("No clothing found for userId={} and cloId={}", userId, cloId);
                    return new IllegalArgumentException("Clothing not found with ID: " + cloId);
                });
    }

    /**
     * Retrieves the tags associated with a specific clothing item.
     *
     * @param clothingId the ID of the clothing item
     * @return a list of TagDTO objects associated with the clothing
     */
    public List<TagDTO> getTagsForClothing(@NotNull Long clothingId) {
        log.debug("Fetching tags for clothing ID: {}", clothingId);
        Clothing clothing = clothingRepository.findById(clothingId)
                .orElseThrow(() -> new IllegalArgumentException("Clothing not found with ID: " + clothingId));
        return clothing.getTags().stream()
                .map(TagDTO::mapToTagDTO)
                .collect(Collectors.toList());
    }

    /**
     * Adds a new clothing item with associated tags.
     *
     * @param clothingName the name of the clothing
     * @param userId the ID of the user
     * @param tagIds the list of tag IDs
     * @param imageUrl the URL of the image
     * @return the created ClothingDTO object
     */
    public ClothingDTO addClothingWithTags(@NotNull String clothingName, @NotNull Long userId, List<Long> tagIds, String imageUrl) {
        validateClothingInput(clothingName, imageUrl);

        log.debug("Fetching tags for IDs: {}", tagIds);
        List<Tag> tags = tagRepository.findAllById(tagIds);
        if (tags.size() != tagIds.size()) {
            throw new IllegalArgumentException("Some tags were not found for IDs: " + tagIds);
        }

        Clothing clothing = new Clothing(clothingName, userId, imageUrl, tags);
        log.debug("Saving new clothing: {}", clothing);
        return mapToClothingDTO(clothingRepository.save(clothing));
    }

    /**
     * Updates an existing clothing item with new data.
     *
     * @param clothingId the ID of the clothing item to update
     * @param newName the new name of the clothing
     * @param userId the ID of the user
     * @param tagIds the list of new tag IDs
     * @param imageUrl the new URL of the image
     * @return the updated ClothingDTO object
     */
    public ClothingDTO updateClothingWithTags(@NotNull Long clothingId, String newName, @NotNull Long userId, List<Long> tagIds, String imageUrl) {
        log.debug("Fetching clothing with ID {} for update", clothingId);
        Clothing clothing = clothingRepository.findById(clothingId)
                .orElseThrow(() -> new IllegalArgumentException("Clothing not found with ID: " + clothingId));

        if (newName != null && !newName.trim().isEmpty()) {
            clothing.setClo_lib(newName);
        }

        if (tagIds != null && !tagIds.isEmpty()) {
            log.debug("Fetching tags for IDs: {}", tagIds);
            List<Tag> tags = tagRepository.findAllById(tagIds);
            if (tags.size() != tagIds.size()) {
                throw new IllegalArgumentException("Some tags were not found for IDs: " + tagIds);
            }
            clothing.setTags(tags);
        }

        if (imageUrl != null && !imageUrl.trim().isEmpty()) {
            clothing.setCloImageUrl(imageUrl);
        }

        log.debug("Saving updated clothing: {}", clothing);
        return mapToClothingDTO(clothingRepository.save(clothing));
    }

    /**
     * Deletes a specific clothing item.
     *
     * @param userId the ID of the user
     * @param cloId the ID of the clothing item
     * @return the deleted ClothingDTO object
     */
    public ClothingDTO deleteClothing(@NotNull Long userId, @NotNull Long cloId) {
        log.debug("Fetching clothing with ID {} for deletion", cloId);
        Clothing clothing = clothingRepository.findByUserIdAndCloId(userId, cloId);
        if (clothing == null) {
            throw new IllegalArgumentException("Clothing not found with ID: " + cloId);
        }
        clothingRepository.delete(clothing);
        log.info("Clothing deleted successfully: {}", clothing);
        return mapToClothingDTO(clothing);
    }

    /**
     * Maps a Clothing object to a ClothingDTO.
     *
     * @param clothing the Clothing object to map
     * @return the mapped ClothingDTO object
     */
    private ClothingDTO mapToClothingDTO(Clothing clothing) {
        log.debug("Mapping clothing to DTO: {}", clothing);

        return new ClothingDTO(
            clothing.getClo_id(),
            clothing.getClo_lib(),
            clothing.getTags() != null
            ? clothing.getTags().stream()
                .map(TagDTO::mapToTagDTO)
                .collect(Collectors.toList())
            : List.of(),
            clothing.getCloImageUrl()
        );
    }

    /**
     * Validates input data for clothing.
     *
     * @param name the name of the clothing
     * @param imageUrl the URL of the image
     */
    private void validateClothingInput(String name, String imageUrl) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Clothing name is required.");
        }

        if (imageUrl != null && !imageUrl.startsWith("http://") && !imageUrl.startsWith("https://")) {
            throw new IllegalArgumentException("Image URL must start with http:// or https://.");
        }
    }
}
