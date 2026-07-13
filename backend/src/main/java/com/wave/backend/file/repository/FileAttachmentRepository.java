package com.wave.backend.file.repository;

import com.wave.backend.file.entity.FileAttachment;
import com.wave.backend.message.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileAttachmentRepository
        extends JpaRepository<FileAttachment, Long> {

    List<FileAttachment> findByMessage(Message message);

    List<FileAttachment> findByMessageId(Long messageId);

}