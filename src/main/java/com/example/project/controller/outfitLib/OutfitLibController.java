package com.example.project.controller.outfitLib;

import com.example.project.model.outfitLib.Outfit;
import com.example.project.model.Authentification.User;
import com.example.project.dto.outfitLib.OutfitDTO;
import com.example.project.dto.outfitLib.OutfitRequest;
import com.example.project.service.outfitLib.OutfitService;
import com.example.project.repository.Authentification.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/outfits")
@Validated
public class OutfitLibController {

    private static final Logger log = LoggerFactory.getLogger(OutfitLibController.class);

    @Autowired
    private OutfitService outfitService;

    @Autowired
    private UserRepository userRepository;

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
     * Fetch outfit by ID.
     *
     * @param id the ID of the outfit
     * @return the requested outfit
     */
    @GetMapping("/{id}")
    public ResponseEntity<Outfit> getOutfitById(@PathVariable Long id) {
        User currentUser = getCurrentUser();
        Outfit outfit = outfitService.getOutfitById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Outfit not found"));

        if (!outfit.getUserId().equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to view this outfit");
        }

        return ResponseEntity.ok(outfit);

    }

    /**
     * Fetch outfits by user ID.
     *
     * @param userId the user ID
     * @return list of outfits associated with the user
     */
    @GetMapping("/my-items")
    public ResponseEntity<List<Outfit>> getOutfitsByUserId() {
        return ResponseEntity.ok(outfitService.getOutfitsByUserId(getCurrentUser().getId()));
    }

    /**
     * Create a new outfit.
     *
     * @param request the outfit creation request
     * @return the created outfit
     */
    @PostMapping
    public ResponseEntity<OutfitDTO> addOutfit(@Valid @RequestBody OutfitRequest request) {
        User currentUser = getCurrentUser();
        log.info("Creating a new outfit for user ID: {}", currentUser.getId());

        OutfitDTO createdOutfit = outfitService.createOutfit(
            currentUser.getId(),
            request.getClothingList(),
            request.getName()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOutfit);
    }

    /**
     * Update an existing outfit.
     *
     * @param id the outfit ID
     * @param request the update request
     * @return the updated outfit
     */
    @PutMapping("/{id}/update")
    public ResponseEntity<OutfitDTO> updateOutfit(@PathVariable Long id, @Valid @RequestBody OutfitRequest request) {
        User currentUser = getCurrentUser();
        log.info("Updating outfit with ID: {} for user ID: {}", id, currentUser.getId());

        OutfitDTO updatedOutfit = outfitService.updateOutfit(
            id,
            currentUser.getId(),
            request.getClothingList(),
            request.getName()
        );
        return ResponseEntity.ok(updatedOutfit);
    }

    /**
     * Delete an outfit.
     *
     * @param id the outfit ID
     * @return response status
     */
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteOutfit(@PathVariable Long id) {
        User currentUser = getCurrentUser();
        log.info("Deleting outfit with ID: {} for user ID: {}", id, currentUser.getId());

        outfitService.deleteOutfit(id, currentUser.getId());
        return ResponseEntity.noContent().build();
    }
}
