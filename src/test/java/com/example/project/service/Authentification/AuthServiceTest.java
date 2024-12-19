package com.example.project.service.Authentification;

import com.example.project.model.Authentification.Role;
import com.example.project.model.Authentification.User;
import com.example.project.repository.Authentification.RoleRepository;
import com.example.project.repository.Authentification.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_UserAlreadyExists() {
        // Données
        String email = "test@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        // Test
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                authService.registerUser(email, "password123", "John", "Doe")
        );

        // Vérifications
        assertEquals("Cet email est déjà utilisé.", exception.getMessage());
        verify(userRepository, times(1)).existsByEmail(email);
    }

    @Test
    void testRegisterUser_Success() {
        // Données
        String email = "new@example.com";
        String hashedPassword = "hashedPassword";
        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn(hashedPassword);
        Role userRole = new Role("ROLE_USER");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(userRole));

        // Test
        authService.registerUser(email, "password123", "John", "Doe");

        // Vérifications
        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode("password123");
    }

    @Test
    void testAuthenticateUser_InvalidCredentials() {
        // Données
        String email = "test@example.com";
        String password = "wrongPassword";
        User user = new User();
        user.setEmail(email);
        user.setPassword("hashedPassword");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(false);

        // Test
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                authService.authenticateUser(email, password)
        );

        // Vérifications
        assertEquals("Email ou mot de passe invalide.", exception.getMessage());
        verify(passwordEncoder, times(1)).matches(password, user.getPassword());
    }

    @Test
    void testAuthenticateUser_Success() {
        // Données
        String email = "test@example.com";
        String password = "password123";
        User user = new User();
        user.setEmail(email);
        user.setPassword("hashedPassword");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);

        // Test
        User authenticatedUser = authService.authenticateUser(email, password);

        // Vérifications
        assertEquals(email, authenticatedUser.getEmail());
        verify(passwordEncoder, times(1)).matches(password, user.getPassword());
    }

    @Test
    void testChangePassword_InvalidOldPassword() {
        // Données
        String email = "test@example.com";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";
        User user = new User();
        user.setEmail(email);
        user.setPassword("hashedOldPassword");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(oldPassword, user.getPassword())).thenReturn(false);

        // Test
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                authService.changePassword(email, oldPassword, newPassword)
        );

        // Vérifications
        assertEquals("L'ancien mot de passe est incorrect.", exception.getMessage());
        verify(passwordEncoder, times(1)).matches(oldPassword, user.getPassword());
    }

    @Test
    void testChangePassword_Success() {
        // Données
        String email = "test@example.com";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";
        String hashedNewPassword = "hashedNewPassword";
        User user = new User();
        user.setEmail(email);
        user.setPassword("hashedOldPassword");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(oldPassword, user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn(hashedNewPassword);

        // Test
        authService.changePassword(email, oldPassword, newPassword);

        // Vérifications
        assertEquals(hashedNewPassword, user.getPassword());
        verify(userRepository, times(1)).save(user);
    }
}
