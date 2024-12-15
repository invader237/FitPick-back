package com.example.project.service.clothingLib;

import com.example.project.dto.clothingLib.ClothingDTO;
import com.example.project.dto.clothingLib.TagDTO;
import com.example.project.modele.clothingLib.Clothing;
import com.example.project.repository.clothingLib.ClothingRepository;
import com.example.project.modele.clothingLib.Tag;
import com.example.project.repository.clothingLib.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClothingService {

    @Autowired
    private ClothingRepository clothingRepository;

    @Autowired
    private TagRepository tagRepository;

    /**
     * Retrieve all clothing items associated with a specific user.
     *
     * @param userId The ID of the user whose clothing items are to be retrieved.
     * @return A list of {@link Clothing} objects belonging to the user.
     */
    public List<Clothing> getClothingByUserId(Long userId) {
        try {
            return clothingRepository.findByUserId(userId);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while retrieving clothing items for user ID " + userId, e);
        }
    }

    /**
     * Retrieve a specific clothing item associated with a user by its ID.
     *
     * @param userId The ID of the user who owns the clothing item.
     * @param cloId  The ID of the clothing item to retrieve.
     * @return The {@link Clothing} object with the given ID, or {@code null} if not found.
     */
    public Clothing getClothingById(Long userId, Long cloId) {
        return clothingRepository.findByUserIdAndCloId(userId, cloId);
    }

    /**
     * Retrieve all tags associated with a specific clothing item owned by a user.
     *
     * @param userId The ID of the user who owns the clothing item.
     * @param cloId  The ID of the clothing item whose tags are to be retrieved.
     * @return A list of {@link Tag} objects associated with the specified clothing item.
     * @throws IllegalArgumentException if the clothing item does not exist.
     */
    public List<Tag> getTagsByClothingId(Long userId, Long cloId) {
        try {
            Clothing clothing = getClothingById(userId, cloId);
            return clothing.getTags();
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while retrieving tags for clothing item with ID " + cloId + " and user ID " + userId, e);
        }
    }

    /**
     * Add a new clothing item to the database.
     *
     * @param clothingName The name of the clothing item.
     * @param userId The ID of the user who owns the clothing.
     * @param tagIds The IDs of the tags to associate with the clothing item.
     * @return The newly added {@link Clothing} object.
     */
    public Clothing addClothingWithTags(String clothingName, Long userId, List<Long> tagIds) {
        try {
            List<Tag> tags = tagRepository.findAllById(tagIds);
            if (tags.size() != tagIds.size()) {
                throw new IllegalArgumentException("Some tags could not be found with IDs: " + tagIds);
            }

            Clothing clothing = new Clothing();
            clothing.setClo_lib(clothingName);
            clothing.setUserId(userId);
            clothing.setTags(tags);

            return clothingRepository.save(clothing);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while adding the clothing with name " + clothingName + " for user ID " + userId, e);
        }
    }

    /**
     * Update an existing clothing item with new details and tags.
     *
     * @param clothingId The ID of the clothing item to update.
     * @param newName    The new name for the clothing item.
     * @param userId     The ID of the user who owns the clothing item.
     * @param tagIds     The IDs of the tags to associate with the clothing item.
     * @return The updated {@link Clothing} object.
     */
    public Clothing updateClothingWithTags(Long clothingId, String newName, Long userId, List<Long> tagIds) {
        try {
            Clothing clothing = clothingRepository.findById(clothingId)
                    .orElseThrow(() -> new IllegalArgumentException("Clothing not found with ID: " + clothingId));

            clothing.setClo_lib(newName);
            clothing.setUserId(userId);

            List<Tag> tags = tagRepository.findAllById(tagIds);
            if (tags.isEmpty() && !tagIds.isEmpty()) {
                throw new IllegalArgumentException("Some tags could not be found with IDs: " + tagIds);
            }
            clothing.setTags(tags);

            return clothingRepository.save(clothing);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while updating the clothing with ID " + clothingId + " for user ID " + userId, e);
        }
    }

    /**
     * Delete a clothing item from the database.
     *
     * @param userId The ID of the user who owns the clothing item.
     * @param cloId  The ID of the clothing item to delete.
     * @return The {@link Clothing} object that was deleted, or throws an exception if not found.
     * @throws IllegalArgumentException if the clothing item does not exist.
     */
    public Clothing deleteClothing(Long userId, Long cloId) {
        try {
            Clothing clothing = getClothingById(userId, cloId);
            clothingRepository.delete(clothing);
            return clothing;
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while deleting the clothing with ID " + cloId + " for user ID " + userId, e);
        }
    }

}
