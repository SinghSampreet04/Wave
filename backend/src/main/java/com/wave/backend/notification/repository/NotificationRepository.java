package com.wave.backend.notification.repository;

import com.wave.backend.notification.entity.Notification;
import com.wave.backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    List<Notification> findByRecipientOrderByCreatedAtDesc(
            User recipient
    );

    List<Notification> findByRecipientAndReadFalseOrderByCreatedAtDesc(
            User recipient
    );

    long countByRecipientAndReadFalse(
            User recipient
    );

}