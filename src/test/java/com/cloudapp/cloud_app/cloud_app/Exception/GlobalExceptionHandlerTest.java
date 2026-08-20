package com.cloudapp.cloud_app.cloud_app.Exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.access.AccessDeniedException;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;
    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
        request = new MockHttpServletRequest();
        request.setRequestURI("/service-requests/1");
    }

    @Test
    void handleIllegalArgument_returnsBadRequestWithSafeDetails() {
        ResponseEntity<ApiError> response = exceptionHandler.handleIllegalArgument(
                new IllegalArgumentException("Request not found"), request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Request not found", response.getBody().message());
        assertEquals("/service-requests/1", response.getBody().path());
    }

    @Test
    void handleAccessDenied_returnsForbiddenResponse() {
        ResponseEntity<ApiError> response = exceptionHandler.handleAccessDenied(
                new AccessDeniedException("forbidden"), request);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("You do not have permission to access this resource", response.getBody().message());
        assertNull(response.getBody().fieldErrors());
    }

    @Test
    void handleUnexpectedError_hidesInternalExceptionDetails() {
        ResponseEntity<ApiError> response = exceptionHandler.handleUnexpectedError(
                new RuntimeException("database password leaked"), request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An unexpected error occurred", response.getBody().message());
    }
}
