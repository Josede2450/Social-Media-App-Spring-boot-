package com.socialmedia.backend.controllers;

import com.socialmedia.backend.dtos.PostRequestDTO;
import com.socialmedia.backend.dtos.PostResponseDTO;
import com.socialmedia.backend.services.PostService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @PostMapping
    public ResponseEntity<PostResponseDTO> createPost(
            @RequestBody PostRequestDTO dto,
            Authentication authentication) {

        return ResponseEntity.ok(
                postService.createPost(dto, authentication)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getPostById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                postService.getPostById(id)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long id,
            Authentication authentication) {

        postService.deletePost(id, authentication);
        return ResponseEntity.noContent().build();
    }
}