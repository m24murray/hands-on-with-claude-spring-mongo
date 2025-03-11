package com.example.userservice.user.controller;

import com.example.userservice.user.model.User;
import com.example.userservice.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * REST controller for User operations.
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User", description = "User management API")
public class UserController {

    private final UserService userService;

    /**
     * Get a user by ID.
     *
     * @param id The ID of the user to retrieve
     * @return The user with the given ID
     */
    @GetMapping("/{id}")
    @Operation(
        summary = "Get a user by ID",
        description = "Retrieves a user by their unique identifier"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "User found",
            content = @Content(schema = @Schema(implementation = User.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User not found",
            content = @Content
        )
    })
    public ResponseEntity<User> getUserById(
            @Parameter(description = "The ID of the user to retrieve", required = true)
            @PathVariable String id) {
        log.debug("REST request to get User by ID: {}", id);
        ObjectId objectId = new ObjectId(id);
        User user = userService.getUserById(objectId);
        return ResponseEntity.ok(user);
    }

    /**
     * Get all users or search by email.
     *
     * @param email Optional email to search for
     * @return List of users matching the criteria
     */
    @GetMapping
    @Operation(
        summary = "Get all users or search by email",
        description = "Retrieves a list of all users or searches for users by email"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "List of users retrieved successfully",
            content = @Content(schema = @Schema(implementation = User.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "No users found matching criteria",
            content = @Content
        )
    })
    public ResponseEntity<List<User>> getUsers(
            @Parameter(description = "Email to search for (optional)")
            @RequestParam(required = false) String email) {
        log.debug("REST request to get Users with email filter: {}", email);
        
        if (StringUtils.hasText(email)) {
            // If email is provided, search by email
            User user = userService.getUserByEmail(email);
            return ResponseEntity.ok(Collections.singletonList(user));
        } else {
            // Otherwise, get all users
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        }
    }
    
    /**
     * Create a new user.
     *
     * @param user The user data to create
     * @return The created user
     */
    @PostMapping
    @Operation(
        summary = "Create a new user",
        description = "Creates a new user with the provided data"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "User created successfully",
            content = @Content(schema = @Schema(implementation = User.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid user data provided",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Email already exists",
            content = @Content
        )
    })
    public ResponseEntity<User> createUser(
            @Parameter(description = "User data to create", required = true)
            @Valid @RequestBody User user) {
        log.debug("REST request to create User: {}", user);
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
    
    /**
     * Update a user completely.
     *
     * @param id The ID of the user to update
     * @param user The updated user data
     * @return The updated user
     */
    @PutMapping("/{id}")
    @Operation(
        summary = "Update a user completely",
        description = "Fully updates a user with the provided data"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "User updated successfully",
            content = @Content(schema = @Schema(implementation = User.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid user data provided",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User not found",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Email already exists",
            content = @Content
        )
    })
    public ResponseEntity<User> updateUser(
            @Parameter(description = "The ID of the user to update", required = true)
            @PathVariable String id,
            @Parameter(description = "Updated user data", required = true)
            @Valid @RequestBody User user) {
        log.debug("REST request to update User: {} with data: {}", id, user);
        ObjectId objectId = new ObjectId(id);
        User updatedUser = userService.updateUser(objectId, user);
        return ResponseEntity.ok(updatedUser);
    }
    
    /**
     * Partially update a user.
     *
     * @param id The ID of the user to update
     * @param fields Map of field names to updated values
     * @return The updated user
     */
    @PatchMapping("/{id}")
    @Operation(
        summary = "Partially update a user",
        description = "Updates specific fields of a user"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "User updated successfully",
            content = @Content(schema = @Schema(implementation = User.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid field data provided",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User not found",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Email already exists",
            content = @Content
        )
    })
    public ResponseEntity<User> patchUser(
            @Parameter(description = "The ID of the user to update", required = true)
            @PathVariable String id,
            @Parameter(description = "Fields to update", required = true)
            @RequestBody Map<String, Object> fields) {
        log.debug("REST request to patch User: {} with fields: {}", id, fields);
        ObjectId objectId = new ObjectId(id);
        User patchedUser = userService.patchUser(objectId, fields);
        return ResponseEntity.ok(patchedUser);
    }
    
    /**
     * Delete a user.
     *
     * @param id The ID of the user to delete
     * @return Empty response with 204 No Content status
     */
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete a user",
        description = "Deletes a user by their ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "User deleted successfully",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User not found",
            content = @Content
        )
    })
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "The ID of the user to delete", required = true)
            @PathVariable String id) {
        log.debug("REST request to delete User: {}", id);
        ObjectId objectId = new ObjectId(id);
        userService.deleteUser(objectId);
        return ResponseEntity.noContent().build();
    }
}