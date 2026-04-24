package com.socialmedia.backend.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    // 🔹 Secret key (must be at least 32 characters)
    private final String SECRET_KEY = "my_super_secret_key_that_is_at_least_32_characters_long";

    // 🔹 Generate JWT token using email as subject
    public String generateToken(String email) {

        return Jwts.builder()
                .setSubject(email.toLowerCase()) // 🔥 normalize email to avoid case issues
                .setIssuedAt(new Date()) // token creation time
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day expiration
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS256) // signing
                .compact();
    }

    // 🔹 Extract email (subject) from token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject); // 🔥 must match setSubject
    }

    // 🔹 Extract expiration date from token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // 🔹 Generic method to extract any claim
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        final Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    // 🔹 Parse token and get all claims
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 🔹 Check if token is expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // 🔹 Validate token against user
    public boolean isTokenValid(String token, com.socialmedia.backend.entities.User user) {

        final String email = extractUsername(token);

        return (
                email != null &&
                        email.equalsIgnoreCase(user.getEmail()) && // 🔥 FIX: ignore case
                        !isTokenExpired(token)
        );
    }
}