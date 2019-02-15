package com.redhat.f4j.base.scalar;

import java.io.Serializable;

import com.google.gson.Gson;

@SuppressWarnings("all")
public abstract class BaseEntity implements Serializable {

    private String id;
    public String getId() {return id;}
    public void setId(String id) {this.id = id;}




    public String parseToJson(){
        return (new Gson()).toJson(this);
    }
    
}