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

    @Field("metadata_id")
    private ObjectId _metadataId;

    public GenericRecord() {
    }

    public ObjectId get_id() {
        return this._id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }


    public ObjectId get_metadataId() {
        return this._metadataId;
    }

    public void set_metadataId(ObjectId _metadataId) {
        this._metadataId = _metadataId;
    }


}
