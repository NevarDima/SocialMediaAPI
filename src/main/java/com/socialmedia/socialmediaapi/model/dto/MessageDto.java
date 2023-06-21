package com.socialmedia.socialmediaapi.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class MessageDto {
    private UUID uuid;
    private UUID fromUuid;
    private UUID toUuid;
    private String messageText;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
