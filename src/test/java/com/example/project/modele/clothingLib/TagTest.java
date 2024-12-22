package com.example.project.modele.clothingLib;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.project.model.clothingLib.Clothing;
import com.example.project.model.clothingLib.Tag;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class TagTest {

    private Tag tag;

    @BeforeEach
    public void setUp() {
        // Initialiser un objet Tag avant chaque test
        tag = new Tag();
        tag.setTag_lib("Summer");
        tag.setTag_temperature_score(5);
        tag.setTag_wind_score(3);
        tag.setTag_rain_score(2);
        
        // Optionnel: Ajouter une relation avec un objet Clothing si nécessaire
        Clothing clothing = new Clothing();
        clothing.setClo_lib("Shirt");
        clothing.setUserId(1L);
        tag.setClothes(Arrays.asList(clothing));
    }

    // Test des getters et setters
    @Test
    public void testTagLib() {
        assertEquals("Summer", tag.getTag_lib());
        tag.setTag_lib("Winter");
        assertEquals("Winter", tag.getTag_lib());
    }

    @Test
    public void testTemperatureScore() {
        assertEquals(5, tag.getTag_temperature_score());
        tag.setTag_temperature_score(7);
        assertEquals(7, tag.getTag_temperature_score());
    }

    @Test
    public void testWindScore() {
        assertEquals(3, tag.getTag_wind_score());
        tag.setTag_wind_score(4);
        assertEquals(4, tag.getTag_wind_score());
    }

    @Test
    public void testRainScore() {
        assertEquals(2, tag.getTag_rain_score());
        tag.setTag_rain_score(6);
        assertEquals(6, tag.getTag_rain_score());
    }

    @Test
    public void testClothes() {
        assertNotNull(tag.getClothes());
        assertEquals(1, tag.getClothes().size());
        assertEquals("Shirt", tag.getClothes().get(0).getClo_lib());
        
        // Modifier la liste des vêtements associés au tag
        Clothing newClothing = new Clothing();
        newClothing.setClo_lib("Jacket");
        newClothing.setUserId(2L);
        tag.setClothes(Arrays.asList(newClothing));
        
        assertEquals(1, tag.getClothes().size());
        assertEquals("Jacket", tag.getClothes().get(0).getClo_lib());
    }

    // Test de la méthode toString()
    @Test
    public void testToString() {
        String expectedString = "Tag{" +
                "tag_id=null" + // tag_id est généré automatiquement et peut être null dans ce contexte
                ", tag_lib='Summer'" +
                ", tag_temperature_score=5" +
                ", tag_wind_score=3" +
                ", tag_rain_score=2" +
                '}';
        
        // Le test va s'assurer que la méthode toString produit la chaîne correcte
        assertEquals(expectedString, tag.toString());
    }
}
