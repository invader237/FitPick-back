package com.example.project.dto;

public class LoginResponseDTO {
    private String token;

    public LoginResponseDTO(String token) {
        this.token = token;
    }

    // Getter uniquement
    public String getToken() {
        return token;
    }
}
