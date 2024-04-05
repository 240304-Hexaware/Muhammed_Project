package com.example.revatureproject.controllers;

import java.io.IOException;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
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
  
    @PostMapping("/uploadFile")
    public ResponseEntity<Metadata> uploadFile(@RequestParam("flatFile") MultipartFile flatFile, @RequestParam("specFile") String folderName) throws IOException {
        try {
            Metadata metaData = fileService.uploadFile(flatFile, folderName);
            return ResponseEntity.ok(metaData);

        } catch (Exception e) {
            throw new IOException(e);
        } 
    }

    @PostMapping("/parseFile")
    public ResponseEntity<List<GenericRecord>> parseFile(@RequestParam("flatFile") MultipartFile flatFile, 
                                                @RequestParam("specFile") MultipartFile specFile,
                                                @RequestParam("_recordType") String recordType,
                                                @RequestParam("recordUser") String recordUser) throws IOException {

        List<GenericRecord> records = fileService.fileParser(flatFile, specFile, recordType, recordUser);
        return ResponseEntity.ok(records);
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
