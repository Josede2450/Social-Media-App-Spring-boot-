package com.socialmedia.backend.controllers;

import com.socialmedia.backend.dtos.CommentDTO;
import com.socialmedia.backend.services.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.status(201).body(commentService.addComment(postId, request));
    }
    @GetMapping
    public List<CommentDTO.CommentResponse> getAll(@PathVariable Long postId) {
        return commentService.getCommentsByPost(postId);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long postId,
            @PathVariable Long commentId) {
        commentService.deleteComment(postId, commentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDTO.CommentResponse> getById(
            @PathVariable Long postId,
            @PathVariable Long commentId) {
        return ResponseEntity.ok(commentService.getCommentById(postId, commentId));
    }
}