package com.cloudapp.cloud_app.cloud_app.Security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil("unit-test-jwt-secret-key-must-be-at-least-32-characters");
    }

    @Test
    void buildToken_createsAValidTokenForTheSpecifiedUsername() {
        String token = jwtUtil.buildToken("customer");

        assertEquals("customer", jwtUtil.extractUsername(token));
        assertTrue(jwtUtil.validateToken(token));
        assertTrue(jwtUtil.verifyToken(token, "customer"));
    }

    @Test
    void validateToken_returnsFalseForAnAlteredToken() {
        String token = jwtUtil.buildToken("customer");
        String alteredToken = token.substring(0, token.length() - 1) + "x";

        assertFalse(jwtUtil.validateToken(alteredToken));
    }

    @Test
    void verifyToken_returnsFalseForADifferentUsername() {
        String token = jwtUtil.buildToken("customer");

        assertFalse(jwtUtil.verifyToken(token, "another-user"));
    }

    @Test
    void validateToken_rejectsATokenSignedWithAnotherSecret() {
        JwtUtil otherJwtUtil = new JwtUtil("another-unit-test-jwt-secret-with-32-characters");
        String token = otherJwtUtil.buildToken("customer");

        assertFalse(jwtUtil.validateToken(token));
    }
}
