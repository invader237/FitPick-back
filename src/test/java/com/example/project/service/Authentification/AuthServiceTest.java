package com.example.project.service.Authentification;

import com.example.project.modele.Authentification.Role;
import com.example.project.modele.Authentification.User;
import com.example.project.repository.Authentification.RoleRepository;
import com.example.project.repository.Authentification.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        passwordEncoder = new BCryptPasswordEncoder(); // Utilisation du même PasswordEncoder que dans votre configuration
        authService = new AuthService(userRepository, roleRepository, passwordEncoder);
    }

    @Test
    void registerUser_ShouldCreateNewUser_WhenDetailsAreValid() {
        // Données d'entrée
        String email = "test@example.com";
        String password = "password123";
        String firstName = "John";
        String lastName = "Doe";

        // Simulation du rôle USER
        Role roleUser = new Role("ROLE_USER");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(roleUser));

        // Simulation pour indiquer qu'aucun utilisateur n'existe déjà
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Simulation pour sauvegarder l'utilisateur
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Appel de la méthode à tester
        User createdUser = authService.registerUser(email, password, firstName, lastName);

        // Assertions pour vérifier le résultat
        assertNotNull(createdUser, "L'utilisateur créé ne doit pas être null.");
        assertEquals(email, createdUser.getEmail(), "L'email de l'utilisateur doit correspondre.");
        assertEquals(firstName, createdUser.getFirstName(), "Le prénom doit correspondre.");
        assertEquals(lastName, createdUser.getLastName(), "Le nom doit correspondre.");
        assertTrue(passwordEncoder.matches(password, createdUser.getPassword()), "Le mot de passe doit être haché correctement.");
        assertEquals(1, createdUser.getRoles().size(), "L'utilisateur doit avoir exactement un rôle.");
        assertTrue(createdUser.getRoles().contains(roleUser), "Le rôle attribué doit être ROLE_USER.");

        // Vérifications des interactions avec les mocks
        verify(roleRepository, times(1)).findByName("ROLE_USER");
        verify(userRepository, times(1)).findByEmail(email);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registerUser_ShouldThrowException_WhenEmailAlreadyExists() {
        // Arrange
        String email = "test@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
                authService.registerUser(email, "password123", "Test", "User")
        );

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void authenticateUser_ShouldThrowException_WhenPasswordIsIncorrect() {
        // Arrange
        String email = "test@example.com";
        String rawPassword = "wrongPassword";

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode("password123"));

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
                authService.authenticateUser(email, rawPassword)
        );

        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void authenticateUser_ShouldThrowException_WhenUserNotFound() {
        // Arrange
        String email = "nonexistent@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
                authService.authenticateUser(email, "password123")
        );

        verify(userRepository, times(1)).findByEmail(email);
    }
}
