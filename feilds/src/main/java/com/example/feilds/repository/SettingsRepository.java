package com.example.feilds.repository;

import com.example.feilds.model.Settings;
import com.example.feilds.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SettingsRepository extends JpaRepository<Settings, Integer> {
    
    /**
     * Find settings by admin
     */
    Optional<Settings> findByAdmin(Users admin);
} 