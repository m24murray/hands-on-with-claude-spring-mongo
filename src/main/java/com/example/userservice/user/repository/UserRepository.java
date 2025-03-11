package com.example.userservice.user.repository;

import com.example.userservice.user.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId>, UserRepositoryCustom {

    /**
     * Find a user by email, case-insensitive
     * 
     * @param email the email address to search for
     * @return an Optional containing the user if found, empty otherwise
     */
    @Query("{ 'email': { $regex: ?0, $options: 'i' } }")
    Optional<User> findByEmailIgnoreCase(String email);
    
    /**
     * Check if a user exists with the given email, case-insensitive
     * 
     * @param email the email address to check
     * @return true if a user exists with the email, false otherwise
     */
    @Query(value = "{ 'email': { $regex: ?0, $options: 'i' } }", exists = true)
    boolean existsByEmailIgnoreCase(String email);
}