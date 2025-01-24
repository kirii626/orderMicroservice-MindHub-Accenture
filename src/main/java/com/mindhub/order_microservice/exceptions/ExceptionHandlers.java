package com.mindhub.order_microservice.exceptions;

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

    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String message) {
        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("timestamp", LocalDateTime.now());
        errorBody.put("status", status.value());
        errorBody.put("error", status.getReasonPhrase());
        errorBody.put("message", message);
        return new ResponseEntity<>(errorBody, status);
    }

    @ExceptionHandler(OrderNotFoundExc.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFoundException(OrderNotFoundExc ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Validation error(s): " + errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid request format. Please check the data you're sending.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + ex.getMessage());
    }

    @ExceptionHandler(OrderAlreadyExistsExc.class)
    public ResponseEntity<Map<String, Object>> handleUserAlreadyExistsException(OrderAlreadyExistsExc ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(InvalidCredentialsExc.class)
    public ResponseEntity<Map<String, Object>> handleInvalidCredentialsException(InvalidCredentialsExc ex) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        if ("Invalid email or password".equals(ex.getMessage())) {
            return buildErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
        }
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + ex.getMessage());
    }

    @ExceptionHandler(InsufficientStockExc.class)
    public ResponseEntity<Map<String, Object>> handleInsufficientStockException(InsufficientStockExc ex) {
        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("timestamp", LocalDateTime.now());
        errorBody.put("status", HttpStatus.CONFLICT.value());
        errorBody.put("error", HttpStatus.CONFLICT.getReasonPhrase());
        errorBody.put("message", ex.getMessage());

        // Agregar información adicional al campo `data`
        Map<String, Object> additionalData = new HashMap<>();
        additionalData.put("productId", ex.getProductId());
        additionalData.put("requestedQuantity", ex.getRequestedQuantity());
        errorBody.put("data", additionalData);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorBody);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Map<String, Object>> handleHttpClientErrorException(HttpClientErrorException ex) {
        String responseBody = ex.getResponseBodyAsString();
        System.out.println("Response body: " + responseBody); // Log para verificar el contenido

        // Aquí asegúrate de que el cuerpo contiene "Product not found with ID" o "User not found for email"
        if (responseBody.contains("Product not found with ID")) {
            String productId = extractProductIdFromMessage(responseBody);
            return buildErrorResponse(HttpStatus.NOT_FOUND, "The product with ID " + productId + " does not exist.");
        }

        if (responseBody.contains("User not found for email")) {
            String email = extractEmailFromMessage(responseBody);
            return buildErrorResponse(HttpStatus.NOT_FOUND, "The user with email " + email + " does not exist.");
        }

        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred.");
    }


    private String extractProductIdFromMessage(String responseBody) {
        String marker = "Product not found with ID: ";
        int startIndex = responseBody.indexOf(marker) + marker.length();
        if (startIndex < marker.length()) {
            return "unknown";
        }

        int endIndex = responseBody.indexOf(",", startIndex);
        if (endIndex == -1) {
            endIndex = responseBody.length();
        }

        // Extrae el ID y elimina comillas adicionales
        String extractedId = responseBody.substring(startIndex, endIndex).trim();
        return extractedId.replace("\"", "");
    }

    private String extractEmailFromMessage(String responseBody) {
        String marker = "User not found for email: ";
        int startIndex = responseBody.indexOf(marker) + marker.length();
        if (startIndex < marker.length()) {
            return "unknown";
        }

        int endIndex = responseBody.indexOf(",", startIndex);
        if (endIndex == -1) {
            endIndex = responseBody.length();
        }

        String extractedEmail = responseBody.substring(startIndex, endIndex).trim();
        return extractedEmail.replace("\"", "");// Elimina comillas si están presentes
    }
}
