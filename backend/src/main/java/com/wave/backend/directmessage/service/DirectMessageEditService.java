package com.wave.backend.directmessage.service;

import com.wave.backend.common.util.SecurityUtil;
import com.wave.backend.directmessage.dto.DirectMessageEditResponse;
import com.wave.backend.directmessage.dto.UpdateDirectMessageRequest;
import com.wave.backend.directmessage.entity.DirectMessage;
import com.wave.backend.directmessage.repository.DirectMessageRepository;
import com.wave.backend.exception.UserNotFoundException;
import com.wave.backend.user.entity.User;
import com.wave.backend.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class DirectMessageEditService {

    private final DirectMessageRepository directMessageRepository;
    private final UserRepository userRepository;

    public DirectMessageEditService(
            DirectMessageRepository directMessageRepository,
            UserRepository userRepository
    ) {
        this.directMessageRepository = directMessageRepository;
        this.userRepository = userRepository;
    }

    public DirectMessageEditResponse editMessage(
            Long messageId,
            UpdateDirectMessageRequest request
    ) {

        String email = SecurityUtil.getCurrentUserEmail();

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found."));

        DirectMessage message = directMessageRepository
                .findById(messageId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Direct message not found."
                        ));

        if (!message.getSender().getId().equals(currentUser.getId())) {

            throw new IllegalArgumentException(
                    "You can only edit your own messages."
            );

        }

        if (message.isDeleted()) {

            throw new IllegalArgumentException(
                    "Deleted messages cannot be edited."
            );

        }

        message.setContent(request.getContent());
        message.setEdited(true);

        message = directMessageRepository.save(message);

        return new DirectMessageEditResponse(
                message.getId(),
                message.getContent(),
                message.isEdited(),
                message.getUpdatedAt()
        );

    }

}