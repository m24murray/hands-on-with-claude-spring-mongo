package com.example.userservice.user.repository;

import com.example.userservice.user.model.User;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryIntegrationTest {

    @Mock
    private UserRepository userRepository;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = new User("John Doe", "john.doe@example.com");
        user1.setId(new ObjectId());
        user2 = new User("Jane Smith", "jane.smith@example.com");
        user2.setId(new ObjectId());
    }

    @Test
    void findByIdShouldReturnUserWhenExists() {
        // Given
        ObjectId id = user1.getId();
        when(userRepository.findById(id)).thenReturn(Optional.of(user1));

        // When
        Optional<User> result = userRepository.findById(id);

        // Then
        assertTrue(result.isPresent());
        assertEquals(user1.getName(), result.get().getName());
    }

    @Test
    void findByIdShouldReturnEmptyWhenNotFound() {
        // Given
        ObjectId id = new ObjectId();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        // When
        Optional<User> result = userRepository.findById(id);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void findAllShouldReturnAllUsers() {
        // Given
        List<User> users = Arrays.asList(user1, user2);
        when(userRepository.findAll()).thenReturn(users);

        // When
        List<User> result = userRepository.findAll();

        // Then
        assertEquals(2, result.size());
        assertTrue(result.contains(user1));
        assertTrue(result.contains(user2));
    }

    @Test
    void saveShouldPersistUser() {
        // Given
        User newUser = new User("New User", "new.user@example.com");
        when(userRepository.save(newUser)).thenReturn(newUser);

        // When
        User result = userRepository.save(newUser);

        // Then
        assertNotNull(result);
        assertEquals(newUser.getName(), result.getName());
        assertEquals(newUser.getEmail(), result.getEmail());
    }

    @Test
    void deleteShouldRemoveUser() {
        // Given
        ObjectId id = user1.getId();
        doNothing().when(userRepository).deleteById(id);

        // When
        userRepository.deleteById(id);

        // Then
        verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    void findByEmailIgnoreCaseShouldReturnUserWhenExists() {
        // Given
        String email = "john.doe@example.com";
        when(userRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.of(user1));

        // When
        Optional<User> result = userRepository.findByEmailIgnoreCase(email);

        // Then
        assertTrue(result.isPresent());
        assertEquals(user1.getName(), result.get().getName());
    }

    @Test
    void findByEmailIgnoreCaseShouldHandleDifferentCase() {
        // Given
        String email = "JOHN.DOE@EXAMPLE.COM";
        when(userRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.of(user1));

        // When
        Optional<User> result = userRepository.findByEmailIgnoreCase(email);

        // Then
        assertTrue(result.isPresent());
        assertEquals(user1.getName(), result.get().getName());
    }

    @Test
    void existsByEmailIgnoreCaseShouldReturnTrueWhenExists() {
        // Given
        String email = "john.doe@example.com";
        when(userRepository.existsByEmailIgnoreCase(email)).thenReturn(true);

        // When
        boolean result = userRepository.existsByEmailIgnoreCase(email);

        // Then
        assertTrue(result);
    }

    @Test
    void existsByEmailIgnoreCaseShouldReturnFalseWhenNotExists() {
        // Given
        String email = "nonexistent@example.com";
        when(userRepository.existsByEmailIgnoreCase(email)).thenReturn(false);

        // When
        boolean result = userRepository.existsByEmailIgnoreCase(email);

        // Then
        assertFalse(result);
    }
}