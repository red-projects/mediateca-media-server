package com.red_projects.mediateca.repositories;

import com.red_projects.mediateca.entities.media.Song;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.Optional;

public interface SongRepository extends JpaRepository<Song, String> {

    Optional<Song> findById(String id);

    ArrayList<Song> findByAlbumId(String id);

    ArrayList<Song> findByArtistId(String id);

    ArrayList<Song> findByTitleAndAlbumId(String title, String albumId);
}
