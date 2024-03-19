package com.example.revatureproject.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.revatureproject.models.GenericRecord;

public interface GenericRecordRepository extends MongoRepository<GenericRecord, ObjectId>{
}
