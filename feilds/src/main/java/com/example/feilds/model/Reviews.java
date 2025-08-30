package com.example.feilds.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
public class Reviews {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Bookings booking;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Users customer;

    @Column(nullable = false)
    private Integer rating;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(nullable = false)
    private Boolean isHidden = false;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    // Default constructor
    public Reviews() {}

    // Constructor with all fields
    public Reviews(Integer id, Bookings booking, Users customer, Integer rating, String comment, Boolean isHidden, LocalDateTime createdAt) {
        this.id = id;
        this.booking = booking;
        this.customer = customer;
        this.rating = rating;
        this.comment = comment;
        this.isHidden = isHidden != null ? isHidden : false;
        this.createdAt = createdAt;
    }

    // Constructor without id (for creation)
    public Reviews(Bookings booking, Users customer, Integer rating, String comment, Boolean isHidden, LocalDateTime createdAt) {
        this.booking = booking;
        this.customer = customer;
        this.rating = rating;
        this.comment = comment;
        this.isHidden = isHidden != null ? isHidden : false;
        this.createdAt = createdAt;
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

    public Users getCustomer() {
        return customer;
    }

    public void setCustomer(Users customer) {
        this.customer = customer;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean getIsHidden() {
        return isHidden;
    }

    public void setIsHidden(Boolean isHidden) {
        this.isHidden = isHidden;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Builder pattern methods
    public static ReviewsBuilder builder() {
        return new ReviewsBuilder();
    }

    public static class ReviewsBuilder {
        private Integer id;
        private Bookings booking;
        private Users customer;
        private Integer rating;
        private String comment;
        private Boolean isHidden = false;
        private LocalDateTime createdAt;

        public ReviewsBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public ReviewsBuilder booking(Bookings booking) {
            this.booking = booking;
            return this;
        }

        public ReviewsBuilder customer(Users customer) {
            this.customer = customer;
            return this;
        }

        public ReviewsBuilder rating(Integer rating) {
            this.rating = rating;
            return this;
        }

        public ReviewsBuilder comment(String comment) {
            this.comment = comment;
            return this;
        }

        public ReviewsBuilder isHidden(Boolean isHidden) {
            this.isHidden = isHidden;
            return this;
        }

        public ReviewsBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Reviews build() {
            return new Reviews(id, booking, customer, rating, comment, isHidden, createdAt);
        }
    }
}
