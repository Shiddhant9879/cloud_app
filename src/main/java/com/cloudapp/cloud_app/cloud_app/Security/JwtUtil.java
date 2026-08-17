package com.cloudapp.cloud_app.cloud_app.Security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    // Token expiry time: 10 hours.
    private static final Long expirationTime = 10 * 60 * 60 * 1000L;

    private final String secretKeyString;

    public JwtUtil(@Value("${jwt.secret}") String secretKeyString) {
        this.secretKeyString = secretKeyString;
    }

    // convert the secret string into a proper SecretKey object
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKeyString.getBytes(StandardCharsets.UTF_8));
    }

    // build the token
    public String buildToken(String username) {

        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey())
                .compact();
    }

    // helper: parse token and get all claims
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // extract the username from token
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // parse and verify the token signature
    public boolean validateToken(String token) {

        try {
            extractAllClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    // check if token expired
    public boolean isTokenExpired(String token) {

        Date expirationDate = extractAllClaims(token).getExpiration();
        return expirationDate.before(new Date());
    }

    // full verification: username + signature + expiry
    public boolean verifyToken(String token, String username) {

        String extractedUsername = extractUsername(token);
        return extractedUsername.equals(username) && validateToken(token) && !isTokenExpired(token);
    }
}
