package com.wave.backend.receipt.listener;

import com.wave.backend.common.event.MessageReadEvent;
import com.wave.backend.receipt.dto.ReadReceiptResponse;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ReadReceiptEventListener {

    private final SimpMessagingTemplate messagingTemplate;

    public ReadReceiptEventListener(
            SimpMessagingTemplate messagingTemplate
    ) {
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void handleMessageRead(
            MessageReadEvent event
    ) {

        ReadReceiptResponse response =
                new ReadReceiptResponse(
                        event.getMessageId(),
                        event.getUserId(),
                        event.getUsername(),
                        LocalDateTime.now()
                );

        messagingTemplate.convertAndSend(
                "/topic/channels/" + event.getChannelId() + "/read",
                response
        );

    }

}