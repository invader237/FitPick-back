package com.example.project.service.clothingLib;

import com.example.project.modele.clothingLib.Clothing;
import com.example.project.repository.clothingLib.ClothingRepository;
import com.example.project.modele.clothingLib.Tag;
import com.example.project.repository.clothingLib.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

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
        return clothingRepository.findByUserId(userId);
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
     */
    public List<Tag> getTagsByClothingId(Long userId, Long cloId) {
        return clothingRepository.findTagsByCloId(userId, cloId);
    }

    /**
     * Add a new clothing item to the database.
     *
     * @param clothing The {@link Clothing} object to add.
     * @return The newly added {@link Clothing} object.
     */
    public Clothing addClothingWithTags(String clothingName, Long userId, List<Long> tagIds) {
        List<Tag> tags = tagRepository.findAllById(tagIds);

        Clothing clothing = new Clothing();
        clothing.setClo_lib(clothingName);
        clothing.setUserId(userId);
        clothing.setTags(tags);

        return clothingRepository.save(clothing);
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
        Clothing clothing = clothingRepository.findById(clothingId)
            .orElseThrow(() -> new RuntimeException("Clothing not found with ID: " + clothingId));

        clothing.setClo_lib(newName);
        clothing.setUserId(userId);

        List<Tag> tags = tagRepository.findAllById(tagIds);
        clothing.setTags(tags);

        return clothingRepository.save(clothing);
    }

    /**
     * Delete a clothing item from the database.
     *
     * @param userId The ID of the user who owns the clothing item.
     * @param cloId  The ID of the clothing item to delete.
     */
    public Clothing deleteClothing(Long userId, Long cloId) {
        Clothing clothing = clothingRepository.findByUserIdAndCloId(userId, cloId);
        if (clothing != null) {
            clothingRepository.delete(clothing); 
        }
        return clothing; 
    }

}
