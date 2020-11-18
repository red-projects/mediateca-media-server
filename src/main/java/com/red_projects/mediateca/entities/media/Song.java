package com.red_projects.mediateca.entities.media;

import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.UUID;

@Entity
public class Song {

    @Id
    private String id;
    private String title;
    private String artistId;
    private String albumId;
    private int trackNumber;
    private String genre;
    private String filePath;
    @Transient
    private String fileUrl;

    public Song() {}

    public Song(String title) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.artistId = null;
        this.albumId = null;
        this.trackNumber = 0;
        this.genre = null;
        this.filePath = null;
        this.fileUrl = null;
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

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public int getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(int trackNumber) {
        this.trackNumber = trackNumber;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public JsonObject toJsonObject() {
        return Json.createObjectBuilder()
                .add("id", id)
                .add("title", title)
                .add("artistId", artistId)
                .add("albumId", albumId)
                .add("trackNumber", trackNumber)
                .add("genre", genre)
                .add("filePath", filePath)
                .add("fileUrl", fileUrl)
                .build();
    }
}
