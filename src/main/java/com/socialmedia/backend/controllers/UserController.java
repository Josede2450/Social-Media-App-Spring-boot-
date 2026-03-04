package com.socialmedia.backend.controllers;

import com.socialmedia.backend.dtos.UserDTO;
import com.socialmedia.backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDTO.UserResponse> createUser(
            @RequestBody UserDTO.CreateUserRequest request) {

        UserDTO.UserResponse created = userService.createUser(request);

        return ResponseEntity
                .created(URI.create("/api/users/" + created.getUserId()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO.UserResponse> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
}