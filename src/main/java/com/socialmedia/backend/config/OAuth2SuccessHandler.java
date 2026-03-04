package com.socialmedia.backend.config;

import com.socialmedia.backend.entities.User;
import com.socialmedia.backend.repositories.UserRepository;
import com.socialmedia.backend.services.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String googleId = oAuth2User.getAttribute("sub");
        String email = oAuth2User.getAttribute("email");
        String fullName = oAuth2User.getAttribute("name");

        // Split name safely
        String firstName = "";
        String lastName = "";

        if (fullName != null) {
            String[] parts = fullName.split(" ", 2);
            firstName = parts[0];
            if (parts.length > 1) {
                lastName = parts[1];
            }
        }

        Optional<User> existingUser = userRepository.findByEmail(email);

        User user;

        if (existingUser.isPresent()) {

            user = existingUser.get();
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);

        } else {

            user = User.builder()
                    .email(email)
                    .username(email.split("@")[0])  // auto username
                    .firstName(firstName)
                    .lastName(lastName)
                    .googleId(googleId)
                    .role("USER")
                    .lastLogin(LocalDateTime.now())
                    .build();

            userRepository.save(user);
        }

        String token = jwtService.generateToken(email);

        response.setContentType("application/json");
        response.getWriter().write("{\"token\":\"" + token + "\"}");
    }
}