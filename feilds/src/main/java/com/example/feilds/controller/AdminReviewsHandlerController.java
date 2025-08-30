package com.example.feilds.controller;

import com.example.feilds.model.Reviews;
import com.example.feilds.model.Users;
import com.example.feilds.service.AdminReviewsHandlerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/reviews")
@CrossOrigin(origins = "*")
public class AdminReviewsHandlerController {

    private final AdminReviewsHandlerService adminReviewsService;
    
    public AdminReviewsHandlerController(AdminReviewsHandlerService adminReviewsService) {
        this.adminReviewsService = adminReviewsService;
    }

    /**
     * Get all reviews for admin (including hidden ones)
     */
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<Reviews>>> getAllReviews(
            @RequestHeader("X-User-ID") Integer userId) {

        if (!adminReviewsService.validateAdminAccess(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error("Access denied. Admin role required or invalid user ID."));
        }

        try {
            List<Reviews> reviews = adminReviewsService.getAllReviewsForAdmin();
            return ResponseEntity.ok(ApiResponse.success(reviews, "All reviews retrieved successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve reviews: " + e.getMessage()));
        }
    }

    /**
     * Get only visible reviews
     */
    @GetMapping("/visible")
    public ResponseEntity<ApiResponse<List<Reviews>>> getVisibleReviews(
            @RequestHeader("X-User-ID") Integer userId) {

        if (!adminReviewsService.validateAdminAccess(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error("Access denied. Admin role required or invalid user ID."));
        }

        try {
            List<Reviews> reviews = adminReviewsService.getVisibleReviews();
            return ResponseEntity.ok(ApiResponse.success(reviews, "Visible reviews retrieved successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve visible reviews: " + e.getMessage()));
        }
    }

    /**
     * Get only hidden reviews
     */
    @GetMapping("/hidden")
    public ResponseEntity<ApiResponse<List<Reviews>>> getHiddenReviews(
            @RequestHeader("X-User-ID") Integer userId) {

        if (!adminReviewsService.validateAdminAccess(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error("Access denied. Admin role required or invalid user ID."));
        }

        try {
            List<Reviews> reviews = adminReviewsService.getHiddenReviews();
            return ResponseEntity.ok(ApiResponse.success(reviews, "Hidden reviews retrieved successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve hidden reviews: " + e.getMessage()));
        }
    }

