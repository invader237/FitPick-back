package com.example.project.controller.outfitLib;

import com.example.project.model.outfitLib.Outfit;
import com.example.project.dto.outfitLib.OutfitDTO;
import com.example.project.dto.clothingLib.ClothingDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.project.service.outfitLib.OutfitService;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/outfits")
public class OutfitLibController {

    private final OutfitService outfitService;

    @Autowired
    public OutfitLibController(OutfitService outfitService) {
        this.outfitService = outfitService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Outfit>> getAllOutfits() {
        return ResponseEntity.ok(outfitService.getAllOutfits());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Outfit> getOutfitById(@PathVariable Long id) {
        return outfitService.getOutfitById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Outfit>> getOutfitsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(outfitService.getOutfitsByUserId(userId));
    }

    @PostMapping("/create")
    public ResponseEntity<OutfitDTO> addOutfit(@RequestBody OutfitDTO request) {
        OutfitDTO createdOutfit = outfitService.createOutfit(
            request.getName(),
            26L, 
            request.getClothingList() 
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOutfit);
    }


    @PutMapping("/{id}/update/{userId}")
    public ResponseEntity<Outfit> updateOutfit(
            @PathVariable Long id,
            @PathVariable Long userId,
            @RequestBody @Valid OutfitDTO outfitDTO) {
        Outfit updatedOutfit = outfitService.updateOutfit(
                id,
                userId,
                outfitDTO.getClothingList(), // Transmettez la liste des identifiants directement
                outfitDTO.getName()
        );
        return ResponseEntity.ok(updatedOutfit);
    }

    @DeleteMapping("/{id}/{userId}")
    public ResponseEntity<Void> deleteOutfit(@PathVariable Long id, @PathVariable Long userId) {
        outfitService.deleteOutfit(id, userId);
        return ResponseEntity.noContent().build();
    }
}
