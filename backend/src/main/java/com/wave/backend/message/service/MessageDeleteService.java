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
import com.wave.backend.file.event.StoredFilesDeletionEvent;
import com.wave.backend.file.entity.FileAttachment;
import com.wave.backend.file.repository.FileAttachmentRepository;
import com.wave.backend.pin.repository.PinnedMessageRepository;
import com.wave.backend.reaction.repository.ReactionRepository;
import com.wave.backend.user.entity.User;
import com.wave.backend.user.repository.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageDeleteService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final FileAttachmentRepository fileAttachmentRepository;
    private final PinnedMessageRepository pinnedMessageRepository;
    private final ReactionRepository reactionRepository;

    public MessageDeleteService(
            MessageRepository messageRepository,
            UserRepository userRepository,
            ApplicationEventPublisher eventPublisher,
            FileAttachmentRepository fileAttachmentRepository,
            PinnedMessageRepository pinnedMessageRepository,
            ReactionRepository reactionRepository
    ) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
        this.fileAttachmentRepository = fileAttachmentRepository;
        this.pinnedMessageRepository = pinnedMessageRepository;
        this.reactionRepository = reactionRepository;
    }

    @Transactional
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

        List<String> storedFilenames =
                fileAttachmentRepository.findByMessage(message)
                        .stream()
                        .map(FileAttachment::getStoredFilename)
                        .toList();

        pinnedMessageRepository.deleteByMessage(message);
        reactionRepository.deleteByMessage(message);
        fileAttachmentRepository.deleteByMessage(message);

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
        if (!storedFilenames.isEmpty()) {
            eventPublisher.publishEvent(
                    new StoredFilesDeletionEvent(storedFilenames)
            );
        }

        return new MessageDeleteResponse(
                message.getId(),
                message.getChannel().getId(),
                message.isDeleted(),
                message.getDeletedAt()
        );
    }
}
