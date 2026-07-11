package com.wave.backend.exception;

public class WorkspaceNotFoundException extends RuntimeException {

    public WorkspaceNotFoundException(String message) {
        super(message);
    }

}