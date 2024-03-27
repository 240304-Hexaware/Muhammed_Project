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
import java.util.stream.Collectors;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.revatureproject.exceptions.ItemNotFoundException;
import com.example.revatureproject.models.Field;
import com.example.revatureproject.models.GenericRecord;
import com.example.revatureproject.models.Metadata;
import com.example.revatureproject.models.User;
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

    public GenericRecord fileParser(MultipartFile flatFile, String recordType) throws IOException {
        String fileString = fileToString(flatFile);
        System.out.println(fileString.length());
        Metadata metadata = uploadFlatFile(flatFile);
        File specFile;
        // String path = "";
        System.out.println("Type:" + recordType);
        if(recordType.equals("car")) {
            specFile = ResourceUtils.getFile("classpath:car.json");
        }
        else if(recordType.equals("jet")) {
            specFile = ResourceUtils.getFile("classpath:jet.json");
        }
        else {
            specFile = ResourceUtils.getFile("classpath:boat.json");
        }
        Map<String, Field> specMap = parseSpec(specFile);
        List<String> parsedDataList = readStringFields(fileString, specMap);
        GenericRecord record = saveNewRecord(parsedDataList, specMap);
        record.set_metadataId(metadata.get_id());
        return record; 
    }

    public Metadata uploadFlatFile(MultipartFile file) throws IOException {
        //String fileName = UUID.randomUUID().toString() + "." + file.getOriginalFilename().split("\\.")[1];
        Metadata metadata = new Metadata();
        String fileName = file.getOriginalFilename();
        System.out.println("File name: " + fileName);
        if(!fileName.toLowerCase().endsWith(".txt")) {
            System.out.println("Wrong File Type");
            return metadata;
        }
        // Get the project directory
        String projectDir = System.getProperty("user.dir");
        
        // path for the flatfiles directory
        String flatFilesDirPath = projectDir + File.separator + "flatfiles";

        File uploadDir = new File(flatFilesDirPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
            System.out.println("folder does not exist : " + uploadDir.getAbsolutePath());
        }

        // Create the file path for the uploaded file
        String filePath = flatFilesDirPath + File.separator + fileName;
        File uploadedFile = new File(filePath);

        // Transfer the uploaded file to the specified path
        file.transferTo(uploadedFile);

        // Save metadata
        metadata.setFilePath("/flatfiles/" + fileName); // Set relative path for access
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
