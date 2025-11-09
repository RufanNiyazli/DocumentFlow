package com.project.documentflow.repository;

import com.project.documentflow.entity.User;
import com.project.documentflow.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT u.email FROM User u WHERE u.role = ?1")
    List<String> findEmailsByRole(Role role);

    Optional<User> findByUsername(String username);
}
