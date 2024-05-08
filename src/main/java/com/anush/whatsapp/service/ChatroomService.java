package com.anush.whatsapp.service;

import com.anush.whatsapp.domain.Chatroom;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface ChatroomService {


    void addUsersToChatroom(UUID chatRoomId, UUID adminId, Set<UUID> userIds);

    void removeUsersFromChatroom(UUID chatRoomId, UUID userId, Set<UUID> userIds);


    void deleteChatroom(UUID chatRoomId, UUID adminId);

    Chatroom createChatroom(UUID userId, String chatroomName, Set<UUID> userIds, String profilePictureUrl);

    void editChatroomName(UUID chatRoomId, UUID userId, String newChatroomName, String profilePictureUrl);

    Optional<Chatroom> getChatroom(UUID chatRoomId);

    Page<Chatroom> getChatroomsByUserId(UUID userId , int page, int size);

    void updateChatroom(Chatroom chatroom);


}
