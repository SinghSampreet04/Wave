package com.wave.backend.mention.repository;

import com.wave.backend.mention.entity.Mention;
import com.wave.backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MentionRepository
        extends JpaRepository<Mention, Long> {

    List<Mention> findByMentionedUserOrderByCreatedAtDesc(
            User mentionedUser
    );

    long countByMentionedUserAndReadFalse(
            User mentionedUser
    );

}