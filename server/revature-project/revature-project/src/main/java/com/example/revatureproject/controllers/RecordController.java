package com.example.revatureproject.controllers;

import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.revatureproject.exceptions.ItemNotFoundException;
import com.example.revatureproject.services.RecordService;

@RestController
// @CrossOrigin(origins = "http://localhost:4200")
public class RecordController {

    private RecordService recordService;

    @Autowired
    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    @GetMapping("/parsedRecord/id/{id}")
    public ResponseEntity<String> viewRecord(@PathVariable String id) throws ItemNotFoundException {
        String recordJson = recordService.findRecordById(new ObjectId(id));
        return ResponseEntity.ok(recordJson);
    }

    @GetMapping("/parsedRecord/all")
    public ResponseEntity<List<String>> viewAllRecords() throws ItemNotFoundException {
        List<String> records = recordService.findAllRecords();
        return ResponseEntity.ok(records);
    }

    @GetMapping("/parsedRecords/filter")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<String>> getFilterRecords(@RequestParam Map<String, String> params)
            throws ItemNotFoundException {
        try {
            List<String> filteredRecords = recordService.findFilteredRecords(params);
            return ResponseEntity.ok(filteredRecords);
        } catch (ItemNotFoundException e) {
           return ResponseEntity.notFound().build();
        }
    }
   
}
