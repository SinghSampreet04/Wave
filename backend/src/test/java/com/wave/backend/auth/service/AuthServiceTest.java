package com.wave.backend.auth.service;

import com.wave.backend.auth.dto.request.LoginRequest;
import com.wave.backend.auth.dto.request.RegisterRequest;
import com.wave.backend.auth.jwt.JwtService;
import com.wave.backend.exception.EmailAlreadyExistsException;
import com.wave.backend.metrics.service.MetricsService;
import com.wave.backend.user.entity.User;
import com.wave.backend.user.entity.UserRole;
import com.wave.backend.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private MetricsService metricsService;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(
                userRepository,
                passwordEncoder,
                jwtService,
                authenticationManager,
                metricsService
        );
    }

    @Test
    void registerNormalizesIdentityAndHashesPassword() {
        RegisterRequest request = registrationRequest();
        when(passwordEncoder.encode("password123"))
                .thenReturn("hashed-password");

        authService.register(request);

        ArgumentCaptor<User> userCaptor =
                ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User saved = userCaptor.getValue();
        assertEquals("person@example.com", saved.getEmail());
        assertEquals("wave-user", saved.getUsername());
        assertEquals("hashed-password", saved.getPassword());
        assertEquals(UserRole.USER, saved.getRole());
        verify(metricsService).incrementUsersRegistered();
    }

    @Test
    void registerRejectsAnExistingEmailBeforeSaving() {
        RegisterRequest request = registrationRequest();
        when(userRepository.existsByEmail("person@example.com"))
                .thenReturn(true);

        assertThrows(
                EmailAlreadyExistsException.class,
                () -> authService.register(request)
        );

        verify(userRepository, never()).save(any());
    }

    @Test
    void loginAuthenticatesAndReturnsSignedAccessToken() {
        LoginRequest request = new LoginRequest();
        request.setEmail("person@example.com");
        request.setPassword("password123");
        when(jwtService.generateAccessToken("person@example.com"))
                .thenReturn("signed-token");

        var response = authService.login(request);

        assertEquals("signed-token", response.getAccessToken());
        verify(authenticationManager).authenticate(any());
        verify(metricsService).incrementSuccessfulLogins();
    }

    private RegisterRequest registrationRequest() {
        RegisterRequest request = new RegisterRequest();
        request.setFirstName("Wave");
        request.setLastName("User");
        request.setUsername(" wave-user ");
        request.setEmail(" Person@Example.COM ");
        request.setPassword("password123");
        return request;
    }
}
