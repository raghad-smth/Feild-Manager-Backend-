package com.example.feilds.repository;

import com.example.feilds.model.Teams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Teams, Long> {

    /**
     * Check if a team name already exists
     */
    boolean existsByName(String name);

    /**
     * Find all active teams
     */
    List<Teams> findByIsActiveTrue();

    /**
     * Find teams by IDs that are active
     */
    List<Teams> findByIdInAndIsActiveTrue(List<Integer> ids);

    /**
     * Find teams by name containing (case insensitive)
     */
    @Query("SELECT t FROM Teams t WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :name, '%')) AND t.isActive = true")
    List<Teams> findByNameContainingIgnoreCaseAndIsActiveTrue(@Param("name") String name);
}