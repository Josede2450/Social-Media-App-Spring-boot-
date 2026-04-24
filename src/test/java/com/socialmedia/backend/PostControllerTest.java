package com.socialmedia.backend.controllers;

import com.socialmedia.backend.config.TestSecurityConfig;
import com.socialmedia.backend.dtos.PostResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestSecurityConfig.class)
class PostControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    // GET /api/posts
    @Test
    void getAllPosts_shouldReturn200() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/posts", String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // GET /api/posts/{id} — post that likely doesn't exist
    @Test
    void getPostById_shouldReturn200Or404() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/posts/1", String.class
        );

        assertThat(response.getStatusCode().value()).isIn(200, 404);
    }

    // GET /api/posts/{id} — id that definitely doesn't exist
    @Test
    void getPostById_notFound_shouldReturn404() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/posts/999999999", String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    // DELETE /api/posts/{id} — id that doesn't exist should return 404
    @Test
    void deletePost_notFound_shouldReturn404() {
        ResponseEntity<String> response = restTemplate.exchange(
                "/api/posts/999999999",
                HttpMethod.DELETE,
                null,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}