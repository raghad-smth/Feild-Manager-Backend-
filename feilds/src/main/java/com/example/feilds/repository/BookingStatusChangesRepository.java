package com.example.feilds.repository;

import com.example.feilds.model.BookingStatusChanges;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingStatusChangesRepository extends JpaRepository<BookingStatusChanges, Integer> {
    List<BookingStatusChanges> findByBooking_IdOrderByCreatedAtAsc(Integer bookingId);
}
