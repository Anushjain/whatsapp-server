package com.anush.whatsapp.controller;


import com.anush.whatsapp.domain.Message;
import com.anush.whatsapp.dto.MessageDTO;
import com.anush.whatsapp.model.Reaction;
import com.anush.whatsapp.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/message")
public class MessageController {

    @Autowired
    MessageService messageService;
    @PostMapping("/send/{chatroomId}")
    @Operation(summary = "Send Message to Chatroom")
    public ResponseEntity<Void> sendMessage(@RequestBody MessageDTO message, @PathVariable UUID chatroomId) {
        messageService.sendMessage(message, chatroomId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{messageId}")
    @Operation(summary = "Get Message by Id")
    public ResponseEntity<Message> getMessage(@PathVariable UUID messageId) {
        Optional<Message> message = messageService.getMessage(messageId);
        return message.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/react")
    @Operation(summary = "React to Message")
    public ResponseEntity<Void> reactToMessage(@RequestParam UUID messageId, @RequestParam UUID senderId, @RequestParam UUID chatroomId, @RequestParam Reaction reaction) {
        messageService.reactToMessage(messageId, senderId, chatroomId, reaction);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{messageId}")
    @Operation(summary = "Delete Message")
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId, @RequestParam UUID userId) {
        messageService.deleteMessage(messageId, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/forward")
    @Operation(summary = "Forward Message")
    public ResponseEntity<Void> forwardMessage(@RequestParam UUID messageId, @RequestParam UUID senderId, @RequestParam UUID receiverId) {
        messageService.forwardMessage(messageId, senderId, receiverId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{messageId}")
    @Operation(summary = "Edit Message")
    public ResponseEntity<Void> editMessage(@PathVariable UUID messageId, @RequestParam UUID userId, @RequestParam String newMessage) {
        messageService.editMessage(messageId, userId, newMessage);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/chatroom/{chatroomId}")
    @Operation(summary = "Get Messages by Chatroom Id")
    public ResponseEntity<Page<Message>> getMessagesByChatroomId(@PathVariable UUID chatroomId, @RequestParam UUID userId,
                                                                 @RequestParam(value = "page", defaultValue = "0") int page,
                                                                 @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<Message> messages = messageService.getMessagesByChatroomId(chatroomId, page, size,userId);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

}
