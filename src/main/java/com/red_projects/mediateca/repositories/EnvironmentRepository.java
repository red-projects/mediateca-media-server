package com.red_projects.mediateca.repositories;

import com.red_projects.mediateca.entities.Environment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.ArrayList;
import java.util.Optional;

public interface EnvironmentRepository extends JpaRepository<Environment, String> {


    Optional<Environment> findById(String id);

    Optional<Environment> findByName(String name);

    ArrayList<Environment> findByIsActiveTrue();
}
