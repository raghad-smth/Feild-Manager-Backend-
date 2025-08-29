package com.example.feilds.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bookings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private Users player;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Teams team;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "field_slot_id", nullable = false)
    private FieldSlots fieldSlot;

    @Column(nullable = false)
    private BigDecimal price;
}
