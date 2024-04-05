package com.example.revatureproject.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.example.revatureproject.auth.JwtTokenProvider;
import com.example.revatureproject.exceptions.ItemNotFoundException;
import com.example.revatureproject.models.GenericRecord;
import com.example.revatureproject.repositories.GenericRecordRepository;
import com.example.revatureproject.repositories.MetadataRepository;

@Service
public class RecordService {

    private GenericRecordRepository genericRecordRepository;
    private MongoTemplate mongoTemplate;

    @Autowired
    public RecordService(GenericRecordRepository genericRecordRepository, MetadataRepository metadataRepository, MongoTemplate mongoTemplate, JwtTokenProvider jwtTokenProvider) {
        this.genericRecordRepository = genericRecordRepository;
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
    public List<String> findAllRecords(String recordUser) {
        List<GenericRecord> records = genericRecordRepository.findByRecordUser(recordUser);
        List<String> recordsList = new ArrayList<>();
        for(Document record : records) {
            recordsList.add(record.toJson().toString());
        }
        return recordsList;
    }

    /**
     * Finds records in the "record" collection based on provided filter parameters
     *
     * @param params A Map containing key-value pairs representing filter criteria
     *                - Key: Field name to filter on
     *                - Value: The value to match for the corresponding field
     * @return A List of Strings representing the JSON representations of the found records
     * @throws ItemNotFoundException If no records are found matching the filter criteria
     */
    public List<String> findFilteredRecords(Map<String, String> params) throws ItemNotFoundException {
        // Create a new empty query object
        Query query = new Query();
        // Iterate through each key-value pair in the provided filter parameters
        params.forEach((key, value) -> {
            // Add a filter criteria to the query for each parameter
            query.addCriteria(Criteria.where(key).is(value));
        });
        // Use the mongoTemplate to find documents in the "record" collection matching the built query
        List<Document> documents = mongoTemplate.find(query, Document.class, "record");
        // Check if any documents were found
        if (documents.size() == 0) {
            // If no documents were found, throw an ItemNotFoundException
            throw new ItemNotFoundException("No records found with filter");
        }
        // Convert each Document object obtained from MongoDB into a JSON string representation
        List<String> jsonRecords = documents.stream()
                                            .map(Document::toJson)
                                            .collect(Collectors.toList());
        // Return the list of JSON strings representing the filtered records
        return jsonRecords;
    }
}
