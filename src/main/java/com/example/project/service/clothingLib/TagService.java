package com.example.project.service.clothingLib;

import com.example.project.modele.clothingLib.Tag;
import com.example.project.repository.clothingLib.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    /**
     * Retrieve all tags available in the system.
     *
     * @return A list of {@link Tag} objects representing all tags.
     */
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    /**
     * Retrieve a specific tag by its ID.
     *
     * @param tagId The ID of the tag to retrieve.
     * @return The {@link Tag} object with the specified ID, or {@code null} if not found.
     */
    public Tag getTagById(Long tagId) {
        return tagRepository.findById(tagId).orElse(null);
    }

}
