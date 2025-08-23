package com.example.feilds.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "booking_status_changes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}
