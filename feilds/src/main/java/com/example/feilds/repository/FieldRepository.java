package com.example.feilds.repository;

import com.example.feilds.model.Fields;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FieldRepository extends JpaRepository<Fields, Integer> {
    
    /**
     * Find all active fields with pagination
     */
    Page<Fields> findByIsActiveTrue(Pageable pageable);
    
    /**
     * Find all active fields
     */
    List<Fields> findByIsActiveTrue();
    
    /**
     * Find fields by location containing text (case insensitive)
     */
    List<Fields> findByLocationAddressContainingIgnoreCase(String location);
    
    /**
     * Find fields by capacity greater than or equal to specified value
     */
    List<Fields> findByPlayersCapacityGreaterThanEqual(Integer capacity);
    
    /**
     * Find active fields by location and capacity
     */
    List<Fields> findByIsActiveTrueAndLocationAddressContainingIgnoreCaseAndPlayersCapacityGreaterThanEqual(
            String location, Integer capacity);
} 