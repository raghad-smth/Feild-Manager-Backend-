package com.example.feilds.model;

import jakarta.persistence.*;

@Entity
@Table(name = "team_players")
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
    private Boolean isAdmin = false;

    @Column(nullable = false)
    private Boolean isActive = true;

    // Default constructor
    public TeamPlayers() {}

    // Constructor with all fields
    public TeamPlayers(Integer id, Users player, Teams team, Boolean isAdmin, Boolean isActive) {
        this.id = id;
        this.player = player;
        this.team = team;
        this.isAdmin = isAdmin != null ? isAdmin : false;
        this.isActive = isActive != null ? isActive : true;
    }

    // Constructor without id (for creation)
    public TeamPlayers(Users player, Teams team, Boolean isAdmin, Boolean isActive) {
        this.player = player;
        this.team = team;
        this.isAdmin = isAdmin != null ? isAdmin : false;
        this.isActive = isActive != null ? isActive : true;
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

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    // Builder pattern methods
    public static TeamPlayersBuilder builder() {
        return new TeamPlayersBuilder();
    }

    public static class TeamPlayersBuilder {
        private Integer id;
        private Users player;
        private Teams team;
        private Boolean isAdmin = false;
        private Boolean isActive = true;

        public TeamPlayersBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public TeamPlayersBuilder player(Users player) {
            this.player = player;
            return this;
        }

        public TeamPlayersBuilder team(Teams team) {
            this.team = team;
            return this;
        }

        public TeamPlayersBuilder isAdmin(Boolean isAdmin) {
            this.isAdmin = isAdmin;
            return this;
        }

        public TeamPlayersBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public TeamPlayers build() {
            return new TeamPlayers(id, player, team, isAdmin, isActive);
        }
    }
}
