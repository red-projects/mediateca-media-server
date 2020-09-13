package com.redprojects.mediateca.communication.response;

import javax.json.Json;
import javax.json.JsonObject;

public class Status {

    private String actionSuccess;
    private String message;

    protected Status() {
        this.actionSuccess = "SUCCEEDED";
        this.message = "";
    }

    protected boolean getActionSuccess() {
        return actionSuccess.contentEquals("SUCCEEDED");
    }

    protected void setActionSuccess(boolean actionSuccess) {
        if (actionSuccess) this.actionSuccess = "SUCCEEDED";
        else this.actionSuccess = "FAILED";
    }

    protected String getMessage() {
        return message;
    }

    protected void setMessage(String message) {
        this.message = message;
    }

    protected JsonObject toJson() {
        return Json.createObjectBuilder()
                .add("actionSuccess", actionSuccess)
                .add("message", message)
                .build();
    }
}
