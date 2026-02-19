package com.socialmedia.backend.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePostDTO {
    private Long userId;       // frontend sends the user's id
    private String title;
    private String description;
    private String contentUrl;
}

