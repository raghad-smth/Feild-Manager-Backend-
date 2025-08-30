package com.example.feilds.service;

import com.example.feilds.model.Reviews;
import com.example.feilds.model.Users;
import com.example.feilds.repository.AdminReviewsHandlerRepository;
import com.example.feilds.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AdminReviewsHandlerService {

    private final AdminReviewsHandlerRepository adminReviewsRepository;
    private final UserRepository userRepository;
    
    public AdminReviewsHandlerService(AdminReviewsHandlerRepository adminReviewsRepository, UserRepository userRepository) {
        this.adminReviewsRepository = adminReviewsRepository;
        this.userRepository = userRepository;
    }

    /**
     * Get user by ID and validate existence
     */
    public Optional<Users> getUserById(Integer userId) {
        if (userId == null) {
            return Optional.empty();
        }
        return userRepository.findById(userId);
    }

    /**
     * Validate admin role and user existence
     */
    public boolean validateAdminAccess(Integer userId) {
        if (userId == null) {
            return false;
        }

        Optional<Users> userOpt = getUserById(userId);
        if (userOpt.isEmpty()) {
            return false;
        }

        Users user = userOpt.get();
        return Users.Role.admin.equals(user.getRole());
    }

    /**
     * Get all reviews for admin view (including hidden ones)
     */
    public List<Reviews> getAllReviewsForAdmin() {
        return adminReviewsRepository.findAllReviewsForAdmin();
    }

    /**
     * Get only visible reviews
     */
    public List<Reviews> getVisibleReviews() {
        return adminReviewsRepository.findVisibleReviews();
    }

    /**
     * Get only hidden reviews
     */
    public List<Reviews> getHiddenReviews() {
        return adminReviewsRepository.findHiddenReviews();
    }

    /**
     * Hide a review (mark as inappropriate)
     */
    @Transactional
    public boolean hideReview(Integer reviewId) {
        try {
            // Check if review exists first
            Optional<Reviews> reviewOpt = adminReviewsRepository.findById(reviewId);
            if (reviewOpt.isEmpty()) {
                return false;
            }

            Reviews review = reviewOpt.get();
            if (review.getIsHidden()) {
                // Already hidden
                return false;
            }

            int rowsUpdated = adminReviewsRepository.hideReview(reviewId);
            return rowsUpdated > 0;
        } catch (Exception e) {
            throw new RuntimeException("Failed to hide review with ID: " + reviewId, e);
        }
    }

    /**
     * Unhide a review (restore visibility)
     */
    @Transactional
    public boolean unhideReview(Integer reviewId) {
        try {
            // Check if review exists first
            Optional<Reviews> reviewOpt = adminReviewsRepository.findById(reviewId);
            if (reviewOpt.isEmpty()) {
                return false;
            }

            Reviews review = reviewOpt.get();
            if (!review.getIsHidden()) {
                // Already visible
                return false;
            }

            int rowsUpdated = adminReviewsRepository.unhideReview(reviewId);
            return rowsUpdated > 0;
        } catch (Exception e) {
            throw new RuntimeException("Failed to unhide review with ID: " + reviewId, e);
        }
    }

    /**
     * Permanently delete a review
     */
    @Transactional
    public boolean deleteReview(Integer reviewId) {
        try {
            Optional<Reviews> review = adminReviewsRepository.findById(reviewId);
            if (review.isPresent()) {
                adminReviewsRepository.deleteById(reviewId);
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete review with ID: " + reviewId, e);
        }
    }

    /**
     * Get reviews by maximum rating (useful for finding potentially problematic
     * reviews)
     */
    public List<Reviews> getReviewsByMaxRating(Integer maxRating) {
        if (maxRating == null || maxRating < 1 || maxRating > 5) {
            throw new IllegalArgumentException("Max rating must be between 1 and 5");
        }
        return adminReviewsRepository.findReviewsByMaxRating(maxRating);
    }

    /**
     * Search reviews containing specific keywords for content moderation
     */
    public List<Reviews> searchReviewsByKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Search keyword cannot be empty");
        }
        return adminReviewsRepository.findReviewsContainingKeyword(keyword.trim());
    }

    /**
     * Get reviews by customer ID
     */
    public List<Reviews> getReviewsByCustomer(Integer customerId) {
        if (customerId == null) {
            throw new IllegalArgumentException("Customer ID cannot be null");
        }
        return adminReviewsRepository.findReviewsByCustomerId(customerId);
    }

    /**
     * Get review statistics
     */
    public ReviewStatistics getReviewStatistics() {
        Long hiddenCount = adminReviewsRepository.countHiddenReviews();
        Long visibleCount = adminReviewsRepository.countVisibleReviews();
        Long totalCount = hiddenCount + visibleCount;

        return ReviewStatistics.builder()
                .totalReviews(totalCount)
                .visibleReviews(visibleCount)
                .hiddenReviews(hiddenCount)
                .hiddenPercentage(totalCount > 0 ? (hiddenCount * 100.0) / totalCount : 0.0)
                .build();
    }

    /**
     * Get a specific review by ID
     */
    public Optional<Reviews> getReviewById(Integer reviewId) {
        if (reviewId == null) {
            return Optional.empty();
        }
        return adminReviewsRepository.findById(reviewId);
    }

    /**
     * Bulk hide multiple reviews
     */
    @Transactional
    public BulkActionResult bulkHideReviews(List<Integer> reviewIds) {
        if (reviewIds == null || reviewIds.isEmpty()) {
            return new BulkActionResult(0, 0, "No review IDs provided");
        }

        int successCount = 0;
        int failCount = 0;
        StringBuilder errors = new StringBuilder();

        for (Integer reviewId : reviewIds) {
            try {
                if (hideReview(reviewId)) {
                    successCount++;
                } else {
                    failCount++;
                    errors.append("Review ID ").append(reviewId).append(" not found or already hidden. ");
                }
            } catch (Exception e) {
                failCount++;
                errors.append("Error hiding review ID ").append(reviewId).append(": ").append(e.getMessage())
                        .append(". ");
            }
        }

        String message = String.format("Bulk hide completed. Success: %d, Failed: %d", successCount, failCount);
        if (errors.length() > 0) {
            message += ". Errors: " + errors.toString();
        }

        return new BulkActionResult(successCount, failCount, message);
    }

    /**
     * Bulk delete multiple reviews
     */
    @Transactional
    public BulkActionResult bulkDeleteReviews(List<Integer> reviewIds) {
        if (reviewIds == null || reviewIds.isEmpty()) {
            return new BulkActionResult(0, 0, "No review IDs provided");
        }

        int successCount = 0;
        int failCount = 0;
        StringBuilder errors = new StringBuilder();

        for (Integer reviewId : reviewIds) {
            try {
                if (deleteReview(reviewId)) {
                    successCount++;
                } else {
                    failCount++;
                    errors.append("Review ID ").append(reviewId).append(" not found. ");
                }
            } catch (Exception e) {
                failCount++;
                errors.append("Error deleting review ID ").append(reviewId).append(": ").append(e.getMessage())
                        .append(". ");
            }
        }

        String message = String.format("Bulk delete completed. Success: %d, Failed: %d", successCount, failCount);
        if (errors.length() > 0) {
            message += ". Errors: " + errors.toString();
        }

        return new BulkActionResult(successCount, failCount, message);
    }

    /**
     * Get all admin users
     */
    public List<Users> getAllAdminUsers() {
        return userRepository.findByRole(Users.Role.admin);
    }

    /**
     * Inner class for review statistics
     */
    public static class ReviewStatistics {
        private Long totalReviews;
        private Long visibleReviews;
        private Long hiddenReviews;
        private Double hiddenPercentage;

        public ReviewStatistics() {}

        public ReviewStatistics(Long totalReviews, Long visibleReviews, Long hiddenReviews, Double hiddenPercentage) {
            this.totalReviews = totalReviews;
            this.visibleReviews = visibleReviews;
            this.hiddenReviews = hiddenReviews;
            this.hiddenPercentage = hiddenPercentage;
        }

        public Long getTotalReviews() { return totalReviews; }
        public void setTotalReviews(Long totalReviews) { this.totalReviews = totalReviews; }
        public Long getVisibleReviews() { return visibleReviews; }
        public void setVisibleReviews(Long visibleReviews) { this.visibleReviews = visibleReviews; }
        public Long getHiddenReviews() { return hiddenReviews; }
        public void setHiddenReviews(Long hiddenReviews) { this.hiddenReviews = hiddenReviews; }
        public Double getHiddenPercentage() { return hiddenPercentage; }
        public void setHiddenPercentage(Double hiddenPercentage) { this.hiddenPercentage = hiddenPercentage; }

        public static ReviewStatisticsBuilder builder() {
            return new ReviewStatisticsBuilder();
        }

        public static class ReviewStatisticsBuilder {
            private Long totalReviews;
            private Long visibleReviews;
            private Long hiddenReviews;
            private Double hiddenPercentage;

            public ReviewStatisticsBuilder totalReviews(Long totalReviews) {
                this.totalReviews = totalReviews;
                return this;
            }

            public ReviewStatisticsBuilder visibleReviews(Long visibleReviews) {
                this.visibleReviews = visibleReviews;
                return this;
            }

            public ReviewStatisticsBuilder hiddenReviews(Long hiddenReviews) {
                this.hiddenReviews = hiddenReviews;
                return this;
            }

            public ReviewStatisticsBuilder hiddenPercentage(Double hiddenPercentage) {
                this.hiddenPercentage = hiddenPercentage;
                return this;
            }

            public ReviewStatistics build() {
                return new ReviewStatistics(totalReviews, visibleReviews, hiddenReviews, hiddenPercentage);
            }
        }
    }

    /**
     * Inner class for bulk operation results
     */
    public static class BulkActionResult {
        private Integer successCount;
        private Integer failCount;
        private String message;

        public BulkActionResult() {}

        public BulkActionResult(Integer successCount, Integer failCount, String message) {
            this.successCount = successCount;
            this.failCount = failCount;
            this.message = message;
        }

        public Integer getSuccessCount() { return successCount; }
        public void setSuccessCount(Integer successCount) { this.successCount = successCount; }
        public Integer getFailCount() { return failCount; }
        public void setFailCount(Integer failCount) { this.failCount = failCount; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}
