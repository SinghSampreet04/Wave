package com.wave.backend.pin.service;

import com.wave.backend.channel.entity.Channel;
import com.wave.backend.channel.repository.ChannelRepository;
import com.wave.backend.channelmember.service.ChannelMemberService;
import com.wave.backend.common.event.MessagePinnedEvent;
import com.wave.backend.common.event.MessageUnpinnedEvent;
import com.wave.backend.common.util.SecurityUtil;
import com.wave.backend.exception.ChannelNotFoundException;
import com.wave.backend.exception.MessageNotFoundException;
import com.wave.backend.exception.UserNotFoundException;
import com.wave.backend.exception.MessageAlreadyDeletedException;
import com.wave.backend.message.entity.Message;
import com.wave.backend.message.repository.MessageRepository;
import com.wave.backend.pin.dto.PinnedMessageResponse;
import com.wave.backend.pin.entity.PinnedMessage;
import com.wave.backend.pin.repository.PinnedMessageRepository;
import com.wave.backend.user.entity.User;
import com.wave.backend.user.repository.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PinnedMessageService {

    private final PinnedMessageRepository pinnedMessageRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final ChannelMemberService channelMemberService;
    private final ApplicationEventPublisher eventPublisher;

    public PinnedMessageService(
            PinnedMessageRepository pinnedMessageRepository,
            MessageRepository messageRepository,
            UserRepository userRepository,
            ChannelRepository channelRepository,
            ChannelMemberService channelMemberService,
            ApplicationEventPublisher eventPublisher
    ) {
        this.pinnedMessageRepository = pinnedMessageRepository;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
        this.channelMemberService = channelMemberService;
        this.eventPublisher = eventPublisher;
    }

    public PinnedMessageResponse pinMessage(
            Long messageId
    ) {

        User currentUser = getCurrentUser();

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() ->
                        new MessageNotFoundException("Message not found."));

        channelMemberService.validateChannelAccess(
                message.getChannel(),
                currentUser
        );

        if (message.isDeleted()) {
            throw new MessageAlreadyDeletedException(
                    "Deleted messages cannot be pinned."
            );
        }

        if (pinnedMessageRepository.existsByMessage(message)) {

            return toResponse(
                    pinnedMessageRepository
                            .findByMessage(message)
                            .orElseThrow()
            );

        }

        PinnedMessage pin = new PinnedMessage();

        pin.setMessage(message);
        pin.setPinnedBy(currentUser);

        pin = pinnedMessageRepository.save(pin);

        eventPublisher.publishEvent(
                new MessagePinnedEvent(
                        message.getId(),
                        message.getChannel().getId(),
                        currentUser.getId(),
                        currentUser.getUsername(),
                        pin.getPinnedAt()
                )
        );

        return toResponse(pin);

    }

    public void unpinMessage(
            Long messageId
    ) {

        User currentUser = getCurrentUser();

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() ->
                        new MessageNotFoundException("Message not found."));

        channelMemberService.validateChannelAccess(
                message.getChannel(),
                currentUser
        );

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

        User currentUser = getCurrentUser();

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() ->
                        new ChannelNotFoundException("Channel not found."));

        channelMemberService.validateChannelAccess(
                channel,
                currentUser
        );

        return pinnedMessageRepository
                .findByMessage_Channel_IdAndMessage_DeletedFalseOrderByPinnedAtDesc(
                        channelId
                )
                .stream()
                .map(this::toResponse)
                .toList();

    }

    private User getCurrentUser() {

        String email = SecurityUtil.getCurrentUserEmail();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found."));

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
