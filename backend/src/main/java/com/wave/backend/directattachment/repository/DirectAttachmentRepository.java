package com.wave.backend.directattachment.repository;

import com.wave.backend.directattachment.entity.DirectAttachment;
import com.wave.backend.directmessage.entity.DirectMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DirectAttachmentRepository
        extends JpaRepository<DirectAttachment, Long> {

    List<DirectAttachment> findByDirectMessage(
            DirectMessage directMessage
    );

}