package com.socialmedia.backend.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateChatDTO {
    private Long user1Id;
    private Long user2Id;
}