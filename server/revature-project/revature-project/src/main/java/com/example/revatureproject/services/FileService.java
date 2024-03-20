package com.example.revatureproject.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
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
    public Map<String, Field> parseSpec(File specFile) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Field> map = mapper.readValue(specFile, new TypeReference<Map<String, Field>>() {});

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
    public List<String> readStringFields(String data, Map<String, Field> spec) throws IOException {
        List<String> fieldList = new ArrayList<>();

        Set<String> fields = spec.keySet();
        for(String fieldName : fields) {
            Field field = spec.get(fieldName);
            String fieldValue = data.substring(field.getStartPos(), field.getEndPos()+1).trim();
            fieldList.add(fieldValue);
            System.out.println("[" + fieldName + "][" + fieldValue + "]");
        }
        return fieldList;
    }

    public GenericRecord saveNewRecord(List<String> parsedDataList, Map<String, Field> specMap) {
        GenericRecord newRecord = new GenericRecord();
        int fieldIndex = 0;
        for(String fieldName : specMap.keySet()) {
            newRecord.append(fieldName, parsedDataList.get(fieldIndex++));
        }
        genericRecordRepository.save(newRecord);
        return newRecord;
    }

    public GenericRecord fileParser(MultipartFile flatFile) throws IOException {
        String fileString = fileToString(flatFile);
        Metadata metadata = uploadFlatFile(flatFile);
        File specFile = ResourceUtils.getFile("classpath:car.json");
        Map<String, Field> specMap = parseSpec(specFile);
        List<String> parsedDataList = readStringFields(fileString, specMap);
        GenericRecord record = saveNewRecord(parsedDataList, specMap);
        record.set_metadataId(metadata.get_id());
        return record; 
    }

    public Metadata uploadFlatFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "." + file.getOriginalFilename().split("\\.")[1];

        // Get the project directory
        String projectDir = System.getProperty("user.dir");
        
        // path for the flatfiles directory
        String flatFilesDirPath = projectDir + File.separator + "flatfiles";

        File uploadDir = new File(flatFilesDirPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Create the file path for the uploaded file
        String filePath = flatFilesDirPath + File.separator + fileName;
        File uploadedFile = new File(filePath);

        // Transfer the uploaded file to the specified path
        file.transferTo(uploadedFile);

        // Save metadata
        Metadata metadata = new Metadata();
        metadata.setFilePath("/flatfiles/" + fileName); // Set relative path for access
        metadataRepository.save(metadata);

        return metadata;
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

}
