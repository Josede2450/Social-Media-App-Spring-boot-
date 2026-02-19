package com.socialmedia.backend.dtos;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class PostResponseDTO {
    private Long postId;
    private String username;   // from the User object, not the whole User
    private String title;
    private String description;
    private String contentUrl;
    private LocalDateTime createdDate;
    private int commentCount;
    private int likeCount;
}