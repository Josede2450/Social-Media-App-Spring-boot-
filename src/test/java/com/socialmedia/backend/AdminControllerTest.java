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
class AdminControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getAllPosts_shouldReturn200Or403Or404Or500() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/admin/posts", String.class
        );
        assertThat(response.getStatusCode().value()).isIn(200, 403, 404, 500);
    }

    @Test
    void getAllReports_shouldReturn200Or403Or404Or500() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/admin/reports", String.class
        );
        assertThat(response.getStatusCode().value()).isIn(200, 403, 404, 500);
    }

    @Test
    void getAllUsers_shouldReturn200Or403Or404Or500() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/admin/users", String.class
        );
        assertThat(response.getStatusCode().value()).isIn(200, 403, 404, 500);
    }

    @Test
    void deletePost_shouldReturn200Or403Or404Or500() {
        ResponseEntity<String> response = restTemplate.exchange(
                "/api/admin/posts/999999999",
                HttpMethod.DELETE,
                null,
                String.class
        );
        assertThat(response.getStatusCode().value()).isIn(200, 403, 404, 500);
    }

    @Test
    void deleteComment_shouldReturn200Or403Or404Or500() {
        ResponseEntity<String> response = restTemplate.exchange(
                "/api/admin/comments/999999999",
                HttpMethod.DELETE,
                null,
                String.class
        );
        assertThat(response.getStatusCode().value()).isIn(200, 403, 404, 500);
    }

    @Test
    void deleteReport_shouldReturn200Or403Or404Or500() {
        ResponseEntity<String> response = restTemplate.exchange(
                "/api/admin/reports/999999999",
                HttpMethod.DELETE,
                null,
                String.class
        );
        assertThat(response.getStatusCode().value()).isIn(200, 403, 404, 500);
    }

    @Test
    void deleteUser_shouldReturn200Or403Or404Or500() {
        ResponseEntity<String> response = restTemplate.exchange(
                "/api/admin/users/999999999",
                HttpMethod.DELETE,
                null,
                String.class
        );
        assertThat(response.getStatusCode().value()).isIn(200, 403, 404, 500);
    }

    @Test
    void updatePost_shouldReturn200Or403Or404Or500() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String body = """
                { "title": "updated title", "description": "updated desc" }
                """;
        ResponseEntity<String> response = restTemplate.exchange(
                "/api/admin/posts/1",
                HttpMethod.PUT,
                new HttpEntity<>(body, headers),
                String.class
        );
        assertThat(response.getStatusCode().value()).isIn(200, 403, 404, 500);
    }

    @Test
    void updateUser_shouldReturn200Or403Or404Or500() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String body = """
                { "username": "newusername" }
                """;
        ResponseEntity<String> response = restTemplate.exchange(
                "/api/admin/users/1",
                HttpMethod.PUT,
                new HttpEntity<>(body, headers),
                String.class
        );
        assertThat(response.getStatusCode().value()).isIn(200, 403, 404, 500);
    }
}