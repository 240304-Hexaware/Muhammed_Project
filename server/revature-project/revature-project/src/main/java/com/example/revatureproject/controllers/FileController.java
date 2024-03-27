package com.example.revatureproject.controllers;

import java.io.IOException;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.Meta;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    @GetMapping("/viewFileNames")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<String>> getAllFileNames() {
        List<String> fileNames = fileService.findAllFileNames();
        if(fileNames == null) { 
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.ok(fileNames);
    }

    @GetMapping("/viewAll")
    public ResponseEntity<List<Metadata>> getAllFiles() {
        List<Metadata> fileNames = fileService.findAllFiles();
        if(fileNames == null) { 
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.ok(fileNames);
    }

    @DeleteMapping("/deleteFile/id/{id}")
    public ResponseEntity<Metadata> removeFileById(@PathVariable String id) throws ItemNotFoundException {
        try {
            Metadata deletedFileMetadata = fileService.deleteFileById(new ObjectId(id));
            return ResponseEntity.ok(deletedFileMetadata);

        } catch (ItemNotFoundException e) {
            throw new ItemNotFoundException("File to delete was not found!");
        }
    }


}
