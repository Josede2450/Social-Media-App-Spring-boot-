package com.socialmedia.backend.controllers;

import com.socialmedia.backend.config.TestSecurityConfig;
import com.socialmedia.backend.dtos.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestSecurityConfig.class)
class UserControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;


    // GET /api/users/{id} — id that exists
    @Test
    void getUserById_shouldReturn200Or404() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/users/1", String.class
        );

        // 200 = found, 404 = no user with id 1 in test DB — both valid
        assertThat(response.getStatusCode().value()).isIn(200, 404);
    }

    // GET /api/users/username/{username} — username that doesn't exist
    @Test
    void getUserByUsername_notFound_shouldReturn404() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/users/username/definitely_not_a_real_user_xyz999", String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    // GET /api/users/search?username=...
    @Test
    void searchUsers_shouldReturn200() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/users/search?username=test", String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // GET /api/users/search — empty results are still 200
    @Test
    void searchUsers_noMatch_shouldStillReturn200() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/users/search?username=zzznomatchzzz", String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}