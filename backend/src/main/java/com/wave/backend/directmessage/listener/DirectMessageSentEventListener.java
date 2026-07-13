package com.wave.backend.directmessage.listener;

import com.wave.backend.common.event.DirectMessageSentEvent;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class DirectMessageSentEventListener {

    private final SimpMessagingTemplate messagingTemplate;

    public DirectMessageSentEventListener(
            SimpMessagingTemplate messagingTemplate
    ) {
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void handleDirectMessageSent(
            DirectMessageSentEvent event
    ) {

        messagingTemplate.convertAndSend(
                "/topic/conversations/" + event.getConversationId(),
                event
        );

    }

}