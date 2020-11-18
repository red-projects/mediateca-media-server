package com.red_projects.mediateca.entities.media;

import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.UUID;

@Entity
public class Artist {

    @Id
    private String id;
    private String name;
    private String description;
    private String imagePath;
    @Transient
    private String imageUrl;

    public Artist() {}

    public Artist(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = null;
        this.imagePath = null;
        this.imageUrl = null;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public JsonObject toJsonObject() {
        return Json.createObjectBuilder()
                .add("id", id)
                .add("name", name)
                .add("description", description)
                .add("imagePath", imagePath)
                .add("imageUrl", imageUrl)
                .build();
    }
}
