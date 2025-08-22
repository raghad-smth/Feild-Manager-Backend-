package com.example.feilds.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "enquiries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Enquiries {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Users customer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(columnDefinition = "TEXT")
    private String adminResponse;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isHidden = false;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public enum Status {
        open, in_progress, closed
    }
}
