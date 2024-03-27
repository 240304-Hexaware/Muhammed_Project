package com.example.revatureproject.repositories;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.revatureproject.models.Metadata;

public interface MetadataRepository extends MongoRepository<Metadata, ObjectId> {
    List<Metadata> findAll();
}