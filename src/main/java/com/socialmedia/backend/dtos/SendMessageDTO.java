package com.socialmedia.backend.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendMessageDTO {
    private Long senderId;
    private String content;
}