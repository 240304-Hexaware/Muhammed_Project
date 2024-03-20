package com.example.revatureproject.repositories;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.revatureproject.models.User;

@Repository
public interface MyRepository extends MongoRepository<User, ObjectId> {
    Optional<User> findByUsername(String username);
}