    /**
     * Hide a review
     */
    @PutMapping("/{reviewId}/hide")
    public ResponseEntity<ApiResponse<String>> hideReview(
            @PathVariable Integer reviewId,
            @RequestHeader("X-User-ID") Integer userId) {

        if (!adminReviewsService.validateAdminAccess(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error("Access denied. Admin role required or invalid user ID."));
        }

        if (reviewId == null || reviewId <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Invalid review ID"));
        }

        try {
            boolean success = adminReviewsService.hideReview(reviewId);
            if (success) {
                return ResponseEntity
                        .ok(ApiResponse.success("Success", "Review with ID " + reviewId + " has been hidden"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Review not found or already hidden"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to hide review: " + e.getMessage()));
        }
    }

    /**
     * Unhide a review
     */
    @PutMapping("/{reviewId}/unhide")
    public ResponseEntity<ApiResponse<String>> unhideReview(
            @PathVariable Integer reviewId,
            @RequestHeader("X-User-ID") Integer userId) {

        if (!adminReviewsService.validateAdminAccess(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error("Access denied. Admin role required or invalid user ID."));
        }

        if (reviewId == null || reviewId <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Invalid review ID"));
        }

        try {
            boolean success = adminReviewsService.unhideReview(reviewId);
            if (success) {
                return ResponseEntity
                        .ok(ApiResponse.success("Success", "Review with ID " + reviewId + " is now visible"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Review not found or already visible"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to unhide review: " + e.getMessage()));
        }
    }

    /**
     * Permanently delete a review
     */
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<String>> deleteReview(
            @PathVariable Integer reviewId,
            @RequestHeader("X-User-ID") Integer userId) {

        if (!adminReviewsService.validateAdminAccess(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error("Access denied. Admin role required or invalid user ID."));
        }

        if (reviewId == null || reviewId <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Invalid review ID"));
        }

        try {
            boolean success = adminReviewsService.deleteReview(reviewId);
            if (success) {
                return ResponseEntity.ok(
                        ApiResponse.success("Success", "Review with ID " + reviewId + " has been permanently deleted"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Review not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to delete review: " + e.getMessage()));
        }
    }

    /**
     * Search reviews by keyword for content moderation
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Reviews>>> searchReviews(
            @RequestParam String keyword,
            @RequestHeader("X-User-ID") Integer userId) {

        if (!adminReviewsService.validateAdminAccess(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error("Access denied. Admin role required or invalid user ID."));
        }

        try {
            List<Reviews> reviews = adminReviewsService.searchReviewsByKeyword(keyword);
            return ResponseEntity.ok(ApiResponse.success(reviews,
                    "Found " + reviews.size() + " reviews containing keyword: '" + keyword + "'"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to search reviews: " + e.getMessage()));
        }
    }

    /**
     * Get reviews with low ratings (potentially problematic)
     */
    @GetMapping("/low-ratings")
    public ResponseEntity<ApiResponse<List<Reviews>>> getLowRatingReviews(
            @RequestParam(defaultValue = "2") Integer maxRating,
            @RequestHeader("X-User-ID") Integer userId) {

        if (!adminReviewsService.validateAdminAccess(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error("Access denied. Admin role required or invalid user ID."));
        }

        try {
            List<Reviews> reviews = adminReviewsService.getReviewsByMaxRating(maxRating);
            return ResponseEntity.ok(
                    ApiResponse.success(reviews, "Found " + reviews.size() + " reviews with rating ≤ " + maxRating));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve low rating reviews: " + e.getMessage()));
        }
    }

    /**
     * Get reviews by specific customer
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<List<Reviews>>> getReviewsByCustomer(
            @PathVariable Integer customerId,
            @RequestHeader("X-User-ID") Integer userId) {

        if (!adminReviewsService.validateAdminAccess(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error("Access denied. Admin role required or invalid user ID."));
        }

        try {
            List<Reviews> reviews = adminReviewsService.getReviewsByCustomer(customerId);
            return ResponseEntity.ok(
                    ApiResponse.success(reviews, "Found " + reviews.size() + " reviews by customer ID: " + customerId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve customer reviews: " + e.getMessage()));
        }
    }

    /**
     * Get review statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<AdminReviewsHandlerService.ReviewStatistics>> getReviewStatistics(
            @RequestHeader("X-User-ID") Integer userId) {

        if (!adminReviewsService.validateAdminAccess(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error("Access denied. Admin role required or invalid user ID."));
        }

        try {
            AdminReviewsHandlerService.ReviewStatistics stats = adminReviewsService.getReviewStatistics();
            return ResponseEntity.ok(ApiResponse.success(stats, "Review statistics retrieved successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve statistics: " + e.getMessage()));
        }
    }

    /**
     * Get specific review details by ID
     */
    @GetMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<Reviews>> getReviewById(
            @PathVariable Integer reviewId,
            @RequestHeader("X-User-ID") Integer userId) {

        if (!adminReviewsService.validateAdminAccess(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error("Access denied. Admin role required or invalid user ID."));
        }

        if (reviewId == null || reviewId <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Invalid review ID"));
        }

        try {
            Optional<Reviews> reviewOpt = adminReviewsService.getReviewById(reviewId);
            if (reviewOpt.isPresent()) {
                return ResponseEntity.ok(ApiResponse.success(reviewOpt.get(), "Review details retrieved successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Review not found with ID: " + reviewId));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve review: " + e.getMessage()));
        }
    }

    /**
     * Bulk hide multiple reviews
     */
    @PutMapping("/bulk/hide")
    public ResponseEntity<ApiResponse<AdminReviewsHandlerService.BulkActionResult>> bulkHideReviews(
            @RequestBody BulkActionRequest request,
            @RequestHeader("X-User-ID") Integer userId) {

        if (!adminReviewsService.validateAdminAccess(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error("Access denied. Admin role required or invalid user ID."));
        }

        if (request == null || request.getReviewIds() == null || request.getReviewIds().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Request body and reviewIds are required"));
        }

        try {
            AdminReviewsHandlerService.BulkActionResult result = adminReviewsService
                    .bulkHideReviews(request.getReviewIds());
            return ResponseEntity.ok(ApiResponse.success(result, "Bulk hide operation completed"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to bulk hide reviews: " + e.getMessage()));
        }
    }

    /**
     * Bulk unhide multiple reviews
     */
    @PutMapping("/bulk/unhide")
    public ResponseEntity<ApiResponse<AdminReviewsHandlerService.BulkActionResult>> bulkUnhideReviews(
            @RequestBody BulkActionRequest request,
            @RequestHeader("X-User-ID") Integer userId) {

        if (!adminReviewsService.validateAdminAccess(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error("Access denied. Admin role required or invalid user ID."));
        }

        if (request == null || request.getReviewIds() == null || request.getReviewIds().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Request body and reviewIds are required"));
        }

        try {
            int successCount = 0;
            int failCount = 0;
            StringBuilder errors = new StringBuilder();

            for (Integer reviewId : request.getReviewIds()) {
                try {
                    if (adminReviewsService.unhideReview(reviewId)) {
                        successCount++;
                    } else {
                        failCount++;
                        errors.append("Review ID ").append(reviewId).append(" not found or already visible. ");
                    }
                } catch (Exception e) {
                    failCount++;
                    errors.append("Error unhiding review ID ").append(reviewId).append(": ").append(e.getMessage())
                            .append(". ");
                }
            }

            String message = String.format("Bulk unhide completed. Success: %d, Failed: %d", successCount, failCount);
            if (errors.length() > 0) {
                message += ". Errors: " + errors.toString();
            }

            AdminReviewsHandlerService.BulkActionResult result = new AdminReviewsHandlerService.BulkActionResult(
                    successCount, failCount, message);
            return ResponseEntity.ok(ApiResponse.success(result, "Bulk unhide operation completed"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to bulk unhide reviews: " + e.getMessage()));
        }
    }

    /**
     * Bulk delete multiple reviews
     */
    @DeleteMapping("/bulk")
    public ResponseEntity<ApiResponse<AdminReviewsHandlerService.BulkActionResult>> bulkDeleteReviews(
            @RequestBody BulkActionRequest request,
            @RequestHeader("X-User-ID") Integer userId) {

        if (!adminReviewsService.validateAdminAccess(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error("Access denied. Admin role required or invalid user ID."));
        }

        if (request == null || request.getReviewIds() == null || request.getReviewIds().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Request body and reviewIds are required"));
        }

        try {
            AdminReviewsHandlerService.BulkActionResult result = adminReviewsService
                    .bulkDeleteReviews(request.getReviewIds());
            return ResponseEntity.ok(ApiResponse.success(result, "Bulk delete operation completed"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to bulk delete reviews: " + e.getMessage()));
        }
    }

    /**
     * Get admin users (for reference/testing)
     */
    @GetMapping("/admins")
    public ResponseEntity<ApiResponse<List<Users>>> getAdminUsers(
            @RequestHeader("X-User-ID") Integer userId) {

        if (!adminReviewsService.validateAdminAccess(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error("Access denied. Admin role required or invalid user ID."));
        }

        try {
            List<Users> admins = adminReviewsService.getAllAdminUsers();
            // Remove passwords from response for security
            admins.forEach(admin -> admin.setPassword("***"));
            return ResponseEntity.ok(ApiResponse.success(admins, "Admin users retrieved successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve admin users: " + e.getMessage()));
        }
    }

    /**
     * DTO for API responses
     */
    public static class ApiResponse<T> {
        private boolean success;
        private String message;
        private T data;

        public ApiResponse() {}

        public ApiResponse(boolean success, String message, T data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }

        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public T getData() { return data; }
        public void setData(T data) { this.data = data; }

        public static <T> ApiResponse<T> success(T data, String message) {
            return new ApiResponse<>(true, message, data);
        }

        public static <T> ApiResponse<T> error(String message) {
            return new ApiResponse<>(false, message, null);
        }
    }

    /**
     * DTO for bulk actions
     */
    public static class BulkActionRequest {
        private List<Integer> reviewIds;

        public BulkActionRequest() {}

        public BulkActionRequest(List<Integer> reviewIds) {
            this.reviewIds = reviewIds;
        }

        public List<Integer> getReviewIds() { return reviewIds; }
        public void setReviewIds(List<Integer> reviewIds) { this.reviewIds = reviewIds; }
    }
}