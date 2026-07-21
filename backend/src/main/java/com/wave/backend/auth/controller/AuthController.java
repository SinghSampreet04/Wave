package com.wave.backend.auth.controller;

import com.wave.backend.auth.dto.request.LoginRequest;
import com.wave.backend.auth.dto.request.RegisterRequest;
import com.wave.backend.auth.dto.response.AuthResponse;
import com.wave.backend.auth.dto.response.LoginResponse;
import com.wave.backend.auth.service.AuthService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(
            AuthService authService
    ) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public AuthResponse register(
            @Valid @RequestBody RegisterRequest request
    ) throws MessagingException {

        System.out.println("=================================");
        System.out.println("REGISTER CONTROLLER REACHED");
        System.out.println("Email: " + request.getEmail());
        System.out.println("Username: " + request.getUsername());
        System.out.println("=================================");

        return authService.register(request);

    }

    @PostMapping("/login")
    public LoginResponse login(
            @Valid @RequestBody LoginRequest request
    ) {

        return authService.login(request);

    }

}