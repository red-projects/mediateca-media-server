package com.redprojects.mediateca.repositories;

import com.redprojects.mediateca.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findById(String id);

    ArrayList<User> findByAdminTrue();

    ArrayList<User> findByEmailAddress(String email);

    Optional<User> findByUsername(String username);

    Long countByAdminTrue();
}
