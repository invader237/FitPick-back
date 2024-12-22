package com.example.project.controller.clothingLib;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.project.service.clothingLib.TagService;
import com.example.project.model.clothingLib.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    /**
     * Get all tags.
     * Example: GET http://localhost:8080/api/tags
     * @return List of all tags.
     */
    @GetMapping("/")
    public List<Tag> getAllTags() {
        return tagService.getAllTags();
    }

    /**
     * Get a tag by its ID.
     * Example: GET http://localhost:8080/api/tags/{id}
     * @param id the ID of the tag to retrieve.
     * @return The tag with the given ID, or 404 if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Tag> getTagById(@PathVariable("id") Long id) {
        Tag tag = tagService.getTagById(id);
        if (tag != null) {
            return ResponseEntity.ok(tag);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
