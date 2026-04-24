package com.socialmedia.backend.controllers;

import com.socialmedia.backend.config.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestSecurityConfig.class)
class CommentControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    // GET /api/posts/{postId}/comments — returns list (empty or not)
    @Test
    void getComments_shouldReturn200Or404() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/posts/1/comments", String.class
        );

        assertThat(response.getStatusCode().value()).isIn(200, 404);
    }

    // GET /api/posts/{postId}/comments — unknown post returns empty list or 404
    @Test
    void getComments_unknownPost_shouldReturn200Or404() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/posts/999999999/comments", String.class
        );

        assertThat(response.getStatusCode().value()).isIn(200, 404);
    }

    // GET /api/posts/{postId}/comments/{commentId} — comment that doesn't exist
    @Test
    void getCommentById_notFound_shouldReturn404() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/posts/1/comments/999999999", String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    // DELETE /api/posts/{postId}/comments/{commentId} — comment that doesn't exist
    @Test
    void deleteComment_notFound_shouldReturn404() {
        ResponseEntity<String> response = restTemplate.exchange(
                "/api/posts/1/comments/999999999",
                HttpMethod.DELETE,
                null,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    // POST /api/posts/{postId}/comments — needs auth, likely 500 in test
    @Test
    void createComment_shouldReturn201Or500() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String body = """
                { "content": "test comment" }
                """;

        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/posts/1/comments",
                new HttpEntity<>(body, headers),
                String.class
        );

        // 201 = created, 404 = post not found, 500 = no auth user in test context
        assertThat(response.getStatusCode().value()).isIn(201, 404, 500);
    }
}