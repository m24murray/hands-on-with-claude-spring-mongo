package com.example.userservice.user.service;

import com.example.userservice.common.exception.BadRequestException;
import com.example.userservice.common.exception.DuplicateResourceException;
import com.example.userservice.common.exception.ResourceNotFoundException;
import com.example.userservice.user.model.User;
import com.example.userservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Implementation of the UserService interface.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getUserById(ObjectId id) {
        log.debug("Getting user with ID: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    @Override
    public List<User> getAllUsers() {
        log.debug("Getting all users");
        return userRepository.findAll();
    }

    @Override
    public User createUser(User user) {
        if (user == null) {
            throw new BadRequestException("User cannot be null");
        }

        log.debug("Creating new user with email: {}", user.getEmail());
        
        // Check for email uniqueness
        if (userRepository.existsByEmailIgnoreCase(user.getEmail())) {
            throw new DuplicateResourceException("User", "email", user.getEmail());
        }
        
        return userRepository.save(user);
    }

    @Override
    public User updateUser(ObjectId id, User user) {
        if (user == null) {
            throw new BadRequestException("User cannot be null");
        }

        log.debug("Updating user with ID: {}", id);
        
        // Check if user exists
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        
        // Check for email uniqueness (only if email is changing)
        if (!existingUser.getEmail().equalsIgnoreCase(user.getEmail()) &&
            userRepository.existsByEmailIgnoreCase(user.getEmail())) {
            throw new DuplicateResourceException("User", "email", user.getEmail());
        }
        
        // Preserve the ID
        user.setId(id);
        
        return userRepository.save(user);
    }

    @Override
    public User patchUser(ObjectId id, Map<String, Object> fields) {
        if (fields == null || fields.isEmpty()) {
            throw new BadRequestException("No fields provided for update");
        }

        log.debug("Partially updating user with ID: {}", id);
        
        // Check if user exists
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        
        // Apply updates to the fields
        boolean updated = false;
        
        if (fields.containsKey("name")) {
            Object nameValue = fields.get("name");
            if (nameValue == null) {
                throw new BadRequestException("Name cannot be null");
            }
            String name = nameValue.toString();
            if (!StringUtils.hasText(name)) {
                throw new BadRequestException("Name cannot be empty");
            }
            user.setName(name);
            updated = true;
        }
        
        if (fields.containsKey("email")) {
            Object emailValue = fields.get("email");
            if (emailValue == null) {
                throw new BadRequestException("Email cannot be null");
            }
            String email = emailValue.toString();
            if (!StringUtils.hasText(email)) {
                throw new BadRequestException("Email cannot be empty");
            }
            
            // Check email uniqueness (only if email is changing)
            if (!user.getEmail().equalsIgnoreCase(email) &&
                userRepository.existsByEmailIgnoreCase(email)) {
                throw new DuplicateResourceException("User", "email", email);
            }
            
            user.setEmail(email);
            updated = true;
        }
        
        if (!updated) {
            throw new BadRequestException("No valid fields provided for update");
        }
        
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(ObjectId id) {
        log.debug("Deleting user with ID: {}", id);
        
        // Check if user exists
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User", "id", id);
        }
        
        userRepository.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        if (!StringUtils.hasText(email)) {
            return false;
        }
        
        return userRepository.existsByEmailIgnoreCase(email);
    }

    @Override
    public User getUserByEmail(String email) {
        if (!StringUtils.hasText(email)) {
            throw new BadRequestException("Email cannot be empty");
        }
        
        log.debug("Getting user with email: {}", email);
        
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }
}