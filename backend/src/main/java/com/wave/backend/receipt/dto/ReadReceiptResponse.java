package com.wave.backend.receipt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReadReceiptResponse {

    private Long messageId;

    private Long userId;

    private String username;

    private LocalDateTime readAt;

}