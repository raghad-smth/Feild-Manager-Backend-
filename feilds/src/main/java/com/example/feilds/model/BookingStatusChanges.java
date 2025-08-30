package com.example.feilds.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "booking_status_changes")
public class BookingStatusChanges {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Bookings booking;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(columnDefinition = "TEXT")
    private String cancelledReason;

    // Default constructor
    public BookingStatusChanges() {}

    // Constructor with all fields
    public BookingStatusChanges(Integer id, Bookings booking, String status, LocalDateTime createdAt, String cancelledReason) {
        this.id = id;
        this.booking = booking;
        this.status = status;
        this.createdAt = createdAt;
        this.cancelledReason = cancelledReason;
    }

    // Constructor without id (for creation)
    public BookingStatusChanges(Bookings booking, String status, LocalDateTime createdAt, String cancelledReason) {
        this.booking = booking;
        this.status = status;
        this.createdAt = createdAt;
        this.cancelledReason = cancelledReason;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Bookings getBooking() {
        return booking;
    }

    public void setBooking(Bookings booking) {
        this.booking = booking;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCancelledReason() {
        return cancelledReason;
    }

    public void setCancelledReason(String cancelledReason) {
        this.cancelledReason = cancelledReason;
    }

    // Builder pattern methods
    public static BookingStatusChangesBuilder builder() {
        return new BookingStatusChangesBuilder();
    }

    public static class BookingStatusChangesBuilder {
        private Integer id;
        private Bookings booking;
        private String status;
        private LocalDateTime createdAt;
        private String cancelledReason;

        public BookingStatusChangesBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public BookingStatusChangesBuilder booking(Bookings booking) {
            this.booking = booking;
            return this;
        }

        public BookingStatusChangesBuilder status(String status) {
            this.status = status;
            return this;
        }

        public BookingStatusChangesBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public BookingStatusChangesBuilder cancelledReason(String cancelledReason) {
            this.cancelledReason = cancelledReason;
            return this;
        }

        public BookingStatusChanges build() {
            return new BookingStatusChanges(id, booking, status, createdAt, cancelledReason);
        }
    }
}
