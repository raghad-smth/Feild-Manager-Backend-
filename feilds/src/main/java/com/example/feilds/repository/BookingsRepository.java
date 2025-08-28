package com.example.feilds.repository;

import com.example.feilds.model.Bookings;
import com.example.feilds.model.Teams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingsRepository extends JpaRepository<Bookings, Integer> {
    List<Bookings> findByTeamOrderByDateDesc(Teams team);
}
