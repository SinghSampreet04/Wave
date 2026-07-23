package com.wave.backend.emailverification.service;

import com.wave.backend.email.service.EmailService;
import com.wave.backend.emailverification.entity.EmailVerificationToken;
import com.wave.backend.emailverification.repository.EmailVerificationTokenRepository;
import com.wave.backend.metrics.service.MetricsService;
import com.wave.backend.user.entity.User;
import com.wave.backend.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class EmailVerificationService {

    private final EmailVerificationTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final MetricsService metricsService;
    private final String frontendUrl;

    public EmailVerificationService(
            EmailVerificationTokenRepository tokenRepository,
            UserRepository userRepository,
            EmailService emailService,
            MetricsService metricsService,
            @Value("${wave.frontend-url:http://localhost:5173}")
            String frontendUrl
    ) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.metricsService = metricsService;
        this.frontendUrl = frontendUrl.replaceAll("/+$", "");
    }

    @Transactional
    public EmailVerificationToken createVerificationToken(User user)
            throws MessagingException {

        tokenRepository.deleteByUser(user);

        EmailVerificationToken token = new EmailVerificationToken();
        token.setUser(user);

        EmailVerificationToken savedToken = tokenRepository.save(token);

        String verificationLink =
                frontendUrl + "/verify-email?token=" + savedToken.getToken();

        emailService.sendHtmlEmail(
                user.getEmail(),
                "Verify your Wave account",
                """
                <html>
                <body style="font-family:Arial,sans-serif">

                    <h2>Verify Your Email</h2>

                    <p>Click the button below to verify your account.</p>

                    <p>
                        <a href="%s"
                           style="
                                background:#2563eb;
                                color:white;
                                padding:12px 20px;
                                border-radius:6px;
                                text-decoration:none;">
                            Verify Email
                        </a>
                    </p>

                    <p>This link expires in 24 hours.</p>

                </body>
                </html>
                """.formatted(verificationLink)
        );

        metricsService.incrementEmailVerificationSent();

        return savedToken;
    }

    @Transactional
    public void verifyEmail(String tokenValue) {

        EmailVerificationToken token = tokenRepository.findByToken(tokenValue)
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid verification token."));

        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Verification token has expired.");
        }

        User user = token.getUser();

        user.setEmailVerified(true);

        metricsService.incrementEmailVerificationSuccess();

        userRepository.save(user);

        tokenRepository.delete(token);
    }

}
