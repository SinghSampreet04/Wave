package com.wave.backend.message.service;

import com.wave.backend.message.dto.MessageSearchResponse;
import com.wave.backend.message.entity.Message;
import com.wave.backend.message.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageSearchService {

    private final MessageRepository messageRepository;

    public MessageSearchService(
            MessageRepository messageRepository
    ) {
        this.messageRepository = messageRepository;
    }

    public List<MessageSearchResponse> searchMessages(
            String keyword
    ) {

        List<Message> messages =
                messageRepository.searchByKeyword(keyword);

        return messages.stream()
                .map(this::toResponse)
                .toList();
    }

    public List<MessageSearchResponse> searchMessages(
            Long channelId,
            String keyword
    ) {

        List<Message> messages =
                messageRepository.searchByChannelAndKeyword(
                        channelId,
                        keyword
                );

        return messages.stream()
                .map(this::toResponse)
                .toList();
    }

    private MessageSearchResponse toResponse(
            Message message
    ) {

        return new MessageSearchResponse(
                message.getId(),
                message.getChannel().getId(),
                message.getSender().getId(),
                message.getSender().getUsername(),
                message.getContent(),
                message.getCreatedAt()
        );

    }

}