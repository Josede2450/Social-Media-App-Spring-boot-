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
class NotificationControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    // GET /api/notifications/{userId} — user with no notifications
    @Test
    void getNotifications_shouldReturn200() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/notifications/1", String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // GET /api/notifications/{userId} — returns empty list for unknown user
    @Test
    void getNotifications_unknownUser_shouldReturn200WithEmptyList() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/notifications/999999999", String.class
        );

        // No exception expected — just an empty list
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("[]");
    }
}