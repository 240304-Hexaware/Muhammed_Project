package com.example.revatureproject.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.revatureproject.models.Field;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FileService {

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

    public List<String> fileParser(MultipartFile flatFile) throws IOException {
        String fileString = fileToString(flatFile);
        File specFile = ResourceUtils.getFile("classpath:car.json");
        System.out.println(new File("car.json").getAbsolutePath());
        //File file = new File("car.json");
        //System.out.println(file.exists() + "  " + file.canRead() + " " + file.isDirectory()); // prints "false  false false"

        Map<String, Field> specMap = parseSpec(specFile);
        return readStringFields(fileString, specMap);
        
    }

}
