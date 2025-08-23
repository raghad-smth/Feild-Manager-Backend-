package com.example.feilds.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "team_players")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamPlayers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private Users player;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Teams team;

    @Column(nullable = false)
    private Boolean isAdmin;

    @Column(nullable = false)
    private Boolean isActive;
}
