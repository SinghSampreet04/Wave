package com.wave.backend.passwordreset.service;

import com.wave.backend.email.service.EmailService;
import com.wave.backend.metrics.service.MetricsService;
import com.wave.backend.passwordreset.dto.request.ResetPasswordRequest;
import com.wave.backend.passwordreset.entity.PasswordResetToken;
import com.wave.backend.passwordreset.repository.PasswordResetTokenRepository;
import com.wave.backend.user.entity.User;
import com.wave.backend.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final MetricsService metricsService;

    public PasswordResetService(
            UserRepository userRepository,
            PasswordResetTokenRepository tokenRepository,
            EmailService emailService,
            PasswordEncoder passwordEncoder,
            MetricsService metricsService
    ) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.metricsService = metricsService;
    }

    @Transactional
    public void forgotPassword(String email) throws MessagingException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found."));

        tokenRepository.deleteByUserId(user.getId());

        PasswordResetToken token = new PasswordResetToken();

        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setCreatedAt(LocalDateTime.now());
        token.setExpiresAt(LocalDateTime.now().plusMinutes(15));

        tokenRepository.save(token);

        String resetLink =
                "http://localhost:3000/reset-password?token=" + token.getToken();

        emailService.sendHtmlEmail(
                user.getEmail(),
                "Reset your Wave password",
                """
                <html>
                <body style="font-family:Arial,sans-serif">
                    <h2>Password Reset</h2>

                    <p>Click the button below to reset your password.</p>

                    <p>
                        <a href="%s"
                           style="
                           background:#2563eb;
                           color:white;
                           padding:12px 20px;
                           text-decoration:none;
                           border-radius:6px;">
                           Reset Password
                        </a>
                    </p>

                    <p>This link expires in 15 minutes.</p>

                    <hr>

                    <small>If you didn't request this email, ignore it.</small>

                </body>
                </html>
                """.formatted(resetLink)
        );

        // Micrometer Metric
        metricsService.incrementPasswordResetSent();
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {

        PasswordResetToken token = tokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new RuntimeException("Invalid token."));

        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired.");
        }

        User user = token.getUser();

        user.setPassword(
                passwordEncoder.encode(request.getNewPassword())
        );

        userRepository.save(user);

        metricsService.incrementPasswordResetSuccess();

        tokenRepository.delete(token);
    }
}