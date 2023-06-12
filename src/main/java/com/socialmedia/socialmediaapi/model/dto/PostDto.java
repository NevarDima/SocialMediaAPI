package com.socialmedia.socialmediaapi.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data

public class PostDto {
    private UUID uuid;
    private String header;
    private String content;
    private String image;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
