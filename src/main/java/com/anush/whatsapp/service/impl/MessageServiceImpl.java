package com.anush.whatsapp.service.impl;

import com.anush.whatsapp.domain.Attachment;
import com.anush.whatsapp.domain.Chatroom;
import com.anush.whatsapp.domain.Message;
import com.anush.whatsapp.domain.User;
import com.anush.whatsapp.dto.MessageDTO;
import com.anush.whatsapp.model.Reaction;
import com.anush.whatsapp.service.*;
import com.anush.whatsapp.repos.MessageRepository;
import com.anush.whatsapp.service.kafka.KafkaProducerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class MessageServiceImpl  implements MessageService {

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    ChatroomService chatroomService;

    @Autowired
    UserService userService;

    @Autowired
    UploadService uploadService;

    @Autowired
    KafkaProducerService kafkaProducerService;



    @Override
    public void sendMessage(MessageDTO messageDto,UUID chatroomId) {
        Message message = createMessage(messageDto, chatroomId);

        sendMessage(message);

    }

    @Override
    public Optional<Message> getMessage(UUID messageId) {
        return messageRepository.findById(messageId);
    }


    @Override
    public void reactToMessage(UUID messageId, UUID senderId, UUID chatroomId, Reaction reaction) {
       Message message = messageRepository.findById(messageId).orElseThrow();
       message.getReactions().add(reaction);
       messageRepository.save(message);
    }

    @Override
    public void deleteMessage(UUID messageId, UUID userId) {
      Message message = messageRepository.findById(messageId).orElseThrow();
         messageRepository.delete(message);
    }

    @Override
    public void forwardMessage(UUID messageId, UUID senderId, UUID receiverId) {
        Message originalMessage = messageRepository.findById(messageId).orElseThrow();
        User sender = userService.getUserById(senderId).orElseThrow();
        Chatroom chatroom = chatroomService.getChatroom(receiverId).orElseThrow();
        if(chatroom.getUsers().contains(sender)){
            throw new RuntimeException("User is not in the chatroom");
        }
        Message forwardedMessage = new Message();
        forwardedMessage.setChatroom(chatroom);
        forwardedMessage.setSender(sender);
        forwardedMessage.setContent(originalMessage.getContent());
        forwardedMessage.setAttachment(originalMessage.getAttachment());

        chatroom.setLastMessageTime(OffsetDateTime.now());
        chatroomService.updateChatroom(chatroom);

        sendMessage(forwardedMessage);

        }

    @Override
    public void editMessage(UUID messageId, UUID userId, String newMessage) {
        Message message = messageRepository.findById(messageId).orElseThrow();
        if(!message.getSender().getId().equals(userId)){
            throw new RuntimeException("User is not the sender of the message");
        }
        message.setContent(newMessage);
        messageRepository.save(message);
    }

    @Override
    public Page<Message> getMessagesByChatroomId(UUID chatroomId, int page, int size, UUID userId) {
        Optional<Chatroom> chatroom = chatroomService.getChatroom(chatroomId);
        Optional<User> user = userService.getUserById(userId);
        if (chatroom.isEmpty()) {
            throw new RuntimeException("Chatroom is empty");
        }else
            if (chatroom.get().getUsers().contains(user.orElseThrow())) {
                throw new RuntimeException("User is not in the chatroom");
        }
        return messageRepository.findByChatroomIdOrderByDateCreatedDesc(chatroomId, PageRequest.of(page, size));
    }

    private Message createMessage(MessageDTO messageDTO, UUID chatroomId){

        User sender = userService.getUserById(UUID.fromString(messageDTO.getSenderId())).orElseThrow();
        Chatroom chatroom = chatroomService.getChatroom(chatroomId).orElseThrow();

        if(chatroom.getUsers().contains(sender)){
            throw new RuntimeException("User is not in the chatroom");
        }
        Message message = new Message();
        if(messageDTO.getAttachmentId() != null){
              Attachment attachment = uploadService.getAttachmentById(UUID.fromString(messageDTO.getAttachmentId()));
            message.setAttachment(attachment);
        }

        message.setContent(messageDTO.getMessage());
        message.setSender(sender);
        message.setChatroom(chatroom);
        chatroom.setLastMessageTime(OffsetDateTime.now());
        chatroomService.updateChatroom(chatroom);
        return message;
    }

    private void sendMessage(Message message){
        ObjectMapper obj = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
        try {
            String jsonStr = obj.writeValueAsString(message);
            kafkaProducerService.sendMessage(jsonStr);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }


}
