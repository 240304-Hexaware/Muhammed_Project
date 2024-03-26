package com.example.revatureproject.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.example.revatureproject.exceptions.ItemNotFoundException;
import com.example.revatureproject.models.GenericRecord;
import com.example.revatureproject.repositories.GenericRecordRepository;
import com.example.revatureproject.repositories.MetadataRepository;

@Service
public class RecordService {

    private GenericRecordRepository genericRecordRepository;
    private MetadataRepository metadataRepository;
    private MongoTemplate mongoTemplate;

    public RecordService(GenericRecordRepository genericRecordRepository, MetadataRepository metadataRepository, MongoTemplate mongoTemplate) {
        this.genericRecordRepository = genericRecordRepository;
        this.metadataRepository = metadataRepository;
        this.mongoTemplate = mongoTemplate;
    }

    /*Get record by id, return record as a JSON */
    public String findRecordById(ObjectId id) throws ItemNotFoundException {
        Optional<GenericRecord> recordOptional = genericRecordRepository.findById(id);
        if(recordOptional.isPresent()) {
            Document record = recordOptional.get();
            String recordJson = record.toJson().toString();
            System.out.println(recordJson);
            return recordJson;
        }
        throw new ItemNotFoundException("Record does not exist");
    }

    /*Get all records */
    public List<String> findAllRecords() {
        List<GenericRecord> records = genericRecordRepository.findAll();
        List<String> recordsList = new ArrayList<>();
        for(Document record : records) {
            recordsList.add(record.toJson().toString());
        }
        return recordsList;
    }

    /*Get records based on fields*/
    public List<String> findFilteredRecords(Map<String, String> params) throws ItemNotFoundException {
        Query query = new Query();
        params.forEach((key, value) -> {
            query.addCriteria(Criteria.where(key).is(value));
        });
        List<Document> documents = mongoTemplate.find(query, Document.class, "record");
        if(documents.size() == 0) { throw new ItemNotFoundException("No records found with filter"); }
        return documents.stream().map(Document::toJson).collect(Collectors.toList()); // return as a JSON 
    }
}
