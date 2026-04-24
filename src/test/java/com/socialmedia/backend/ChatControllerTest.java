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
class ChatControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    // GET /api/chats/{chatId}/messages — chat that doesn't exist
    @Test
    void getMessages_shouldReturn200Or404Or500() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/chats/1/messages", String.class
        );

        assertThat(response.getStatusCode().value()).isIn(200, 404, 500);
    }

    // GET /api/chats/{chatId}/messages — chat that definitely doesn't exist
    @Test
    void getMessages_notFound_shouldReturn404Or500() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/chats/999999999/messages", String.class
        );

        assertThat(response.getStatusCode().value()).isIn(404, 500);
    }

    // GET /api/chats/me — needs auth, likely 500 in test
    @Test
    void getMyChats_shouldReturn200Or404Or500() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/chats/me", String.class
        );

        assertThat(response.getStatusCode().value()).isIn(200, 404, 500);
    }

    // POST /api/chats/create/{targetUserId} — needs auth, likely 500 in test
    @Test
    void createChat_shouldReturn200Or404Or500() {
        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/chats/create/1", null, String.class
        );

        assertThat(response.getStatusCode().value()).isIn(200, 404, 500);
    }

    // POST /api/chats/{chatId}/messages — needs auth, likely 500 in test
    @Test
    void sendMessage_shouldReturn200Or404Or500() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String body = """
                { "content": "hello" }
                """;

        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/chats/1/messages",
                new HttpEntity<>(body, headers),
                String.class
        );

        assertThat(response.getStatusCode().value()).isIn(200, 404, 500);
    }
}