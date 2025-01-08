package com.example.project.service.Profile;

import com.example.project.model.Authentification.User;
import com.example.project.repository.Authentification.UserRepository;
import com.example.project.service.AwsS3.AwsS3Service;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class ProfileService {

    private final UserRepository userRepository;
    private final AwsS3Service awsS3Service;

    public ProfileService(UserRepository userRepository, AwsS3Service awsS3Service) {
        this.userRepository = userRepository;
        this.awsS3Service = awsS3Service;
    }

    public User getProfileByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new RuntimeException("Utilisateur introuvable avec l'email : " + email);
        }
        return user.get();
    }

    public User updateProfile(String email, User updatedUser) {
        User existingUser = getProfileByEmail(email);
        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        return userRepository.save(existingUser);
    }

    public String updateAvatar(String email, MultipartFile avatarFile) throws IOException {
        User user = getProfileByEmail(email);
    
        // Upload de l'image vers AWS S3
        String avatarUrl = awsS3Service.uploadFile(avatarFile);
    
        // Mise à jour de l'URL de l'avatar
        user.setAvatar(avatarUrl);
        userRepository.save(user);
    
        return avatarUrl;
    }    
}
