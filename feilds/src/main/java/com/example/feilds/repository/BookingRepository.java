package com.example.feilds.repository;

import com.example.feilds.model.Bookings;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Bookings, Integer> {

    @Query("SELECT b FROM Bookings b WHERE b.fieldSlot.field.id = :fieldId AND b.date = :date")
    List<Bookings> findByFieldAndDate(@Param("fieldId") Integer fieldId, @Param("date") LocalDate date);
}
