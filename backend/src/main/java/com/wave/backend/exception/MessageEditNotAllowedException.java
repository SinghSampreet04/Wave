package com.wave.backend.exception;

public class MessageEditNotAllowedException extends RuntimeException {

    public MessageEditNotAllowedException(String message) {
        super(message);
    }

}