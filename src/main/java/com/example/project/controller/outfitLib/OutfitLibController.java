package com.example.project.controller.outfitLib;

import com.example.project.model.outfitLib.Outfit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.project.service.outfitLib.OutfitService;
import java.util.List;

@RestController
@RequestMapping("/api/outfits")
public class OutfitLibController {

    @Autowired
    private OutfitService outfitService;

    @GetMapping("/")
    public List<Outfit> getAllOutfits() {
        return outfitService.getAllOutfits();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Outfit> getOutfitById(@PathVariable("id") Long id) {
        Outfit outfit = outfitService.getOutfitById(id);
        if (outfit != null) {
            return ResponseEntity.ok(outfit);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{userId}")
    public List<Outfit> getOutfitsByUserId(@PathVariable("userId") Long userId) {
        return outfitService.getOutfitsByUserId(userId);
    }

    @PostMapping("/")
    public Outfit createOutfit(@RequestBody Outfit outfit) {
        return outfitService.createOutfit(outfit);
    }


}


