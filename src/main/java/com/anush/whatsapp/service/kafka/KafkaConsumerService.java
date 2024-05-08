package com.anush.whatsapp.service.kafka;

import com.anush.whatsapp.domain.Message;
import com.anush.whatsapp.repos.MessageRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @Autowired
    MessageRepository messageRepository;
    @KafkaListener(id = "myConsumer",topics = "message_whatsapp_test", groupId = "whatsapp")
    public void consume(String value) {
        System.out.println("Consumed message: " + value);
        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
        try {
            Message message =    mapper.readValue(value, Message.class);
            messageRepository.save(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}