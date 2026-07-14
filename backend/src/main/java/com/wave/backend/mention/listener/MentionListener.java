package com.wave.backend.mention.listener;

import com.wave.backend.common.event.MentionCreatedEvent;
import com.wave.backend.mention.dto.MentionWebSocketResponse;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MentionListener {

    private final SimpMessagingTemplate messagingTemplate;

    public MentionListener(
            SimpMessagingTemplate messagingTemplate
    ) {
        this.messagingTemplate = messagingTemplate;
    }

@EventListener
public void handleMentionCreated(
        MentionCreatedEvent event
) {

    MentionWebSocketResponse response =
            new MentionWebSocketResponse(
                    event.getMentionId(),
                    event.getMentionedByUserId(),
                    event.getMentionedByUsername(),
                    event.getMessageId(),
                    event.getDirectMessageId(),
                    LocalDateTime.now()
            );

    messagingTemplate.convertAndSend(
            "/topic/users/"
                    + event.getMentionedUserId()
                    + "/mentions",
            response
    );

}

}