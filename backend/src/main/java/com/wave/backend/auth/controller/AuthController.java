package com.wave.backend.auth.controller;

import com.wave.backend.auth.dto.request.LoginRequest;
import com.wave.backend.auth.dto.request.RegisterRequest;
import com.wave.backend.auth.dto.response.AuthResponse;
import com.wave.backend.auth.dto.response.LoginResponse;
import com.wave.backend.auth.jwt.JwtProperties;
import com.wave.backend.auth.jwt.JwtService;
import com.wave.backend.auth.service.AuthService;
import com.wave.backend.auth.service.RefreshSessionService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final RefreshSessionService refreshSessionService;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;
    private final boolean secureRefreshCookie;

    public AuthController(
            AuthService authService,
            RefreshSessionService refreshSessionService,
            JwtService jwtService,
            JwtProperties jwtProperties,
            @Value("${wave.auth.refresh-cookie-secure:false}")
            boolean secureRefreshCookie
    ) {
        this.authService = authService;
        this.refreshSessionService = refreshSessionService;
        this.jwtService = jwtService;
        this.jwtProperties = jwtProperties;
        this.secureRefreshCookie = secureRefreshCookie;
    }

    @PostMapping("/register")
    public AuthResponse register(
            @Valid @RequestBody RegisterRequest request
    ) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public LoginResponse login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        LoginResponse login = authService.login(request);
        RefreshSessionService.Rotation rotation =
                refreshSessionService.issueForEmail(request.getEmail());
        setRefreshCookie(response, rotation.rawToken());
        return login;
    }

    @PostMapping("/refresh")
    public LoginResponse refresh(
            @CookieValue(name = "wave_refresh", required = false)
            String refreshToken,
            HttpServletResponse response
    ) {
        RefreshSessionService.Rotation rotation =
                refreshSessionService.rotate(refreshToken);
        setRefreshCookie(response, rotation.rawToken());
        return new LoginResponse(
                "Session refreshed.",
                jwtService.generateAccessToken(rotation.email())
        );
    }

    @PostMapping("/logout")
    public void logout(
            @CookieValue(name = "wave_refresh", required = false)
            String refreshToken,
            HttpServletResponse response
    ) {
        refreshSessionService.revoke(refreshToken);
        response.addHeader(
                HttpHeaders.SET_COOKIE,
                cookie("", Duration.ZERO).toString()
        );
    }

    private void setRefreshCookie(
            HttpServletResponse response,
            String token
    ) {
        response.addHeader(
                HttpHeaders.SET_COOKIE,
                cookie(
                        token,
                        Duration.ofMillis(
                                jwtProperties.getRefreshTokenExpiration()
                        )
                ).toString()
        );
    }

    private ResponseCookie cookie(
            String value,
            Duration maxAge
    ) {
        return ResponseCookie.from("wave_refresh", value)
                .httpOnly(true)
                .secure(secureRefreshCookie)
                .sameSite("Strict")
                .path("/api/v1/auth")
                .maxAge(maxAge)
                .build();
    }
}
