package com.wave.backend.message.listener;

import com.wave.backend.common.event.ThreadReplyEvent;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.context.event.EventListener;

@Component
public class ThreadReplyEventListener {

    private final SimpMessagingTemplate messagingTemplate;

    public ThreadReplyEventListener(
            SimpMessagingTemplate messagingTemplate
    ) {
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void handleThreadReply(
            ThreadReplyEvent event
    ) {

        messagingTemplate.convertAndSend(
                "/topic/threads/" + event.getParentMessageId(),
                event
        );

    }

}