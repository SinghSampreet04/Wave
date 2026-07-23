package com.wave.backend.reaction.repository;

import com.wave.backend.message.entity.Message;
import com.wave.backend.reaction.entity.Reaction;
import com.wave.backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {

    List<Reaction> findByMessage(Message message);

    Optional<Reaction> findByMessageAndUserAndEmoji(
            Message message,
            User user,
            String emoji
    );

    long countByMessageAndEmoji(
            Message message,
            String emoji
    );

    List<Reaction> findAllByMessageOrderByEmojiAsc(
            Message message
    );

    void deleteByMessage(Message message);

}
