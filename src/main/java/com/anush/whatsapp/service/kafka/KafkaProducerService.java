package com.anush.whatsapp.service.kafka;

import com.anush.whatsapp.domain.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.concurrent.CompletableFuture;


@Service
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, String > kafkaTemplate;

    public void sendMessage( String message) {


        CompletableFuture<SendResult<String,String>> future =  kafkaTemplate.send("message_whatsapp_test",message, message);

        future.whenComplete((result, ex) -> {
            if (ex == null) {  
               System.out.printf("Produced event to topic %s: key = %-10s value = %s%n", result.getRecordMetadata().topic(), message, message);
            } else {
                ex.printStackTrace(System.out);
            }
        });
    }
}