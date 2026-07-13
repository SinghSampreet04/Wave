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

@Service
public class FileStorageService {

    @Value("${wave.upload-dir:uploads}")
    private String uploadDirectory;

    public String storeFile(MultipartFile file) {

        try {

            Path uploadPath = Paths.get(uploadDirectory);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFilename =
                    StringUtils.cleanPath(file.getOriginalFilename());

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

}