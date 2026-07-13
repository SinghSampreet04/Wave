package com.wave.backend.directattachment.controller;

import com.wave.backend.directattachment.dto.DeleteDirectAttachmentResponse;
import com.wave.backend.directattachment.dto.DirectAttachmentDownloadResponse;
import com.wave.backend.directattachment.dto.DirectAttachmentResponse;
import com.wave.backend.directattachment.service.DirectAttachmentService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/direct-attachments")
public class DirectAttachmentController {

    private final DirectAttachmentService directAttachmentService;

    public DirectAttachmentController(
            DirectAttachmentService directAttachmentService
    ) {
        this.directAttachmentService = directAttachmentService;
    }

    @PostMapping(
            value = "/{directMessageId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public DirectAttachmentResponse uploadAttachment(
            @PathVariable Long directMessageId,
            @RequestPart("file") MultipartFile file
    ) throws IOException {

        return directAttachmentService.uploadAttachment(
                directMessageId,
                file
        );

    }

    @GetMapping("/{attachmentId}")
    public ResponseEntity<ByteArrayResource> downloadAttachment(
            @PathVariable Long attachmentId
    ) throws IOException {

        DirectAttachmentDownloadResponse response =
                directAttachmentService.downloadAttachment(
                        attachmentId
                );

        ByteArrayResource resource =
                new ByteArrayResource(response.getData());

        return ResponseEntity.ok()
                .contentType(
                        MediaType.parseMediaType(
                                response.getContentType()
                        )
                )
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" +
                                response.getOriginalFileName() +
                                "\""
                )
                .contentLength(
                        response.getData().length
                )
                .body(resource);

    }

    @DeleteMapping("/{attachmentId}")
    public DeleteDirectAttachmentResponse deleteAttachment(
            @PathVariable Long attachmentId
    ) throws IOException {

        return directAttachmentService.deleteAttachment(
                attachmentId
        );

    }

}