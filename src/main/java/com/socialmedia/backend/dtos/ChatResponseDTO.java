package com.socialmedia.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponseDTO {
    private Long chatId;
    private Long otherUserId;
    private String otherUsername;
    private String otherFirstName;
    private String otherLastName;
    private String otherEmail;
    private String otherBio;
    private String otherProfileImage;
}