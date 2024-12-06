package com.example.project.controller.clothingLib;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.project.service.clothingLib.ClothingService;
import com.example.project.modele.clothingLib.Clothing;
import com.example.project.modele.clothingLib.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/clothing")
public class ClothingLibController {

    @Autowired
    private ClothingService clothingService;

    /**
     * Get all clothing items for a specific user.
     * Example: GET http://localhost:8080/api/clothing/user/{userId}
     * @param userId the ID of the user.
     * @return List of clothing items belonging to the user.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Clothing>> getClothingByUserId(@PathVariable("userId") Long userId) {
        List<Clothing> clothingList = clothingService.getClothingByUserId(userId);
        if (!clothingList.isEmpty()) {
            return ResponseEntity.ok(clothingList);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get all tags for a specific clothing item of a user.
     * Example: GET http://localhost:8080/api/clothing/user/{userId}/item/{clothingId}/tags
     * @param userId the ID of the user.
     * @param clothingId the ID of the clothing item.
     * @return List of tags for the clothing item.
     */
    @GetMapping("/user/{userId}/item/{clothingId}/tags")
    public ResponseEntity<List<Tag>> getTagsByClothingId(@PathVariable("userId") Long userId, @PathVariable("clothingId") Long clothingId) {
        List<Tag> tags = clothingService.getTagsByClothingId(userId, clothingId);
        if (!tags.isEmpty()) {
            return ResponseEntity.ok(tags);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get details of a specific clothing item for a user.
     * Example: GET http://localhost:8080/api/clothing/user/{userId}/item/{clothingId}
     * @param userId the ID of the user.
     * @param clothingId the ID of the clothing item.
     * @return The clothing item details.
     */
    @GetMapping("/user/{userId}/item/{clothingId}")
    public ResponseEntity<Clothing> getClothingById(@PathVariable("userId") Long userId, @PathVariable("clothingId") Long clothingId) {
        Clothing clothing = clothingService.getClothingById(userId, clothingId);
        if (clothing != null) {
            return ResponseEntity.ok(clothing);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
