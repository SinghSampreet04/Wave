package com.wave.backend.notification.service;

import com.wave.backend.common.event.NotificationCreatedEvent;
import com.wave.backend.common.dto.PagedResponse;
import com.wave.backend.common.util.SecurityUtil;
import com.wave.backend.exception.UserNotFoundException;
import com.wave.backend.notification.dto.NotificationResponse;
import com.wave.backend.notification.entity.Notification;
import com.wave.backend.notification.entity.NotificationType;
import com.wave.backend.notification.repository.NotificationRepository;
import com.wave.backend.user.entity.User;
import com.wave.backend.user.repository.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(readOnly = true)
    public PagedResponse<NotificationResponse> getMyNotifications(
            boolean unreadOnly,
            int page,
            int size
    ) {
        User currentUser = getCurrentUser();
        PageRequest pageable = PageRequest.of(
                Math.max(page, 0),
                Math.min(Math.max(size, 1), 100)
        );
        Page<NotificationResponse> notifications = (
                unreadOnly
                        ? notificationRepository
                                .findByRecipientAndReadFalseOrderByCreatedAtDesc(
                                        currentUser,
                                        pageable
                                )
                        : notificationRepository
                                .findByRecipientOrderByCreatedAtDesc(
                                        currentUser,
                                        pageable
                                )
                )
                .map(this::toResponse);
        return PagedResponse.from(notifications);
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

        String email = SecurityUtil.getCurrentUserEmail();

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found."));

        Notification notification =
                notificationRepository.findByIdAndRecipient(
                                notificationId,
                                currentUser
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Notification not found."
                                ));

        notification.setRead(true);

        notificationRepository.save(notification);

    }

    @Transactional
    public void markAllAsRead() {
        String email = SecurityUtil.getCurrentUserEmail();

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found."));

        List<Notification> unread =
                notificationRepository
                        .findByRecipientAndReadFalseOrderByCreatedAtDesc(
                                currentUser
                        );

        unread.forEach(notification ->
                notification.setRead(true));
        notificationRepository.saveAll(unread);
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

    private User getCurrentUser() {
        String email = SecurityUtil.getCurrentUserEmail();
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found."));
    }

}
