package com.wave.backend.exception;

public class MessageAlreadyDeletedException extends RuntimeException {

    public MessageAlreadyDeletedException(String message) {
        super(message);
    }

}