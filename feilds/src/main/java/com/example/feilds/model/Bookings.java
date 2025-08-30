package com.example.feilds.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "bookings")
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
    private String status = "pending";

    @ManyToOne
    @JoinColumn(name = "field_slot_id", nullable = false)
    private FieldSlots fieldSlot;

    @Column(nullable = false)
    private BigDecimal price;

    // Default constructor
    public Bookings() {}

    // Constructor with all fields
    public Bookings(Integer id, Users player, Teams team, LocalDate date, String status, FieldSlots fieldSlot, BigDecimal price) {
        this.id = id;
        this.player = player;
        this.team = team;
        this.date = date;
        this.status = status != null ? status : "pending";
        this.fieldSlot = fieldSlot;
        this.price = price;
    }

    // Constructor without id (for creation)
    public Bookings(Users player, Teams team, LocalDate date, String status, FieldSlots fieldSlot, BigDecimal price) {
        this.player = player;
        this.team = team;
        this.date = date;
        this.status = status != null ? status : "pending";
        this.fieldSlot = fieldSlot;
        this.price = price;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Users getPlayer() {
        return player;
    }

    public void setPlayer(Users player) {
        this.player = player;
    }

    public Teams getTeam() {
        return team;
    }

    public void setTeam(Teams team) {
        this.team = team;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public FieldSlots getFieldSlot() {
        return fieldSlot;
    }

    public void setFieldSlot(FieldSlots fieldSlot) {
        this.fieldSlot = fieldSlot;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    // Builder pattern methods
    public static BookingsBuilder builder() {
        return new BookingsBuilder();
    }

    public static class BookingsBuilder {
        private Integer id;
        private Users player;
        private Teams team;
        private LocalDate date;
        private String status = "pending";
        private FieldSlots fieldSlot;
        private BigDecimal price;

        public BookingsBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public BookingsBuilder player(Users player) {
            this.player = player;
            return this;
        }

        public BookingsBuilder team(Teams team) {
            this.team = team;
            return this;
        }

        public BookingsBuilder date(LocalDate date) {
            this.date = date;
            return this;
        }

        public BookingsBuilder status(String status) {
            this.status = status;
            return this;
        }

        public BookingsBuilder fieldSlot(FieldSlots fieldSlot) {
            this.fieldSlot = fieldSlot;
            return this;
        }

        public BookingsBuilder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Bookings build() {
            return new Bookings(id, player, team, date, status, fieldSlot, price);
        }
    }
}
