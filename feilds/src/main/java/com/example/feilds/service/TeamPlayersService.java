package com.example.feilds.service;

import com.example.feilds.model.TeamPlayers;
import com.example.feilds.repository.TeamPlayersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TeamPlayersService {
    
    @Autowired
    private TeamPlayersRepository teamPlayersRepository;
    
    // Activate player in a specific team (Admin only)
    public TeamPlayers activatePlayerInTeam(Integer playerId, Integer teamId, Integer requestingUserId) {
        // Check if requesting user is admin of the team
        if (!teamPlayersRepository.isPlayerAdminOfTeam(requestingUserId, teamId)) {
            throw new RuntimeException("Access denied: Only team admins can activate players");
        }
        
        Optional<TeamPlayers> teamPlayerOpt = teamPlayersRepository.findByPlayerIdAndTeamId(playerId, teamId);
        if (teamPlayerOpt.isEmpty()) {
            throw new RuntimeException("Player not found in team with playerId: " + playerId + " and teamId: " + teamId);
        }
        
        TeamPlayers teamPlayer = teamPlayerOpt.get();
        teamPlayer.setIsActive(true);
        return teamPlayersRepository.save(teamPlayer);
    }
    
    // Deactivate player in a specific team (Admin only)
    public TeamPlayers deactivatePlayerInTeam(Integer playerId, Integer teamId, Integer requestingUserId) {
        // Check if requesting user is admin of the team
        if (!teamPlayersRepository.isPlayerAdminOfTeam(requestingUserId, teamId)) {
            throw new RuntimeException("Access denied: Only team admins can deactivate players");
        }
        
        // Prevent admin from deactivating themselves
        if (playerId.equals(requestingUserId)) {
            throw new RuntimeException("Admins cannot deactivate themselves");
        }
        
        Optional<TeamPlayers> teamPlayerOpt = teamPlayersRepository.findByPlayerIdAndTeamId(playerId, teamId);
        if (teamPlayerOpt.isEmpty()) {
            throw new RuntimeException("Player not found in team with playerId: " + playerId + " and teamId: " + teamId);
        }
        
        TeamPlayers teamPlayer = teamPlayerOpt.get();
        teamPlayer.setIsActive(false);
        return teamPlayersRepository.save(teamPlayer);
    }
    
    // Get player status in a specific team (Everyone can access)
    public TeamPlayers getPlayerStatusInTeam(Integer playerId, Integer teamId) {
        return teamPlayersRepository.findByPlayerIdAndTeamId(playerId, teamId)
                .orElseThrow(() -> new RuntimeException("Player not found in team with playerId: " + playerId + " and teamId: " + teamId));
    }
    
    public List<TeamPlayers> getPlayerAllTeamMemberships(Integer playerId) {
        List<TeamPlayers> memberships = teamPlayersRepository.findByPlayerId(playerId);
        if (memberships.isEmpty()) {
            throw new RuntimeException("No team memberships found for playerId: " + playerId);
        }
        return memberships;
    }
    
    // Check if user is admin (utility method)
    public boolean isPlayerAdmin(Integer playerId, Integer teamId) {
        return teamPlayersRepository.isPlayerAdminOfTeam(playerId, teamId);
    }
    
    // Check if user is admin of any team (utility method)
    public boolean isPlayerAdminOfAnyTeam(Integer playerId) {
        return teamPlayersRepository.isPlayerAdminOfAnyTeam(playerId);
    }
    

    // Legacy methods for backward compatibility
    public List<TeamPlayers> getActivePlayersForTeam(Integer teamId) {
        return teamPlayersRepository.findByTeamId(teamId).stream()
                .filter(TeamPlayers::getIsActive)
                .toList();
    }
    
    public List<TeamPlayers> getAllPlayersForTeam(Integer teamId) {
        return teamPlayersRepository.findByTeamId(teamId);
    }
}