package com.example.feilds.repository;

import com.example.feilds.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {

    /**
     * Find user by email
     */
    Optional<Users> findByEmail(String email);

    /**
     * Find all users by role
     */
    List<Users> findByRole(String role);
}