// ReviewsService.java
package com.example.feilds.service;

import com.example.feilds.model.Reviews;
import com.example.feilds.repository.ReviewsRepository;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewsService {

    private final ReviewsRepository reviewsRepository;

    // Domain exception kept local (no extra files)
    public static class NotFoundException extends RuntimeException {
        public NotFoundException(String message) {
            super(message);
        }
    }

    // Lightweight DTO for rating histogram (no new files; inner class)
    @Value
    public static class RatingCount {
        Integer rating;
        Long count;
    }

    /* ------------ Validation helpers (service-layer only) ------------ */

    private void requirePositive(Integer value, String field) {
        if (value == null || value <= 0)
            throw new IllegalArgumentException(field + " must be a positive integer.");
    }

    private void requireNonNegative(int value, String field) {
        if (value < 0)
            throw new IllegalArgumentException(field + " must be >= 0.");
    }

    private void validatePageSize(int page, int size) {
        requireNonNegative(page, "page");
        if (size <= 0 || size > 200) { // sane cap
            throw new IllegalArgumentException("size must be between 1 and 200.");
        }
    }

    private Sort parseSort(String sort) {
        // Accept "field,desc" or "field,asc"; default to "id,desc"
        String fallbackProp = "id";
        Sort.Direction fallbackDir = Sort.Direction.DESC;

        if (sort == null || sort.isBlank()) {
            return Sort.by(fallbackDir, fallbackProp);
        }
        String[] parts = sort.split(",", 2);
        String prop = parts[0].trim().isEmpty() ? fallbackProp : parts[0].trim();
        Sort.Direction dir = fallbackDir;
        if (parts.length > 1) {
            String raw = parts[1].trim();
            if ("asc".equalsIgnoreCase(raw))
                dir = Sort.Direction.ASC;
            if ("desc".equalsIgnoreCase(raw))
                dir = Sort.Direction.DESC;
        }
        return Sort.by(dir, prop);
    }

    private Pageable pageRequest(int page, int size, String sort) {
        validatePageSize(page, size);
        return PageRequest.of(page, size, parseSort(sort));
    }

    private void validateRatingRange(Integer min, Integer max) {
        if (min == null || max == null)
            throw new IllegalArgumentException("min and max ratings are required.");
        if (min < 1 || max > 5)
            throw new IllegalArgumentException("rating range must be within 1..5.");
        if (min > max)
            throw new IllegalArgumentException("min cannot be greater than max.");
    }

    /* -------------------- VIEW-ONLY operations -------------------- */

    public List<Reviews> getAllVisibleReviews() {
        try {
            log.debug("Fetching all visible reviews");
            return reviewsRepository.findByIsHiddenFalse();
        } catch (DataAccessException dae) {
            log.error("DB error: getAllVisibleReviews", dae);
            throw new RuntimeException("Failed to fetch reviews. Please try again later.");
        }
    }

    public Page<Reviews> getVisibleReviewsPaged(int page, int size, String sort) {
        Pageable pageable = pageRequest(page, size, sort);
        try {
            log.debug("Fetching visible reviews page={} size={} sort={}", page, size, sort);
            return reviewsRepository.findByIsHiddenFalse(pageable);
        } catch (DataAccessException dae) {
            log.error("DB error: getVisibleReviewsPaged", dae);
            throw new RuntimeException("Failed to fetch reviews. Please try again later.");
        }
    }

    public Page<Reviews> getVisibleReviewsByRatingRange(Integer min, Integer max, int page, int size, String sort) {
        validateRatingRange(min, max);
        Pageable pageable = pageRequest(page, size, sort);
        try {
            log.debug("Fetching visible reviews by rating min={} max={} page={} size={} sort={}", min, max, page, size,
                    sort);
            return reviewsRepository.findByIsHiddenFalseAndRatingBetween(min, max, pageable);
        } catch (DataAccessException dae) {
            log.error("DB error: getVisibleReviewsByRatingRange", dae);
            throw new RuntimeException("Failed to fetch filtered reviews. Please try again later.");
        }
    }

    public Reviews getVisibleReviewById(Integer id) {
        requirePositive(id, "id");
        try {
            log.debug("Fetching visible review id={}", id);
            return reviewsRepository.findByIdAndIsHiddenFalse(id)
                    .orElseThrow(() -> new NotFoundException("Review not found"));
        } catch (DataAccessException dae) {
            log.error("DB error: getVisibleReviewById id={}", id, dae);
            throw new RuntimeException("Failed to fetch the review. Please try again later.");
        }
    }

    public List<Reviews> getVisibleReviewsByCustomerId(Integer customerId) {
        requirePositive(customerId, "customerId");
        try {
            log.debug("Fetching visible reviews for customerId={}", customerId);
            return reviewsRepository.findByCustomerIdAndIsHiddenFalse(customerId);
        } catch (DataAccessException dae) {
            log.error("DB error: getVisibleReviewsByCustomerId customerId={}", customerId, dae);
            throw new RuntimeException("Failed to fetch customer reviews. Please try again later.");
        }
    }

    public Double getAverageRating() {
        try {
            log.debug("Calculating average rating (visible only)");
            Double avg = reviewsRepository.getAverageRating();
            return avg != null ? avg : 0.0d;
        } catch (DataAccessException dae) {
            log.error("DB error: getAverageRating", dae);
            throw new RuntimeException("Failed to calculate average rating. Please try again later.");
        }
    }

    public Double getAverageRatingByCustomer(Integer customerId) {
        requirePositive(customerId, "customerId");
        try {
            log.debug("Calculating average rating for customerId={}", customerId);
            Double avg = reviewsRepository.getAverageRatingByCustomer(customerId);
            return avg != null ? avg : 0.0d;
        } catch (DataAccessException dae) {
            log.error("DB error: getAverageRatingByCustomer customerId={}", customerId, dae);
            throw new RuntimeException("Failed to calculate customer's average rating. Please try again later.");
        }
    }

    public long getTotalVisibleReviews() {
        try {
            log.debug("Counting visible reviews");
            return reviewsRepository.countByIsHiddenFalse();
        } catch (DataAccessException dae) {
            log.error("DB error: getTotalVisibleReviews", dae);
            throw new RuntimeException("Failed to count reviews. Please try again later.");
        }
    }

    public List<RatingCount> getRatingBreakdown() {
        try {
            log.debug("Getting ratings breakdown (visible only)");
            List<Object[]> raw = reviewsRepository.getRatingBreakdown();
            List<RatingCount> out = new ArrayList<>();
            for (Object[] row : raw) {
                Integer rating = row[0] == null ? null : ((Number) row[0]).intValue();
                Long count = row[1] == null ? 0L : ((Number) row[1]).longValue();
                if (rating != null)
                    out.add(new RatingCount(rating, count));
            }
            return out;
        } catch (DataAccessException dae) {
            log.error("DB error: getRatingBreakdown", dae);
            throw new RuntimeException("Failed to get ratings breakdown. Please try again later.");
        }
    }

    public Page<Reviews> getRecentVisibleReviews(int limit) {
        if (limit <= 0 || limit > 200)
            throw new IllegalArgumentException("limit must be between 1 and 200.");
        try {
            // Default sorting by id desc to approximate "recent" without assuming createdAt
            // exists
            Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "id"));
            log.debug("Fetching recent visible reviews, limit={}", limit);
            return reviewsRepository.findByIsHiddenFalse(pageable);
        } catch (DataAccessException dae) {
            log.error("DB error: getRecentVisibleReviews", dae);
            throw new RuntimeException("Failed to fetch recent reviews. Please try again later.");
        }
    }
}
