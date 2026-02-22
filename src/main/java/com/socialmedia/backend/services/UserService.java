package com.socialmedia.backend.services;

import com.socialmedia.backend.dtos.UserResponseDTO;
import com.socialmedia.backend.entities.User;
import com.socialmedia.backend.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDTO getCurrentUser(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String googleId = oauthUser.getAttribute("sub");

        User user = userRepository.findByGoogleId(googleId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return UserResponseDTO.builder()
                .id(user.getUserId())   // Use your actual ID field
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }
}