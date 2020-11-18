package com.red_projects.mediateca.entities.media;

import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.UUID;

@Entity
public class Album {

    @Id
    private String id;
    private String title;
    private String artistId;
    private int numberOfTracks;
    private int releaseYear;
    private String imagePath;
    @Transient
    private String imageUrl;

    public Album() {}

    public Album(String title) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.artistId = null;
        this.numberOfTracks = 0;
        this.releaseYear = 0;
        this.imagePath = null;
        this.imageUrl = null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public int getNumberOfTracks() {
        return numberOfTracks;
    }

    public void setNumberOfTracks(int numberOfTracks) {
        this.numberOfTracks = numberOfTracks;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
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
                .add("title", title)
                .add("artistId", artistId)
                .add("numberOfTracks", numberOfTracks)
                .add("releaseYear", releaseYear)
                .add("imagePath", imagePath)
                .add("imageUrl", imageUrl)
                .build();
    }
}
