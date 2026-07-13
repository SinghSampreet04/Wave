package com.wave.backend.mention.listener;

import com.wave.backend.common.event.MentionCreatedEvent;
import com.wave.backend.notification.entity.NotificationType;
import com.wave.backend.notification.service.NotificationService;
import com.wave.backend.user.entity.User;
import com.wave.backend.user.repository.UserRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MentionListener {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    public MentionListener(
            NotificationService notificationService,
            UserRepository userRepository
    ) {
        this.notificationService = notificationService;
        this.userRepository = userRepository;
    }

    @EventListener
    public void handleMentionCreated(
            MentionCreatedEvent event
    ) {

        User recipient =
                userRepository.findById(event.getMentionedUserId())
                        .orElse(null);

        if (recipient == null) {
            return;
        }

        Long referenceId =
                event.getMessageId() != null
                        ? event.getMessageId()
                        : event.getDirectMessageId();

        String referenceType =
                event.getMessageId() != null
                        ? "CHANNEL_MESSAGE"
                        : "DIRECT_MESSAGE";

        notificationService.createNotification(
                recipient,
                NotificationType.MENTION,
                "New Mention",
                event.getMentionedByUsername() + " mentioned you.",
                referenceId,
                referenceType
        );

    }

}