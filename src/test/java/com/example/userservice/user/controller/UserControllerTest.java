package com.example.userservice.user.controller;

import com.example.userservice.common.exception.BadRequestException;
import com.example.userservice.common.exception.DuplicateResourceException;
import com.example.userservice.common.exception.ResourceNotFoundException;
import com.example.userservice.user.model.User;
import com.example.userservice.user.service.UserService;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
//@ContextConfiguration(classes = UserControllerTest.TestConfig.class)
class UserControllerTest {

//    @Configuration
//    @EnableAutoConfiguration(exclude = {
//            MongoAutoConfiguration.class,
//            MongoDataAutoConfiguration.class
//    })
//    static class TestConfig {
//        // Configure Jackson for ObjectId serialization
//        @Bean
//        public ObjectMapper objectMapper() {
//            ObjectMapper objectMapper = new ObjectMapper();
//            SimpleModule module = new SimpleModule();
//            module.addSerializer(ObjectId.class, new JsonSerializer<ObjectId>() {
//                @Override
//                public void serialize(ObjectId value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
//                    gen.writeString(value.toHexString());
//                }
//            });
//            objectMapper.registerModule(module);
//            return objectMapper;
//        }
//    }
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private User user1;
    private User user2;
    private ObjectId userId1;
    private ObjectId userId2;

    @BeforeEach
    void setUp() {
        userId1 = new ObjectId();
        userId2 = new ObjectId();

        user1 = new User("John Doe", "john.doe@example.com");
        user1.setId(userId1);

        user2 = new User("Jane Smith", "jane.smith@example.com");
        user2.setId(userId2);
    }

    @Test
    void getUserByIdShouldReturnUser() throws Exception {
        // Given
        when(userService.getUserById(any(ObjectId.class))).thenReturn(user1);

        // When & Then
        mockMvc.perform(get("/api/v1/users/{id}", userId1.toHexString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("John Doe")))
                .andExpect(jsonPath("$.email", is("john.doe@example.com")));
    }

    @Test
    void getUserByIdShouldReturn404WhenUserNotFound() throws Exception {
        // Given
        ObjectId nonExistentId = new ObjectId();
        when(userService.getUserById(any(ObjectId.class)))
                .thenThrow(new ResourceNotFoundException("User", "id", nonExistentId));

        // When & Then
        mockMvc.perform(get("/api/v1/users/{id}", nonExistentId.toHexString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getUsersShouldReturnListOfUsers() throws Exception {
        // Given
        List<User> users = Arrays.asList(user1, user2);
        when(userService.getAllUsers()).thenReturn(users);

        // When & Then
        mockMvc.perform(get("/api/v1/users")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("John Doe")))
                .andExpect(jsonPath("$[0].email", is("john.doe@example.com")))
                .andExpect(jsonPath("$[1].name", is("Jane Smith")))
                .andExpect(jsonPath("$[1].email", is("jane.smith@example.com")));
    }

    @Test
    void getUsersShouldReturnEmptyListWhenNoUsers() throws Exception {
        // Given
        when(userService.getAllUsers()).thenReturn(List.of());

        // When & Then
        mockMvc.perform(get("/api/v1/users")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }
    
    @Test
    void getUsersWithEmailShouldReturnMatchingUser() throws Exception {
        // Given
        when(userService.getUserByEmail("john.doe@example.com")).thenReturn(user1);

        // When & Then
        mockMvc.perform(get("/api/v1/users")
                .param("email", "john.doe@example.com")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("John Doe")))
                .andExpect(jsonPath("$[0].email", is("john.doe@example.com")));
    }
    
    @Test
    void getUsersWithEmailShouldReturn404WhenUserNotFound() throws Exception {
        // Given
        String nonExistentEmail = "nonexistent@example.com";
        when(userService.getUserByEmail(nonExistentEmail))
                .thenThrow(new ResourceNotFoundException("User", "email", nonExistentEmail));

        // When & Then
        mockMvc.perform(get("/api/v1/users")
                .param("email", nonExistentEmail)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void createUserShouldReturnCreatedUser() throws Exception {
        // Given
        User newUser = new User("New User", "new.user@example.com");
        User savedUser = new User("New User", "new.user@example.com");
        savedUser.setId(new ObjectId());
        
        when(userService.createUser(any(User.class))).thenReturn(savedUser);
        
        // When & Then
        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUser))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("New User")))
                .andExpect(jsonPath("$.email", is("new.user@example.com")));
        
        verify(userService).createUser(any(User.class));
    }
    
