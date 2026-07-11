package com.wave.backend.message.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MessageEditResponse {

    private Long messageId;

    private Long channelId;

    private String content;

    private boolean edited;

    private LocalDateTime editedAt;

}