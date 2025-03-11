package com.example.userservice.user.repository;

import com.example.userservice.user.model.User;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;

import java.util.Optional;

@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public boolean isEmailUnique(String email, String excludeUserId) {
        if (!StringUtils.hasText(email)) {
            return true; // Empty emails are considered unique (they'll be caught by validation)
        }

        Criteria criteria = Criteria.where("email").regex("^" + email + "$", "i");
        
        // If excludeUserId is provided, exclude that user from the check
        if (StringUtils.hasText(excludeUserId)) {
            try {
                ObjectId objectId = new ObjectId(excludeUserId);
                criteria = criteria.and("id").ne(objectId);
            } catch (IllegalArgumentException e) {
                // If ObjectId is invalid, ignore the exclusion
            }
        }
        
        Query query = new Query(criteria);
        return !mongoTemplate.exists(query, User.class);
    }

    @Override
    public Optional<User> findByExactEmail(String email) {
        if (!StringUtils.hasText(email)) {
            return Optional.empty();
        }
        
        Query query = new Query(Criteria.where("email").is(email));
        User user = mongoTemplate.findOne(query, User.class);
        return Optional.ofNullable(user);
    }
}