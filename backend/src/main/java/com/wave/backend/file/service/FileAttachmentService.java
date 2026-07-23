package com.wave.backend.file.service;

import com.wave.backend.channelmember.service.ChannelMemberService;
import com.wave.backend.common.util.SecurityUtil;
import com.wave.backend.exception.MessageNotFoundException;
import com.wave.backend.exception.UserNotFoundException;
import com.wave.backend.file.dto.FileDownloadResponse;
import com.wave.backend.file.dto.FileUploadResponse;
import com.wave.backend.file.entity.FileAttachment;
import com.wave.backend.file.repository.FileAttachmentRepository;
import com.wave.backend.message.entity.Message;
import com.wave.backend.message.repository.MessageRepository;
import com.wave.backend.metrics.service.MetricsService;
import com.wave.backend.user.entity.User;
import com.wave.backend.user.repository.UserRepository;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
public class FileAttachmentService {

    private final FileAttachmentRepository fileAttachmentRepository;
    private final MessageRepository messageRepository;
    private final FileStorageService fileStorageService;
    private final UserRepository userRepository;
    private final ChannelMemberService channelMemberService;
    private final MetricsService metricsService;

    public FileAttachmentService(
            FileAttachmentRepository fileAttachmentRepository,
            MessageRepository messageRepository,
            FileStorageService fileStorageService,
            UserRepository userRepository,
            ChannelMemberService channelMemberService,
            MetricsService metricsService
    ) {
        this.fileAttachmentRepository = fileAttachmentRepository;
        this.messageRepository = messageRepository;
        this.fileStorageService = fileStorageService;
        this.userRepository = userRepository;
        this.channelMemberService = channelMemberService;
        this.metricsService = metricsService;
    }

    public FileUploadResponse uploadFile(
            Long messageId,
            MultipartFile file
    ) {

        User currentUser = getCurrentUser();

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() ->
                        new MessageNotFoundException("Message not found."));

        channelMemberService.validateChannelAccess(
                message.getChannel(),
                currentUser
        );

        String storedFilename =
                fileStorageService.storeFile(file);

        FileAttachment attachment = new FileAttachment();

        attachment.setMessage(message);
        attachment.setOriginalFilename(file.getOriginalFilename());
        attachment.setStoredFilename(storedFilename);
        attachment.setContentType(file.getContentType());
        attachment.setFileSize(file.getSize());
        attachment.setStoragePath("uploads/" + storedFilename);

        attachment = fileAttachmentRepository.save(attachment);

        metricsService.incrementFileUpload();

        return new FileUploadResponse(
                attachment.getId(),
                attachment.getOriginalFilename(),
                attachment.getStoredFilename(),
                attachment.getContentType(),
                attachment.getFileSize()
        );

    }

    public FileDownloadResponse getFileInfo(
            Long fileId
    ) {

        User currentUser = getCurrentUser();

        FileAttachment attachment = getAttachment(fileId);

        channelMemberService.validateChannelAccess(
                attachment.getMessage().getChannel(),
                currentUser
        );

        return new FileDownloadResponse(
                attachment.getId(),
                attachment.getOriginalFilename(),
                attachment.getContentType(),
                attachment.getFileSize()
        );

    }

    public Resource downloadFile(
            Long fileId
    ) {

        User currentUser = getCurrentUser();

        FileAttachment attachment = getAttachment(fileId);

        channelMemberService.validateChannelAccess(
                attachment.getMessage().getChannel(),
                currentUser
        );

        return fileStorageService.loadFile(
                attachment.getStoredFilename()
        );

    }

    public FileAttachment getAttachment(
            Long fileId
    ) {

        return fileAttachmentRepository.findById(fileId)
                .orElseThrow(() ->
                        new RuntimeException("File not found."));

    }

    public List<FileDownloadResponse> getFilesForMessage(
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

        return fileAttachmentRepository.findByMessageId(messageId)
                .stream()
                .map(attachment -> new FileDownloadResponse(
                        attachment.getId(),
                        attachment.getOriginalFilename(),
                        attachment.getContentType(),
                        attachment.getFileSize()
                ))
                .toList();

    }

    private User getCurrentUser() {

        String email = SecurityUtil.getCurrentUserEmail();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found."));

    }

}
