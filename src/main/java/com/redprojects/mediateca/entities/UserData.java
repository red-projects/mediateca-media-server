package com.redprojects.mediateca.entities;

import javax.json.Json;
import javax.json.JsonObject;
import java.sql.Timestamp;

public class UserData {

    private String id;
    private String name;
    private String emailAddress;
    private String username;
    private String status;
    private boolean admin;
    private Timestamp creationDate;
    private Timestamp lastUpdated;

    public UserData(String id) {
        this.id = id;
    }

    public UserData(User user) {
        id = user.getId();
        setName(user.getFirstName(), user.getLastName());
        emailAddress = user.getEmailAddress();
        username = user.getUsername();
        status = user.getStatus();
        admin = user.isAdmin();
        creationDate = user.getCreationDate();
        lastUpdated = user.getLastUpdated();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String firstName, String lastName) {
        this.name = firstName + " " + lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public Timestamp getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public JsonObject toCondensedJsonObject() {
        return Json.createObjectBuilder()
                .add("id", id)
                .add("username", username)
                .add("status", status)
                .add("isAdmin", admin)
                .build();
    }

    public JsonObject toJsonObject() {
        return Json.createObjectBuilder()
                .add("id", id)
                .add("name", name)
                .add("emailAddress", emailAddress)
                .add("username", username)
                .add("status", status)
                .add("isAdmin", admin)
                .add("lastUpdated", lastUpdated.toString())
                .add("creationDate", creationDate.toString())
                .build();
    }
}
