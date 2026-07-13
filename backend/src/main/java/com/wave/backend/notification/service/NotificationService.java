package com.wave.backend.notification.service;

import com.wave.backend.common.event.NotificationCreatedEvent;
import com.wave.backend.common.util.SecurityUtil;
import com.wave.backend.exception.UserNotFoundException;
import com.wave.backend.notification.dto.NotificationResponse;
import com.wave.backend.notification.entity.Notification;
import com.wave.backend.notification.entity.NotificationType;
import com.wave.backend.notification.repository.NotificationRepository;
import com.wave.backend.user.entity.User;
import com.wave.backend.user.repository.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    public NotificationService(
            NotificationRepository notificationRepository,
            UserRepository userRepository,
            ApplicationEventPublisher eventPublisher
    ) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
    }

    public NotificationResponse createNotification(
            User recipient,
            NotificationType type,
            String title,
            String body,
            Long referenceId,
            String referenceType
    ) {

        Notification notification = new Notification();

        notification.setRecipient(recipient);
        notification.setType(type);
        notification.setTitle(title);
        notification.setBody(body);
        notification.setReferenceId(referenceId);
        notification.setReferenceType(referenceType);

        notification = notificationRepository.save(notification);

        eventPublisher.publishEvent(
                new NotificationCreatedEvent(
                        notification.getId(),
                        recipient.getId(),
                        notification.getType(),
                        notification.getTitle(),
                        notification.getBody(),
                        notification.getReferenceId(),
                        notification.getReferenceType(),
                        notification.getCreatedAt()
                )
        );

        return toResponse(notification);

    }

    public List<NotificationResponse> getMyNotifications() {

        String email = SecurityUtil.getCurrentUserEmail();

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found."));

        return notificationRepository
                .findByRecipientOrderByCreatedAtDesc(currentUser)
                .stream()
                .map(this::toResponse)
                .toList();

    }

    public List<NotificationResponse> getUnreadNotifications() {

        String email = SecurityUtil.getCurrentUserEmail();

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found."));

        return notificationRepository
                .findByRecipientAndReadFalseOrderByCreatedAtDesc(currentUser)
                .stream()
                .map(this::toResponse)
                .toList();

    }

    public long getUnreadCount() {

        String email = SecurityUtil.getCurrentUserEmail();

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found."));

        return notificationRepository
                .countByRecipientAndReadFalse(currentUser);

    }

    public void markAsRead(
            Long notificationId
    ) {

        Notification notification =
                notificationRepository.findById(notificationId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Notification not found."
                                ));

        notification.setRead(true);

        notificationRepository.save(notification);

    }

    private NotificationResponse toResponse(
            Notification notification
    ) {

        return new NotificationResponse(
                notification.getId(),
                notification.getType(),
                notification.getTitle(),
                notification.getBody(),
                notification.getReferenceId(),
                notification.getReferenceType(),
                notification.isRead(),
                notification.getCreatedAt()
        );

    }

}