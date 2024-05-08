package com.anush.whatsapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDTO {

    private String message;
    private String attachmentId;
    private String senderId;

}
