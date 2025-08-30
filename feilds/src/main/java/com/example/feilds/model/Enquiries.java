package com.example.feilds.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "enquiries")
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
    private Status status = Status.open;

    @Column(columnDefinition = "TEXT")
    private String adminResponse;

    @Column(nullable = false)
    private Boolean isHidden = false;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public enum Status {
        open, in_progress, closed
    }

    // Default constructor
    public Enquiries() {}

    // Constructor with all fields
    public Enquiries(Integer id, String content, Users customer, Status status, String adminResponse, Boolean isHidden, LocalDateTime createdAt) {
        this.id = id;
        this.content = content;
        this.customer = customer;
        this.status = status != null ? status : Status.open;
        this.adminResponse = adminResponse;
        this.isHidden = isHidden != null ? isHidden : false;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Users getCustomer() {
        return customer;
    }

    public void setCustomer(Users customer) {
        this.customer = customer;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getAdminResponse() {
        return adminResponse;
    }

    public void setAdminResponse(String adminResponse) {
        this.adminResponse = adminResponse;
    }

    public Boolean getIsHidden() {
        return isHidden;
    }

    public void setIsHidden(Boolean isHidden) {
        this.isHidden = isHidden;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Builder pattern methods
    public static EnquiriesBuilder builder() {
        return new EnquiriesBuilder();
    }

    public static class EnquiriesBuilder {
        private Integer id;
        private String content;
        private Users customer;
        private Status status = Status.open;
        private String adminResponse;
        private Boolean isHidden = false;
        private LocalDateTime createdAt;

        public EnquiriesBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public EnquiriesBuilder content(String content) {
            this.content = content;
            return this;
        }

        public EnquiriesBuilder customer(Users customer) {
            this.customer = customer;
            return this;
        }

        public EnquiriesBuilder status(Status status) {
            this.status = status;
            return this;
        }

        public EnquiriesBuilder adminResponse(String adminResponse) {
            this.adminResponse = adminResponse;
            return this;
        }

        public EnquiriesBuilder isHidden(Boolean isHidden) {
            this.isHidden = isHidden;
            return this;
        }

        public EnquiriesBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Enquiries build() {
            return new Enquiries(id, content, customer, status, adminResponse, isHidden, createdAt);
        }
    }
}
