package com.wave.backend.file.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.Set;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class FileStorageService {

    private static final Logger log =
            LoggerFactory.getLogger(FileStorageService.class);
    private static final long MAX_FILE_SIZE = 25 * 1024 * 1024;
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/png", "image/jpeg", "image/gif", "image/webp",
            "application/pdf", "application/zip", "application/x-zip-compressed",
            "text/plain", "text/csv", "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/vnd.ms-excel",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    );

    @Value("${wave.upload-dir:uploads}")
    private String uploadDirectory;

    public String storeFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty.");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("Maximum allowed file size is 25 MB.");
        }
        if (file.getContentType() == null
                || !ALLOWED_CONTENT_TYPES.contains(file.getContentType())) {
            throw new IllegalArgumentException("Unsupported file type.");
        }

        try {

            Path uploadPath = Paths.get(uploadDirectory);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFilename =
                    StringUtils.cleanPath(file.getOriginalFilename());
            if (originalFilename.contains("..")) {
                throw new IllegalArgumentException("Invalid file name.");
            }

            String extension = "";

            int index = originalFilename.lastIndexOf(".");

            if (index > 0) {
                extension = originalFilename.substring(index);
            }

            String storedFilename =
                    UUID.randomUUID() + extension;

            Path target =
                    uploadPath.resolve(storedFilename);

            Files.copy(
                    file.getInputStream(),
                    target,
                    StandardCopyOption.REPLACE_EXISTING
            );

            return storedFilename;

        } catch (IOException ex) {
            throw new RuntimeException(
                    "Could not store uploaded file.",
                    ex
            );
        }
    }

    public Resource loadFile(String storedFilename) {

        try {

            Path file = Paths.get(uploadDirectory)
                    .resolve(storedFilename)
                    .normalize();

            Resource resource = new UrlResource(file.toUri());

            if (!resource.exists()) {
                throw new RuntimeException("File not found.");
            }

            return resource;

        } catch (MalformedURLException ex) {
            throw new RuntimeException(
                    "Could not load file.",
                    ex
            );
        }
    }

    public void deleteFiles(
            Collection<String> storedFilenames
    ) {
        Path uploadPath = Paths.get(uploadDirectory)
                .toAbsolutePath()
                .normalize();

        for (String storedFilename : storedFilenames) {
            if (storedFilename == null || storedFilename.isBlank()) {
                continue;
            }

            Path target = uploadPath
                    .resolve(storedFilename)
                    .normalize();

            if (!target.getParent().equals(uploadPath)) {
                log.warn(
                        "Skipped unsafe stored file path: {}",
                        storedFilename
                );
                continue;
            }

            try {
                Files.deleteIfExists(target);
            } catch (IOException ex) {
                log.error(
                        "Could not delete stored file {}",
                        storedFilename,
                        ex
                );
            }
        }
    }

}
