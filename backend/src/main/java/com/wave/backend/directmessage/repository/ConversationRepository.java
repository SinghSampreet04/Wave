package com.wave.backend.directmessage.repository;

import com.wave.backend.directmessage.entity.Conversation;
import com.wave.backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository
        extends JpaRepository<Conversation, Long> {

    Optional<Conversation> findByUserOneAndUserTwo(
            User userOne,
            User userTwo
    );

    Optional<Conversation> findByUserTwoAndUserOne(
            User userTwo,
            User userOne
    );

    List<Conversation> findByUserOneOrUserTwoOrderByLastMessageAtDesc(
            User userOne,
            User userTwo
    );

}