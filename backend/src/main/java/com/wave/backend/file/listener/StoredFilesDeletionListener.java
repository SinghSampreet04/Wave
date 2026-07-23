package com.wave.backend.file.listener;

import com.wave.backend.file.event.StoredFilesDeletionEvent;
import com.wave.backend.file.service.FileStorageService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class StoredFilesDeletionListener {

    private final FileStorageService fileStorageService;

    public StoredFilesDeletionListener(
            FileStorageService fileStorageService
    ) {
        this.fileStorageService = fileStorageService;
    }

    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void removeStoredFiles(
            StoredFilesDeletionEvent event
    ) {
        fileStorageService.deleteFiles(
                event.storedFilenames()
        );
    }
}
