package com.wave.backend.pin.listener;

import com.wave.backend.common.event.MessagePinnedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessagePinnedEventListener {

    private final SimpMessagingTemplate messagingTemplate;

    public MessagePinnedEventListener(
            SimpMessagingTemplate messagingTemplate
    ) {
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void handleMessagePinned(
            MessagePinnedEvent event
    ) {

        messagingTemplate.convertAndSend(
                "/topic/channels/" + event.getChannelId() + "/pins",
                event
        );

    }

}