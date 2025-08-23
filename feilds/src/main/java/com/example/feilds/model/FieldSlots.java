package com.example.feilds.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalTime;

@Entity
@Table(name = "field_slots")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FieldSlots {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "field_id", nullable = false)
    private Fields field;

    @ManyToOne
    @JoinColumn(name = "week_day_id", nullable = false)
    private WeekDays weekDay;

    @Column(nullable = false)
    private LocalTime from;

    @Column(nullable = false)
    private LocalTime to;

    @Column(nullable = false)
    private BigDecimal price;
}
