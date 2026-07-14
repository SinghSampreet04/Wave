package com.wave.backend.emailverification.repository;

import com.wave.backend.emailverification.entity.EmailVerificationToken;
import com.wave.backend.user.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface EmailVerificationTokenRepository
        extends JpaRepository<EmailVerificationToken, Long> {

    Optional<EmailVerificationToken> findByToken(
            String token
    );

    Optional<EmailVerificationToken> findByUser(
            User user
    );

    @Modifying
    @Transactional
    void deleteByUser(
            User user
    );

}