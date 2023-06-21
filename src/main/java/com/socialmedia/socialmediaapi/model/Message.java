package com.socialmedia.socialmediaapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder
@Table(name = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message implements Comparable<Message>{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;
    private UUID fromUuid;
    private UUID toUuid;
    private String messageText;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Override
    public int compareTo(Message message) {
        return getCreatedAt().compareTo(message.getCreatedAt());
    }
}
