package com.example.feilds.repository;

import com.example.feilds.model.Enquiries;
import com.example.feilds.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnquiryRepository extends JpaRepository<Enquiries, Integer> {
    
    /**
     * Find all enquiries by customer
     */
    List<Enquiries> findByCustomer(Users customer);
    
    /**
     * Find all visible enquiries (not hidden)
     */
    List<Enquiries> findByIsHiddenFalse();
    
    /**
     * Find enquiries by status
     */
    List<Enquiries> findByStatus(Enquiries.Status status);
    
    /**
     * Find visible enquiries by status
     */
    List<Enquiries> findByIsHiddenFalseAndStatus(Enquiries.Status status);
    
    /**
     * Find enquiries by customer and visibility
     */
    List<Enquiries> findByCustomerAndIsHiddenFalse(Users customer);
} 