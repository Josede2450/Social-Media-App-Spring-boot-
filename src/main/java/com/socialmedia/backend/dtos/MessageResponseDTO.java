package com.socialmedia.backend.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class MessageResponseDTO {
    private Long messageId;
    private Long senderId;
    private String senderUsername;
    private String senderFirstName;
    private String senderLastName;
    private String content;
    private LocalDateTime createdDate;
}