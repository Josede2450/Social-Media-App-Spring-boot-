package com.socialmedia.backend.services;

import com.socialmedia.backend.dtos.CommentDTO.CommentRequest;
import com.socialmedia.backend.dtos.CommentDTO.CommentResponse;
import com.socialmedia.backend.entities.Comment;
import com.socialmedia.backend.entities.Notification;
import com.socialmedia.backend.entities.Post;
import com.socialmedia.backend.entities.User;
import com.socialmedia.backend.repositories.CommentRepository;
import com.socialmedia.backend.repositories.NotificationRepository;
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
    private final UserService userService;
    private final NotificationRepository notificationRepository;

    public CommentResponse addComment(Long postId, CommentRequest request) {

        if (request.getContent() == null || request.getContent().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Comment cannot be empty");
        }

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found")
                );

        User currentUser = userService.getCurrentAuthenticatedUser();

        Comment comment = Comment.builder()
                .content(request.getContent())
                .post(post)
                .user(currentUser)
                .build();

        Comment saved = commentRepository.save(comment);

        User postOwner = post.getUser();

        if (!postOwner.getUserId().equals(currentUser.getUserId())) {
            Notification notification = Notification.builder()
                    .user(postOwner)
                    .message(currentUser.getUsername() + " commented on your post")
                    .type("COMMENT")
                    .createdDate(LocalDateTime.now())
                    .build();

            notificationRepository.save(notification);
        }

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
                .createdDate(comment.getCreatedDate())
                .userId(comment.getUser().getUserId())
                .username(comment.getUser().getUsername())
                .firstName(comment.getUser().getFirstName())
                .lastName(comment.getUser().getLastName())
                .profilePictureUrl(comment.getUser().getProfilePictureUrl())
                .build();
    }
}