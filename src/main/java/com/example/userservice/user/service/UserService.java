package com.example.userservice.user.service;

import com.example.userservice.user.model.User;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Map;

/**
 * Service interface for User operations.
 */
public interface UserService {

    /**
     * Retrieve a user by ID.
     *
     * @param id The ObjectId of the user to retrieve
     * @return The user with the given ID
     * @throws com.example.userservice.common.exception.ResourceNotFoundException if user not found
     */
    User getUserById(ObjectId id);

    /**
     * Retrieve all users.
     *
     * @return A list of all users
     */
    List<User> getAllUsers();

    /**
     * Create a new user.
     *
     * @param user The user to create
     * @return The created user with generated ID
     * @throws com.example.userservice.common.exception.DuplicateResourceException if email already exists
     * @throws com.example.userservice.common.exception.BadRequestException if user data is invalid
     */
    User createUser(User user);

    /**
     * Update a user completely.
     *
     * @param id The ObjectId of the user to update
     * @param user The updated user data
     * @return The updated user
     * @throws com.example.userservice.common.exception.ResourceNotFoundException if user not found
     * @throws com.example.userservice.common.exception.DuplicateResourceException if email already exists
     * @throws com.example.userservice.common.exception.BadRequestException if user data is invalid
     */
    User updateUser(ObjectId id, User user);

    /**
     * Update a user partially.
     *
     * @param id The ObjectId of the user to update
     * @param fields Map of field names to updated values
     * @return The updated user
     * @throws com.example.userservice.common.exception.ResourceNotFoundException if user not found
     * @throws com.example.userservice.common.exception.DuplicateResourceException if email already exists
     * @throws com.example.userservice.common.exception.BadRequestException if user data is invalid
     */
    User patchUser(ObjectId id, Map<String, Object> fields);

    /**
     * Delete a user.
     *
     * @param id The ObjectId of the user to delete
     * @throws com.example.userservice.common.exception.ResourceNotFoundException if user not found
     */
    void deleteUser(ObjectId id);

    /**
     * Check if a user with the given email exists.
     *
     * @param email The email to check
     * @return true if a user with the email exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Find a user by email.
     *
     * @param email The email to search for
     * @return The user with the given email
     * @throws com.example.userservice.common.exception.ResourceNotFoundException if user not found
     */
    User getUserByEmail(String email);
}