package com.wave.backend.pin.listener;

import com.wave.backend.common.event.MessageUnpinnedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageUnpinnedEventListener {

    private final SimpMessagingTemplate messagingTemplate;

    public MessageUnpinnedEventListener(
            SimpMessagingTemplate messagingTemplate
    ) {
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void handleMessageUnpinned(
            MessageUnpinnedEvent event
    ) {

        messagingTemplate.convertAndSend(
                "/topic/channels/" + event.getChannelId() + "/pins",
                event
        );

    }

}