package com.example.feilds.controller;

import com.example.feilds.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@RequestMapping("/admin/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/bookField")
    public ResponseEntity<String> bookField(
            @RequestParam Integer fieldId,
            @RequestParam Integer weekDayId,
            @RequestParam LocalTime from,
            @RequestParam LocalTime to,
            @RequestParam BigDecimal price,
            @RequestParam Integer adminId,
            @RequestParam LocalDate date,
            @RequestParam String role
    ) {
        try {
            String result = bookingService.bookField(fieldId, weekDayId, from, to, price, adminId, date, role);
            if (result.startsWith("Field booked successfully")) {
                return ResponseEntity.ok(result);
            } else if (result.equals("Only admins can book fields.")) {
                return ResponseEntity.status(403).body(result);
            } else {
                return ResponseEntity.badRequest().body(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}
