package com.example.feilds.model;

import jakarta.persistence.*;

@Entity
@Table(name = "fields")
public class Fields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String images;

    @Column(nullable = false)
    private Integer playersCapacity;

    @Column(nullable = false)
    private String locationAddress;

    @Column(nullable = false)
    private Boolean isActive = true;

    // Default constructor
    public Fields() {}

    // Constructor with all fields
    public Fields(Integer id, String name, String images, Integer playersCapacity, String locationAddress, Boolean isActive) {
        this.id = id;
        this.name = name;
        this.images = images;
        this.playersCapacity = playersCapacity;
        this.locationAddress = locationAddress;
        this.isActive = isActive != null ? isActive : true;
    }

    // Constructor without id (for creation)
    public Fields(String name, String images, Integer playersCapacity, String locationAddress, Boolean isActive) {
        this.name = name;
        this.images = images;
        this.playersCapacity = playersCapacity;
        this.locationAddress = locationAddress;
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

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public Integer getPlayersCapacity() {
        return playersCapacity;
    }

    public void setPlayersCapacity(Integer playersCapacity) {
        this.playersCapacity = playersCapacity;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    // Builder pattern methods
    public static FieldsBuilder builder() {
        return new FieldsBuilder();
    }

    public static class FieldsBuilder {
        private Integer id;
        private String name;
        private String images;
        private Integer playersCapacity;
        private String locationAddress;
        private Boolean isActive = true;

        public FieldsBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public FieldsBuilder name(String name) {
            this.name = name;
            return this;
        }

        public FieldsBuilder images(String images) {
            this.images = images;
            return this;
        }

        public FieldsBuilder playersCapacity(Integer playersCapacity) {
            this.playersCapacity = playersCapacity;
            return this;
        }

        public FieldsBuilder locationAddress(String locationAddress) {
            this.locationAddress = locationAddress;
            return this;
        }

        public FieldsBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public Fields build() {
            return new Fields(id, name, images, playersCapacity, locationAddress, isActive);
        }
    }
}
