package com.example.project.service.clothingLib;

import com.example.project.modele.clothingLib.Clothing;
import com.example.project.modele.clothingLib.Tag;
import com.example.project.repository.clothingLib.ClothingRepository;
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
public class ClothingServiceTest {

    @Mock
    private ClothingRepository clothingRepository;

    @InjectMocks
    private ClothingService clothingService;

    // Test for getClothingByUserId
    @Test
    public void testGetClothingByUserId() {
        // Setup mock
        Clothing clothing1 = new Clothing();
        clothing1.setClo_id(1L);
        clothing1.setClo_lib("Shirt");

        Clothing clothing2 = new Clothing();
        clothing2.setClo_id(2L);
        clothing2.setClo_lib("Pants");

        List<Clothing> clothingList = Arrays.asList(clothing1, clothing2);

        when(clothingRepository.findByUserId(1L)).thenReturn(clothingList);

        // Execute method
        List<Clothing> result = clothingService.getClothingByUserId(1L);

        // Assert results
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Shirt", result.get(0).getClo_lib());
    }

    // Test for getClothingById
    @Test
    public void testGetClothingById() {
        // Setup mock
        Clothing clothing = new Clothing();
        clothing.setClo_id(1L);
        clothing.setClo_lib("Shirt");

        when(clothingRepository.findByUserIdAndCloId(1L, 1L)).thenReturn(clothing);

        // Execute method
        Clothing result = clothingService.getClothingById(1L, 1L);

        // Assert results
        assertNotNull(result);
        assertEquals("Shirt", result.getClo_lib());
    }

    @Test
    public void testGetClothingByIdNotFound() {
        // Setup mock
        when(clothingRepository.findByUserIdAndCloId(1L, 1L)).thenReturn(null);

        // Execute method
        Clothing result = clothingService.getClothingById(1L, 1L);

        // Assert results
        assertNull(result);
    }

    // Test for getTagsByClothingId
    @Test
    public void testGetTagsByClothingId() {
        // Setup mock
        Tag tag1 = new Tag();
        tag1.setTag_lib("Summer");
        Tag tag2 = new Tag();
        tag2.setTag_lib("Casual");

        List<Tag> tags = Arrays.asList(tag1, tag2);

        when(clothingRepository.findTagsByCloId(1L, 1L)).thenReturn(tags);

        // Execute method
        List<Tag> result = clothingService.getTagsByClothingId(1L, 1L);

        // Assert results
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Summer", result.get(0).getTag_lib());
    }

    @Test
    public void testGetTagsByClothingIdNotFound() {
        // Setup mock
        when(clothingRepository.findTagsByCloId(1L, 1L)).thenReturn(Arrays.asList());

        // Execute method
        List<Tag> result = clothingService.getTagsByClothingId(1L, 1L);

        // Assert results
        assertTrue(result.isEmpty());
    }
}
