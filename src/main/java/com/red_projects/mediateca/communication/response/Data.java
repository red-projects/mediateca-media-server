package com.red_projects.mediateca.communication.response;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class Data {

    private JsonObjectBuilder dataBuilder;

    protected Data() {
        this.dataBuilder = Json.createObjectBuilder();
    }

    protected void addElement(String key, String value) {
        this.dataBuilder.add(key, value);
    }

    protected void addElement(String key, JsonObject jsonObject) {
        this.dataBuilder.add(key, jsonObject);
    }

    protected void addElement(String key, JsonArray jsonArray) {
        this.dataBuilder.add(key, jsonArray);
    }

    protected JsonObject build() {
        return dataBuilder.build();
    }

}
