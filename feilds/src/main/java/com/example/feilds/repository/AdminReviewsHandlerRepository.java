package com.example.feilds.repository;

import com.example.feilds.model.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AdminReviewsHandlerRepository extends JpaRepository<Reviews, Integer> {

    // Find all reviews (including hidden ones for admin view)
    @Query("SELECT r FROM Reviews r ORDER BY r.createdAt DESC")
    List<Reviews> findAllReviewsForAdmin();

    // Find only visible reviews
    @Query("SELECT r FROM Reviews r WHERE r.isHidden = false ORDER BY r.createdAt DESC")
    List<Reviews> findVisibleReviews();

    // Find only hidden reviews
    @Query("SELECT r FROM Reviews r WHERE r.isHidden = true ORDER BY r.createdAt DESC")
    List<Reviews> findHiddenReviews();

    // Find reviews by rating (for filtering inappropriate content)
    @Query("SELECT r FROM Reviews r WHERE r.rating <= :rating ORDER BY r.createdAt DESC")
    List<Reviews> findReviewsByMaxRating(@Param("rating") Integer rating);

    // Find reviews containing specific keywords (for content moderation)
    @Query("SELECT r FROM Reviews r WHERE LOWER(r.comment) LIKE LOWER(CONCAT('%', :keyword, '%')) ORDER BY r.createdAt DESC")
    List<Reviews> findReviewsContainingKeyword(@Param("keyword") String keyword);

    // Hide a review (set isHidden to true)
    @Modifying
    @Transactional
    @Query("UPDATE Reviews r SET r.isHidden = true WHERE r.id = :reviewId")
    int hideReview(@Param("reviewId") Integer reviewId);

    // Unhide a review (set isHidden to false)
    @Modifying
    @Transactional
    @Query("UPDATE Reviews r SET r.isHidden = false WHERE r.id = :reviewId")
    int unhideReview(@Param("reviewId") Integer reviewId);

    // Find reviews by customer ID
    @Query("SELECT r FROM Reviews r WHERE r.customer.id = :customerId ORDER BY r.createdAt DESC")
    List<Reviews> findReviewsByCustomerId(@Param("customerId") Integer customerId);

    // Count hidden reviews
    @Query("SELECT COUNT(r) FROM Reviews r WHERE r.isHidden = true")
    Long countHiddenReviews();

    // Count visible reviews
    @Query("SELECT COUNT(r) FROM Reviews r WHERE r.isHidden = false")
    Long countVisibleReviews();
}