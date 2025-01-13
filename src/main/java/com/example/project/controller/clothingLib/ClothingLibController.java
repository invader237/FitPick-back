package com.example.project.controller.clothingLib;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.multipart.MultipartFile;

import com.example.project.service.AwsS3.AwsS3Service;
import com.example.project.service.clothingLib.ClothingService;
import com.example.project.dto.clothingLib.ClothingDTO;
import com.example.project.dto.clothingLib.ClothingRequest;
import com.example.project.dto.clothingLib.TagDTO;
import com.example.project.model.Authentification.User;
import com.example.project.model.clothingLib.Clothing;
import com.example.project.repository.Authentification.UserRepository;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * REST Controller for managing clothing-related operations.
 * Provides endpoints for creating, retrieving, updating, and deleting clothing items,
 * as well as uploading images to AWS S3.
 */
@RestController
@RequestMapping("/api/clothing")
@Validated
public class ClothingLibController {

    private static final Logger log = LoggerFactory.getLogger(ClothingLibController.class);

    @Autowired
    private ClothingService clothingService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AwsS3Service awsS3Service;

    /**
     * Retrieves the currently authenticated user.
     *
     * @return the authenticated User object
     * @throws ResponseStatusException if the user is not authenticated or not found
     */
    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.debug("Authenticating user with email: {}", email);

        return userRepository.findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
    }

    /**
     * Retrieves all clothing items for the currently authenticated user.
     *
     * @return a list of ClothingDTO objects
     */
    @GetMapping("/my-items")
    public ResponseEntity<List<ClothingDTO>> getClothingForCurrentUser() {
        try {
            User currentUser = getCurrentUser();
            log.debug("Fetching clothing items for user ID: {}", currentUser.getId());

            List<ClothingDTO> clothingDtoList = clothingService.getClothingByUserId(currentUser.getId());

            log.info("Returning {} clothing items.", clothingDtoList.size());
            return ResponseEntity.ok(clothingDtoList);
        } catch (Exception e) {
            log.error("Error retrieving clothing items: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Retrieves a specific clothing item by its ID for the authenticated user.
     *
     * @param clothingId the ID of the clothing item to retrieve
     * @return the Clothing object
     */
    @GetMapping("/{id}")
    public ResponseEntity<Clothing> fetchClothingById(@PathVariable("id") Long clothingId) {
        log.debug("Received request for clothing with ID: {}", clothingId);

        User currentUser = getCurrentUser();
        log.debug("Authenticated user ID: {}", currentUser.getId());

        try {
            Clothing clothing = clothingService.getClothingById(currentUser.getId(), clothingId);
            log.info("Clothing item retrieved successfully: {}", clothing);
            return ResponseEntity.ok(clothing);
        } catch (IllegalArgumentException e) {
            log.warn("Clothing not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Retrieves the tags associated with a specific clothing item.
     *
     * @param clothingId the ID of the clothing item
     * @return a list of TagDTO objects associated with the clothing
     */
    @GetMapping("/{id}/tags")
    public ResponseEntity<List<TagDTO>> getTagsForClothing(@PathVariable("id") Long clothingId) {
        try {
            log.debug("Fetching tags for clothing ID: {}", clothingId);
            List<TagDTO> tags = clothingService.getTagsForClothing(clothingId);
            return ResponseEntity.ok(tags);
        } catch (IllegalArgumentException e) {
            log.warn("Clothing not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            log.error("Unexpected error while fetching tags: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Adds a new clothing item for the authenticated user.
     *
     * @param request the ClothingRequest containing the clothing details
     * @return the created ClothingDTO object
     */
    @PostMapping
    public ResponseEntity<ClothingDTO> addClothing(@RequestBody @Validated ClothingRequest request) {
        try {
            User currentUser = getCurrentUser();
            log.debug("Adding new clothing for user ID: {}", currentUser.getId());

            ClothingDTO clothingDto = clothingService.addClothingWithTags(
                request.getName(),
                currentUser.getId(),
                request.getTagIds(),
                request.getImageUrl()
            );

            log.info("New clothing added: {}", clothingDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(clothingDto);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid request: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            log.error("Error adding clothing: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Updates an existing clothing item for the authenticated user.
     *
     * @param clothingId the ID of the clothing item to update
     * @param request the ClothingRequest containing updated clothing details
     * @return the updated ClothingDTO object
     */
    @PutMapping("/{id}/update")
    public ResponseEntity<ClothingDTO> updateClothing(
            @PathVariable("id") Long clothingId,
            @RequestBody @Validated ClothingRequest request
    ) {
        try {
            User currentUser = getCurrentUser();
            log.debug("Updating clothing with ID: {} for user ID: {}", clothingId, currentUser.getId());
    
            ClothingDTO updatedClothing = clothingService.updateClothingWithTags(
                clothingId,
                request.getName(),
                currentUser.getId(),
                request.getTagIds(),
                request.getImageUrl()
            );
    
            log.info("Clothing updated successfully: {}", updatedClothing);
            return ResponseEntity.ok(updatedClothing);
        } catch (IllegalArgumentException e) {
            log.warn("Error updating clothing: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            log.error("Unexpected error during update: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }    

    /**
     * Deletes a specific clothing item for the authenticated user.
     *
     * @param clothingId the ID of the clothing item to delete
     * @return a success message if the deletion is successful
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClothing(@PathVariable("id") Long clothingId) {
        try {
            User currentUser = getCurrentUser();
            log.debug("Deleting clothing with ID: {} for user ID: {}", clothingId, currentUser.getId());

            clothingService.deleteClothing(currentUser.getId(), clothingId);

            log.info("Clothing deleted successfully.");
            return ResponseEntity.ok("Clothing deleted successfully.");
        } catch (IllegalArgumentException e) {
            log.warn("Error deleting clothing: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error during deletion: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the clothing.");
        }
    }

    /**
     * Uploads an image to AWS S3 and returns the image URL.
     *
     * @param file the image file to upload
     * @return the URL of the uploaded image
     */
    @PostMapping("/upload-image")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            log.debug("Uploading image file: {}", file.getOriginalFilename());
            String imageUrl = awsS3Service.uploadFile(file);

            log.info("Image uploaded successfully: {}", imageUrl);
            return ResponseEntity.ok(imageUrl);
        } catch (Exception e) {
            log.error("Error occurred while uploading image: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while uploading the image.");
        }
    }
}
