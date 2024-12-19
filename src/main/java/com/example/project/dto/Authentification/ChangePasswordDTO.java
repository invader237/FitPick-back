package com.example.project.dto.Authentification;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO pour le changement de mot de passe.
 */
public class ChangePasswordDTO {

    @NotBlank(message = "L'ancien mot de passe est obligatoire.")
    private String oldPassword;

    @NotBlank(message = "Le nouveau mot de passe est obligatoire.")
    private String newPassword;

    // Getters et setters
    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
