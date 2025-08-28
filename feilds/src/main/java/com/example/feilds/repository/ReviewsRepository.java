// ReviewsRepository.java
package com.example.feilds.repository;

import com.example.feilds.model.Reviews;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewsRepository extends JpaRepository<Reviews, Integer> {

    // Visible (not hidden) listings
    List<Reviews> findByIsHiddenFalse();

    // Visible (not hidden) with paging/sorting
    Page<Reviews> findByIsHiddenFalse(Pageable pageable);

    // Visible by ID
    Optional<Reviews> findByIdAndIsHiddenFalse(Integer id);

    // Visible by Customer
    List<Reviews> findByCustomerIdAndIsHiddenFalse(Integer customerId);

    // Visible by rating range (inclusive) with paging/sorting
    Page<Reviews> findByIsHiddenFalseAndRatingBetween(Integer min, Integer max, Pageable pageable);

    // Count visible
    long countByIsHiddenFalse();

    // Average rating for visible reviews
    @Query("SELECT AVG(r.rating) FROM Reviews r WHERE r.isHidden = false")
    Double getAverageRating();

    // Average rating for a customer's visible reviews
    @Query("SELECT AVG(r.rating) FROM Reviews r WHERE r.customer.id = :customerId AND r.isHidden = false")
    Double getAverageRatingByCustomer(@Param("customerId") Integer customerId);

    // Ratings breakdown: [rating, count]
    @Query("SELECT r.rating AS rating, COUNT(r) AS cnt " +
           "FROM Reviews r WHERE r.isHidden = false " +
           "GROUP BY r.rating ORDER BY r.rating DESC")
    List<Object[]> getRatingBreakdown();
}
