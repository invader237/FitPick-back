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


}
