package com.mindhub.order_microservice.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionHandlers {

    private static final Logger log = LoggerFactory.getLogger(ExceptionHandlers.class);

    // Generates a basic error response structure with status, timestamp, and message
    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String message) {
        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("timestamp", LocalDateTime.now());
        errorBody.put("status", status.value());
        errorBody.put("error", status.getReasonPhrase());
        errorBody.put("message", message);
        return new ResponseEntity<>(errorBody, status);
    }

    // Extended version of the error response structure to include additional data
    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String message, Map<String, Object> additionalData) {
        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("timestamp", LocalDateTime.now());
        errorBody.put("status", status.value());
        errorBody.put("error", status.getReasonPhrase());
        errorBody.put("message", message);

        // Add additional data if present
        if (additionalData != null && !additionalData.isEmpty()) {
            errorBody.put("data", additionalData);
        }

        return new ResponseEntity<>(errorBody, status);
    }

    @ExceptionHandler(OrderNotFoundExc.class)
    public ResponseEntity<Map<String, Object>> handleOrderNotFoundException(OrderNotFoundExc ex) {
        log.error("Order NOT FOUND: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.warn("Illegal argument provided: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Extract validation errors from the exception and format them as a single message
        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        log.warn("Validation failed: {}", errors);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Validation error(s): " + errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        // Handle cases where the request body cannot be parsed
        log.warn("Malformed request body: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid request format. Please check the data you're sending.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
        log.error("Unexpected error occurred: {}", ex.getMessage(), ex);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + ex.getMessage());
    }

    @ExceptionHandler(OrderAlreadyExistsExc.class)
    public ResponseEntity<Map<String, Object>> handleOrderAlreadyExistsException(OrderAlreadyExistsExc ex) {
        log.warn("Order already exists: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(InvalidCredentialsExc.class)
    public ResponseEntity<Map<String, Object>> handleInvalidCredentialsException(InvalidCredentialsExc ex) {
        log.warn("Invalid credentials: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        log.error("Unexpected runtime exception occurred: {}", ex.getMessage(), ex);
        if ("Invalid email or password".equals(ex.getMessage())) {
            return buildErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
        }
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + ex.getMessage());
    }

    @ExceptionHandler(InsufficientStockExc.class)
    public ResponseEntity<Map<String, Object>> handleInsufficientStockException(InsufficientStockExc ex) {
        log.error("Insufficient stock for product ID: {}. Requested quantity: {}", ex.getProductId(), ex.getRequestedQuantity());
        Map<String, Object> additionalData = new HashMap<>();
        additionalData.put("productId", ex.getProductId());
        additionalData.put("requestedQuantity", ex.getRequestedQuantity());

        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage(), additionalData);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Map<String, Object>> handleHttpClientErrorException(HttpClientErrorException ex) {
        // Handle errors when communicating with other services
        String responseBody = ex.getResponseBodyAsString();
        log.error("HTTP Client Error: Status {}, Response Body: {}", ex.getStatusCode(), responseBody);

        // Check if the response contains specific error messages from other services
        if (responseBody.contains("Product not found with ID")) {
            String productId = extractProductIdFromMessage(responseBody);
            log.warn("Product not found with ID: {}", productId);
            return buildErrorResponse(HttpStatus.NOT_FOUND, "The product with ID " + productId + " does not exist.");
        }

        if (responseBody.contains("User not found for email")) {
            String email = extractEmailFromMessage(responseBody);
            log.warn("User not found for email: {}", email);
            return buildErrorResponse(HttpStatus.NOT_FOUND, "The user with email " + email + " does not exist.");
        }

        log.error("Unhandled HttpClientErrorException: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred.");
    }


    private String extractProductIdFromMessage(String responseBody) {
        String marker = "Product not found with ID: ";
        int startIndex = responseBody.indexOf(marker) + marker.length();
        if (startIndex < marker.length()) {
            log.warn("Failed to extract product ID. Marker not found.");
            return "unknown";
        }

        int endIndex = responseBody.indexOf(",", startIndex);
        if (endIndex == -1) {
            endIndex = responseBody.length();
        }

        String extractedId = responseBody.substring(startIndex, endIndex).trim();
        log.debug("Extracted product ID: {}", extractedId);
        return extractedId.replace("\"", "");
    }

    private String extractEmailFromMessage(String responseBody) {
        String marker = "User not found for email: ";
        int startIndex = responseBody.indexOf(marker) + marker.length();
        if (startIndex < marker.length()) {
            log.warn("Failed to extract email. Marker not found.");
            return "unknown";
        }

        int endIndex = responseBody.indexOf(",", startIndex);
        if (endIndex == -1) {
            endIndex = responseBody.length();
        }

        String extractedEmail = responseBody.substring(startIndex, endIndex).trim();
        log.debug("Extracted email: {}", extractedEmail);
        return extractedEmail.replace("\"", "");
    }
}
