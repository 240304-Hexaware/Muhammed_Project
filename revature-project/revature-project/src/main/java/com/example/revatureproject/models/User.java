package com.example.revatureproject.models;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document("user")
public class User {
    @Field("_id")
    @MongoId(FieldType.OBJECT_ID) // helps spring understand how to utilize mongo's ID objects
    private ObjectId _id;// with that above annotation we could use String in place of ObjectId type.

    @Field(name = "username")
    private String username;

    @Field(name = "password")
    private String password;

    @Field(name = "role")
    private String role;


    public User() {
    }

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User(ObjectId _id, String username, String password, String role) {
        this._id = _id;
        this.username = username;
        this.password = password;
        this.role = role;
    }


    public ObjectId get_id() {
        return this._id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
