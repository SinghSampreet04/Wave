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

    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists.");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyExistsException("Username already exists.");
        }

        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.USER);

        userRepository.save(user);

        // Increment metric
        metricsService.incrementUsersRegistered();

        return new AuthResponse(
                "User registered successfully."
        );
    }

    public LoginResponse login(LoginRequest request) {

        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
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
                jwtService.generateAccessToken(request.getEmail());

        metricsService.incrementSuccessfulLogins();

        return new LoginResponse(
                "Login successful.",
                accessToken
        );
    }

}