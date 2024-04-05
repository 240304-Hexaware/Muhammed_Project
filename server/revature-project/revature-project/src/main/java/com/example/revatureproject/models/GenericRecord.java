package com.example.revatureproject.models;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document("record")
public class GenericRecord extends org.bson.Document {

    @Field("_id")
    @MongoId(FieldType.OBJECT_ID)
    private ObjectId _id;

    @Field("recordUser")
    private String recordUser;

    @Field("_recordType")
    private String _recordType;

    @Field("flat_metadata_id")
    private ObjectId flat_metadataId;

    @Field("spec_metadata_id")
    private ObjectId spec_metadataId;

    public GenericRecord() {
    }

    public ObjectId get_id() {
        return this._id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String get_recordType() {
        return this._recordType;
    }

    public void set_recordType(String _recordType) {
        this._recordType = _recordType;
    }

    public ObjectId getFlat_metadataId() {
        return this.flat_metadataId;
    }

    public void setFlat_metadataId(ObjectId flat_metadataId) {
        this.flat_metadataId = flat_metadataId;
    }

    public ObjectId getSpec_metadataId() {
        return this.spec_metadataId;
    }

    public void setSpec_metadataId(ObjectId spec_metadataId) {
        this.spec_metadataId = spec_metadataId;
    }
   
    public String getRecordUser() {
        return this.recordUser;
    }

    public void setRecordUser(String recordUser) {
        this.recordUser = recordUser;
    }


}
