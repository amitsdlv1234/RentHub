package com.AK.RentHub.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class JwtUtil {

    // Generate a secure key for HS384
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS384);

    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        // Add custom claims here if needed

        // Create the JWT
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY) // Use the secure key for signing
                .compact();
    }

    public String extractUserName(String token) {
        return extractClaims(token).getSubject();
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY) // Use the secure key for validation
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    public String generateTokenForOtp(String email, String otp) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("otp", otp); // Add the OTP to the claims

        // Create the JWT for OTP validation
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email) // Set email as the subject
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Token validity
                .signWith(SECRET_KEY, SignatureAlgorithm.HS384) // Sign the token
                .compact();
    }

}
