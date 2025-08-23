package com.example.feilds.controller;

import com.example.feilds.model.TeamPlayers;
import com.example.feilds.service.TeamPlayersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/players")
@CrossOrigin(origins = "*")
public class ActivatAndDeactivatePlayerController {
    
    @Autowired
    private TeamPlayersService teamPlayersService;
    
    public ActivatAndDeactivatePlayerController() {
        System.out.println("ActivatAndDeactivatePlayerController constructor called!");
    }
    
    @PostConstruct
    public void init() {
        System.out.println("ActivatAndDeactivatePlayerController initialized successfully!");
        System.out.println("TeamPlayersService injected: " + (teamPlayersService != null));
    }
    
    // Test endpoint
    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> test() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "PlayerManagementController is working!");
        response.put("timestamp", System.currentTimeMillis());
        response.put("service_available", teamPlayersService != null);
        return ResponseEntity.ok(response);
    }
    
    // ADMIN ONLY: Activate player in team
    @PutMapping("/team/{teamId}/player/{playerId}/activate")
    public ResponseEntity<?> activatePlayer(
            @PathVariable Integer teamId,
            @PathVariable Integer playerId,
            @RequestHeader("Admin-Id") Integer requestingUserId) {
        try {
            TeamPlayers updatedPlayer = teamPlayersService.activatePlayerInTeam(playerId, teamId, requestingUserId);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Player activated successfully");
            response.put("player", updatedPlayer);
            response.put("teamId", teamId);
            response.put("playerId", playerId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            errorResponse.put("teamId", teamId);
            errorResponse.put("playerId", playerId);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    // ADMIN ONLY: Deactivate player in team
    @PutMapping("/team/{teamId}/player/{playerId}/deactivate")
    public ResponseEntity<?> deactivatePlayer(
            @PathVariable Integer teamId,
            @PathVariable Integer playerId,
            @RequestHeader("Admin-Id") Integer requestingUserId) {
        try {
            TeamPlayers updatedPlayer = teamPlayersService.deactivatePlayerInTeam(playerId, teamId, requestingUserId);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Player deactivated successfully");
            response.put("player", updatedPlayer);
            response.put("teamId", teamId);
            response.put("playerId", playerId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            errorResponse.put("teamId", teamId);
            errorResponse.put("playerId", playerId);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    // EVERYONE: Get player status in specific team
    @GetMapping("/team/{teamId}/player/{playerId}/status")
    public ResponseEntity<?> getPlayerStatus(
            @PathVariable Integer teamId,
            @PathVariable Integer playerId) {
        try {
            TeamPlayers playerStatus = teamPlayersService.getPlayerStatusInTeam(playerId, teamId);
            Map<String, Object> response = new HashMap<>();
            response.put("playerId", playerId);
            response.put("teamId", teamId);
            response.put("isActive", playerStatus.getIsActive());
            response.put("isAdmin", playerStatus.getIsAdmin());
            response.put("playerDetails", playerStatus);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            errorResponse.put("teamId", teamId);
            errorResponse.put("playerId", playerId);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @GetMapping("/player/{playerId}/teams")
    public ResponseEntity<?> getPlayerTeams(@PathVariable Integer playerId) {
        try {
            List<TeamPlayers> memberships = teamPlayersService.getPlayerAllTeamMemberships(playerId);
            Map<String, Object> response = new HashMap<>();
            response.put("playerId", playerId);
            response.put("totalTeams", memberships.size());
            response.put("memberships", memberships);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            errorResponse.put("playerId", playerId);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @GetMapping("/team/{teamId}/player/{playerId}/is-admin")
    public ResponseEntity<Map<String, Object>> checkIsAdmin(
            @PathVariable Integer teamId,
            @PathVariable Integer playerId) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean isAdmin = teamPlayersService.isPlayerAdmin(playerId, teamId);
            boolean isAdminAnyTeam = teamPlayersService.isPlayerAdminOfAnyTeam(playerId);
            
            response.put("playerId", playerId);
            response.put("teamId", teamId);
            response.put("isAdminOfThisTeam", isAdmin);
            response.put("isAdminOfAnyTeam", isAdminAnyTeam);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @GetMapping("/team/{teamId}/players")
    public ResponseEntity<?> getTeamPlayers(
            @PathVariable Integer teamId,
            @RequestParam(defaultValue = "false") Boolean activeOnly) {
        try {
            List<TeamPlayers> players;
            if (activeOnly) {
                players = teamPlayersService.getActivePlayersForTeam(teamId);
            } else {
                players = teamPlayersService.getAllPlayersForTeam(teamId);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("teamId", teamId);
            response.put("activeOnly", activeOnly);
            response.put("totalPlayers", players.size());
            response.put("players", players);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            errorResponse.put("teamId", teamId);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}