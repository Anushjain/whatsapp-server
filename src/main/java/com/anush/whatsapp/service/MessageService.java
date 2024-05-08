package com.anush.whatsapp.service;

import com.anush.whatsapp.domain.Message;
import com.anush.whatsapp.dto.MessageDTO;
import com.anush.whatsapp.model.Reaction;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface MessageService {

    public void sendMessage(MessageDTO message , UUID chatroomId);

    public Optional<Message> getMessage(UUID messageId);


    public void reactToMessage(UUID messageId, UUID senderId, UUID chatroomId, Reaction reaction);

    public void deleteMessage(UUID messageId, UUID userId);

    public void forwardMessage(UUID messageId, UUID senderId, UUID receiverId);

    public void editMessage(UUID messageId, UUID userId, String newMessage);

    Page<Message> getMessagesByChatroomId(UUID chatroomId, int page, int size, UUID userId);

}
