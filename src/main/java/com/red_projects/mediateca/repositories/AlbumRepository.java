package com.red_projects.mediateca.repositories;

import com.red_projects.mediateca.entities.media.Album;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.Optional;

public interface AlbumRepository extends JpaRepository<Album, String> {
    Optional<Album> findById(String id);

    ArrayList<Album> findByReleaseYear(int year);

    ArrayList<Album> findByArtistId(String id);

    ArrayList<Album> findByTitleAndArtistId(String title, String artistId);
}
