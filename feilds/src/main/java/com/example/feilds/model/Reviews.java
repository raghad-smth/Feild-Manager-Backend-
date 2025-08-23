package com.example.feilds.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    private Boolean isHidden;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
