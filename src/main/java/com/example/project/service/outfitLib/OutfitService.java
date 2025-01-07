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
import java.util.Optional;
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

    public Optional<Outfit> getOutfitById(Long outfitId) {
        return outfitRepository.findById(outfitId);
    }

    public List<Outfit> getOutfitsByUserId(Long userId) {
        return outfitRepository.findAllByUserId(userId);
    }

    public OutfitDTO getOutfitDetails(Long outfitId) {
        Outfit outfit = outfitRepository.findDetailedOutfitById(outfitId);
        if (outfit == null) {
            return null;
        }
        List<ClothingDTO> clothingDTOs = mapClothingListToDTO(outfit.getClothes());
        return new OutfitDTO(outfit.getFit_id(), outfit.getFit_lib(), clothingDTOs);
    }

    public OutfitDTO createOutfit(Long userId, List<Long> clothingIds, String outfitName) {
        // Vérifie si les vêtements appartiennent à l'utilisateur
        List<Clothing> userClothes = clothingRepository.findByUserIdAndCloIdIn(userId, clothingIds);
        if (userClothes.size() != clothingIds.size()) {
            throw new IllegalArgumentException("Certains vêtements ne sont pas associés à cet utilisateur.");
        }

        // Crée la tenue avec les vêtements et le nom donnés
        Outfit outfit = new Outfit();
        outfit.setUserId(userId);
        outfit.setFit_lib(outfitName);
        outfit.setClothes(userClothes);

        Outfit createdOutfit = outfitRepository.save(outfit);
        List<ClothingDTO> clothingDTOs = mapClothingListToDTO(createdOutfit.getClothes());
        return new OutfitDTO(createdOutfit.getFit_id(), createdOutfit.getFit_lib(), clothingDTOs);
    }

    public OutfitDTO updateOutfit(Long outfitId, Long userId, List<Long> clothingIds, String outfitName) {
        Outfit outfit = outfitRepository.findById(outfitId)
                .orElseThrow(() -> new IllegalArgumentException("Tenue non trouvée."));
        if (!outfit.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Cette tenue n'appartient pas à l'utilisateur.");
        }

        // Vérifie si les vêtements appartiennent à l'utilisateur
        List<Clothing> userClothes = clothingRepository.findByUserIdAndCloIdIn(userId, clothingIds);
        if (userClothes.size() != clothingIds.size()) {
            throw new IllegalArgumentException("Certains vêtements ne sont pas associés à cet utilisateur.");
        }

        // Met à jour la tenue avec les vêtements et le nom donnés
        outfit.setFit_lib(outfitName);
        outfit.setClothes(userClothes);

        Outfit updatedOutfit = outfitRepository.save(outfit);
        List<ClothingDTO> clothingDTOs = mapClothingListToDTO(updatedOutfit.getClothes());
        return new OutfitDTO(updatedOutfit.getFit_id(), updatedOutfit.getFit_lib(), clothingDTOs);
    }

    public void deleteOutfit(Long outfitId, Long userId) {
        Outfit outfit = outfitRepository.findById(outfitId)
                .orElseThrow(() -> new IllegalArgumentException("Tenue non trouvée."));
        if (!outfit.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Cette tenue n'appartient pas à l'utilisateur.");
        }
        outfitRepository.delete(outfit);
    }

    private List<ClothingDTO> mapClothingListToDTO(List<Clothing> clothingList) {
        return clothingList.stream()
                .map(clothing -> new ClothingDTO(clothing.getClo_id(), clothing.getClo_lib(), null))
                .collect(Collectors.toList());
    }
}
