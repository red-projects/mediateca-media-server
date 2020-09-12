package com.redprojects.mediateca.communication;

import javax.json.Json;
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


     private class Status {

        private String actionSuccess;
        private String message;

        private Status() {
            this.actionSuccess = "SUCCEEDED";
            this.message = "";
        }

        private boolean getActionSuccess() {
            return actionSuccess.contentEquals("SUCCEEDED");
        }

        private void setActionSuccess(boolean actionSuccess) {
            if (actionSuccess) this.actionSuccess = "SUCCEEDED";
            else this.actionSuccess = "FAILED";
        }

        private String getMessage() {
            return message;
        }

        private void setMessage(String message) {
            this.message = message;
        }

        private JsonObject toJson() {
            return Json.createObjectBuilder()
                    .add("actionSuccess", actionSuccess)
                    .add("message", message)
                    .build();
        }
    }


    public class Data {
        private JsonObjectBuilder dataBuilder;

        public Data() {
            this.dataBuilder = Json.createObjectBuilder();
        }

        public void addElement(String key, String value) {
            this.dataBuilder.add(key, value);
        }

        public void addElement(String key, JsonObject jsonObject) {
            this.dataBuilder.add(key, jsonObject);
        }

        public JsonObject build() {
            return dataBuilder.build();
        }

    }
}