    @Test
    void createUserShouldReturn400WithInvalidUser() throws Exception {
        // Given
        User invalidUser = new User("", "");
        
        // When & Then
        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidUser))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        
        verify(userService, never()).createUser(any(User.class));
    }
    
    @Test
    void createUserShouldReturn409WhenEmailAlreadyExists() throws Exception {
        // Given
        User newUser = new User("John Doe", "john.doe@example.com");
        when(userService.createUser(any(User.class)))
                .thenThrow(new DuplicateResourceException("User", "email", "john.doe@example.com"));
        
        // When & Then
        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUser))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }
    
    @Test
    void updateUserShouldReturnUpdatedUser() throws Exception {
        // Given
        User updateUser = new User("Updated User", "updated.user@example.com");
        User updatedUser = new User("Updated User", "updated.user@example.com");
        updatedUser.setId(userId1);
        
        when(userService.updateUser(eq(userId1), any(User.class))).thenReturn(updatedUser);
        
        // When & Then
        mockMvc.perform(put("/api/v1/users/{id}", userId1.toHexString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateUser))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("Updated User")))
                .andExpect(jsonPath("$.email", is("updated.user@example.com")));
        
        verify(userService).updateUser(eq(userId1), any(User.class));
    }
    
    @Test
    void updateUserShouldReturn404WhenUserNotFound() throws Exception {
        // Given
        ObjectId nonExistentId = new ObjectId();
        User updateUser = new User("Updated User", "updated.user@example.com");
        
        when(userService.updateUser(eq(nonExistentId), any(User.class)))
                .thenThrow(new ResourceNotFoundException("User", "id", nonExistentId));
        
        // When & Then
        mockMvc.perform(put("/api/v1/users/{id}", nonExistentId.toHexString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateUser))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void patchUserShouldReturnUpdatedUser() throws Exception {
        // Given
        Map<String, Object> patchFields = new HashMap<>();
        patchFields.put("name", "Patched User");
        
        User patchedUser = new User("Patched User", "john.doe@example.com");
        patchedUser.setId(userId1);
        
        when(userService.patchUser(eq(userId1), any(Map.class))).thenReturn(patchedUser);
        
        // When & Then
        mockMvc.perform(patch("/api/v1/users/{id}", userId1.toHexString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patchFields))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("Patched User")))
                .andExpect(jsonPath("$.email", is("john.doe@example.com")));
        
        verify(userService).patchUser(eq(userId1), any(Map.class));
    }
    
    @Test
    void patchUserShouldReturn404WhenUserNotFound() throws Exception {
        // Given
        ObjectId nonExistentId = new ObjectId();
        Map<String, Object> patchFields = new HashMap<>();
        patchFields.put("name", "Patched User");
        
        when(userService.patchUser(eq(nonExistentId), any(Map.class)))
                .thenThrow(new ResourceNotFoundException("User", "id", nonExistentId));
        
        // When & Then
        mockMvc.perform(patch("/api/v1/users/{id}", nonExistentId.toHexString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patchFields))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void deleteUserShouldReturn204() throws Exception {
        // Given
        doNothing().when(userService).deleteUser(userId1);
        
        // When & Then
        mockMvc.perform(delete("/api/v1/users/{id}", userId1.toHexString()))
                .andExpect(status().isNoContent());
                
        verify(userService).deleteUser(userId1);
    }
    
    @Test
    void deleteUserShouldReturn404WhenUserNotFound() throws Exception {
        // Given
        ObjectId nonExistentId = new ObjectId();
        doThrow(new ResourceNotFoundException("User", "id", nonExistentId))
                .when(userService).deleteUser(eq(nonExistentId));
        
        // When & Then
        mockMvc.perform(delete("/api/v1/users/{id}", nonExistentId.toHexString()))
                .andExpect(status().isNotFound());
    }
}