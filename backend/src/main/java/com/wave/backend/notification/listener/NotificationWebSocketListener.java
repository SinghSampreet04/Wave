package com.wave.backend.notification.listener;

import com.wave.backend.common.event.NotificationCreatedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class NotificationWebSocketListener {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationWebSocketListener(
            SimpMessagingTemplate messagingTemplate
    ) {
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void handleNotificationCreated(
            NotificationCreatedEvent event
    ) {

        messagingTemplate.convertAndSend(
                "/topic/notifications/" + event.getRecipientId(),
                event
        );

    }

}