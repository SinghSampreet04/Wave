package com.wave.backend.notification.listener;

import com.wave.backend.common.event.DirectMessageSentEvent;
import com.wave.backend.directmessage.entity.Conversation;
import com.wave.backend.directmessage.repository.ConversationRepository;
import com.wave.backend.notification.entity.NotificationType;
import com.wave.backend.notification.service.NotificationService;
import com.wave.backend.user.entity.User;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DirectMessageNotificationListener {

    private final ConversationRepository conversationRepository;
    private final NotificationService notificationService;

    public DirectMessageNotificationListener(
            ConversationRepository conversationRepository,
            NotificationService notificationService
    ) {
        this.conversationRepository = conversationRepository;
        this.notificationService = notificationService;
    }

    @EventListener
    public void handleDirectMessageSent(
            DirectMessageSentEvent event
    ) {

        Conversation conversation = conversationRepository
                .findById(event.getConversationId())
                .orElse(null);

        if (conversation == null) {
            return;
        }

        User recipient;

        if (conversation.getUserOne().getId().equals(event.getSenderId())) {

            recipient = conversation.getUserTwo();

        } else {

            recipient = conversation.getUserOne();

        }

        notificationService.createNotification(
                recipient,
                NotificationType.DIRECT_MESSAGE,
                "New Direct Message",
                event.getSenderUsername() + " sent you a message.",
                conversation.getId(),
                "CONVERSATION"
        );

    }

}