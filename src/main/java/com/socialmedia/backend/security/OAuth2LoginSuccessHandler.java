package com.socialmedia.backend.security;

import com.socialmedia.backend.entities.User;
import com.socialmedia.backend.repositories.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;

    public OAuth2LoginSuccessHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

        String googleId = oauthUser.getAttribute("sub");
        String email = oauthUser.getAttribute("email");
        String firstName = oauthUser.getAttribute("given_name");
        String lastName = oauthUser.getAttribute("family_name");

        userRepository.findByGoogleId(googleId)
                .orElseGet(() -> {

                    User user = User.builder()
                            .googleId(googleId)
                            .email(email)
                            .firstName(firstName)
                            .lastName(lastName)
                            .password("") // not used for Google login
                            .role("ROLE_USER")
                            .lastLogin(LocalDateTime.now())
                            .build();

                    return userRepository.save(user);
                });

        response.sendRedirect("/");
    }
}