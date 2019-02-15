package com.redhat.f4j.service.project.verticle.dto;

import com.redhat.f4j.base.scalar.Project;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * WrapProject
 */

@DataObject
public class WrapProject extends Project{

    private static final long serialVersionUID = 1L;

    public WrapProject(){}

    public WrapProject(JsonObject json){

        super.setTitle(json.getString("title"));
        super.setDescription(json.getString("description"));
        super.setStatus(json.getString("status"));
        super.setId(json.getString("id"));
        JsonObject ownerJson = json.getJsonObject("owner");

        if(ownerJson!=null){
            // Person owner = new Person();
            super.getOwner().setFirstName(ownerJson.getString("firstName"));
            super.getOwner().setLastName(ownerJson.getString("lastName"));
            super.getOwner().setEmail(ownerJson.getString("email"));
        }

    }


    public JsonObject toJson() {
        final JsonObject json = new JsonObject(super.parseToJson());
        return json;
    }

    

    
}