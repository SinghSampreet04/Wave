package com.wave.backend.message.listener;

import com.wave.backend.common.event.MessageDeletedEvent;
import com.wave.backend.message.dto.MessageDeletedResponse;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageDeletedEventListener {

    private final SimpMessagingTemplate messagingTemplate;

    public MessageDeletedEventListener(
            SimpMessagingTemplate messagingTemplate
    ) {
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void handleMessageDeleted(
            MessageDeletedEvent event
    ) {

        MessageDeletedResponse response =
                new MessageDeletedResponse(
                        event.getMessageId(),
                        event.getChannelId(),
                        true,
                        event.getDeletedAt()
                );

        messagingTemplate.convertAndSend(
    "/topic/channels/" + event.getChannelId() + "/deleted",
    response
);

    }

}