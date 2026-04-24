package com.socialmedia.backend.dtos;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponseDTO {

    private Long userId;
    private Long postId;
    private String username;
    private String firstName;
    private String lastName;
    private String profilePictureUrl;
    private String title;
    private String description;
    private String contentUrl;
    private LocalDateTime createdDate;
    private int commentCount;
    private int likeCount;
    private boolean likedByCurrentUser;
}