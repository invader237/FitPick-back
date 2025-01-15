package com.example.project.controller.ClothingReco;

import com.example.project.dto.ClothingReco.WeatherRecoDTO;
import com.example.project.model.Weather.WeatherResponse;
import com.example.project.dto.outfitLib.OutfitDTO;
import com.example.project.dto.outfitLib.OutfitDisplay;
import com.example.project.model.Authentification.User;
import com.example.project.repository.Authentification.UserRepository;
import com.example.project.service.ClothingReco.RecoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/reco")
@CrossOrigin(origins = "http://localhost:3000")
public class RecoController {

    private static final Logger log = LoggerFactory.getLogger(RecoController.class);

    @Autowired
    private RecoService recoService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Retrieves the currently authenticated user.
     *
     * @return the authenticated User object
     * @throws ResponseStatusException if the user is not authenticated or not found
     */
    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.debug("Authenticating user with email: {}", email);

        return userRepository.findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
    }

    @GetMapping
    public OutfitDTO getReco() {

        User currentUser = getCurrentUser();

        System.out.println("\n\n");
        System.out.println("=====================================");
        System.out.println("GETTING RECOMMENDATIONS FOR USER: " + currentUser.getId());
        System.out.println("=====================================");
        System.out.println("\n\n");
        return recoService.getReco(currentUser.getId());

    }
}
