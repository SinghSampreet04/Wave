package com.wave.backend.file.controller;

import com.wave.backend.file.dto.FileDownloadResponse;
import com.wave.backend.file.dto.FileUploadResponse;
import com.wave.backend.file.entity.FileAttachment;
import com.wave.backend.file.service.FileAttachmentService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/files")
public class FileController {

    private final FileAttachmentService fileAttachmentService;

    public FileController(
            FileAttachmentService fileAttachmentService
    ) {
        this.fileAttachmentService = fileAttachmentService;
    }

    @PostMapping(
            value = "/upload/{messageId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public FileUploadResponse uploadFile(
            @PathVariable Long messageId,
            @RequestPart("file") MultipartFile file
    ) {

        return fileAttachmentService.uploadFile(
                messageId,
                file
        );

    }

    @GetMapping("/{fileId}")
    public FileDownloadResponse getFileInfo(
            @PathVariable Long fileId
    ) {

        return fileAttachmentService.getFileInfo(fileId);

    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable Long fileId
    ) {

        Resource resource =
                fileAttachmentService.downloadFile(fileId);

        FileAttachment attachment =
                fileAttachmentService.getAttachment(fileId);

        return ResponseEntity.ok()
                .contentType(
                        MediaType.parseMediaType(
                                attachment.getContentType()
                        )
                )
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" +
                                attachment.getOriginalFilename() +
                                "\""
                )
                .body(resource);

    }

    @GetMapping("/message/{messageId}")
    public List<FileDownloadResponse> getFilesForMessage(
            @PathVariable Long messageId
    ) {

        return fileAttachmentService.getFilesForMessage(
                messageId
        );

    }

}
