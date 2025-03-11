package com.example.userservice.user.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldCreateValidUser() {
        // Given
        User user = new User("John Doe", "john.doe@example.com");
        
        // When
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        
        // Then
        assertTrue(violations.isEmpty());
        assertEquals("John Doe", user.getName());
        assertEquals("john.doe@example.com", user.getEmail());
    }

    @Test
    void shouldTrimNameAndEmail() {
        // Given
        User user = new User("  John Doe  ", "  john.doe@example.com  ");
        
        // Then
        assertEquals("John Doe", user.getName());
        assertEquals("john.doe@example.com", user.getEmail());
    }

    @Test
    void shouldConvertEmailToLowercase() {
        // Given
        User user = new User("John Doe", "JOHN.DOE@EXAMPLE.COM");
        
        // Then
        assertEquals("john.doe@example.com", user.getEmail());
    }

    @Test
    void shouldRejectEmptyName() {
        // Given
        User user = new User("", "john.doe@example.com");

        // When
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        // Then
        assertFalse(violations.isEmpty());
        assertEquals(2, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")
                        && v.getMessage().contains("Name is required")),
                "Name is required");

        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")
                        && v.getMessage().contains("Name must be between 4 and 30 characters")),
                "Name must be between 4 and 30 characters");
    }

    @Test
    void shouldRejectShortName() {
        // Given
        User user = new User("Jo", "john.doe@example.com");
        
        // When
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        
        // Then
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Name must be between 4 and 30 characters", violations.iterator().next().getMessage());
    }

    @Test
    void shouldRejectLongName() {
        // Given
        User user = new User("John Doe with a very very very long name", "john.doe@example.com");
        
        // When
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        
        // Then
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Name must be between 4 and 30 characters", violations.iterator().next().getMessage());
    }

    @Test
    void shouldRejectInvalidEmail() {
        // Given
        User user = new User("John Doe", "not-an-email");
        
        // When
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        
        // Then
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Email must be valid", violations.iterator().next().getMessage());
    }

    @Test
    void shouldRejectEmptyEmail() {
        // Given
        User user = new User("John Doe", "");
        
        // When
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        
        // Then
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Email is required", violations.iterator().next().getMessage());
    }
}