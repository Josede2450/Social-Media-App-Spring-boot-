package com.socialmedia.backend.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowUserResponseDTO {

    private Long userId;
    private String username;
    private String firstName;
    private String lastName;

    // 🔥 CHANGE THIS
    private String profilePictureUrl;
}