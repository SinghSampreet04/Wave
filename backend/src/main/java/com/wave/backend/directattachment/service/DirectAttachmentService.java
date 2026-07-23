package com.wave.backend.directattachment.service;

import com.wave.backend.common.util.SecurityUtil;
import com.wave.backend.directattachment.dto.DeleteDirectAttachmentResponse;
import com.wave.backend.directattachment.dto.DirectAttachmentDownloadResponse;
import com.wave.backend.directattachment.dto.DirectAttachmentResponse;
import com.wave.backend.directattachment.entity.DirectAttachment;
import com.wave.backend.directattachment.repository.DirectAttachmentRepository;
import com.wave.backend.directmessage.entity.Conversation;
import com.wave.backend.directmessage.entity.DirectMessage;
import com.wave.backend.directmessage.repository.DirectMessageRepository;
import com.wave.backend.exception.UserNotFoundException;
import com.wave.backend.user.entity.User;
import com.wave.backend.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DirectAttachmentService {

    private static final String UPLOAD_DIRECTORY =
            "uploads/direct-messages";

    private static final long MAX_FILE_SIZE =
            25 * 1024 * 1024;

    private static final Set<String> ALLOWED_CONTENT_TYPES =
            Set.of(
                    "image/png",
                    "image/jpeg",
                    "image/gif",
                    "image/webp",
                    "application/pdf",
                    "application/zip",
                    "application/x-zip-compressed",
                    "text/plain",
                    "application/msword",
                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
            );

    private final DirectAttachmentRepository attachmentRepository;
    private final DirectMessageRepository directMessageRepository;
    private final UserRepository userRepository;

    public DirectAttachmentService(
            DirectAttachmentRepository attachmentRepository,
            DirectMessageRepository directMessageRepository,
            UserRepository userRepository
    ) {
        this.attachmentRepository = attachmentRepository;
        this.directMessageRepository = directMessageRepository;
        this.userRepository = userRepository;
    }

    public DirectAttachmentResponse uploadAttachment(
            Long directMessageId,
            MultipartFile file
    ) throws IOException {

        User currentUser = getCurrentUser();

        validateFile(file);

        DirectMessage directMessage =
                directMessageRepository.findById(directMessageId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Direct message not found."
                                ));

        validateParticipant(
                currentUser,
                directMessage.getConversation()
        );
        validateMessageOwner(currentUser, directMessage);

        String originalFilename =
                StringUtils.cleanPath(file.getOriginalFilename());
        if (originalFilename.contains("..")) {
            throw new IllegalArgumentException("Invalid file name.");
        }

        String storedFilename =
                UUID.randomUUID() + "_" + originalFilename;

        Path uploadPath =
                Paths.get(UPLOAD_DIRECTORY);

        Files.createDirectories(uploadPath);

        Files.copy(
                file.getInputStream(),
                uploadPath.resolve(storedFilename)
        );

        DirectAttachment attachment =
                new DirectAttachment();

        attachment.setDirectMessage(directMessage);
        attachment.setOriginalFileName(originalFilename);
        attachment.setStoredFileName(storedFilename);
        attachment.setContentType(file.getContentType());
        attachment.setFileSize(file.getSize());
        attachment.setFilePath(
                uploadPath.resolve(storedFilename).toString()
        );

        attachment = attachmentRepository.save(
                attachment
        );

        return new DirectAttachmentResponse(
                attachment.getId(),
                directMessage.getId(),
                attachment.getOriginalFileName(),
                attachment.getContentType(),
                attachment.getFileSize(),
                attachment.getUploadedAt()
        );

    }

    public DirectAttachmentDownloadResponse downloadAttachment(
            Long attachmentId
    ) throws IOException {

        User currentUser = getCurrentUser();

        DirectAttachment attachment =
                attachmentRepository.findById(attachmentId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Attachment not found."
                                ));

        validateParticipant(
                currentUser,
                attachment.getDirectMessage().getConversation()
        );

        Path filePath =
                Paths.get(attachment.getFilePath());

        byte[] data =
                Files.readAllBytes(filePath);

        return new DirectAttachmentDownloadResponse(
                attachment.getOriginalFileName(),
                attachment.getContentType(),
                data
        );

    }

    @Transactional(readOnly = true)
    public List<DirectAttachmentResponse> getMessageAttachments(
            Long directMessageId
    ) {
        User currentUser = getCurrentUser();
        DirectMessage directMessage =
                directMessageRepository.findById(directMessageId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Direct message not found."
                                ));
        validateParticipant(currentUser, directMessage.getConversation());
        return attachmentRepository.findByDirectMessage(directMessage)
                .stream()
                .map(attachment -> new DirectAttachmentResponse(
                        attachment.getId(),
                        directMessage.getId(),
                        attachment.getOriginalFileName(),
                        attachment.getContentType(),
                        attachment.getFileSize(),
                        attachment.getUploadedAt()
                ))
                .toList();
    }

    public DeleteDirectAttachmentResponse deleteAttachment(
            Long attachmentId
    ) throws IOException {

        User currentUser = getCurrentUser();

        DirectAttachment attachment =
                attachmentRepository.findById(attachmentId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Attachment not found."
                                ));

        validateParticipant(
                currentUser,
                attachment.getDirectMessage().getConversation()
        );
        validateMessageOwner(currentUser, attachment.getDirectMessage());

        Files.deleteIfExists(
                Paths.get(attachment.getFilePath())
        );

        attachmentRepository.delete(
                attachment
        );

        return new DeleteDirectAttachmentResponse(
                attachmentId,
                "Attachment deleted successfully."
        );

    }

    private User getCurrentUser() {

        String email =
                SecurityUtil.getCurrentUserEmail();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found."
                        ));

    }

    private void validateParticipant(
            User currentUser,
            Conversation conversation
    ) {

        boolean participant =
                conversation.getUserOne().getId().equals(currentUser.getId())
                        || conversation.getUserTwo().getId().equals(currentUser.getId());

        if (!participant) {

            throw new IllegalArgumentException(
                    "Access denied."
            );

        }

    }

    private void validateMessageOwner(
            User currentUser,
            DirectMessage directMessage
    ) {
        if (!directMessage.getSender().getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException(
                    "Only the message author can change attachments."
            );
        }
    }

    private void validateFile(
            MultipartFile file
    ) {

        if (file == null || file.isEmpty()) {

            throw new IllegalArgumentException(
                    "File cannot be empty."
            );

        }

        if (file.getSize() > MAX_FILE_SIZE) {

            throw new IllegalArgumentException(
                    "Maximum allowed file size is 25 MB."
            );

        }

        String contentType =
                file.getContentType();

        if (contentType == null
                || !ALLOWED_CONTENT_TYPES.contains(contentType)) {

            throw new IllegalArgumentException(
                    "Unsupported file type."
            );

        }

    }

}
