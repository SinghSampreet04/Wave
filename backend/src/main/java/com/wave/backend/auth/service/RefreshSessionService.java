package com.wave.backend.auth.service;

import com.wave.backend.auth.entity.RefreshSession;
import com.wave.backend.auth.jwt.JwtProperties;
import com.wave.backend.auth.repository.RefreshSessionRepository;
import com.wave.backend.exception.InvalidCredentialsException;
import com.wave.backend.exception.UserNotFoundException;
import com.wave.backend.user.entity.User;
import com.wave.backend.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HexFormat;

@Service
public class RefreshSessionService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final RefreshSessionRepository refreshSessionRepository;
    private final UserRepository userRepository;
    private final JwtProperties jwtProperties;

    public RefreshSessionService(
            RefreshSessionRepository refreshSessionRepository,
            UserRepository userRepository,
            JwtProperties jwtProperties
    ) {
        this.refreshSessionRepository = refreshSessionRepository;
        this.userRepository = userRepository;
        this.jwtProperties = jwtProperties;
    }

    @Transactional
    public Rotation issueForEmail(String email) {
        User user = userRepository.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        return createSession(user);
    }

    @Transactional
    public Rotation rotate(String rawToken) {
        if (rawToken == null || rawToken.isBlank()) {
            throw invalidSession();
        }

        RefreshSession existing = refreshSessionRepository
                .findByTokenHash(hash(rawToken))
                .orElseThrow(this::invalidSession);

        if (existing.getRevokedAt() != null
                || existing.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw invalidSession();
        }

        existing.setRevokedAt(LocalDateTime.now());
        refreshSessionRepository.save(existing);
        return createSession(existing.getUser());
    }

    @Transactional
    public void revoke(String rawToken) {
        if (rawToken == null || rawToken.isBlank()) {
            return;
        }

        refreshSessionRepository.findByTokenHash(hash(rawToken))
                .filter(session -> session.getRevokedAt() == null)
                .ifPresent(session -> {
                    session.setRevokedAt(LocalDateTime.now());
                    refreshSessionRepository.save(session);
                });
    }

    private Rotation createSession(User user) {
        byte[] bytes = new byte[64];
        SECURE_RANDOM.nextBytes(bytes);
        String rawToken = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);

        RefreshSession session = new RefreshSession();
        session.setUser(user);
        session.setTokenHash(hash(rawToken));
        session.setExpiresAt(
                LocalDateTime.now().plusNanos(
                        jwtProperties.getRefreshTokenExpiration() * 1_000_000
                )
        );
        refreshSessionRepository.save(session);

        return new Rotation(rawToken, user.getEmail());
    }

    private String hash(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(
                    digest.digest(token.getBytes(StandardCharsets.UTF_8))
            );
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 is unavailable.", ex);
        }
    }

    private InvalidCredentialsException invalidSession() {
        return new InvalidCredentialsException("Refresh session is invalid or expired.");
    }

    public record Rotation(String rawToken, String email) {
    }
}
