package com.example.userservice.user.repository;

import com.example.userservice.user.model.User;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRepositoryTest {

    @Mock
    private MongoTemplate mongoTemplate;
    
    private UserRepositoryCustomImpl userRepositoryCustom;
    
    @BeforeEach
    void setUp() {
        userRepositoryCustom = new UserRepositoryCustomImpl(mongoTemplate);
    }
    
    @Test
    void isEmailUniqueShouldReturnTrueWhenEmailDoesNotExist() {
        // Given
        String email = "test@example.com";
        when(mongoTemplate.exists(any(Query.class), eq(User.class))).thenReturn(false);
        
        // When
        boolean result = userRepositoryCustom.isEmailUnique(email, null);
        
        // Then
        assertTrue(result);
    }
    
    @Test
    void isEmailUniqueShouldReturnFalseWhenEmailExists() {
        // Given
        String email = "test@example.com";
        when(mongoTemplate.exists(any(Query.class), eq(User.class))).thenReturn(true);
        
        // When
        boolean result = userRepositoryCustom.isEmailUnique(email, null);
        
        // Then
        assertFalse(result);
    }
    
    @Test
    void isEmailUniqueShouldExcludeUserWhenIdProvided() {
        // Given
        String email = "test@example.com";
        String userId = new ObjectId().toHexString();
        when(mongoTemplate.exists(any(Query.class), eq(User.class))).thenReturn(false);
        
        // When
        boolean result = userRepositoryCustom.isEmailUnique(email, userId);
        
        // Then
        assertTrue(result);
    }
    
    @Test
    void isEmailUniqueShouldHandleInvalidUserId() {
        // Given
        String email = "test@example.com";
        String invalidUserId = "not-a-valid-objectid";
        when(mongoTemplate.exists(any(Query.class), eq(User.class))).thenReturn(false);
        
        // When
        boolean result = userRepositoryCustom.isEmailUnique(email, invalidUserId);
        
        // Then
        assertTrue(result);
    }
    
    @Test
    void isEmailUniqueShouldReturnTrueForEmptyEmail() {
        // When
        boolean result = userRepositoryCustom.isEmailUnique("", null);
        
        // Then
        assertTrue(result);
    }
    
    @Test
    void findByExactEmailShouldReturnUserWhenFound() {
        // Given
        String email = "test@example.com";
        User user = new User("Test User", email);
        when(mongoTemplate.findOne(any(Query.class), eq(User.class))).thenReturn(user);
        
        // When
        Optional<User> result = userRepositoryCustom.findByExactEmail(email);
        
        // Then
        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
    }
    
    @Test
    void findByExactEmailShouldReturnEmptyWhenNotFound() {
        // Given
        String email = "test@example.com";
        when(mongoTemplate.findOne(any(Query.class), eq(User.class))).thenReturn(null);
        
        // When
        Optional<User> result = userRepositoryCustom.findByExactEmail(email);
        
        // Then
        assertTrue(result.isEmpty());
    }
    
    @Test
    void findByExactEmailShouldReturnEmptyForEmptyEmail() {
        // When
        Optional<User> result = userRepositoryCustom.findByExactEmail("");
        
        // Then
        assertTrue(result.isEmpty());
    }
}