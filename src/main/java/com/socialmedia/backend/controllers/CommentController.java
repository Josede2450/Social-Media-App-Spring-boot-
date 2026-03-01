package com.socialmedia.backend.controllers;

import com.socialmedia.backend.dtos.CommentDTO;
import com.socialmedia.backend.services.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentDTO.CommentResponse> create(
            @PathVariable Long postId,
            @Valid @RequestBody CommentDTO.CommentRequest request) {

        CommentDTO.CommentResponse created = commentService.addComment(postId, request);

        return ResponseEntity
                .created(URI.create("/api/posts/" + postId + "/comments/" + created.getId()))
                .body(created);
    }

    @GetMapping
    public ResponseEntity<List<CommentDTO.CommentResponse>> getAll(
            @PathVariable Long postId) {

        return ResponseEntity.ok(commentService.getCommentsByPost(postId));
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDTO.CommentResponse> getById(
            @PathVariable Long postId,
            @PathVariable Long commentId) {

        return ResponseEntity.ok(
                commentService.getCommentById(postId, commentId)
        );
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long postId,
            @PathVariable Long commentId) {

        commentService.deleteComment(postId, commentId);
        return ResponseEntity.noContent().build();
    }
}