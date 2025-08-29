package com.example.feilds.repository;

import com.example.feilds.model.TeamPlayers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamPlayersRepository extends JpaRepository<TeamPlayers, Integer> {
    
    List<TeamPlayers> findByIsActive(Boolean isActive);
    
    List<TeamPlayers> findByTeamId(Integer teamId);
    
    Optional<TeamPlayers> findByPlayerIdAndTeamId(Integer playerId, Integer teamId);
    
    // Find all team memberships for a specific player
    List<TeamPlayers> findByPlayerId(Integer playerId);
    
    // Find specific player in specific team
    Optional<TeamPlayers> findByPlayerIdAndTeamIdAndIsActive(Integer playerId, Integer teamId, Boolean isActive);
    
    // Update player status by player ID and team ID
    @Modifying
    @Query("UPDATE TeamPlayers tp SET tp.isActive = :status WHERE tp.player.id = :playerId AND tp.team.id = :teamId")
    int updatePlayerStatusByPlayerAndTeam(@Param("playerId") Integer playerId, @Param("teamId") Integer teamId, @Param("status") Boolean status);
    
    // Update all team memberships for a player (if needed)
    @Modifying  
    @Query("UPDATE TeamPlayers tp SET tp.isActive = :status WHERE tp.player.id = :playerId")
    int updateAllPlayerMemberships(@Param("playerId") Integer playerId, @Param("status") Boolean status);
    
    // Check if user is admin of any team
    @Query("SELECT CASE WHEN COUNT(tp) > 0 THEN true ELSE false END FROM TeamPlayers tp WHERE tp.player.id = :playerId AND tp.isAdmin = true")
    boolean isPlayerAdminOfAnyTeam(@Param("playerId") Integer playerId);
    
    // Check if user is admin of specific team
    @Query("SELECT CASE WHEN COUNT(tp) > 0 THEN true ELSE false END FROM TeamPlayers tp WHERE tp.player.id = :playerId AND tp.team.id = :teamId AND tp.isAdmin = true")
    boolean isPlayerAdminOfTeam(@Param("playerId") Integer playerId, @Param("teamId") Integer teamId);
}