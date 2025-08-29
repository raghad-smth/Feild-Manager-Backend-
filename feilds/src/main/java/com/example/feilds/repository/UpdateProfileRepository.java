package com.example.feilds.repository;

import com.example.feilds.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UpdateProfileRepository extends JpaRepository<Users, Integer> {
    // JpaRepository already provides findById, save, etc.
}
