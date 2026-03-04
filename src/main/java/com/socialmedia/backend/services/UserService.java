package com.socialmedia.backend.services;

import com.socialmedia.backend.dtos.UserDTO;
import com.socialmedia.backend.entities.User;
import com.socialmedia.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDTO.UserResponse createUser(UserDTO.CreateUserRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }

        User user = User.builder()
                .googleId(request.getGoogleId())
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(request.getUsername())
                .role("USER")
                .dateCreated(LocalDateTime.now())
                .build();

        User saved = userRepository.save(user);

        return toResponse(saved);
    }

    public UserDTO.UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
                );

        return toResponse(user);
    }

    /**
     * 🔥 NEW METHOD — Get currently authenticated user
     */
    public User getCurrentAuthenticatedUser() {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof org.springframework.security.oauth2.core.user.OAuth2User oauthUser) {

            String email = oauthUser.getAttribute("email");

            return userRepository.findByEmail(email)
                    .orElseThrow(() ->
                            new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
                    );
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid authentication type");
    }

    private UserDTO.UserResponse toResponse(User user) {
        return UserDTO.UserResponse.builder()
                .userId(user.getUserId())
                .googleId(user.getGoogleId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .build();
    }
}