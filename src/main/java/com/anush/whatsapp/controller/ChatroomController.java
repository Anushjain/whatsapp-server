package com.anush.whatsapp.controller;

import com.anush.whatsapp.domain.Chatroom;
import com.anush.whatsapp.service.ChatroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;


@RestController
@RequestMapping("/api/chatroom")
public class ChatroomController {


    @Autowired
    ChatroomService chatroomService;

    @PostMapping
    public ResponseEntity<Chatroom> createChatroom(@RequestParam UUID userId, @RequestParam String chatroomName, @RequestBody Set<UUID> userIds) {
      Chatroom chatroom =  chatroomService.createChatroom(userId, chatroomName, userIds);
        return new ResponseEntity<>(chatroom,HttpStatus.CREATED);
    }

    @PutMapping("/{chatRoomId}")
    public ResponseEntity<Void> editChatroomName(@PathVariable UUID chatRoomId, @RequestParam UUID userId, @RequestParam String newChatroomName) {
        chatroomService.editChatroomName(chatRoomId, userId, newChatroomName);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/addUsers")
    public ResponseEntity<Void> addUsersToChatroom(@RequestParam UUID chatRoomId, @RequestParam UUID adminId, @RequestBody Set<UUID> userIds) {
        chatroomService.addUsersToChatroom(chatRoomId, adminId, userIds);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/removeUsers")
    public ResponseEntity<Void> removeUsersFromChatroom(@RequestParam UUID chatRoomId, @RequestParam UUID userId, @RequestBody Set<UUID> userIds) {
        chatroomService.removeUsersFromChatroom(chatRoomId, userId, userIds);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{chatRoomId}")
    public ResponseEntity<Void> deleteChatroom(@PathVariable UUID chatRoomId, @RequestParam UUID adminId) {
        chatroomService.deleteChatroom(chatRoomId, adminId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{chatRoomId}")
    public ResponseEntity<Chatroom> getChatroom(@PathVariable UUID chatRoomId) {
        Optional<Chatroom> chatroom = chatroomService.getChatroom(chatRoomId);
        return chatroom.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<Chatroom>> getChatroomsByUserId(@PathVariable UUID userId,
                                                               @RequestParam(value = "page", defaultValue = "0") int page,
                                                               @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<Chatroom> chatrooms = chatroomService.getChatroomsByUserId(userId, page, size);
        return new ResponseEntity<>(chatrooms, HttpStatus.OK);
    }

}
