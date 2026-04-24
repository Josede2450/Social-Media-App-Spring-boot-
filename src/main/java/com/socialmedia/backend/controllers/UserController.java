package com.socialmedia.backend.controllers;

import com.socialmedia.backend.dtos.UserDTO;
import com.socialmedia.backend.entities.User;
import com.socialmedia.backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    public static class UpdateBioRequest {
        private String bio;

        public String getBio() {
            return bio;
        }

        public void setBio(String bio) {
            this.bio = bio;
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO.UserResponse> getCurrentUser() {
        User currentUser = userService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(userService.getUserById(currentUser.getUserId()));
    }

    @PostMapping
    public ResponseEntity<UserDTO.UserResponse> createUser(
            @RequestBody UserDTO.CreateUserRequest request) {

        UserDTO.UserResponse created = userService.createUser(request);

        return ResponseEntity
                .created(URI.create("/api/users/" + created.getUserId()))
                .body(created);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserDTO.UserResponse> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO.UserResponse> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/me/bio")
    public ResponseEntity<UserDTO.UserResponse> updateMyBio(
            @RequestBody UpdateBioRequest request) {
        return ResponseEntity.ok(userService.updateCurrentUserBio(request.getBio()));
    }

    @PutMapping(value = "/me/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDTO.UserResponse> updateMyProfile(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String bio,
            @RequestPart(required = false) MultipartFile profilePicture) {

        return ResponseEntity.ok(
                userService.updateCurrentUserProfile(firstName, lastName, bio, profilePicture)
        );
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserDTO.UserResponse>> searchUsers(
            @RequestParam String username) {
        return ResponseEntity.ok(userService.searchUsers(username));
    }
}