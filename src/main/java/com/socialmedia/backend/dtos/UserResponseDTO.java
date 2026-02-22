package com.socialmedia.backend.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDTO {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
}