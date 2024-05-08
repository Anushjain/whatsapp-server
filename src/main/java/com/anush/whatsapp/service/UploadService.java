package com.anush.whatsapp.service;

import com.anush.whatsapp.domain.Attachment;
import com.anush.whatsapp.repos.AttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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


    private final String attachmentPicturePath = "root/picture";
    private final String attachmentVideoPath = "root/video";
    private final String attachmentOtherPath = "root/other";


    private final String profilePicturePath = "root/profile";

    public Attachment uploadAttachment(MultipartFile file) {
        try {
            String uploadDirectory = file.getContentType().contains("image") ?
                    attachmentPicturePath : file.getContentType().contains("video") ?
                    attachmentVideoPath : attachmentOtherPath;
            if(file.getSize() > 10000000){
                throw new RuntimeException("File size should be less than 10MB");
            }
            UUID fileId = UUID.randomUUID();
            String fileName = fileId.toString() + "_" + file.getOriginalFilename();
            File uploadPath = new File(uploadDirectory);
            if (!uploadPath.exists()) {
                uploadPath.mkdirs();
            }
            File dest = new File(uploadPath.getAbsolutePath() + File.separator + fileName);
            file.transferTo(dest);


            Attachment attachment = new Attachment();
            attachment.setId(fileId);
            attachment.setType(file.getContentType());
            attachment.setUrl(dest.getPath());
            attachment.setSize(file.getSize());

            return attachmentRepository.save(attachment);

        } catch (IOException e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    public String uploadProfilePicture(MultipartFile file, UUID userId) {
        try {

            if(file.getSize() > 2000000){
                throw new RuntimeException("File size should be less than 2MB");
            }

            UUID fileId = UUID.randomUUID();
            String fileName = fileId.toString() + "_" + file.getOriginalFilename();
            File uploadPath = new File(profilePicturePath + File.separator + userId);
            if (!uploadPath.exists()) {
                uploadPath.mkdirs();
            }
            File dest = new File(uploadPath.getAbsolutePath() + File.separator + fileName);
            file.transferTo(dest);
            return dest.getPath();

        } catch (IOException e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }


    public Attachment getAttachmentById(UUID id) {
      return attachmentRepository.findById(id).orElseThrow(() -> new RuntimeException("File not found"));

    }
}