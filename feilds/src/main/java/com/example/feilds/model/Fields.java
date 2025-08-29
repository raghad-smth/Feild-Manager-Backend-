package com.example.feilds.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "fields")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    private Boolean isActive;
}
