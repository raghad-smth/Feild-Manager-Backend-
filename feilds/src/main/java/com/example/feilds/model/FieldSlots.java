package com.example.feilds.model;

import jakarta.persistence.*;
import lombok.*;
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

    // Avoid using reserved SQL keywords like 'from' and 'to'
    @Column(name = "from_time", nullable = false)
    private LocalTime fromTime;

    @Column(name = "to_time", nullable = false)
    private LocalTime toTime;

    @Column(nullable = false)
    private BigDecimal price;
}
