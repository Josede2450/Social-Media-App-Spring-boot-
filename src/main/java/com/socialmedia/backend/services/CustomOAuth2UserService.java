package com.socialmedia.backend.services;

import com.socialmedia.backend.entities.User;
import com.socialmedia.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest)
            throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String googleId = oAuth2User.getAttribute("sub");
        String email = oAuth2User.getAttribute("email");
        String fullName = oAuth2User.getAttribute("name");
        String picture = oAuth2User.getAttribute("picture");

        Optional<User> userOptional = userRepository.findByGoogleId(googleId);

        if (userOptional.isEmpty()) {

            String firstName = "";
            String lastName = "";

            if (fullName != null) {
                String[] parts = fullName.split(" ");
                firstName = parts[0];
                if (parts.length > 1) {
                    lastName = parts[1];
                }
            }

            User user = User.builder()
                    .googleId(googleId)
                    .email(email)
                    .firstName(firstName)
                    .lastName(lastName)
                    .profilePictureUrl(picture)
                    .lastLogin(LocalDateTime.now())
                    .build();

            userRepository.save(user);

        } else {
            User existingUser = userOptional.get();
            existingUser.setLastLogin(LocalDateTime.now());
            userRepository.save(existingUser);
        }

        return oAuth2User;
    }
}