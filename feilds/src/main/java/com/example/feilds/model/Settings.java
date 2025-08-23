package com.example.feilds.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "settings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Settings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private Users admin;

    @Column(nullable = false)
    private String name;

    private String logoUrl;
    private String aboutImageUrl;

    @Column(columnDefinition = "TEXT")
    private String aboutDescription;

    @Column(columnDefinition = "TEXT")
    private String termsAndConditions;

    private String facebookUrl;
    private String whatsappNumber;
    private String phoneNumber;
    private String secondPhoneNumber;
}
