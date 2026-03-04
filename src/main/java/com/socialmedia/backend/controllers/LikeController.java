package com.socialmedia.backend.controllers;

import com.socialmedia.backend.services.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/{postId}/like")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<Void> like(@PathVariable Long postId) {

        likeService.likePost(postId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> unlike(@PathVariable Long postId) {

        likeService.unlikePost(postId);
        return ResponseEntity.noContent().build();
    }
}