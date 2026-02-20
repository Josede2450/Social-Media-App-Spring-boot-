package com.socialmedia.backend.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

public class CommentDTO {

    @Getter @Setter
    public static class CommentRequest {
        @NotBlank(message = "Comment cannot be empty")
        private String content;
    }

    @Getter @Setter
    public static class CommentResponse {
        private Long id;
        private String content;
        private Long postId;
    }
}