package com.example.project.service.outfitLib;

import org.springframework.beans.factory.annotation.Autowired;
import com.example.project.repository.outfitLib.OutfitRepository;
import org.springframework.stereotype.Service;

import com.example.project.model.outfitLib.Outfit;

import java.util.List;

@Service
public class OutfitService {

    @Autowired
    private OutfitRepository outfitRepository;

    public List<Outfit> getAllOutfits() {
        return outfitRepository.findAll();
    }

    public Outfit getOutfitById(Long outfitId) {
        return outfitRepository.findById(outfitId).orElse(null);
    }

    public List<Outfit> getOutfitsByUserId(Long userId) {
        return outfitRepository.findByUserId(userId);
    }

    public OutfitDTO getOutfitDetails(Long outfitId) {
        Outfit outfit = outfitRepository.findById(outfitId).orElse(null);
        if (outfit == null) {
            return null;
        }
        return new OutfitDTO(outfit.getId(), outfit.getName(), outfit.getClothingItems());
    }

    public Outfit createOutfit(Long userId, List<Long> clothingIds, String outfitName) {
        List<Clothing> userClothes = clothingRepository.findByUserIdAndClothingIdIn(userId, clothingIds);
        if (userClothes.size() != clothingIds.size()) {
            throw new IllegalArgumentException("Certains vêtements ne sont pas associés à cet utilisateur.");
        }
        Outfit outfit = new Outfit();
        outfit.setName(outfitName);
        outfit.setUserId(userId);
        outfit.setClothingItems(userClothes);
        return outfitRepository.save(outfit);
    }

    public Outfit updateOutfit(Long outfitId, Long userId, List<Long> clothingIds, String outfitName) {
        Outfit outfit = outfitRepository.findById(outfitId).orElseThrow(() -> new IllegalArgumentException("Tenue non trouvée."));
        if (!outfit.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Cette tenue n'appartient pas à l'utilisateur.");
        }
        List<Clothing> userClothes = clothingRepository.findByUserIdAndClothingIdIn(userId, clothingIds);
        if (userClothes.size() != clothingIds.size()) {
            throw new IllegalArgumentException("Certains vêtements ne sont pas associés à cet utilisateur.");
        }
        outfit.setName(outfitName);
        outfit.setClothingItems(userClothes);
        return outfitRepository.save(outfit);
    }

    public void deleteOutfit(Long outfitId, Long userId) {
        Outfit outfit = outfitRepository.findById(outfitId).orElseThrow(() -> new IllegalArgumentException("Tenue non trouvée."));
        if (!outfit.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Cette tenue n'appartient pas à l'utilisateur.");
        }
        outfitRepository.delete(outfit);
    }
}
