package com.wave.backend.file.event;

import java.util.List;

public record StoredFilesDeletionEvent(
        List<String> storedFilenames
) {
    public StoredFilesDeletionEvent {
        storedFilenames = List.copyOf(storedFilenames);
    }
}
