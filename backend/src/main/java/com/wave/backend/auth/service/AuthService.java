package com.wave.backend.auth.service;

import com.wave.backend.auth.dto.request.RegisterRequest;
import com.wave.backend.auth.dto.response.AuthResponse;
import com.wave.backend.exception.EmailAlreadyExistsException;
import com.wave.backend.exception.UsernameAlreadyExistsException;
import com.wave.backend.user.entity.User;
import com.wave.backend.user.entity.UserRole;
import com.wave.backend.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.wave.backend.auth.dto.request.LoginRequest;
import com.wave.backend.exception.InvalidCredentialsException;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

        return new AuthResponse(
        "User registered successfully.",
        null
);
    }

    public AuthResponse login(LoginRequest request) {

    User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password."));

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
        throw new InvalidCredentialsException("Invalid email or password.");
    }

    return new AuthResponse(
            "Login successful.",
            null
    );

}

}