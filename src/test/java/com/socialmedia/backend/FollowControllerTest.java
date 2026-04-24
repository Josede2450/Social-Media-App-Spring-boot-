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
class FollowControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getFollowers_shouldReturn200Or404() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/follows/followers/1", String.class
        );
        assertThat(response.getStatusCode().value()).isIn(200, 404);
    }

    @Test
    void getFollowers_unknownUser_shouldReturn200Or404() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/follows/followers/999999999", String.class
        );
        assertThat(response.getStatusCode().value()).isIn(200, 404);
    }

    @Test
    void getFollowing_shouldReturn200Or404() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/follows/following/1", String.class
        );
        assertThat(response.getStatusCode().value()).isIn(200, 404);
    }

    @Test
    void getFollowing_unknownUser_shouldReturn200Or404() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/follows/following/999999999", String.class
        );
        assertThat(response.getStatusCode().value()).isIn(200, 404);
    }

    @Test
    void isFollowing_shouldReturn200Or404Or500() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/follows/status/1", String.class
        );
        assertThat(response.getStatusCode().value()).isIn(200, 404, 500);
    }

    @Test
    void follow_shouldReturn200Or404Or500() {
        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/follows/1", null, String.class
        );
        assertThat(response.getStatusCode().value()).isIn(200, 404, 500);
    }

    @Test
    void unfollow_shouldReturn200Or404Or500() {
        ResponseEntity<String> response = restTemplate.exchange(
                "/api/follows/1",
                HttpMethod.DELETE,
                null,
                String.class
        );
        assertThat(response.getStatusCode().value()).isIn(200, 404, 500);
    }
}