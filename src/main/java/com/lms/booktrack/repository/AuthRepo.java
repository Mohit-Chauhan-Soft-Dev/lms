package com.lms.booktrack.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lms.booktrack.model.User;

@Repository
public interface AuthRepo extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String username);

}
