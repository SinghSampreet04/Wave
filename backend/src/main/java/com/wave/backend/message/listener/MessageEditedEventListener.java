package com.wave.backend.message.listener;

import com.wave.backend.common.event.MessageEditedEvent;
import com.wave.backend.message.dto.MessageEditResponse;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageEditedEventListener {

    private final SimpMessagingTemplate messagingTemplate;

    public MessageEditedEventListener(
            SimpMessagingTemplate messagingTemplate
    ) {
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void handleMessageEdited(
            MessageEditedEvent event
    ) {

        MessageEditResponse response =
                new MessageEditResponse(
                        event.getMessageId(),
                        event.getChannelId(),
                        event.getContent(),
                        true,
                        event.getEditedAt()
                );

        messagingTemplate.convertAndSend(
                "/topic/channels/" + event.getChannelId() + "/edited",
                response
        );

    }

}