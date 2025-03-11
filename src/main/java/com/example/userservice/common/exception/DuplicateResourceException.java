package com.example.userservice.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when an attempt is made to create a resource with a unique constraint violation.
 * Will result in HTTP 409 Conflict response.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }

    public DuplicateResourceException(String resourceType, String fieldName, Object fieldValue) {
        super(String.format("%s already exists with %s: '%s'", resourceType, fieldName, fieldValue));
    }
}