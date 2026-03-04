package com.socialmedia.backend.dtos;

import lombok.*;

public class UserDTO {

    @Getter
    @Setter
    public static class CreateUserRequest {
        private String googleId;
        private String email;
        private String firstName;
        private String lastName;
        private String username;
    }

    @Getter
    @Setter
    @Builder
    public static class UserResponse {
        private Long userId;
        private String googleId;
        private String email;
        private String firstName;
        private String lastName;
        private String username;
    }
}