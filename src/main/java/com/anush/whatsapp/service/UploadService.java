package com.anush.whatsapp.service;

import com.anush.whatsapp.domain.Attachment;
import com.anush.whatsapp.repos.AttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@Service
public class UploadService {

    @Autowired
    AttachmentRepository attachmentRepository;

    private final Path attachmentPicturePath = Paths.get("root/picture");
    private final Path attachmentVideoPath = Paths.get("root/video");
    private final Path attachmentOtherPath = Paths.get("root/other");


    private final Path profilePicturePath = Paths.get("root/profile");

    public Attachment uploadAttachment(MultipartFile file) {
        try {
            Path attachmentPath = null;
            if (Objects.requireNonNull(file.getContentType()).contains("image")) {
                attachmentPath = attachmentPicturePath;
            } else if (Objects.requireNonNull(file.getContentType()).contains("video")) {
                attachmentPath = attachmentVideoPath;
            } else {
                attachmentPath = attachmentOtherPath;
            }

            if (!Files.exists(attachmentPath)) {
                Files.createDirectory(attachmentPath);
            }
            if(file.getSize() > 10000000){
                throw new RuntimeException("File size should be less than 10MB");
            }
            UUID fileId = UUID.randomUUID();
            Path filePath = attachmentPath.resolve(fileId.toString());
            Files.copy(file.getInputStream(), filePath);
            Attachment attachment = new Attachment();
            attachment.setId(fileId);
            attachment.setType(file.getContentType());
            attachment.setUrl(filePath.toString());
            attachment.setSize(file.getSize());

            return attachmentRepository.save(attachment);

        } catch (IOException e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    public String uploadProfilePicture(MultipartFile file, UUID userId) {
        try {
            if (!Files.exists(profilePicturePath)) {
                Files.createDirectory(profilePicturePath);
            }
            if(file.getSize() > 2000000){
                throw new RuntimeException("File size should be less than 2MB");
            }

            Path filePath = profilePicturePath.resolve(userId.toString());
            Files.copy(file.getInputStream(), filePath);
            return filePath.toString();

        } catch (IOException e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }


    public Attachment getAttachmentById(UUID id) {
      return attachmentRepository.findById(id).orElseThrow(() -> new RuntimeException("File not found"));

    }
}