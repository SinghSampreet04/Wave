package com.wave.backend.auth.service;

import com.wave.backend.auth.dto.request.LoginRequest;
import com.wave.backend.auth.dto.request.RegisterRequest;
import com.wave.backend.auth.dto.response.AuthResponse;
import com.wave.backend.auth.dto.response.LoginResponse;
import com.wave.backend.auth.jwt.JwtService;
import com.wave.backend.exception.EmailAlreadyExistsException;
import com.wave.backend.exception.InvalidCredentialsException;
import com.wave.backend.exception.UsernameAlreadyExistsException;
import com.wave.backend.metrics.service.MetricsService;
import com.wave.backend.user.entity.User;
import com.wave.backend.user.entity.UserRole;
import com.wave.backend.user.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final MetricsService metricsService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager,
            MetricsService metricsService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.metricsService = metricsService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String email = request.getEmail().trim().toLowerCase();
        String username = request.getUsername().trim();

        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("Email already exists.");
        }

        if (userRepository.existsByUsername(username)) {
            throw new UsernameAlreadyExistsException("Username already exists.");
        }

        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.USER);

        userRepository.save(user);

        metricsService.incrementUsersRegistered();

        return new AuthResponse(
                "User registered successfully."
        );
    }

    public LoginResponse login(LoginRequest request) {
        String email = request.getEmail().trim().toLowerCase();

        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            email,
                            request.getPassword()
                    )
            );

        } catch (BadCredentialsException ex) {

            metricsService.incrementFailedLogins();

            throw new InvalidCredentialsException(
                    "Invalid email or password."
            );
        }

        String accessToken =
                jwtService.generateAccessToken(email);

        metricsService.incrementSuccessfulLogins();

        return new LoginResponse(
                "Login successful.",
                accessToken
        );
    }

}
