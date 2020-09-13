package com.redprojects.mediateca.communication.response;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class Response {

    private String action;
    private Status status;
    private Data data;

    public Response(String action) {
        this.action = action;
        this.status = new Status();
        this.data = new Data();
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public boolean getActionSuccess() {
        return status.getActionSuccess();
    }

    public void actionFailed() {
        status.setActionSuccess(false);
    }

    public void actionSucceeded() {
        status.setActionSuccess(true);
    }

    public void setActionSuccess(boolean actionSuccess) {
        status.setActionSuccess(actionSuccess);
    }

    public String getMessage() {
        return status.getMessage();
    }

    public void setMessage(String message) {
        status.setMessage(message);
    }

    public void addBodyElement(String key, String value) {
        data.addElement(key, value);
    }

    public void addBodyElement(String key, JsonObject value) {
        data.addElement(key, value);
    }

    public void addBodyElement(String key, JsonArray value) {
        data.addElement(key, value);
    }

    public JsonObject toJsonObject() {
        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder()
                .add("action", action)
                .add("status", status.toJson());
        JsonObject body = data.build();
        if (!body.isEmpty())
            jsonBuilder.add("data", body);
        return jsonBuilder.build();
    }

    public String toJson() {
        return toJsonObject().toString();
    }






}


