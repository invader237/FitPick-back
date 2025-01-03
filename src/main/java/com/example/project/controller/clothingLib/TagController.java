package com.example.project.controller.clothingLib;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.project.service.clothingLib.TagService;
import com.example.project.dto.clothingLib.TagDTO;

import java.util.List;

/**
 * REST Controller for managing Tags.
 * Provides endpoints to retrieve all tags or specific tags by their IDs.
 */
@RestController
@RequestMapping("/api/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    /**
     * Get all tags.
     * Example: GET http://localhost:8080/api/tags
     *
     * @return List of all tags as {@link TagDTO}.
     */
    @GetMapping
    public ResponseEntity<List<TagDTO>> getAllTags() {
        List<TagDTO> tags = tagService.getAllTags();
        return ResponseEntity.ok(tags);
    }

    /**
     * Get a tag by its ID.
     * Example: GET http://localhost:8080/api/tags/{id}
     *
     * @param id The ID of the tag to retrieve.
     * @return The tag as {@link TagDTO} with the given ID, or 404 if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TagDTO> getTagById(@PathVariable("id") Long id) {
        TagDTO tag = tagService.getTagById(id);
        if (tag != null) {
            return ResponseEntity.ok(tag);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get multiple tags by their IDs.
     * Example: POST http://localhost:8080/api/tags/by-ids
     *
     * @param tagIds List of tag IDs to retrieve.
     * @return List of tags as {@link TagDTO} corresponding to the given IDs.
     */
    @PostMapping("/by-ids")
    public ResponseEntity<List<TagDTO>> getTagsByIds(@RequestBody List<Long> tagIds) {
        try {
            List<TagDTO> tags = tagService.getTagsByIds(tagIds);
            return ResponseEntity.ok(tags);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
