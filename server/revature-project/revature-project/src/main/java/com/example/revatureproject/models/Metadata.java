package com.example.revatureproject.models;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("metadata")
public class Metadata {
    
    @Field("_id")
    @MongoId(FieldType.OBJECT_ID)
    private ObjectId _id;

    // @Field("file")
    // private MultipartFile file;

    @Field("fileName")
    private String fileName;

    @Field("uploader_name")
    private String uploaderName;

    @Field("file_path")
    private String filePath;
    
    @Field("date_uploaded")
    private Date _dateUploaded;
    

    public Metadata() {
    }


    public Metadata(ObjectId _id, String fileName, String uploaderName, String filePath, Date _dateUploaded) {
        this._id = _id;
        // this.file = file;
        this.fileName = fileName;
        this.uploaderName = uploaderName;
        this.filePath = filePath;
        this._dateUploaded = _dateUploaded;
    }


    public ObjectId get_id() {
        return this._id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUploaderName() {
        return this.uploaderName;
    }

    public void setUploaderName(String uploaderName) {
        this.uploaderName = uploaderName;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Date get_dateUploaded() {
        return this._dateUploaded;
    }

    public void set_dateUploaded(Date _dateUploaded) {
        this._dateUploaded = _dateUploaded;
    }


}
