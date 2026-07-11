package com.wave.backend.exception;

public class WorkspaceAccessDeniedException extends RuntimeException {

    public WorkspaceAccessDeniedException(String message) {
        super(message);
    }

}