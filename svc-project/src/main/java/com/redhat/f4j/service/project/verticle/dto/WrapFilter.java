package com.redhat.f4j.service.project.verticle.dto;

import com.redhat.f4j.base.scalar.LabelValueScalar;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * WrabFilter
 */
@DataObject
public class WrapFilter extends LabelValueScalar{


    private static final long serialVersionUID = 1L;

    public WrapFilter(){}

    public WrapFilter(JsonObject json){

        super.setLabel(json.getString("label"));
        super.setValue(json.getString("value"));
    }


    public JsonObject toJson() {

        final JsonObject json = new JsonObject();
        json.put("label", super.getLabel());
        json.put("value", super.getValue());
        return json;
    }

    
}