package com.wave.backend.notification.repository;

import com.wave.backend.notification.entity.Notification;
import com.wave.backend.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    List<Notification> findByRecipientOrderByCreatedAtDesc(
            User recipient
    );

    Page<Notification> findByRecipientOrderByCreatedAtDesc(
            User recipient,
            Pageable pageable
    );

    List<Notification> findByRecipientAndReadFalseOrderByCreatedAtDesc(
            User recipient
    );

    Page<Notification> findByRecipientAndReadFalseOrderByCreatedAtDesc(
            User recipient,
            Pageable pageable
    );

    long countByRecipientAndReadFalse(
            User recipient
    );

    Optional<Notification> findByIdAndRecipient(
            Long id,
            User recipient
    );

}
