package com.example.feilds.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "week_days")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeekDays {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;
    // Minimal constructor to use in your booking endpoint
    public WeekDays(Integer id) {
        this.id = id;
    }
}
