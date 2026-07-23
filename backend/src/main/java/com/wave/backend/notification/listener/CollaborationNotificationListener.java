package com.wave.backend.notification.listener;

import com.wave.backend.common.event.MentionCreatedEvent;
import com.wave.backend.common.event.ReactionAddedEvent;
import com.wave.backend.common.event.ThreadReplyEvent;
import com.wave.backend.message.entity.Message;
import com.wave.backend.message.repository.MessageRepository;
import com.wave.backend.notification.entity.NotificationType;
import com.wave.backend.notification.service.NotificationService;
import com.wave.backend.user.entity.User;
import com.wave.backend.user.repository.UserRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CollaborationNotificationListener {

    private final NotificationService notificationService;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public CollaborationNotificationListener(
            NotificationService notificationService,
            MessageRepository messageRepository,
            UserRepository userRepository
    ) {
        this.notificationService = notificationService;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    @EventListener
    @Transactional
    public void onMention(MentionCreatedEvent event) {
        User recipient = userRepository.findById(event.getMentionedUserId())
                .orElse(null);
        if (recipient == null) return;

        boolean direct = event.getDirectMessageId() != null;
        notificationService.createNotification(
                recipient,
                NotificationType.MENTION,
                "You were mentioned",
                event.getMentionedByUsername() + " mentioned you.",
                direct ? event.getDirectMessageId() : event.getMessageId(),
                direct ? "DIRECT_MESSAGE" : "MESSAGE"
        );
    }

    @EventListener
    @Transactional
    public void onThreadReply(ThreadReplyEvent event) {
        Message parent = messageRepository.findById(event.getParentMessageId())
                .orElse(null);
        if (parent == null || parent.getSender().getId().equals(event.getSenderId())) {
            return;
        }

        notificationService.createNotification(
                parent.getSender(),
                NotificationType.THREAD_REPLY,
                "New thread reply",
                event.getSenderUsername() + " replied to your message.",
                parent.getId(),
                "MESSAGE"
        );
    }

    @EventListener
    @Transactional
    public void onReaction(ReactionAddedEvent event) {
        if (!event.isReacted()) return;
        Message message = messageRepository.findById(event.getMessageId())
                .orElse(null);
        if (message == null
                || message.getSender().getId().equals(event.getActorUserId())) {
            return;
        }

        User actor = userRepository.findById(event.getActorUserId()).orElse(null);
        if (actor == null) return;
        notificationService.createNotification(
                message.getSender(),
                NotificationType.REACTION,
                "New reaction",
                actor.getUsername() + " reacted " + event.getEmoji() + " to your message.",
                message.getId(),
                "MESSAGE"
        );
    }
}
