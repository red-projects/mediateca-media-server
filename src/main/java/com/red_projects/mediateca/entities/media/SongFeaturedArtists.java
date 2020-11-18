package com.red_projects.mediateca.entities.media;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SongFeaturedArtists {

    @Id
    private String id;
    private String songId;
    private String artistId;

    private SongFeaturedArtists() {}

    private SongFeaturedArtists(String songId, String artistId) {
        this.songId = songId;
        this.artistId = artistId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }
}
