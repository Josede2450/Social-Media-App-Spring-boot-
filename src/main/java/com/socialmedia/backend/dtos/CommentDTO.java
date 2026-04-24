package com.socialmedia.backend.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

public class CommentDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CommentRequest {

        @NotBlank(message = "Comment cannot be empty")
        private String content;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CommentResponse {

        private Long id;
        private String content;
        private Long postId;
        private LocalDateTime createdDate;

        private Long userId;
        private String username;
        private String firstName;
        private String lastName;
        private String profilePictureUrl;
    }
}