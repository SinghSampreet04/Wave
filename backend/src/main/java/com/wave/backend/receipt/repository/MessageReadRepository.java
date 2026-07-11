package com.wave.backend.receipt.repository;

import com.wave.backend.message.entity.Message;
import com.wave.backend.receipt.entity.MessageRead;
import com.wave.backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessageReadRepository
        extends JpaRepository<MessageRead, Long> {

    Optional<MessageRead> findByMessageAndUser(
            Message message,
            User user
    );

    List<MessageRead> findByMessage(Message message);

    long countByMessage(Message message);

}