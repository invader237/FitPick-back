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

    @GetMapping("/{id}/details")
    public ResponseEntity<OutfitDTO> getOutfitDetails(@PathVariable("id") Long id) {
        OutfitDTO outfitDetails = outfitService.getOutfitDetails(id);
        if (outfitDetails != null) {
            return ResponseEntity.ok(outfitDetails);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Outfit> createOutfit(@RequestParam Long userId, @RequestBody OutfitDTO outfitDTO) {
        Outfit outfit = outfitService.createOutfit(userId, outfitDTO.getClothingList().stream().map(ClothingDTO::getId).toList(), outfitDTO.getName());
        return ResponseEntity.ok(outfit);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<Outfit> updateOutfit(@PathVariable Long id, @RequestParam Long userId, @RequestBody OutfitDTO outfitDTO) {
        Outfit updatedOutfit = outfitService.updateOutfit(id, userId, outfitDTO.getClothingList().stream().map(ClothingDTO::getId).toList(), outfitDTO.getName());
        return ResponseEntity.ok(updatedOutfit);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOutfit(@PathVariable Long id, @RequestParam Long userId) {
        outfitService.deleteOutfit(id, userId);
        return ResponseEntity.noContent().build();
    }
}


