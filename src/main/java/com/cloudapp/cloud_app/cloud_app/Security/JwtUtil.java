package com.cloudapp.cloud_app.cloud_app.Security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {

    // define a secret key and its expiration time (10 hours)

    private static final String secretKeyString = "mysecretkeymysecretkeymysecretkey"; // kam se kam 32 characters HS256
                                                                                       // ke liye
    private static final Long expirationTime = 10 * 60 * 60 * 1000L;

    // convert the secret string into a proper SecretKey object
    private static SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKeyString.getBytes());
    }

    // build the token
    public static String buildToken(String username) {

        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey())
                .compact();
    }

    // helper: parse token and get all claims
    private static Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // extract the username from token
    public static String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // parse and verify the token signature
    public static boolean validateToken(String token) {

        try {
            extractAllClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    // check if token expired
    public static boolean isTokenExpired(String token) {

        Date expirationDate = extractAllClaims(token).getExpiration();
        return expirationDate.before(new Date());
    }

    // full verification: username + signature + expiry
    public static boolean verifyToken(String token, String username) {

        String extractedUsername = extractUsername(token);
        return extractedUsername.equals(username) && validateToken(token) && !isTokenExpired(token);
    }
}