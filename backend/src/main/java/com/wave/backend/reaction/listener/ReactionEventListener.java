package com.wave.backend.reaction.listener;

import com.wave.backend.common.event.ReactionAddedEvent;
import com.wave.backend.reaction.dto.ReactionEventResponse;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class ReactionEventListener {

    private final SimpMessagingTemplate messagingTemplate;

    public ReactionEventListener(
            SimpMessagingTemplate messagingTemplate
    ) {
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void handleReactionAdded(
            ReactionAddedEvent event
    ) {

        ReactionEventResponse response =
                new ReactionEventResponse(
                        event.getMessageId(),
                        event.getEmoji(),
                        event.getCount(),
                        event.getActorUserId(),
                        event.isReacted()
                );

        messagingTemplate.convertAndSend(
                "/topic/channels/" + event.getChannelId() + "/reactions",
                response
        );
    }
}
