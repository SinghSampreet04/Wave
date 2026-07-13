package com.wave.backend.directmessage.read.repository;

import com.wave.backend.directmessage.entity.Conversation;
import com.wave.backend.directmessage.read.entity.ConversationReadState;
import com.wave.backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConversationReadStateRepository
        extends JpaRepository<ConversationReadState, Long> {

    Optional<ConversationReadState> findByConversationAndUser(
            Conversation conversation,
            User user
    );

}