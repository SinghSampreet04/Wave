package com.wave.backend.message.service;

import com.wave.backend.common.event.MessageEditedEvent;
import com.wave.backend.common.util.SecurityUtil;
import com.wave.backend.exception.MessageAccessDeniedException;
import com.wave.backend.exception.MessageNotFoundException;
import com.wave.backend.exception.UserNotFoundException;
import com.wave.backend.message.dto.MessageEditResponse;
import com.wave.backend.message.dto.UpdateMessageRequest;
import com.wave.backend.message.entity.Message;
import com.wave.backend.message.repository.MessageRepository;
import com.wave.backend.user.entity.User;
import com.wave.backend.user.repository.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MessageEditService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    public MessageEditService(
            MessageRepository messageRepository,
            UserRepository userRepository,
            ApplicationEventPublisher eventPublisher
    ) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
    }

    public MessageEditResponse editMessage(
            Long messageId,
            UpdateMessageRequest request
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
                    "You can only edit your own messages."
            );
        }

        message.setContent(request.getContent());
        message.setEdited(true);
        message.setEditedAt(LocalDateTime.now());

        message = messageRepository.save(message);

        eventPublisher.publishEvent(
                new MessageEditedEvent(
                        message.getId(),
                        message.getChannel().getId(),
                        message.getContent(),
                        message.getEditedAt()
                )
        );

        return new MessageEditResponse(
                message.getId(),
                message.getChannel().getId(),
                message.getContent(),
                message.isEdited(),
                message.getEditedAt()
        );
    }

}