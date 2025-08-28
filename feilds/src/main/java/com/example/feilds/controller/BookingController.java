package com.example.feilds.controller;

import com.example.feilds.model.*;
import com.example.feilds.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingsRepository bookingsRepo;
    private final BookingStatusChangesRepository statusRepo;
    private final ReviewsRepository reviewsRepo;
    private final TeamRepository teamRepo;
    private final UserRepository userRepo;

    public BookingController(BookingsRepository bookingsRepo,
                             BookingStatusChangesRepository statusRepo,
                             ReviewsRepository reviewsRepo,
                             TeamRepository teamRepo,
                             UserRepository userRepo) {
        this.bookingsRepo = bookingsRepo;
        this.statusRepo = statusRepo;
        this.reviewsRepo = reviewsRepo;
        this.teamRepo = teamRepo;
        this.userRepo = userRepo;
    }

    // 1) Track booking status timeline
    @GetMapping("/{bookingId}/status")
    public ResponseEntity<List<BookingStatusChanges>> getStatusTimeline(@PathVariable Integer bookingId) {
        return ResponseEntity.ok(statusRepo.findByBooking_IdOrderByCreatedAtAsc(bookingId));
    }

    // 2) View booking history for a team
    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<Bookings>> getTeamHistory(@PathVariable Integer teamId) {
        Teams team = teamRepo.findById(teamId).orElse(null);
        if (team == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(bookingsRepo.findByTeamOrderByDateDesc(team));
    }

    // 3) Rate a booking
    @PostMapping("/{bookingId}/rate")
    public ResponseEntity<Reviews> rateBooking(@PathVariable Integer bookingId, @RequestBody Map<String, Object> body) {
        Integer rating = (Integer) body.get("rating");
        Integer customerId = (Integer) body.get("customerId");
        if (rating == null || rating < 1 || rating > 5) return ResponseEntity.badRequest().build();
        Bookings booking = bookingsRepo.findById(bookingId).orElse(null);
        Users customer = userRepo.findById(customerId).orElse(null);
        if (booking == null || customer == null) return ResponseEntity.notFound().build();
        Reviews review = Reviews.builder()
                .booking(booking)
                .customer(customer)
                .rating(rating)
                .comment(null)
                .isHidden(false)
                .createdAt(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(reviewsRepo.save(review));
    }

    // 4) Review a booking (with optional rating)
    @PostMapping("/{bookingId}/review")
    public ResponseEntity<Reviews> reviewBooking(@PathVariable Integer bookingId, @RequestBody Map<String, Object> body) {
        Integer rating = body.get("rating") instanceof Integer ? (Integer) body.get("rating") : null;
        String comment = (String) body.get("comment");
        Integer customerId = (Integer) body.get("customerId");
        Bookings booking = bookingsRepo.findById(bookingId).orElse(null);
        Users customer = userRepo.findById(customerId).orElse(null);
        if (booking == null || customer == null) return ResponseEntity.notFound().build();
        Reviews review = Reviews.builder()
                .booking(booking)
                .customer(customer)
                .rating(rating != null ? rating : 0)
                .comment(comment)
                .isHidden(false)
                .createdAt(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(reviewsRepo.save(review));
    }
}
