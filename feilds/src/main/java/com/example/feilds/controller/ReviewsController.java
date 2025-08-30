// ReviewsController.java
package com.example.feilds.controller;

import com.example.feilds.model.Reviews;
import com.example.feilds.service.ReviewsService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewsController {

    private final ReviewsService reviewsService;
    
    public ReviewsController(ReviewsService reviewsService) {
        this.reviewsService = reviewsService;
    }

    // Consistent API wrapper (no new files)
    private static class ApiResponse<T> {
        private boolean success;
        private String message;
        private T data;
        private Instant timestamp;

        public ApiResponse(boolean success, String message, T data, Instant timestamp) {
            this.success = success;
            this.message = message;
            this.data = data;
            this.timestamp = timestamp;
        }

        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public T getData() { return data; }
        public void setData(T data) { this.data = data; }
        public Instant getTimestamp() { return timestamp; }
        public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }

        public static <T> ApiResponse<T> ok(String message, T data) {
            return new ApiResponse<>(true, message, data, Instant.now());
        }

        public static <T> ApiResponse<T> fail(String message) {
            return new ApiResponse<>(false, message, null, Instant.now());
        }
    }

    /* ------------------------ VIEW-ONLY ENDPOINTS ------------------------ */

    /** List all visible reviews (paged & sorted) */
    @GetMapping("/visible")
    public ResponseEntity<ApiResponse<Page<Reviews>>> getAllVisibleReviewsPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String sort // e.g. "id,desc" or "id,asc"
    ) {
        Page<Reviews> data = reviewsService.getVisibleReviewsPaged(page, size, sort);
        return ResponseEntity.ok(ApiResponse.ok("Fetched visible reviews", data));
    }

    /** Filter visible reviews by rating range (inclusive) with paging/sorting */
    @GetMapping("/visible/by-rating")
    public ResponseEntity<ApiResponse<Page<Reviews>>> getVisibleByRatingRange(
            @RequestParam Integer min, @RequestParam Integer max,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String sort) {
        Page<Reviews> data = reviewsService.getVisibleReviewsByRatingRange(min, max, page, size, sort);
        return ResponseEntity.ok(ApiResponse.ok("Fetched visible reviews by rating range", data));
    }

    /** Get a single visible review by ID */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Reviews>> getVisibleReviewById(@PathVariable Integer id) {
        Reviews review = reviewsService.getVisibleReviewById(id);
        return ResponseEntity.ok(ApiResponse.ok("Fetched review", review));
    }

    /** Visible reviews by customer (list) */
    @GetMapping("/customers/{customerId}/visible")
    public ResponseEntity<ApiResponse<List<Reviews>>> getVisibleReviewsByCustomerId(@PathVariable Integer customerId) {
        List<Reviews> reviews = reviewsService.getVisibleReviewsByCustomerId(customerId);
        return ResponseEntity.ok(ApiResponse.ok("Fetched customer's visible reviews", reviews));
    }

    /** Average rating (visible only) */
    @GetMapping("/average-rating")
    public ResponseEntity<ApiResponse<Double>> getAverageRating() {
        Double avg = reviewsService.getAverageRating();
        return ResponseEntity.ok(ApiResponse.ok("Average rating", avg));
    }

    /** Average rating by customer (visible only) */
    @GetMapping("/customers/{customerId}/average-rating")
    public ResponseEntity<ApiResponse<Double>> getAverageRatingByCustomer(@PathVariable Integer customerId) {
        Double avg = reviewsService.getAverageRatingByCustomer(customerId);
        return ResponseEntity.ok(ApiResponse.ok("Customer's average rating", avg));
    }

    /** Total visible reviews count */
    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> getTotalVisibleReviews() {
        long count = reviewsService.getTotalVisibleReviews();
        return ResponseEntity.ok(ApiResponse.ok("Total visible reviews", count));
    }

    /** Ratings breakdown (histogram) */
    @GetMapping("/breakdown")
    public ResponseEntity<ApiResponse<List<ReviewsService.RatingCount>>> getRatingBreakdown() {
        List<ReviewsService.RatingCount> breakdown = reviewsService.getRatingBreakdown();
        return ResponseEntity.ok(ApiResponse.ok("Ratings breakdown", breakdown));
    }

    /** Recent visible reviews (limit) */
    @GetMapping("/recent")
    public ResponseEntity<ApiResponse<Page<Reviews>>> getRecentVisibleReviews(
            @RequestParam(defaultValue = "10") int limit) {
        Page<Reviews> data = reviewsService.getRecentVisibleReviews(limit);
        return ResponseEntity.ok(ApiResponse.ok("Recent visible reviews", data));
    }

    /* ------------------------ Error Handling ------------------------ */

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgument(IllegalArgumentException ex) {
        // Validation error
        return ResponseEntity.badRequest().body(ApiResponse.fail(ex.getMessage()));
    }

    @ExceptionHandler(ReviewsService.NotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(ReviewsService.NotFoundException ex) {
        // Not found error
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.fail(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleOtherErrors(Exception ex) {
        // Unexpected error
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.fail("An unexpected error occurred. Please try again later."));
    }
}
