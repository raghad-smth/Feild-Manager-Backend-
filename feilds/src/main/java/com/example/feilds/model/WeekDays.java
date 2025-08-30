package com.example.feilds.model;

import jakarta.persistence.*;

@Entity
@Table(name = "week_days")
public class WeekDays {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    // Default constructor
    public WeekDays() {}

    // Constructor with all fields
    public WeekDays(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    // Constructor without id (for creation)
    public WeekDays(String name) {
        this.name = name;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Builder pattern methods
    public static WeekDaysBuilder builder() {
        return new WeekDaysBuilder();
    }

    public static class WeekDaysBuilder {
        private Integer id;
        private String name;

        public WeekDaysBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public WeekDaysBuilder name(String name) {
            this.name = name;
            return this;
        }

        public WeekDays build() {
            return new WeekDays(id, name);
        }
    }
}
