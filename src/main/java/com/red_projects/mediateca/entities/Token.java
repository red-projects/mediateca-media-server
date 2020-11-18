package com.red_projects.mediateca.entities;

import com.red_projects.mediateca.entities.utils.TokenAction;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
public class Token {

    @Id
    private String id;
    private String userId;
    private String action;
    private String encryptedCode;
    private Timestamp creationDate;

    public Token() {}

    public Token(TokenAction action, String userId) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.action = action.name();
        this.creationDate = new Timestamp(System.currentTimeMillis());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getEncryptedCode() {
        return encryptedCode;
    }

    public void setEncryptedCode(String encryptedCode) {
        this.encryptedCode = encryptedCode;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }
}
