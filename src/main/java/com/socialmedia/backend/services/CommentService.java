package com.socialmedia.backend.services;
import com.socialmedia.backend.dtos.CommentDTO.CommentRequest;
import com.socialmedia.backend.dtos.CommentDTO.CommentResponse;
import com.socialmedia.backend.dtos.CommentDTO;
import com.socialmedia.backend.entities.Comment;
import com.socialmedia.backend.entities.Post;
import com.socialmedia.backend.repositories.CommentRepository;
import com.socialmedia.backend.repositories.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    public CommentDTO.CommentResponse addComment(Long postId, CommentDTO.CommentRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Comment comment = new Comment();

        comment.setContent(request.getContent());
        comment.setPost(post);

        Comment saved = commentRepository.save(comment);
        return toResponse(saved);
    }

    public List<CommentResponse> getCommentsByPost(Long postId) {
        return commentRepository.findByPostPostId(postId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private CommentResponse toResponse(Comment comment) {
        CommentResponse res = new CommentResponse();
        res.setId(comment.getCommentId());
        res.setContent(comment.getContent());
        res.setPostId(comment.getPost().getPostId());
        return res;
    }

    public void deleteComment(Long postId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getPost().getPostId().equals(postId)) {
            throw new RuntimeException("Comment does not belong to this post");
        }

        commentRepository.delete(comment);
    }
    public CommentResponse getCommentById(Long postId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getPost().getPostId().equals(postId)) {
            throw new RuntimeException("Comment does not belong to this post");
        }

        return toResponse(comment);
    }
}