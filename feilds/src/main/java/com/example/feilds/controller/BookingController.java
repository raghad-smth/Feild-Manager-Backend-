package com.example.feilds.controller;

import com.example.feilds.model.*;
import com.example.feilds.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
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

    // 5) Create a new booking request (team admin only)
    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody Map<String, Object> body) {
        try {
            Integer teamId = (Integer) body.get("teamId");
            Integer fieldSlotId = (Integer) body.get("fieldSlotId");
            Integer playerId = (Integer) body.get("playerId");
            String dateStr = (String) body.get("date");

            if (teamId == null || fieldSlotId == null || playerId == null || dateStr == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Team ID, Field Slot ID, Player ID, and date are required"));
            }

            // Validate inputs and create booking
            Teams team = teamRepo.findById(teamId).orElse(null);
            Users player = userRepo.findById(playerId).orElse(null);
            
            if (team == null || player == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid team or player"));
            }

            // TODO: Add field slot validation and create booking
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Booking request created successfully");
            
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to create booking: " + e.getMessage()));
        }
    }

    // 6) Get booking history with filters (admin)
    @GetMapping("/admin/history")
    public ResponseEntity<?> getBookingHistory(
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @RequestParam(required = false) Integer fieldId,
            @RequestParam(required = false) Integer teamId,
            @RequestHeader("X-Admin-ID") Integer adminId) {
        
        try {
            // TODO: Implement proper filtering logic
            List<Bookings> bookings = bookingsRepo.findAll();
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Booking history retrieved successfully");
            response.put("data", bookings);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve booking history: " + e.getMessage()));
        }
    }

    // 7) Approve/Reject booking (admin only)
    @PutMapping("/{bookingId}/status")
    public ResponseEntity<?> updateBookingStatus(
            @PathVariable Integer bookingId,
            @RequestBody Map<String, String> body,
            @RequestHeader("X-Admin-ID") Integer adminId) {
        
        try {
            String status = body.get("status");
            String reason = body.get("reason");

            if (status == null || status.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Status is required"));
            }

            Bookings booking = bookingsRepo.findById(bookingId).orElse(null);
            if (booking == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Booking not found"));
            }

            // Update booking status
            booking.setStatus(status);
            bookingsRepo.save(booking);

            // Create status change record
            BookingStatusChanges statusChange = BookingStatusChanges.builder()
                    .booking(booking)
                    .status(status)
                    .cancelledReason(reason)
                    .createdAt(LocalDateTime.now())
                    .build();
            statusRepo.save(statusChange);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Booking status updated successfully");
            response.put("data", booking);
            
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update booking status: " + e.getMessage()));
        }
    }

    // 8) Get all pending bookings (admin only)
    @GetMapping("/admin/pending")
    public ResponseEntity<?> getPendingBookings(@RequestHeader("X-Admin-ID") Integer adminId) {
        try {
            // TODO: Implement proper query for pending bookings
            List<Bookings> pendingBookings = bookingsRepo.findAll(); // Filter by status = 'pending'
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Pending bookings retrieved successfully");
            response.put("data", pendingBookings);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve pending bookings: " + e.getMessage()));
        }
    }
}
