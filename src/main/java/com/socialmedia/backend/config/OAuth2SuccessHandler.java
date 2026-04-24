package com.socialmedia.backend.config;

import com.socialmedia.backend.entities.User;
import com.socialmedia.backend.repositories.UserRepository;
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

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String googleId = oAuth2User.getAttribute("sub");
        String email = oAuth2User.getAttribute("email");
        String fullName = oAuth2User.getAttribute("name");
        String picture = oAuth2User.getAttribute("picture");

        String firstName = "";
        String lastName = "";

        if (fullName != null && !fullName.isBlank()) {
            String[] parts = fullName.trim().split(" ", 2);
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

            if (user.getGoogleId() == null || user.getGoogleId().isBlank()) {
                user.setGoogleId(googleId);
            }

            // Only use Google image if user still does not have a profile picture
            if ((user.getProfilePictureUrl() == null || user.getProfilePictureUrl().isBlank())
                    && picture != null
                    && !picture.isBlank()) {
                user.setProfilePictureUrl(picture);
            }

            userRepository.save(user);
        } else {
            user = User.builder()
                    .email(email)
                    .username(email.split("@")[0])
                    .firstName(firstName)
                    .lastName(lastName)
                    .googleId(googleId)
                    .profilePictureUrl(picture)
                    .role("USER")
                    .lastLogin(LocalDateTime.now())
                    .build();

            userRepository.save(user);
        }

        response.sendRedirect("http://localhost:3000/home");
    }
}