package com.example.feilds.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalTime;

@Entity
@Table(name = "field_slots")
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

    // Default constructor
    public FieldSlots() {}

    // Constructor with all fields
    public FieldSlots(Integer id, Fields field, WeekDays weekDay, LocalTime fromTime, LocalTime toTime, BigDecimal price) {
        this.id = id;
        this.field = field;
        this.weekDay = weekDay;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.price = price;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Fields getField() {
        return field;
    }

    public void setField(Fields field) {
        this.field = field;
    }

    public WeekDays getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(WeekDays weekDay) {
        this.weekDay = weekDay;
    }

    public LocalTime getFromTime() {
        return fromTime;
    }

    public void setFromTime(LocalTime fromTime) {
        this.fromTime = fromTime;
    }

    public LocalTime getToTime() {
        return toTime;
    }

    public void setToTime(LocalTime toTime) {
        this.toTime = toTime;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    // Builder pattern methods
    public static FieldSlotsBuilder builder() {
        return new FieldSlotsBuilder();
    }

    public static class FieldSlotsBuilder {
        private Integer id;
        private Fields field;
        private WeekDays weekDay;
        private LocalTime fromTime;
        private LocalTime toTime;
        private BigDecimal price;

        public FieldSlotsBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public FieldSlotsBuilder field(Fields field) {
            this.field = field;
            return this;
        }

        public FieldSlotsBuilder weekDay(WeekDays weekDay) {
            this.weekDay = weekDay;
            return this;
        }

        public FieldSlotsBuilder fromTime(LocalTime fromTime) {
            this.fromTime = fromTime;
            return this;
        }

        public FieldSlotsBuilder toTime(LocalTime toTime) {
            this.toTime = toTime;
            return this;
        }

        public FieldSlotsBuilder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public FieldSlots build() {
            return new FieldSlots(id, field, weekDay, fromTime, toTime, price);
        }
    }
}
