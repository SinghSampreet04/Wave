package com.wave.backend.receipt.service;

import com.wave.backend.common.event.MessageReadEvent;
import com.wave.backend.common.util.SecurityUtil;
import com.wave.backend.message.entity.Message;
import com.wave.backend.message.repository.MessageRepository;
import com.wave.backend.receipt.dto.CreateReadReceiptRequest;
import com.wave.backend.receipt.dto.ReadReceiptResponse;
import com.wave.backend.receipt.entity.MessageRead;
import com.wave.backend.receipt.repository.MessageReadRepository;
import com.wave.backend.user.entity.User;
import com.wave.backend.user.repository.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReadReceiptService {

    private final MessageReadRepository messageReadRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    public ReadReceiptService(
            MessageReadRepository messageReadRepository,
            MessageRepository messageRepository,
            UserRepository userRepository,
            ApplicationEventPublisher eventPublisher
    ) {
        this.messageReadRepository = messageReadRepository;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
    }

    public ReadReceiptResponse markAsRead(
            CreateReadReceiptRequest request
    ) {

        String email = SecurityUtil.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found."));

        Message message = messageRepository.findById(request.getMessageId())
                .orElseThrow(() ->
                        new RuntimeException("Message not found."));

        return messageReadRepository
                .findByMessageAndUser(message, user)
                .map(existing -> new ReadReceiptResponse(
                        message.getId(),
                        user.getId(),
                        user.getUsername(),
                        existing.getReadAt()
                ))
                .orElseGet(() -> {

                    MessageRead messageRead = new MessageRead();
                    messageRead.setMessage(message);
                    messageRead.setUser(user);

                    messageRead = messageReadRepository.save(messageRead);

                    eventPublisher.publishEvent(
                            new MessageReadEvent(
                                    message.getId(),
                                    message.getChannel().getId(),
                                    user.getId(),
                                    user.getUsername()
                            )
                    );

                    return new ReadReceiptResponse(
                            message.getId(),
                            user.getId(),
                            user.getUsername(),
                            messageRead.getReadAt()
                    );

                });
    }

}