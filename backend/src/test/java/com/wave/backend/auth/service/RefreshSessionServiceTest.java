package com.wave.backend.auth.service;

import com.wave.backend.auth.entity.RefreshSession;
import com.wave.backend.auth.jwt.JwtProperties;
import com.wave.backend.auth.repository.RefreshSessionRepository;
import com.wave.backend.exception.InvalidCredentialsException;
import com.wave.backend.user.entity.User;
import com.wave.backend.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RefreshSessionServiceTest {

    @Mock
    private RefreshSessionRepository refreshSessionRepository;

    @Mock
    private UserRepository userRepository;

    private RefreshSessionService service;
    private User user;

    @BeforeEach
    void setUp() {
        JwtProperties properties = new JwtProperties();
        properties.setRefreshTokenExpiration(604_800_000);
        service = new RefreshSessionService(
                refreshSessionRepository,
                userRepository,
                properties
        );
        user = new User();
        user.setId(7L);
        user.setEmail("person@example.com");
    }

    @Test
    void issueStoresOnlyAHashAndNormalizesEmail() {
        when(userRepository.findByEmail("person@example.com"))
                .thenReturn(Optional.of(user));

        RefreshSessionService.Rotation rotation =
                service.issueForEmail(" PERSON@EXAMPLE.COM ");

        ArgumentCaptor<RefreshSession> session =
                ArgumentCaptor.forClass(RefreshSession.class);
        verify(refreshSessionRepository).save(session.capture());

        assertThat(rotation.rawToken()).hasSizeGreaterThan(80);
        assertThat(rotation.email()).isEqualTo("person@example.com");
        assertThat(session.getValue().getTokenHash()).hasSize(64);
        assertThat(session.getValue().getTokenHash())
                .doesNotContain(rotation.rawToken());
        assertThat(session.getValue().getExpiresAt())
                .isAfter(LocalDateTime.now().plusDays(6));
    }

    @Test
    void rotateRevokesThePreviousSessionAndIssuesANewToken() {
        RefreshSession existing = new RefreshSession();
        existing.setUser(user);
        existing.setExpiresAt(LocalDateTime.now().plusDays(1));
        when(refreshSessionRepository.findByTokenHash(anyString()))
                .thenReturn(Optional.of(existing));

        RefreshSessionService.Rotation rotation = service.rotate("old-token");

        assertThat(existing.getRevokedAt()).isNotNull();
        assertThat(rotation.rawToken()).isNotEqualTo("old-token");
        verify(refreshSessionRepository, atLeast(2)).save(any(RefreshSession.class));
    }

    @Test
    void expiredSessionCannotBeRotated() {
        RefreshSession expired = new RefreshSession();
        expired.setUser(user);
        expired.setExpiresAt(LocalDateTime.now().minusSeconds(1));
        when(refreshSessionRepository.findByTokenHash(anyString()))
                .thenReturn(Optional.of(expired));

        assertThatThrownBy(() -> service.rotate("expired-token"))
                .isInstanceOf(InvalidCredentialsException.class)
                .hasMessageContaining("invalid or expired");
    }
}
