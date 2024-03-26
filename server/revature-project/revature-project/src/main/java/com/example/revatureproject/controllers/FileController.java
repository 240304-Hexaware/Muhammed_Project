package com.example.revatureproject.controllers;

import java.io.IOException;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.revatureproject.exceptions.ItemNotFoundException;
import com.example.revatureproject.models.GenericRecord;
import com.example.revatureproject.models.Metadata;
import com.example.revatureproject.services.FileService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class FileController {
    private FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }
    // read the specFile as a json and convert it to a map<String, Field> (service)

    // flat file comes form postman as a post mapping to upload the file
    // then read the file and return it as a string

    @PostMapping("/uploadFile")
    public ResponseEntity<Metadata> uploadFile(@RequestParam("file") MultipartFile flatFile) throws IOException {
        try {
            Metadata metaData = fileService.uploadFlatFile(flatFile);
            return ResponseEntity.ok(metaData);

        } catch (Exception e) {
            throw new IOException(e);
        }
        
    }

    @PostMapping("/parseFile")
    public ResponseEntity<GenericRecord> parseFile(@RequestParam("file") MultipartFile flatFile, @RequestParam("type") String recordType) throws IOException {
        GenericRecord record = fileService.fileParser(flatFile, recordType);
        return ResponseEntity.ok(record);
    }

    // pass the spec map and data string to a method that reads each field from the flat file string data
    // according to the spec map and insert into a document structure for example:
    // {
    //     "manufacturer": "toyota",
    //     "year": "2009",
    //     "model": "corolla"
    // }
}
