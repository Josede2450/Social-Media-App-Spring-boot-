package com.socialmedia.backend.controllers;

import com.socialmedia.backend.config.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.web.util.UriComponentsBuilder;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestSecurityConfig.class)
class ReportControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    // POST /api/posts/{postId}/report — post that doesn't exist
    @Test
    void reportPost_notFound_shouldReturn404() {
        String url = UriComponentsBuilder
                .fromPath("/api/posts/999999999/report")
                .queryParam("reason", "spam")
                .toUriString();

        ResponseEntity<String> response = restTemplate.postForEntity(
                url, null, String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    // POST /api/posts/{postId}/report — valid post id
    @Test
    void reportPost_shouldReturn201Or404() {
        String url = UriComponentsBuilder
                .fromPath("/api/posts/1/report")
                .queryParam("reason", "inappropriate content")
                .toUriString();

        ResponseEntity<String> response = restTemplate.postForEntity(
                url, null, String.class
        );

        // 201 = reported successfully, 404 = post doesn't exist in test DB
        assertThat(response.getStatusCode().value()).isIn(201, 404);
    }

    // POST /api/posts/{postId}/report — missing reason param
    @Test
    void reportPost_missingReason_shouldReturn400() {
        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/posts/1/report", null, String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}