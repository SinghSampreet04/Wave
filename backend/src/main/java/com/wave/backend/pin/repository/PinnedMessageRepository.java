package com.wave.backend.pin.repository;

import com.wave.backend.message.entity.Message;
import com.wave.backend.pin.entity.PinnedMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PinnedMessageRepository
        extends JpaRepository<PinnedMessage, Long> {

    Optional<PinnedMessage> findByMessage(
            Message message
    );

    boolean existsByMessage(
            Message message
    );

    List<PinnedMessage> findByMessage_Channel_IdAndMessage_DeletedFalseOrderByPinnedAtDesc(
            Long channelId
    );

    void deleteByMessage(Message message);

}
