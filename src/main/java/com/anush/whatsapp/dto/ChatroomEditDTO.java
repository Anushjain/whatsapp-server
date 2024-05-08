package com.anush.whatsapp.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class ChatroomEditDTO {
    private UUID userId;
    private String chatRoomName;
    private String profilePictureUrl;
}
