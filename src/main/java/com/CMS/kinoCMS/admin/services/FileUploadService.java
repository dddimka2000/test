package com.CMS.kinoCMS.admin.services;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Log4j2
public class FileUploadService {
    @Value("${upload.path}")
    private String uploadPath;

    public String uploadFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            log.error("File is empty or null");
            throw new IllegalArgumentException("File is empty or null");
        }
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            boolean dirCreated = uploadDir.mkdir();
            if (dirCreated) {
                log.info("Created upload directory: {}", uploadPath);
            } else {
                log.error("Failed to create upload directory: {}", uploadPath);
            }
        }
        String uuidFile = UUID.randomUUID().toString();
        String originalFilename = Objects.requireNonNull(file.getOriginalFilename()).replaceAll("[\\s()]", "");
        String resultFilename = uuidFile + "." + originalFilename;
        File destinationFile = new File(uploadPath + "/" + resultFilename);
        try {
            file.transferTo(destinationFile);
            log.info("Successfully uploaded file: {} to {}", originalFilename, destinationFile.getAbsolutePath());
        } catch (IOException e) {
            log.error("Failed to upload file: {} to {}", originalFilename, destinationFile.getAbsolutePath(), e);
            throw e;
        }

        return resultFilename;
    }

    public List<String> uploadAdditionalFiles(MultipartFile[] files) throws IOException {
        if (files == null || files.length == 0) {
            log.error("Files array is empty");
            throw new IllegalArgumentException("Files is empty");
        }

        List<String> newImageNames = new ArrayList<>();
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    String resultFilename = uploadFile(file);
                    newImageNames.add(resultFilename);
                } catch (IOException e) {
                    log.error("Failed to upload file: {}", file.getOriginalFilename(), e);
                }
            }
        }
        return newImageNames;
    }
}
