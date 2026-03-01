package com.socialmedia.backend.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

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
    }
}