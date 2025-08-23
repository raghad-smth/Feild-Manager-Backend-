package com.example.feilds.service;

import com.example.feilds.model.Teams;
import com.example.feilds.model.TeamPlayers;
import com.example.feilds.model.Users;
import com.example.feilds.repository.TeamRepository;
import com.example.feilds.repository.TeamPlayersRepository;
import com.example.feilds.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamPlayersRepository teamPlayersRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Create a new team with the player as admin
     */
    @Transactional
    public Teams createTeam(String teamName, Integer playerId) {
        // Validate player exists
        Optional<Users> playerOpt = userRepository.findById(playerId);
        if (!playerOpt.isPresent()) {
            throw new IllegalArgumentException("Player not found");
        }

        Users player = playerOpt.get();
        if (!"player".equals(player.getRole())) {
            throw new IllegalArgumentException("Only players can create teams");
        }

        // Check if team name already exists
        if (teamRepository.existsByName(teamName)) {
            throw new IllegalArgumentException("Team name already exists");
        }

        // Create the team
        Teams team = Teams.builder()
                .name(teamName)
                .isActive(true)
                .build();
        Teams savedTeam = teamRepository.save(team);

        // Add the creator as team admin
        TeamPlayers teamPlayer = TeamPlayers.builder()
                .player(player)
                .team(savedTeam)
                .isAdmin(true)
                .isActive(true)
                .build();
        teamPlayersRepository.save(teamPlayer);

        return savedTeam;
    }

    /**
     * Get all teams that a player is part of
     */
    public List<Teams> getPlayerTeams(Integer playerId) {
        // Validate player exists
        if (!userRepository.existsById(playerId)) {
            throw new IllegalArgumentException("Player not found");
        }

        List<TeamPlayers> playerTeams = teamPlayersRepository.findByPlayerId(playerId);

        // Filter only active memberships and get team IDs
        List<Integer> teamIds = playerTeams.stream()
                .filter(tp -> tp.getIsActive())
                .map(tp -> tp.getTeam().getId())
                .collect(Collectors.toList());

        if (teamIds.isEmpty()) {
            return List.of(); // Return empty list if no teams
        }

        return teamRepository.findByIdInAndIsActiveTrue(teamIds);
    }

    /**
     * Get all active teams for browsing
     */
    public List<Teams> getAllActiveTeams() {
        return teamRepository.findByIsActiveTrue();
    }

    /**
     * Join a team
     */
    @Transactional
    public void joinTeam(Integer teamId, Integer playerId) {
        // Validate team exists and is active
        Optional<Teams> teamOpt = teamRepository.findById(Long.valueOf(teamId));
        if (!teamOpt.isPresent()) {
            throw new IllegalArgumentException("Team not found");
        }

        Teams team = teamOpt.get();
        if (!team.getIsActive()) {
            throw new IllegalArgumentException("Team is not active");
        }

        // Validate player exists
        Optional<Users> playerOpt = userRepository.findById(playerId);
        if (!playerOpt.isPresent()) {
            throw new IllegalArgumentException("Player not found");
        }

        Users player = playerOpt.get();
        if (!"player".equals(player.getRole())) {
            throw new IllegalArgumentException("Only players can join teams");
        }

        // Check if player is already in the team (active membership)
        Optional<TeamPlayers> existingMembership = teamPlayersRepository
                .findByPlayerIdAndTeamIdAndIsActive(playerId, teamId, true);
        if (existingMembership.isPresent()) {
            throw new IllegalArgumentException("Player is already a member of this team");
        }

        // Add player to team
        TeamPlayers teamPlayer = TeamPlayers.builder()
                .player(player)
                .team(team)
                .isAdmin(false)
                .isActive(true)
                .build();
        teamPlayersRepository.save(teamPlayer);
    }

    /**
     * Leave a team
     */
    @Transactional
    public void leaveTeam(Integer teamId, Integer playerId) {
        // Check if player is in the team
        Optional<TeamPlayers> teamPlayerOpt = teamPlayersRepository
                .findByPlayerIdAndTeamIdAndIsActive(playerId, teamId, true);

        if (!teamPlayerOpt.isPresent()) {
            throw new IllegalArgumentException("Player is not a member of this team");
        }

        TeamPlayers teamPlayer = teamPlayerOpt.get();

        // Check if this is the last admin
        if (teamPlayer.getIsAdmin()) {
            // Count active team members
            List<TeamPlayers> activeMembers = teamPlayersRepository.findByTeamId(teamId).stream()
                    .filter(tp -> tp.getIsActive())
                    .collect(Collectors.toList());

            long adminCount = activeMembers.stream()
                    .filter(tp -> tp.getIsAdmin())
                    .count();

            if (adminCount <= 1) {
                if (activeMembers.size() > 1) {
                    throw new IllegalArgumentException(
                            "Cannot leave team as you are the only admin. " +
                                    "Please promote another member to admin first or transfer team ownership."
                    );
                } else {
                    // Last member leaving, deactivate the team
                    Optional<Teams> teamOpt = teamRepository.findById(Long.valueOf(teamId));
                    if (teamOpt.isPresent()) {
                        Teams team = teamOpt.get();
                        team.setIsActive(false);
                        teamRepository.save(team);
                    }
                }
            }
        }

        // Deactivate player from team (soft delete)
        teamPlayer.setIsActive(false);
        teamPlayersRepository.save(teamPlayer);
    }

    /**
     * Get team details with members
     */
    public Map<String, Object> getTeamWithMembers(Integer teamId) {
        // Get team
        Optional<Teams> teamOpt = teamRepository.findById(Long.valueOf(teamId));
        if (!teamOpt.isPresent()) {
            throw new IllegalArgumentException("Team not found");
        }

        Teams team = teamOpt.get();
        Map<String, Object> result = new HashMap<>();
        result.put("team", team);

        // Get active team members
        List<TeamPlayers> teamMembers = teamPlayersRepository.findByTeamId(teamId).stream()
                .filter(tp -> tp.getIsActive())
                .collect(Collectors.toList());

        List<Map<String, Object>> members = teamMembers.stream()
                .map(tp -> {
                    Map<String, Object> memberInfo = new HashMap<>();
                    memberInfo.put("playerId", tp.getPlayer().getId());
                    memberInfo.put("playerName", tp.getPlayer().getName());
                    memberInfo.put("playerEmail", tp.getPlayer().getEmail());
                    memberInfo.put("playerPhone", tp.getPlayer().getPhone());
                    memberInfo.put("isAdmin", tp.getIsAdmin());
                    return memberInfo;
                })
                .collect(Collectors.toList());

        result.put("members", members);
        return result;
    }

    /**
     * Check if player is admin of a team
     */
    public boolean isPlayerTeamAdmin(Integer playerId, Integer teamId) {
        return teamPlayersRepository.isPlayerAdminOfTeam(playerId, teamId);
    }
}