package com.example.project.dto.Authentification;

/**
 * DTO pour la réponse après connexion, contenant le token JWT.
 */
public class LoginResponseDTO {

    private String token;

    public LoginResponseDTO(String token) {
        this.token = token;
    }

    // Getter
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
