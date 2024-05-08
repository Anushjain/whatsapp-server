package com.anush.whatsapp.controller;


import com.anush.whatsapp.domain.Attachment;
import com.anush.whatsapp.service.UploadService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
public class UploadController {

    @Autowired
    UploadService uploadService;
    @PostMapping(value ="/attachment" , consumes = {"multipart/form-data"})
    @Operation(summary = "Upload Attachment")
    public ResponseEntity<Attachment> uploadAttachment(@RequestParam("file") MultipartFile file) {
        Attachment attachment = uploadService.uploadAttachment(file);
        return new ResponseEntity<>(attachment, HttpStatus.CREATED);
    }

    @PostMapping(value = "/profilePicture/{userId}" ,consumes = {"multipart/form-data"})
    @Operation(summary = "Upload Profile Picture")
    public ResponseEntity<String> uploadProfilePicture(@RequestParam("file") MultipartFile file,@PathVariable UUID userId) {
        String profilePictureUrl = uploadService.uploadProfilePicture(file, userId);
        return new ResponseEntity<>(profilePictureUrl, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Attachment by Id")
    public ResponseEntity<Attachment> getAttachmentById(@PathVariable UUID id) {
        Attachment attachment = uploadService.getAttachmentById(id);
        return new ResponseEntity<>(attachment, HttpStatus.OK);
    }
}
