package com.cloudapp.cloud_app.cloud_app.Repository;

import com.cloudapp.cloud_app.cloud_app.model.Users.Users;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserLoginRepository extends JpaRepository<Users, Long> {
    boolean existsByUsername(String username);

    Optional<Users> findByUsername(String username);
}