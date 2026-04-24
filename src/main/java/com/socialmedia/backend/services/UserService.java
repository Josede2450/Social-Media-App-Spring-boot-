package com.socialmedia.backend.services;

import com.socialmedia.backend.dtos.UserDTO;
import com.socialmedia.backend.entities.User;
import com.socialmedia.backend.repositories.FollowRepository;
import com.socialmedia.backend.repositories.PostRepository;
import com.socialmedia.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final PostRepository postRepository;

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

    public UserDTO.UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
                );

        return toResponse(user);
    }

    public User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }

        String email;
        Object principal = authentication.getPrincipal();

        if (principal instanceof OAuth2User oauthUser) {
            email = oauthUser.getAttribute("email");
        } else if (principal instanceof org.springframework.security.core.userdetails.User userDetails) {
            email = userDetails.getUsername();
        } else {
            email = authentication.getName();
        }

        return userRepository.findByEmail(email.toLowerCase().trim())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
                );
    }

    public UserDTO.UserResponse updateCurrentUserBio(String bio) {
        User currentUser = getCurrentAuthenticatedUser();
        currentUser.setBio(bio);
        User savedUser = userRepository.save(currentUser);
        return toResponse(savedUser);
    }

    public UserDTO.UserResponse updateCurrentUserProfile(
            String firstName,
            String lastName,
            String bio,
            MultipartFile profilePicture
    ) {
        User currentUser = getCurrentAuthenticatedUser();

        currentUser.setFirstName(firstName != null ? firstName.trim() : "");
        currentUser.setLastName(lastName != null ? lastName.trim() : "");
        currentUser.setBio(bio != null ? bio.trim() : "");

        if (profilePicture != null && !profilePicture.isEmpty()) {
            String imageUrl = saveProfilePicture(profilePicture);
            currentUser.setProfilePictureUrl(imageUrl);
        }

        User savedUser = userRepository.save(currentUser);
        return toResponse(savedUser);
    }

    private String saveProfilePicture(MultipartFile file) {
        try {
            String uploadDir = "uploads/profile-pictures";
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFilename = file.getOriginalFilename();
            String extension = "";

            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            String filename = UUID.randomUUID() + extension;
            Path filePath = uploadPath.resolve(filename);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return "http://localhost:8080/uploads/profile-pictures/" + filename;
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to save profile picture"
            );
        }
    }

    private UserDTO.UserResponse toResponse(User user) {
        return UserDTO.UserResponse.builder()
                .userId(user.getUserId())
                .googleId(user.getGoogleId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .bio(user.getBio())
                .profilePictureUrl(user.getProfilePictureUrl())

                // ✅ ADD THIS LINE
                .role(user.getRole())

                .followerCount(followRepository.findByFollowingUserId(user.getUserId()).size())
                .followingCount(followRepository.findByFollowerUserId(user.getUserId()).size())
                .postCount(user.getPosts() != null ? user.getPosts().size() : 0)
                .build();
    }

    public List<UserDTO.UserResponse> searchUsers(String username) {
        return userRepository.findByUsernameContainingIgnoreCase(username)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public int getFollowerCount(Long userId) {
        return followRepository.findByFollowingUserId(userId).size();
    }

    public int getFollowingCount(Long userId) {
        return followRepository.findByFollowerUserId(userId).size();
    }

    public int getPostCount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
                );

        return user.getPosts() != null ? user.getPosts().size() : 0;
    }
}