package com.wave.backend.directmessage.listener;

import com.wave.backend.common.event.DirectMessageDeletedEvent;
import com.wave.backend.common.event.DirectMessageEditedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class DirectMessageMutationEventListener {

    private final SimpMessagingTemplate messagingTemplate;

    public DirectMessageMutationEventListener(
            SimpMessagingTemplate messagingTemplate
    ) {
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void handleEdited(
            DirectMessageEditedEvent event
    ) {
        messagingTemplate.convertAndSend(
                "/topic/conversations/"
                        + event.getConversationId()
                        + "/edited",
                event
        );
    }

    @EventListener
    public void handleDeleted(
            DirectMessageDeletedEvent event
    ) {
        messagingTemplate.convertAndSend(
                "/topic/conversations/"
                        + event.getConversationId()
                        + "/deleted",
                event
        );
    }
}
