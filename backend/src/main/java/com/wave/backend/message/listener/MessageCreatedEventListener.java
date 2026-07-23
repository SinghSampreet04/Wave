package com.wave.backend.message.listener;

import com.wave.backend.common.event.MessageCreatedEvent;
import com.wave.backend.websocket.dto.ChatMessageResponse;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class MessageCreatedEventListener {

    private final SimpMessagingTemplate messagingTemplate;

    public MessageCreatedEventListener(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onMessageCreated(MessageCreatedEvent event) {
        var message = event.message();
        messagingTemplate.convertAndSend(
                "/topic/channels/" + message.getChannelId(),
                new ChatMessageResponse(
                        message.getId(),
                        message.getChannelId(),
                        message.getSenderId(),
                        message.getSenderUsername(),
                        message.getContent(),
                        message.getCreatedAt()
                )
        );
    }
}
