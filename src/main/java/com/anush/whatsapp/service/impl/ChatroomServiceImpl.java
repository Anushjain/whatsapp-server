package com.anush.whatsapp.service.impl;

import com.anush.whatsapp.domain.User;
import com.anush.whatsapp.repos.ChatroomRepository;
import com.anush.whatsapp.service.ChatroomService;
import com.anush.whatsapp.domain.Chatroom;
import com.anush.whatsapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class ChatroomServiceImpl implements ChatroomService {

    @Autowired
    private ChatroomRepository chatroomRepository;
    @Autowired
    private UserService userService;
    @Override
    public void addUsersToChatroom(UUID chatRoomId, UUID adminId, Set<UUID> userIds) {
        getChatroom(chatRoomId).ifPresent(chatroom -> {
            if(chatroom.getAdminId().equals(adminId)) {
                userIds.forEach(userId -> {
                    User user = userService.getUserById(userId).orElseThrow();
                    chatroom.getUsers().add(user);
                });
                chatroomRepository.save(chatroom);
            }
        });
    }

    @Override
        public void removeUsersFromChatroom (UUID chatRoomId,UUID adminId, Set<UUID> userIds) {

          getChatroom(chatRoomId).ifPresent(chatroom -> {
           if(chatroom.getAdminId().equals(adminId)){
               userIds.forEach(id -> {
                   User user = userService.getUserById(id).orElseThrow();
                   chatroom.getUsers().remove(user);
               });
           }else{
                throw new RuntimeException("Chatroom not found or you are not the admin of the chatroom");
           }
        });
    }

    @Override
    public void deleteChatroom(UUID chatRoomId, UUID adminId) {

        if(getChatroom(chatRoomId).isPresent() &&
                getChatroom(chatRoomId).get().getAdminId().equals(adminId))
        chatroomRepository.deleteById(chatRoomId);
        else{
            throw new RuntimeException("Chatroom not found or you are not the admin of the chatroom");
        }
    }

    @Override
    public Chatroom createChatroom(UUID userId, String chatroomName, Set<UUID> userIds, String profilePictureUrl) {
       User admin =  userService.getUserById(userId).orElseThrow();
        Chatroom chatroom = new Chatroom();
        chatroom.setName(chatroomName);
        chatroom.setAdminId(userId);
        chatroom.setProfileImageUrl(profilePictureUrl);
        chatroom.setUsers(new HashSet<>());
        chatroom.setMessages(new HashSet<>());
        chatroom.getUsers().add(admin);

        userIds.forEach(id -> {
            User user = userService.getUserById(id).orElseThrow();
            chatroom.getUsers().add(user);
        });
        return chatroomRepository.save(chatroom);
    }

    @Override
    public void editChatroomName(UUID chatRoomId, UUID userId, String newChatroomName, String profilePictureUrl) {
        getChatroom(chatRoomId).ifPresent(chatroom -> {
            if(chatroom.getAdminId().equals(userId)){
                chatroom.setName(newChatroomName);
                chatroom.setProfileImageUrl(profilePictureUrl);
                chatroomRepository.save(chatroom);
            }
        });
    }

    @Override
    public Optional<Chatroom> getChatroom(UUID chatRoomId) {
        return chatroomRepository.findById(chatRoomId);
    }

    @Override
    public Page<Chatroom> getChatroomsByUserId(UUID userId, int page, int size) {
        return chatroomRepository.findByUsersIdOrderByLastMessageTimeDesc(userId, PageRequest.of(page, size));
    }

    @Override
    public void updateChatroom(Chatroom chatroom) {
        chatroomRepository.save(chatroom);
    }
}
