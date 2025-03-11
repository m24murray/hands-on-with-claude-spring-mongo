package com.example.userservice.user.repository;

import com.example.userservice.user.model.User;

import java.util.Optional;

/**
 * Custom repository interface for User-specific operations that cannot be
 * expressed with MongoRepository's method conventions
 */
public interface UserRepositoryCustom {
    
    /**
     * Validates if the email is unique in the database (case insensitive)
     * 
     * @param email the email to validate
     * @param excludeUserId optional user ID to exclude from the check (for updates)
     * @return true if email is unique, false otherwise
     */
    boolean isEmailUnique(String email, String excludeUserId);
    
    /**
     * Finds a user by exact email match (case sensitive)
     * 
     * @param email the exact email to find
     * @return an Optional containing the user if found, empty otherwise
     */
    Optional<User> findByExactEmail(String email);
}