package com.cloudapp.cloud_app.cloud_app.Repository;

import com.cloudapp.cloud_app.cloud_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserLoginRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
}
