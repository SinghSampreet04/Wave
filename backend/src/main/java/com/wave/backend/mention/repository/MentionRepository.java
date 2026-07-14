package com.wave.backend.mention.repository;

import com.wave.backend.mention.entity.Mention;
import com.wave.backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MentionRepository
        extends JpaRepository<Mention, Long> {

    List<Mention> findByMentionedUserOrderByCreatedAtDesc(
            User mentionedUser
    );

    List<Mention> findByMentionedUserAndReadFalseOrderByCreatedAtDesc(
            User mentionedUser
    );

    long countByMentionedUserAndReadFalse(
            User mentionedUser
    );

    Optional<Mention> findByIdAndMentionedUser(
            Long id,
            User mentionedUser
    );

}