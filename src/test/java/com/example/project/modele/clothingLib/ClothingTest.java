package com.example.project.modele.clothingLib;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.project.model.clothingLib.Clothing;
import com.example.project.model.clothingLib.Tag;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class ClothingTest {

    private Clothing clothing;

    @BeforeEach
    public void setUp() {
        // Initialiser un objet Clothing avant chaque test
        clothing = new Clothing();
        clothing.setClo_id(1L);
        clothing.setClo_lib("Shirt");
        clothing.setUserId(1L);

        Tag tag1 = new Tag();
        tag1.setTag_lib("Summer");
        
        Tag tag2 = new Tag();
        tag2.setTag_lib("Casual");
        
        clothing.setTags(Arrays.asList(tag1, tag2));
    }

    // Test des getters et setters
    @Test
    public void testClo_id() {
        assertEquals(1L, clothing.getClo_id());
        clothing.setClo_id(2L);
        assertEquals(2L, clothing.getClo_id());
    }

    @Test
    public void testClo_lib() {
        assertEquals("Shirt", clothing.getClo_lib());
        clothing.setClo_lib("Pants");
        assertEquals("Pants", clothing.getClo_lib());
    }

    @Test
    public void testUserId() {
        assertEquals(1L, clothing.getUserId());
        clothing.setUserId(2L);
        assertEquals(2L, clothing.getUserId());
    }

    @Test
    public void testTags() {
        assertNotNull(clothing.getTags());
        assertEquals(2, clothing.getTags().size());
        assertEquals("Summer", clothing.getTags().get(0).getTag_lib());
        assertEquals("Casual", clothing.getTags().get(1).getTag_lib());
        
        Tag tag3 = new Tag();
        tag3.setTag_lib("Winter");
        
        clothing.setTags(Arrays.asList(tag3));
        assertEquals(1, clothing.getTags().size());
        assertEquals("Winter", clothing.getTags().get(0).getTag_lib());
    }

    // Test de la méthode toString()
    @Test
    public void testToString() {
        String expectedString = "Clothing{" +
                "clo_id=" + 1L +
                ", clo_lib='Shirt'" +
                ", tags=" + clothing.getTags() +
                ", userId=" + 1L +
                '}';
        
        assertEquals(expectedString, clothing.toString());
    }
}
