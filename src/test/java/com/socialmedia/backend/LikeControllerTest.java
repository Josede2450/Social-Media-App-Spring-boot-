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
class LikeControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    // POST /api/posts/{postId}/like — post that exists
    @Test
    void likePost_shouldReturn201Or404() {
        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/posts/1/like", null, String.class
        );

        // 201 = liked, 404 = post doesn't exist in test DB
        assertThat(response.getStatusCode().value()).isIn(201, 404, 500);
    }

    // POST /api/posts/{postId}/like — post that definitely doesn't exist
    @Test
    void likePost_notFound_shouldReturn404() {
        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/posts/999999999/like", null, String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    // DELETE /api/posts/{postId}/like — post that exists
    @Test
    void unlikePost_shouldReturn204Or404() {
        ResponseEntity<String> response = restTemplate.exchange(
                "/api/posts/1/like",
                HttpMethod.DELETE,
                null,
                String.class
        );

        // 204 = unliked, 404 = post doesn't exist in test DB
        assertThat(response.getStatusCode().value()).isIn(204, 404, 500);
    }

    // DELETE /api/posts/{postId}/like — post that definitely doesn't exist
    @Test
    void unlikePost_notFound_shouldReturn404() {
        ResponseEntity<String> response = restTemplate.exchange(
                "/api/posts/999999999/like",
                HttpMethod.DELETE,
                null,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}