package com.wave.backend.directreaction.repository;

import com.wave.backend.directmessage.entity.DirectMessage;
import com.wave.backend.directreaction.entity.DirectReaction;
import com.wave.backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DirectReactionRepository
        extends JpaRepository<DirectReaction, Long> {

    DirectReaction findByDirectMessageAndUserAndEmoji(
            DirectMessage directMessage,
            User user,
            String emoji
    );

    long countByDirectMessageAndEmoji(
            DirectMessage directMessage,
            String emoji
    );

}