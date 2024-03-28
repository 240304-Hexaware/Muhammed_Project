package com.example.revatureproject.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.revatureproject.exceptions.ItemNotFoundException;
import com.example.revatureproject.models.Field;
import com.example.revatureproject.models.GenericRecord;
import com.example.revatureproject.models.Metadata;
import com.example.revatureproject.repositories.GenericRecordRepository;
import com.example.revatureproject.repositories.MetadataRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FileService {

    private GenericRecordRepository genericRecordRepository;
    private MetadataRepository metadataRepository;

    public FileService(GenericRecordRepository genericRecordRepository, MetadataRepository metadataRepository) {
        this.genericRecordRepository = genericRecordRepository;
        this.metadataRepository = metadataRepository;
    }

    /**
     * Here is an example of parsing a JSON file into Java objects. Here we create a map
     * of Tokens which represent fields in a fixed-length file with a name, start position, and end position.
     * Data types are also included here. These tokens can then be used to parse the fixed-length file.
     * @param specFile - Specification JSON file
     * @return Map of tokens
     * @throws IOException
     */
    public Map<String, Field> parseSpec(MultipartFile specFile) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File tempFile = File.createTempFile("upload_", ".tmp");
        specFile.transferTo(tempFile);
        Map<String, Field> map = mapper.readValue(tempFile, new TypeReference<Map<String, Field>>() {});

        Set<String> keySet = map.keySet();
        for(String s : keySet) {
            map.get(s).setName(s);
        }
        System.out.println(map);
        return map;
    }

    public String fileToString(MultipartFile flatFile) throws IOException {
        InputStreamReader reader = new InputStreamReader(flatFile.getInputStream());
        StringBuilder builder = new StringBuilder();
        while(reader.ready()) {
            builder.append((char)reader.read());
        }
        return builder.toString();
    }

    /**
     * This will take a flat file and a spec map in order to create a list of strings, each representing
     * one field value from the flat file.
     * @param data
     * @param spec
     * @return
     * @throws IOException
     */
    public List<Map<String, String>> readStringFields(String data, Map<String, Field> spec) throws IOException {
        List<Map<String, String>> records = new ArrayList<>();
        String[] lines = data.split("\r\n|\n");
        for(String line : lines) {
            System.out.println(line);
            Map<String, String> record = new HashMap<>();
            Set<String> fields = spec.keySet();
            
            
            for(String fieldName : fields) {
                Field field = spec.get(fieldName);
                if (line.length() >= field.getEndPos()) {
                    String fieldValue = line.substring(field.getStartPos(), Math.min(line.length(), field.getEndPos() + 1)).trim();
                    record.put(fieldName, fieldValue);
                    System.out.println("[" + fieldName + "][" + fieldValue + "]");
                }
            }
            records.add(record);
        }

        return records;
    }

    public GenericRecord saveNewRecord(Map<String, String> parsedRecord, Map<String, Field> specMap, String recordType) {
        GenericRecord newRecord = new GenericRecord();
        
        for (Map.Entry<String, String> entry : parsedRecord.entrySet()) {
            String fieldName = entry.getKey();
            String fieldValue = entry.getValue();
            newRecord.append(fieldName, fieldValue);
        }
        
        newRecord.append("_recordType", recordType);
        return newRecord;
    }

    public List<GenericRecord> fileParser(MultipartFile flatFile, MultipartFile specFile, String recordType) throws IOException {
        String fileString = fileToString(flatFile);

        System.out.println(fileString);
        System.out.println(fileString.length());
        System.out.println("Type:" + recordType);

        Metadata flatMetadata = uploadFile(flatFile, "flatfiles");

        Map<String, Field> specMap = parseSpec(specFile);
        List<Map<String, String>> parsedRecords = readStringFields(fileString, specMap);
        List<GenericRecord> records = new ArrayList<>();

        for(Map<String, String> parsedRecord : parsedRecords) {
            GenericRecord record = saveNewRecord(parsedRecord, specMap, recordType); // Save each record individually
            record.setFlat_metadataId(flatMetadata.get_id());
            record.set_recordType(recordType);
            System.out.println("flat metadata id:" + record.getFlat_metadataId());
            System.out.println("record type:" + record.get_recordType());
        
            records.add(record);
        }

        //Metadata specMetadata = uploadFile(specFile, "specfiles");
        //record.setSpec_metadataId(specMetadata.get_id());
        genericRecordRepository.saveAll(records);
        return records; 
    }

    

    public Metadata uploadFile(MultipartFile file, String folder) throws IOException {
        Metadata metadata = new Metadata();
        String fileName = file.getOriginalFilename();
        System.out.println("File name: " + fileName);

        // Get the project directory
        String projectDir = System.getProperty("user.dir");
        
        // path for the flatfiles directory
        String flatFilesDirPath = projectDir + File.separator + folder;
        System.out.println("flatFilesDirPath = " + flatFilesDirPath);
        File uploadDir = new File(flatFilesDirPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
            System.out.println("folder does not exist : " + uploadDir.getAbsolutePath());
        }

        // Create the file path for the uploaded file
        // String filePath = flatFilesDirPath + File.separator + fileName;
        String filePath = flatFilesDirPath + File.separator;

        System.out.println("filePath: " + filePath);
        File uploadedFile = new File(filePath, fileName);

        // Transfer the uploaded file to the specified path
        file.transferTo(uploadedFile);
        // Use InputStream and Files.copy to write the file to the target directory
        
        // Save metadata
        metadata.setFilePath("/"+ folder + "/" + fileName); // Set relative path for access
        metadata.setFileName(fileName);
        // metadata.setFile(file);
        metadataRepository.save(metadata);

        return metadata;
    }

    public List<String> findAllFileNames() {
        return metadataRepository.findAll().stream()
            .map(Metadata::getFileName)
            .collect(Collectors.toList());
    }

    public List<Metadata> findAllFiles() {
        return metadataRepository.findAll();
    }

    public Metadata deleteFileById(ObjectId id) throws ItemNotFoundException{

        try {
            Metadata metadata = findById(id);
            System.out.println("****************************Deleting file with ID:" + id+ "****************************");
            System.out.println("****************************Metadata: " + metadata.toString());
            metadataRepository.deleteById(id);
            return metadata;
        } catch (Exception e) {
            throw new ItemNotFoundException("File for deletion not found");
        }
    }

    public Metadata findById(ObjectId id) throws ItemNotFoundException {
        /*
        This returns our result of one exists, otherwise it throws ItemNotFound
         */
        return metadataRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Query returned no result."));
    } 
}
