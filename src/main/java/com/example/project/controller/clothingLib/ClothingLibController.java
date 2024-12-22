package com.example.project.controller.clothingLib;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.example.project.service.clothingLib.ClothingService;
import com.example.project.model.clothingLib.Clothing;
import com.example.project.model.clothingLib.Tag;
import com.example.project.dto.clothingLib.ClothingRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/clothing")
public class ClothingLibController {

    @Autowired
    private ClothingService clothingService;

    /**
     * Get all clothing items for a specific user.
     * Example: GET http://localhost:8080/api/clothing/user/{userId}
     *
     * @param userId the ID of the user.
     * @return List of clothing items belonging to the user, or an appropriate error response.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getClothingByUserId(@PathVariable("userId") Long userId) {
        try {
            List<Clothing> clothingList = clothingService.getClothingByUserId(userId);

            if (clothingList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body("No clothing items found for user ID " + userId);
            }

            return ResponseEntity.ok(clothingList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while retrieving clothing items for user ID " + userId);
        }
    }

    /**
     * Get all tags for a specific clothing item of a user.
     * Example: GET http://localhost:8080/api/clothing/user/{userId}/item/{clothingId}/tags
     *
     * @param userId the ID of the user.
     * @param clothingId the ID of the clothing item.
     * @return List of tags for the clothing item, or an error response if not found.
     */
    @GetMapping("/user/{userId}/item/{clothingId}/tags")
    public ResponseEntity<?> getTagsByClothingId(
            @PathVariable("userId") Long userId,
            @PathVariable("clothingId") Long clothingId
    ) {
        try {
            List<Tag> tags = clothingService.getTagsByClothingId(userId, clothingId);

            if (tags.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body("No tags found for clothing item with ID " + clothingId + " owned by user ID " + userId);
            }

            return ResponseEntity.ok(tags);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while retrieving tags for the clothing item.");
        }
    }

    /**
     * Get details of a specific clothing item for a user along with its associated tags.
     * Example: GET http://localhost:8080/api/clothing/user/{userId}/item/{clothingId}
     *
     * @param userId the ID of the user.
     * @param clothingId the ID of the clothing item.
     * @return The clothing item details, including its tags.
     */
    @GetMapping("/user/{userId}/item/{clothingId}")
    public ResponseEntity<?> getClothingById(
            @PathVariable("userId") Long userId,
            @PathVariable("clothingId") Long clothingId
    ) {
        try {
            Clothing clothing = clothingService.getClothingById(userId, clothingId);

            if (clothing == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Clothing item not found for user ID " + userId + " and clothing ID " + clothingId);
            }

            List<Tag> tags = clothingService.getTagsByClothingId(userId, clothingId);
            Map<String, Object> response = new HashMap<>();
            response.put("clothing", clothing);
            response.put("tags", tags);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while retrieving the clothing item.");
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
     *
     * @param userId the ID of the user.
     * @param request the clothing item details.
     * @return A response indicating the result of the addition.
     */
    @PostMapping("/user/{userId}/add")
    public ResponseEntity<?> addClothing(
            @PathVariable("userId") Long userId,
            @RequestBody ClothingRequest request
    ) {
        try {
            Clothing clothing = clothingService.addClothingWithTags(
                request.getName(),
                userId,
                request.getTagIds()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(clothing);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while adding the clothing.");
        }
    }

    /**
     * Update an existing clothing item with new details and tags.
     * Example: PUT http://localhost:8080/api/clothing/user/{userId}/{id}
     * Request body:
     * {
     *     "name": "New Jacket",
     *     "tagIds": [4, 5]
     * }
     *
     * @param userId the ID of the user.
     * @param clothingId the ID of the clothing item.
     * @param request the new clothing item details.
     * @return A response indicating the result of the update operation.
     */
    @PutMapping("/user/{userId}/{id}")
    public ResponseEntity<String> updateClothing(
            @PathVariable("userId") Long userId,
            @PathVariable("id") Long clothingId,
            @RequestBody ClothingRequest request
    ) {
        try {
            Clothing updatedClothing = clothingService.updateClothingWithTags(
                clothingId,
                request.getName(),
                userId,
                request.getTagIds()
            );

            return ResponseEntity.ok("Clothing '" + updatedClothing.getClo_lib() + "' updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the clothing.");
        }
    }

    /**
     * Delete a clothing item for a user.
     * Example: DELETE http://localhost:8080/api/clothing/user/{userId}/{id}/delete
     *
     * @param userId the ID of the user.
     * @param clothingId the ID of the clothing item.
     * @return A response indicating the result of the deletion.
     */
    @DeleteMapping("/user/{userId}/{id}/delete")
    public ResponseEntity<String> deleteClothing(
            @PathVariable("userId") Long userId,
            @PathVariable("id") Long clothingId
    ) {
        try {
            Clothing deletedClothing = clothingService.deleteClothing(userId, clothingId);
            return ResponseEntity.ok("Clothing '" + deletedClothing.getClo_lib() + "' deleted successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the clothing.");
        }
    }

}
