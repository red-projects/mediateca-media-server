package com.red_projects.mediateca.repositories;

import com.red_projects.mediateca.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findById(String id);

    ArrayList<User> findByAccessLevel(String accessLevel);

    ArrayList<User> findByEmailAddress(String email);

    Optional<User> findByUsername(String username);

    Long countByAccessLevel(String accessLevel);
}
