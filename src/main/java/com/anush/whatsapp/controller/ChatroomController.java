package com.anush.whatsapp.controller;

import com.anush.whatsapp.domain.Chatroom;
import com.anush.whatsapp.dto.ChatroomCreationDTO;
import com.anush.whatsapp.dto.ChatroomEditDTO;
import com.anush.whatsapp.service.ChatroomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(summary = "Create Chatroom")
    public ResponseEntity<Chatroom> createChatroom(@RequestBody ChatroomCreationDTO chatroomCreationDTO) {
      Chatroom chatroom =  chatroomService.createChatroom(chatroomCreationDTO.getUserId(), chatroomCreationDTO.getChatRoomName(), chatroomCreationDTO.getUserIds(),chatroomCreationDTO.getProfilePictureUrl());
        return new ResponseEntity<>(chatroom,HttpStatus.CREATED);
    }

    @PutMapping("/{chatRoomId}")
    @Operation(summary = "Edit Chatroom")
    public ResponseEntity<Void> editChatroomName(@PathVariable UUID chatRoomId, @RequestBody ChatroomEditDTO chatroomEditDTO) {
        chatroomService.editChatroomName(chatRoomId, chatroomEditDTO.getUserId(), chatroomEditDTO.getChatRoomName(),chatroomEditDTO.getProfilePictureUrl());
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/addUsers")
    @Operation(summary = "Add users to Chatroom")
    public ResponseEntity<Void> addUsersToChatroom(@RequestParam UUID chatRoomId, @RequestParam UUID adminId, @RequestBody Set<UUID> userIds) {
        chatroomService.addUsersToChatroom(chatRoomId, adminId, userIds);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/removeUsers")
    @Operation(summary = "Remove users from Chatroom")
    public ResponseEntity<Void> removeUsersFromChatroom(@RequestParam UUID chatRoomId, @RequestParam UUID userId, @RequestBody Set<UUID> userIds) {
        chatroomService.removeUsersFromChatroom(chatRoomId, userId, userIds);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{chatRoomId}")
    @Operation(summary = "Delete Chatroom")
    public ResponseEntity<Void> deleteChatroom(@PathVariable UUID chatRoomId, @RequestParam UUID adminId) {
        chatroomService.deleteChatroom(chatRoomId, adminId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{chatRoomId}")
    @Operation(summary = "Get Chatroom")
    public ResponseEntity<Chatroom> getChatroom(@PathVariable UUID chatRoomId) {
        Optional<Chatroom> chatroom = chatroomService.getChatroom(chatRoomId);
        return chatroom.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get Chatroom for User")
    public ResponseEntity<Page<Chatroom>> getChatroomsByUserId(@PathVariable UUID userId,
                                                               @RequestParam(value = "page", defaultValue = "0") int page,
                                                               @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<Chatroom> chatrooms = chatroomService.getChatroomsByUserId(userId, page, size);
        return new ResponseEntity<>(chatrooms, HttpStatus.OK);
    }

}
