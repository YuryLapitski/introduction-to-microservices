package com.epam.learn.song_service.controller;

import com.epam.learn.song_service.model.ErrorResponse;
import com.epam.learn.song_service.model.ErrorResponseWithDetails;

import org.apache.coyote.BadRequestException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex) {
        return createErrorResponse(ex.getMessage(), "400", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ChangeSetPersister.NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(ChangeSetPersister.NotFoundException ex) {
        return createErrorResponse(ex.getMessage(), "404", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        return createErrorResponse(ex.getMessage(), "500", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException ex) {
        return createErrorResponse(ex.getMessage(), "404", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        return createErrorResponse(ex.getMessage(), "409", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        ErrorResponseWithDetails errorResponse;
        if (ex.getPropertyName().equals("id")) {
            return createErrorResponse(String.format("Invalid value '%s' for ID. Must be a positive integer",
                    ex.getValue()), "400", HttpStatus.BAD_REQUEST);
        } else {
            return createErrorResponse(ex.getMessage(), "400", HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseWithDetails> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> details = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            details.put(fieldName, errorMessage);
        });
        return createErrorResponseWithDetails("Validation error", details, "400", HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ErrorResponse> createErrorResponse(String message, String code, HttpStatus status) {
        ErrorResponse errorResponse = new ErrorResponse(message, code);
        return new ResponseEntity<>(errorResponse, status);
    }

    private ResponseEntity<ErrorResponseWithDetails> createErrorResponseWithDetails(String message,
                                                                                    Map<String, String> details,
                                                                                    String code,
                                                                                    HttpStatus status) {
        ErrorResponseWithDetails errorResponse = new ErrorResponseWithDetails(message, details, code);
        return new ResponseEntity<>(errorResponse, status);
    }
}