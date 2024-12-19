package com.example.project.service.clothingLib;

import com.example.project.modele.clothingLib.Tag;
import com.example.project.repository.clothingLib.TagRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TagServiceTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagService tagService;

    // Test for getAllTags
    @Test
    public void testGetAllTags() {
        // Setup mock data
        Tag tag1 = new Tag();
        tag1.setTag_lib("Summer");

        Tag tag2 = new Tag();
        tag2.setTag_lib("Winter");

        List<Tag> tags = Arrays.asList(tag1, tag2);

        // Simulate the behavior of the tagRepository
        when(tagRepository.findAll()).thenReturn(tags);

        // Execute method
        List<Tag> result = tagService.getAllTags();

        // Assert results
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Summer", result.get(0).getTag_lib());
        assertEquals("Winter", result.get(1).getTag_lib());
    }

    // Test for getTagById when tag is found
    @Test
    public void testGetTagById() {
        // Setup mock data
        Tag tag = new Tag();
        tag.setTag_lib("Summer");

        // Simulate the behavior of the tagRepository
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));

        // Execute method
        Tag result = tagService.getTagById(1L);

        // Assert results
        assertNotNull(result);
        assertEquals("Summer", result.getTag_lib());
    }

    // Test for getTagById when tag is not found
    @Test
    public void testGetTagByIdNotFound() {
        // Simulate the behavior of the tagRepository
        when(tagRepository.findById(1L)).thenReturn(Optional.empty());

        // Execute method
        Tag result = tagService.getTagById(1L);

        // Assert results
        assertNull(result);
    }
}
