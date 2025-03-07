package com.example.userservice.user.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@Data
@NoArgsConstructor
@Document(collection = "users")
public class User {

    @Id
    private UUID id;

    @NotBlank(message = "Name is required")
    @Size(min = 4, max = 30, message = "Name must be between 4 and 30 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Indexed(unique = true)
    private String email;

    public User(String name, String email) {
        this.id = UUID.randomUUID();
        setName(name);
        setEmail(email);
    }
    
    public void setName(String name) {
        this.name = name != null ? name.trim() : null;
    }
    
    public void setEmail(String email) {
        this.email = email != null ? email.trim().toLowerCase() : null;
    }
}