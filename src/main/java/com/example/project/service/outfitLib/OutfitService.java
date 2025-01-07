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

    public OutfitDTO createOutfit(String name, Long userId, List<Long> clothingIds) {
    try {
        // Récupérer tous les vêtements associés à leurs IDs
        List<Clothing> userClothes = clothingRepository.findByUserIdAndCloIdIn(userId, clothingIds);

        // Vérifier que tous les IDs fournis correspondent à des vêtements existants
        if (userClothes.size() != clothingIds.size()) {
            throw new IllegalArgumentException("Certains vêtements ne sont pas trouvés ou n'appartiennent pas à l'utilisateur : " + clothingIds);
        }

        // Créer une nouvelle tenue avec les vêtements récupérés
        Outfit newOutfit = new Outfit();
        newOutfit.setFit_lib(name);
        newOutfit.setUserId(userId);
        newOutfit.setClothes(userClothes);

        // Sauvegarder la tenue
        Outfit savedOutfit = outfitRepository.save(newOutfit);

        // Retourner un DTO contenant les détails de la tenue créée
        return new OutfitDTO(savedOutfit.getFit_id(), savedOutfit.getFit_lib(), clothingIds);

    } catch (Exception e) {
        // Gestion des exceptions génériques pour plus de traçabilité
        throw new RuntimeException("Une erreur est survenue lors de la création de la tenue avec le nom " + name + " pour l'utilisateur ID " + userId, e);
    }
}


    public Outfit updateOutfit(Long outfitId, Long userId, List<Long> clothingIds, String outfitName) {
        Outfit outfit = outfitRepository.findById(outfitId)
                .orElseThrow(() -> new IllegalArgumentException("Tenue non trouvée."));
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
