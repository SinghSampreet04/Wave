package com.wave.backend.pin.service;

import com.wave.backend.common.event.MessagePinnedEvent;
import com.wave.backend.common.event.MessageUnpinnedEvent;
import com.wave.backend.common.util.SecurityUtil;
import com.wave.backend.exception.MessageNotFoundException;
import com.wave.backend.exception.UserNotFoundException;
import com.wave.backend.message.entity.Message;
import com.wave.backend.message.repository.MessageRepository;
import com.wave.backend.pin.dto.PinnedMessageResponse;
import com.wave.backend.pin.entity.PinnedMessage;
import com.wave.backend.pin.repository.PinnedMessageRepository;
import com.wave.backend.user.entity.User;
import com.wave.backend.user.repository.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PinnedMessageService {

    private final PinnedMessageRepository pinnedMessageRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    public PinnedMessageService(
            PinnedMessageRepository pinnedMessageRepository,
            MessageRepository messageRepository,
            UserRepository userRepository,
            ApplicationEventPublisher eventPublisher
    ) {
        this.pinnedMessageRepository = pinnedMessageRepository;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
    }

    public PinnedMessageResponse pinMessage(
            Long messageId
    ) {

        String email = SecurityUtil.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found."));

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() ->
                        new MessageNotFoundException("Message not found."));

        if (pinnedMessageRepository.existsByMessage(message)) {

            return toResponse(
                    pinnedMessageRepository
                            .findByMessage(message)
                            .orElseThrow()
            );

        }

        PinnedMessage pin = new PinnedMessage();

        pin.setMessage(message);
        pin.setPinnedBy(user);

        pin = pinnedMessageRepository.save(pin);

        eventPublisher.publishEvent(
                new MessagePinnedEvent(
                        message.getId(),
                        message.getChannel().getId(),
                        user.getId(),
                        user.getUsername(),
                        pin.getPinnedAt()
                )
        );

        return toResponse(pin);
    }

    public void unpinMessage(
            Long messageId
    ) {

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() ->
                        new MessageNotFoundException("Message not found."));

        pinnedMessageRepository.findByMessage(message)
                .ifPresent(pin -> {

                    pinnedMessageRepository.delete(pin);

                    eventPublisher.publishEvent(
                            new MessageUnpinnedEvent(
                                    message.getId(),
                                    message.getChannel().getId()
                            )
                    );

                });

    }

    public List<PinnedMessageResponse> getPinnedMessages(
            Long channelId
    ) {

        return pinnedMessageRepository
                .findByMessage_Channel_IdOrderByPinnedAtDesc(channelId)
                .stream()
                .map(this::toResponse)
                .toList();

    }

    private PinnedMessageResponse toResponse(
            PinnedMessage pin
    ) {

        return new PinnedMessageResponse(
                pin.getId(),
                pin.getMessage().getId(),
                pin.getMessage().getChannel().getId(),
                pin.getPinnedBy().getId(),
                pin.getPinnedBy().getUsername(),
                pin.getMessage().getContent(),
                pin.getPinnedAt()
        );

    }

}