package com.red_projects.mediateca.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Environment {

    @Id
    private String id;
    private String name;
    private String directory;
    private String hostname;
    private boolean sslEnabled;
    private boolean isActive;
    // implement password security settings

    public Environment() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public boolean isSslEnabled() {
        return sslEnabled;
    }

    public void setSslEnabled(boolean sslEnabled) {
        this.sslEnabled = sslEnabled;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActiveStatus(boolean active) {
        isActive = active;
    }
}
