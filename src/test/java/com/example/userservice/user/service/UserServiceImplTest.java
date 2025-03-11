package com.example.userservice.user.service;

import com.example.userservice.common.exception.BadRequestException;
import com.example.userservice.common.exception.DuplicateResourceException;
import com.example.userservice.common.exception.ResourceNotFoundException;
import com.example.userservice.user.model.User;
import com.example.userservice.user.repository.UserRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private ObjectId userId;

    @BeforeEach
    void setUp() {
        userId = new ObjectId();
        user = new User("John Doe", "john.doe@example.com");
        user.setId(userId);
    }

    @Test
    void getUserByIdShouldReturnUserWhenExists() {
        // Given
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // When
        User result = userService.getUserById(userId);

        // Then
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("John Doe", result.getName());
        assertEquals("john.doe@example.com", result.getEmail());
    }

    @Test
    void getUserByIdShouldThrowExceptionWhenNotFound() {
        // Given
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(userId));
    }

    @Test
    void getAllUsersShouldReturnListOfUsers() {
        // Given
        User user2 = new User("Jane Smith", "jane.smith@example.com");
        user2.setId(new ObjectId());
        List<User> users = Arrays.asList(user, user2);
        when(userRepository.findAll()).thenReturn(users);

        // When
        List<User> result = userService.getAllUsers();

        // Then
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("Jane Smith", result.get(1).getName());
    }

    @Test
    void createUserShouldSaveAndReturnUser() {
        // Given
        User newUser = new User("New User", "new.user@example.com");
        when(userRepository.existsByEmailIgnoreCase(any())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        // When
        User result = userService.createUser(newUser);

        // Then
        assertNotNull(result);
        assertEquals("New User", result.getName());
        assertEquals("new.user@example.com", result.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUserShouldThrowExceptionWhenEmailExists() {
        // Given
        User newUser = new User("New User", "existing@example.com");
        when(userRepository.existsByEmailIgnoreCase("existing@example.com")).thenReturn(true);

        // When & Then
        assertThrows(DuplicateResourceException.class, () -> userService.createUser(newUser));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUserShouldThrowExceptionWhenUserIsNull() {
        // When & Then
        assertThrows(BadRequestException.class, () -> userService.createUser(null));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateUserShouldUpdateAndReturnUser() {
        // Given
        User updatedUser = new User("Updated Name", "updated@example.com");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmailIgnoreCase("updated@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        // When
        User result = userService.updateUser(userId, updatedUser);

        // Then
        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        assertEquals("updated@example.com", result.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUserShouldThrowExceptionWhenUserNotFound() {
        // Given
        User updatedUser = new User("Updated Name", "updated@example.com");
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(userId, updatedUser));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateUserShouldThrowExceptionWhenEmailExists() {
        // Given
        User updatedUser = new User("Updated Name", "existing@example.com");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmailIgnoreCase("existing@example.com")).thenReturn(true);

        // When & Then
        assertThrows(DuplicateResourceException.class, () -> userService.updateUser(userId, updatedUser));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateUserShouldNotCheckEmailWhenUnchanged() {
        // Given
        User updatedUser = new User("Updated Name", "john.doe@example.com");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        // When
        User result = userService.updateUser(userId, updatedUser);

        // Then
        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        assertEquals("john.doe@example.com", result.getEmail());
        verify(userRepository, never()).existsByEmailIgnoreCase(anyString());
    }

    @Test
    void patchUserShouldPartiallyUpdateAndReturnUser() {
        // Given
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "Patched Name");
        
        User patchedUser = new User("Patched Name", "john.doe@example.com");
        patchedUser.setId(userId);
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(patchedUser);

        // When
        User result = userService.patchUser(userId, fields);

        // Then
        assertNotNull(result);
        assertEquals("Patched Name", result.getName());
        assertEquals("john.doe@example.com", result.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void patchUserShouldThrowExceptionWhenUserNotFound() {
        // Given
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "Patched Name");
        
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> userService.patchUser(userId, fields));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void patchUserShouldThrowExceptionWhenFieldsEmpty() {
        // Given
        Map<String, Object> fields = new HashMap<>();
        
        // When & Then
        assertThrows(BadRequestException.class, () -> userService.patchUser(userId, fields));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void patchUserShouldThrowExceptionWhenNameNull() {
        // Given
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", null);
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // When & Then
        assertThrows(BadRequestException.class, () -> userService.patchUser(userId, fields));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void patchUserShouldThrowExceptionWhenNameEmpty() {
        // Given
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "");
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // When & Then
        assertThrows(BadRequestException.class, () -> userService.patchUser(userId, fields));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void patchUserShouldThrowExceptionWhenEmailExists() {
        // Given
        Map<String, Object> fields = new HashMap<>();
        fields.put("email", "existing@example.com");
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmailIgnoreCase("existing@example.com")).thenReturn(true);

        // When & Then
        assertThrows(DuplicateResourceException.class, () -> userService.patchUser(userId, fields));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteUserShouldRemoveUser() {
        // Given
        when(userRepository.existsById(userId)).thenReturn(true);
        doNothing().when(userRepository).deleteById(userId);

        // When
        userService.deleteUser(userId);

        // Then
        verify(userRepository).deleteById(userId);
    }

    @Test
    void deleteUserShouldThrowExceptionWhenUserNotFound() {
        // Given
        when(userRepository.existsById(userId)).thenReturn(false);

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(userId));
        verify(userRepository, never()).deleteById(any());
    }

    @Test
    void existsByEmailShouldReturnTrueWhenExists() {
        // Given
        String email = "existing@example.com";
        when(userRepository.existsByEmailIgnoreCase(email)).thenReturn(true);

        // When
        boolean result = userService.existsByEmail(email);

        // Then
        assertTrue(result);
    }

    @Test
    void existsByEmailShouldReturnFalseWhenNotExists() {
        // Given
        String email = "nonexistent@example.com";
        when(userRepository.existsByEmailIgnoreCase(email)).thenReturn(false);

        // When
        boolean result = userService.existsByEmail(email);

        // Then
        assertFalse(result);
    }

    @Test
    void getUserByEmailShouldReturnUserWhenExists() {
        // Given
        String email = "john.doe@example.com";
        when(userRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.of(user));

        // When
        User result = userService.getUserByEmail(email);

        // Then
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("John Doe", result.getName());
        assertEquals("john.doe@example.com", result.getEmail());
    }

    @Test
    void getUserByEmailShouldThrowExceptionWhenNotFound() {
        // Given
        String email = "nonexistent@example.com";
        when(userRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserByEmail(email));
    }

    @Test
    void getUserByEmailShouldThrowExceptionWhenEmailEmpty() {
        // When & Then
        assertThrows(BadRequestException.class, () -> userService.getUserByEmail(""));
        verify(userRepository, never()).findByEmailIgnoreCase(anyString());
    }
}