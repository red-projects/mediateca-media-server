package com.red_projects.mediateca.repositories;

import com.red_projects.mediateca.entities.media.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.Optional;

public interface ArtistRepository extends JpaRepository<Artist, String> {

    Optional<Artist> findById(String id);

    Optional<Artist> findByName(String name);

}
