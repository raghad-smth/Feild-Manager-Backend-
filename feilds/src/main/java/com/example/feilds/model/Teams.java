package com.example.feilds.model;

import jakarta.persistence.*;

@Entity
@Table(name = "teams")
public class Teams {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    // Default constructor
    public Teams() {}

    // Constructor with all fields
    public Teams(Integer id, String name, Boolean isActive) {
        this.id = id;
        this.name = name;
        this.isActive = isActive != null ? isActive : true;
    }

    // Constructor without id (for creation)
    public Teams(String name, Boolean isActive) {
        this.name = name;
        this.isActive = isActive != null ? isActive : true;
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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    // Builder pattern methods
    public static TeamsBuilder builder() {
        return new TeamsBuilder();
    }

    public static class TeamsBuilder {
        private Integer id;
        private String name;
        private Boolean isActive = true;

        public TeamsBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public TeamsBuilder name(String name) {
            this.name = name;
            return this;
        }

        public TeamsBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public Teams build() {
            return new Teams(id, name, isActive);
        }
    }
}