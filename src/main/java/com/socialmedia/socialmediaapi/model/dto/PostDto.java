package com.socialmedia.socialmediaapi.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data

public class PostDto {
    private long id;
    private String header;
    private String content;
    private String image;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
