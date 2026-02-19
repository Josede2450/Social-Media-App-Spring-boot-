package com.socialmedia.backend.controllers;

import com.socialmedia.backend.dtos.CreatePostDTO;
import com.socialmedia.backend.dtos.PostResponseDTO;
import com.socialmedia.backend.entities.Post;
import com.socialmedia.backend.services.PostService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @PostMapping
    public ResponseEntity<PostResponseDTO> createPost(@RequestBody CreatePostDTO dto) {
        return ResponseEntity.ok(postService.createPost(dto));
    }

}
