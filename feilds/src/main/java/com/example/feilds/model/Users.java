package com.example.feilds.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.player;

    public enum Role {
        admin, player
    }

    // Default constructor
    public Users() {}

    // Constructor with all fields
    public Users(Integer id, String name, String email, String phone, String password, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.role = role != null ? role : Role.player;
    }

    // Constructor without id (for creation)
    public Users(String name, String email, String phone, String password, Role role) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.role = role != null ? role : Role.player;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    // Builder pattern methods
    public static UsersBuilder builder() {
        return new UsersBuilder();
    }

    public static class UsersBuilder {
        private Integer id;
        private String name;
        private String email;
        private String phone;
        private String password;
        private Role role = Role.player;

        public UsersBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public UsersBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UsersBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UsersBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public UsersBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UsersBuilder role(Role role) {
            this.role = role;
            return this;
        }

        public Users build() {
            return new Users(id, name, email, phone, password, role);
        }
    }
}
