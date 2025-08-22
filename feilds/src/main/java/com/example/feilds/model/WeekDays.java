package com.example.feilds.model;

import jakarta.persistence.*;
import lombok.*;

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
}
