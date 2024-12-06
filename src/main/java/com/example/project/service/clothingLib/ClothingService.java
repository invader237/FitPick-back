package com.example.project.service.clothingLib;

import com.example.project.modele.clothingLib.Clothing;
import com.example.project.repository.clothingLib.ClothingRepository;
import com.example.project.modele.clothingLib.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClothingService {

    @Autowired
    private ClothingRepository clothingRepository;

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

}
