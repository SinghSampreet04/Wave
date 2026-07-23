package com.wave.backend.message.service;

import com.wave.backend.common.event.MessageDeletedEvent;
import com.wave.backend.common.util.SecurityUtil;
import com.wave.backend.exception.MessageAccessDeniedException;
import com.wave.backend.exception.MessageAlreadyDeletedException;
import com.wave.backend.exception.MessageNotFoundException;
import com.wave.backend.exception.UserNotFoundException;
import com.wave.backend.message.dto.MessageDeleteResponse;
import com.wave.backend.message.entity.Message;
import com.wave.backend.message.repository.MessageRepository;
import com.wave.backend.user.entity.User;
import com.wave.backend.user.repository.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MessageDeleteService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    public MessageDeleteService(
            MessageRepository messageRepository,
            UserRepository userRepository,
            ApplicationEventPublisher eventPublisher
    ) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
    }

    public MessageDeleteResponse deleteMessage(
            Long messageId
    ) {

        String email = SecurityUtil.getCurrentUserEmail();

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found."));

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() ->
                        new MessageNotFoundException("Message not found."));

        if (!message.getSender().getId().equals(currentUser.getId())) {
            throw new MessageAccessDeniedException(
                    "You can only delete your own messages."
            );
        }

        if (message.isDeleted()) {
            throw new MessageAlreadyDeletedException(
                    "Message has already been deleted."
            );
        }

        message.setDeleted(true);
        message.setDeletedAt(LocalDateTime.now());
        message.setEdited(true);
        message.setContent("This message was deleted.");

        message = messageRepository.save(message);

        eventPublisher.publishEvent(
                new MessageDeletedEvent(
                        message.getId(),
                        message.getChannel().getId(),
                        message.getDeletedAt()
                )
        );

        return new MessageDeleteResponse(
                message.getId(),
                message.getChannel().getId(),
                message.isDeleted(),
                message.getDeletedAt()
        );
    }
}