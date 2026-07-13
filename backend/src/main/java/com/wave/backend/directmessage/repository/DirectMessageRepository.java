package com.wave.backend.directmessage.repository;

import com.wave.backend.directmessage.entity.Conversation;
import com.wave.backend.directmessage.entity.DirectMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DirectMessageRepository
        extends JpaRepository<DirectMessage, Long> {

    List<DirectMessage> findByConversationOrderByCreatedAtAsc(
            Conversation conversation
    );

    Page<DirectMessage> findByConversationOrderByCreatedAtDesc(
            Conversation conversation,
            Pageable pageable
    );

    List<DirectMessage> findByConversationAndContentContainingIgnoreCaseOrderByCreatedAtDesc(
            Conversation conversation,
            String content
    );

}