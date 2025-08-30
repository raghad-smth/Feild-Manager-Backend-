package com.example.feilds.repository;

import com.example.feilds.model.Fields;
import com.example.feilds.model.FieldSlots;
import com.example.feilds.model.WeekDays;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface FieldSlotRepository extends JpaRepository<FieldSlots, Integer> {
    
    /**
     * Find all time slots for a specific field
     */
    List<FieldSlots> findByField(Fields field);
    
    /**
     * Find time slots by field and week day
     */
    List<FieldSlots> findByFieldAndWeekDay(Fields field, WeekDays weekDay);
    
    /**
     * Find time slots by field, week day, and time range
     */
    List<FieldSlots> findByFieldAndWeekDayAndFromTimeGreaterThanEqualAndToTimeLessThanEqual(
            Fields field, WeekDays weekDay, LocalTime fromTime, LocalTime toTime);
} 