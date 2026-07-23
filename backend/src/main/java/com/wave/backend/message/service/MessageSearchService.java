package com.wave.backend.message.service;

import com.wave.backend.common.util.SecurityUtil;
import com.wave.backend.exception.UserNotFoundException;
import com.wave.backend.message.dto.MessageSearchResponse;
import com.wave.backend.message.entity.Message;
import com.wave.backend.message.repository.MessageRepository;
import com.wave.backend.user.entity.User;
import com.wave.backend.user.repository.UserRepository;
import com.wave.backend.file.repository.FileAttachmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@Service
public class MessageSearchService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final FileAttachmentRepository fileAttachmentRepository;

    public MessageSearchService(
            MessageRepository messageRepository,
            UserRepository userRepository,
            FileAttachmentRepository fileAttachmentRepository
    ) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.fileAttachmentRepository = fileAttachmentRepository;
    }

    public List<MessageSearchResponse> searchMessages(
            String keyword
    ) {

        User currentUser = getCurrentUser();

        List<Message> messages =
                messageRepository.searchAccessibleByKeyword(
                        normalizedKeyword(keyword),
                        currentUser
                );

        return messages.stream()
                .map(this::toResponse)
                .toList();
    }

    public Page<MessageSearchResponse> searchMessages(
            String keyword,
            Long workspaceId,
            Long channelId,
            Long senderId,
            LocalDateTime fromDate,
            LocalDateTime toDate,
            Boolean hasAttachment,
            Boolean hasReaction,
            int page,
            int size
    ) {
        User currentUser = getCurrentUser();
        return messageRepository.searchAccessible(
                        normalizedKeyword(keyword),
                        workspaceId,
                        channelId,
                        senderId,
                        fromDate,
                        toDate,
                        hasAttachment,
                        hasReaction,
                        currentUser,
                        PageRequest.of(page, Math.min(Math.max(size, 1), 100))
                )
                .map(this::toResponse);
    }

    public List<MessageSearchResponse> searchMessages(
            Long channelId,
            String keyword
    ) {

        User currentUser = getCurrentUser();

        List<Message> messages =
                messageRepository.searchAccessibleByChannelAndKeyword(
                        channelId,
                        normalizedKeyword(keyword),
                        currentUser
                );

        return messages.stream()
                .map(this::toResponse)
                .toList();
    }

    private User getCurrentUser() {
        String email = SecurityUtil.getCurrentUserEmail();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found."));
    }

    private String normalizedKeyword(String keyword) {
        if (keyword == null || keyword.trim().length() < 2) {
            throw new IllegalArgumentException(
                    "Search query must contain at least 2 characters."
            );
        }

        return keyword.trim();
    }

    private MessageSearchResponse toResponse(
            Message message
    ) {

        return new MessageSearchResponse(
                message.getId(),
                message.getChannel().getId(),
                message.getChannel().getName(),
                message.getChannel().getWorkspace().getId(),
                message.getChannel().getWorkspace().getName(),
                message.getSender().getId(),
                message.getSender().getUsername(),
                message.getContent(),
                message.getCreatedAt(),
                fileAttachmentRepository.existsByMessageId(message.getId()),
                !message.getReactions().isEmpty()
        );

    }

}
