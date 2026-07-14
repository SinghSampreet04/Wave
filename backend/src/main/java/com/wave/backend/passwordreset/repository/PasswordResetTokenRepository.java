package com.wave.backend.passwordreset.repository;

import com.wave.backend.passwordreset.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface PasswordResetTokenRepository
        extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);

    @Query("""
            SELECT t
            FROM PasswordResetToken t
            WHERE t.user.id = :userId
            """)
    Optional<PasswordResetToken> findByUserId(Long userId);

    @Modifying
    @Transactional
    @Query("""
            DELETE FROM PasswordResetToken t
            WHERE t.user.id = :userId
            """)
    void deleteByUserId(Long userId);
}