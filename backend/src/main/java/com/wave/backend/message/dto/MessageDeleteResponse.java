package com.wave.backend.message.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MessageDeleteResponse {

    private Long messageId;

    private Long channelId;

    private boolean deleted;

    private LocalDateTime deletedAt;

}