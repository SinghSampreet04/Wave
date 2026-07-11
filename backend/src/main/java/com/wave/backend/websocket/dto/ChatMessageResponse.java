package com.wave.backend.websocket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ChatMessageResponse {

    private Long messageId;

    private Long channelId;

    private Long senderId;

    private String senderUsername;

    private String content;

    private LocalDateTime sentAt;

}