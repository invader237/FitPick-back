package com.example.project.controller.clothingLib;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.project.service.clothingLib.ClothingService;
import com.example.project.modele.clothingLib.Clothing;
import com.example.project.modele.clothingLib.Tag;
import com.example.project.dto.clothingLib.ClothingRequest;

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

    /**
     * Add a new clothing item with tags for a user.
     * Example: POST http://localhost:8080/api/clothing/user/{userId}/add
     * Request body:
     * {
     *     "name": "Jacket",
     *     "tagIds": [1, 2, 3]
     * }
     * @param userId the ID of the user.
     * @param request the clothing item details.
     * @return The newly added clothing item.
     */
    @PostMapping("/user/{userId}/add")
    public ResponseEntity<Clothing> addClothing(@PathVariable("userId") Long userId, @RequestBody ClothingRequest request) {
        Clothing clothing = clothingService.addClothingWithTags(
            request.getName(),
            userId,
            request.getTagIds()
        );
        return ResponseEntity.ok(clothing);
    }

    /**
     * Update an existing clothing item with new details and tags.
     * Example: PUT http://localhost:8080/api/clothing/user/{userId}/{id}
     * Request body:
     * {
     *     "name": "New Jacket",
     *     "tagIds": [4, 5]
     * }
     * @param userId the ID of the user.
     * @param clothingId the ID of the clothing item.
     * @param request the new clothing item details.
     * @return The updated clothing item.
     */
    @PutMapping("/user/{userId}/{id}")
    public ResponseEntity<Clothing> updateClothing( @PathVariable("userId") Long userId, @PathVariable("id") Long clothingId, @RequestBody ClothingRequest request) {
        Clothing updatedClothing = clothingService.updateClothingWithTags(
            clothingId,
            request.getName(),
            userId,
            request.getTagIds()
        );
        return ResponseEntity.ok(updatedClothing);
    }


    /**
     * Delete a clothing item for a user.
     * Example: DELETE http://localhost:8080/api/clothing/user/{userId}/{id}/delete
     * @param userId the ID of the user.
     * @param clothingId the ID of the clothing item.
     * @return The deleted clothing item.
     */
    @DeleteMapping("/user/{userId}/{id}/delete")
    public ResponseEntity<Clothing> deleteClothing( @PathVariable("userId") Long userId, @PathVariable("id") Long clothingId) {
        Clothing deletedClothing = clothingService.deleteClothing(userId, clothingId);
        if (deletedClothing == null) {
            return ResponseEntity.notFound().build(); 
        }
        return ResponseEntity.ok(deletedClothing);
    }

}
