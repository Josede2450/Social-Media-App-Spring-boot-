package com.socialmedia.backend.services;

import com.socialmedia.backend.dtos.CommentDTO.CommentRequest;
import com.socialmedia.backend.dtos.CommentDTO.CommentResponse;
import com.socialmedia.backend.entities.Comment;
import com.socialmedia.backend.entities.Post;
import com.socialmedia.backend.entities.User;
import com.socialmedia.backend.repositories.CommentRepository;
import com.socialmedia.backend.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserService userService; // ✅ Inject UserService

    public CommentResponse addComment(Long postId, CommentRequest request) {

        if (request.getContent() == null || request.getContent().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Comment cannot be empty");
        }

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found")
                );

        // ✅ Get authenticated user
        User currentUser = userService.getCurrentAuthenticatedUser();

        Comment comment = Comment.builder()
                .content(request.getContent())
                .post(post)
                .user(currentUser) // ✅ Attach user to comment
                .createdDate(LocalDateTime.now())
                .build();

        Comment saved = commentRepository.save(comment);

        return toResponse(saved);
    }

    public List<CommentResponse> getCommentsByPost(Long postId) {

        if (!postRepository.existsById(postId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
        }

        return commentRepository.findByPostPostId(postId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public CommentResponse getCommentById(Long postId, Long commentId) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found")
                );

        if (!comment.getPost().getPostId().equals(postId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Comment does not belong to this post");
        }

        return toResponse(comment);
    }

    public void deleteComment(Long postId, Long commentId) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found")
                );

        if (!comment.getPost().getPostId().equals(postId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Comment does not belong to this post");
        }

        // ✅ Only owner can delete
        User currentUser = userService.getCurrentAuthenticatedUser();

        if (!comment.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot delete this comment");
        }

        commentRepository.delete(comment);
    }

    private CommentResponse toResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getCommentId())
                .content(comment.getContent())
                .postId(comment.getPost().getPostId())
                .username(comment.getUser().getUsername()) // Optional if your DTO supports it
                .build();
    }
}