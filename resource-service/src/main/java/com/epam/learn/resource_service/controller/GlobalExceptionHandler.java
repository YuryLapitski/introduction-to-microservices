package com.epam.learn.resource_service.controller;

import com.epam.learn.resource_service.model.ErrorResponse;
import org.apache.coyote.BadRequestException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleUnsupportedMediaType(HttpMediaTypeNotSupportedException ex) {
        return createErrorResponse("Unsupported media type. Received Content-Type: "
                + (ex.getContentType() != null ? ex.getContentType().toString() : "undefined")
                + ". Accepted format: 'audio/mpeg'", "400", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        ErrorResponse errorResponse;
        if (ex.getPropertyName().equals("id")) {
            return createErrorResponse(String.format("Invalid value '%s' for ID. Must be a positive integer",
                            ex.getValue()), "400", HttpStatus.BAD_REQUEST);
        } else {
            return createErrorResponse(ex.getMessage(), "400", HttpStatus.BAD_REQUEST);
        }
    }

    private ResponseEntity<ErrorResponse> createErrorResponse(String message, String code, HttpStatus status) {
        ErrorResponse errorResponse = new ErrorResponse(message, code);
        return new ResponseEntity<>(errorResponse, status);
    }
}