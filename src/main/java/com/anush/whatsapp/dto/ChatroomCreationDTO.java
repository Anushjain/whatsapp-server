package com.anush.whatsapp.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class ChatroomCreationDTO {
    private UUID userId;
    private String chatRoomName;
    private String profilePictureUrl;
    private Set<UUID> userIds;
}
