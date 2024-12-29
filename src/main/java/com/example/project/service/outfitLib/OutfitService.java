package com.example.project.service.outfitLib;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.project.repository.outfitLib.OutfitRepository;
import com.example.project.repository.clothingLib.ClothingRepository;

import com.example.project.model.outfitLib.Outfit;
import com.example.project.model.clothingLib.Clothing;

import com.example.project.dto.outfitLib.OutfitDTO;
import com.example.project.dto.clothingLib.ClothingDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OutfitService {

    @Autowired
    private OutfitRepository outfitRepository;

    @Autowired
    private ClothingRepository clothingRepository;

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

        // Conversion des vêtements en DTO
        List<ClothingDTO> clothingDTOs = mapClothingListToDTO(outfit.getClothes());

        return new OutfitDTO(outfit.getFit_id(), outfit.getFit_lib(), clothingDTOs);
    }

    public Outfit createOutfit(Long userId, List<Long> clothingIds, String outfitName) {
        List<Clothing> userClothes = clothingRepository.findByUserIdAndCloIdIn(userId, clothingIds);
        if (userClothes.size() != clothingIds.size()) {
            throw new IllegalArgumentException("Certains vêtements ne sont pas associés à cet utilisateur.");
        }

        Outfit outfit = new Outfit();
        outfit.setFit_lib(outfitName);
        outfit.setUserId(userId);
        outfit.setClothes(userClothes);

        return outfitRepository.save(outfit);
    }

    public Outfit updateOutfit(Long outfitId, Long userId, List<Long> clothingIds, String outfitName) {
        Outfit outfit = outfitRepository.findById(outfitId).orElseThrow(() -> new IllegalArgumentException("Tenue non trouvée."));
        if (!outfit.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Cette tenue n'appartient pas à l'utilisateur.");
        }

        List<Clothing> userClothes = clothingRepository.findByUserIdAndCloIdIn(userId, clothingIds);
        if (userClothes.size() != clothingIds.size()) {
            throw new IllegalArgumentException("Certains vêtements ne sont pas associés à cet utilisateur.");
        }

        outfit.setFit_lib(outfitName);
        outfit.setClothes(userClothes);

        return outfitRepository.save(outfit);
    }

    public void deleteOutfit(Long outfitId, Long userId) {
        Outfit outfit = outfitRepository.findById(outfitId).orElseThrow(() -> new IllegalArgumentException("Tenue non trouvée."));
        if (!outfit.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Cette tenue n'appartient pas à l'utilisateur.");
        }

        outfitRepository.delete(outfit);
    }

    private List<ClothingDTO> mapClothingListToDTO(List<Clothing> clothingList) {
        return clothingList.stream()
                .map(clothing -> new ClothingDTO(clothing.getClo_id(), clothing.getClo_lib(), null)) // Adaptez si des tags sont nécessaires
                .collect(Collectors.toList());
    }
}
