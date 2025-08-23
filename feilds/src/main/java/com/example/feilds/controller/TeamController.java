package com.example.feilds.controller;

import com.example.feilds.model.Teams;
import com.example.feilds.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/teams")
@CrossOrigin(origins = "*")
public class TeamController {

    @Autowired
    private TeamService teamService;

    /**
     * Create a new team
     * POST /api/teams
     */
    @PostMapping
    public ResponseEntity<?> createTeam(@RequestBody Map<String, String> request) {
        try {
            String teamName = request.get("name");
            String playerIdStr = request.get("playerId");

            if (teamName == null || teamName.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Team name is required"));
            }

            if (playerIdStr == null || playerIdStr.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Player ID is required"));
            }

            Integer playerId;
            try {
                playerId = Integer.parseInt(playerIdStr);
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid player ID format"));
            }

            Teams createdTeam = teamService.createTeam(teamName.trim(), playerId);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "message", "Team created successfully",
                            "team", createdTeam
                    ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to create team: " + e.getMessage()));
        }
    }

    /**
     * Get all teams for a specific player
     * GET /api/teams/player/{playerId}
     */
    @GetMapping("/player/{playerId}")
    public ResponseEntity<?> getPlayerTeams(@PathVariable Integer playerId) {
        try {
            List<Teams> teams = teamService.getPlayerTeams(playerId);
            return ResponseEntity.ok(Map.of(
                    "message", "Teams retrieved successfully",
                    "teams", teams
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve teams: " + e.getMessage()));
        }
    }

    /**
     * Get all teams (for browsing and joining)
     * GET /api/teams
     */
    @GetMapping
    public ResponseEntity<?> getAllTeams() {
        try {
            List<Teams> teams = teamService.getAllActiveTeams();
            return ResponseEntity.ok(Map.of(
                    "message", "Teams retrieved successfully",
                    "teams", teams
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve teams: " + e.getMessage()));
        }
    }

    /**
     * Join a team
     * POST /api/teams/{teamId}/join
     */
    @PostMapping("/{teamId}/join")
    public ResponseEntity<?> joinTeam(@PathVariable Integer teamId, @RequestBody Map<String, String> request) {
        try {
            String playerIdStr = request.get("playerId");

            if (playerIdStr == null || playerIdStr.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Player ID is required"));
            }

            Integer playerId;
            try {
                playerId = Integer.parseInt(playerIdStr);
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid player ID format"));
            }

            teamService.joinTeam(teamId, playerId);

            return ResponseEntity.ok(Map.of(
                    "message", "Successfully joined the team"
            ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to join team: " + e.getMessage()));
        }
    }

    /**
     * Leave a team
     * DELETE /api/teams/{teamId}/leave
     */
    @DeleteMapping("/{teamId}/leave")
    public ResponseEntity<?> leaveTeam(@PathVariable Integer teamId, @RequestBody Map<String, String> request) {
        try {
            String playerIdStr = request.get("playerId");

            if (playerIdStr == null || playerIdStr.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Player ID is required"));
            }

            Integer playerId;
            try {
                playerId = Integer.parseInt(playerIdStr);
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid player ID format"));
            }

            teamService.leaveTeam(teamId, playerId);

            return ResponseEntity.ok(Map.of(
                    "message", "Successfully left the team"
            ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to leave team: " + e.getMessage()));
        }
    }

    /**
     * Get team details with members
     * GET /api/teams/{teamId}
     */
    @GetMapping("/{teamId}")
    public ResponseEntity<?> getTeamDetails(@PathVariable Integer teamId) {
        try {
            Map<String, Object> teamDetails = teamService.getTeamWithMembers(teamId);
            return ResponseEntity.ok(Map.of(
                    "message", "Team details retrieved successfully",
                    "teamDetails", teamDetails
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve team details: " + e.getMessage()));
        }
    }
}