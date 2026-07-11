package com.wave.backend.websocket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TypingResponse {

    private Long userId;

    private String username;

    private boolean typing;

}