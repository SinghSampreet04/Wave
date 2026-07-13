package com.wave.backend.file.service;

import com.wave.backend.exception.MessageNotFoundException;
import com.wave.backend.file.dto.FileDownloadResponse;
import com.wave.backend.file.dto.FileUploadResponse;
import com.wave.backend.file.entity.FileAttachment;
import com.wave.backend.file.repository.FileAttachmentRepository;
import com.wave.backend.message.entity.Message;
import com.wave.backend.message.repository.MessageRepository;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class FileAttachmentService {

    private final FileAttachmentRepository fileAttachmentRepository;
    private final MessageRepository messageRepository;
    private final FileStorageService fileStorageService;

    public FileAttachmentService(
            FileAttachmentRepository fileAttachmentRepository,
            MessageRepository messageRepository,
            FileStorageService fileStorageService
    ) {
        this.fileAttachmentRepository = fileAttachmentRepository;
        this.messageRepository = messageRepository;
        this.fileStorageService = fileStorageService;
    }

    public FileUploadResponse uploadFile(
            Long messageId,
            MultipartFile file
    ) {

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() ->
                        new MessageNotFoundException("Message not found."));

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

        return new FileUploadResponse(
                attachment.getId(),
                attachment.getOriginalFilename(),
                attachment.getStoredFilename(),
                attachment.getContentType(),
                attachment.getFileSize()
        );
    }

    public FileDownloadResponse getFileInfo(Long fileId) {

        FileAttachment attachment = fileAttachmentRepository.findById(fileId)
                .orElseThrow(() ->
                        new RuntimeException("File not found."));

        return new FileDownloadResponse(
                attachment.getId(),
                attachment.getOriginalFilename(),
                attachment.getContentType(),
                attachment.getFileSize()
        );
    }

    public Resource downloadFile(Long fileId) {

        FileAttachment attachment = fileAttachmentRepository.findById(fileId)
                .orElseThrow(() ->
                        new RuntimeException("File not found."));

        return fileStorageService.loadFile(
                attachment.getStoredFilename()
        );
    }

    public FileAttachment getAttachment(Long fileId) {

        return fileAttachmentRepository.findById(fileId)
                .orElseThrow(() ->
                        new RuntimeException("File not found."));
    }

    public List<FileAttachment> getFilesForMessage(Long messageId) {

        messageRepository.findById(messageId)
                .orElseThrow(() ->
                        new MessageNotFoundException("Message not found."));

        return fileAttachmentRepository.findByMessageId(messageId);
    }

}