package com.socialmedia.backend.controllers;

import com.socialmedia.backend.dtos.CreatePostDTO;
import com.socialmedia.backend.dtos.PostResponseDTO;
import com.socialmedia.backend.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @PostMapping
    public ResponseEntity<PostResponseDTO> createPost(@RequestBody CreatePostDTO dto) {
        PostResponseDTO created = postService.createPost(dto);

        return ResponseEntity
                .created(URI.create("/api/posts/" + created.getPostId()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}