package com.example.feilds.service;

import com.example.feilds.model.Fields;
import com.example.feilds.model.FieldSlots;
import com.example.feilds.model.Users;
import com.example.feilds.model.WeekDays;
import com.example.feilds.repository.FieldRepository;
import com.example.feilds.repository.FieldSlotRepository;
import com.example.feilds.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Service
public class FieldSlotService {

    private final FieldSlotRepository fieldSlotRepository;
    private final FieldRepository fieldRepository;
    private final UserRepository userRepository;
    
    public FieldSlotService(FieldSlotRepository fieldSlotRepository, FieldRepository fieldRepository, UserRepository userRepository) {
        this.fieldSlotRepository = fieldSlotRepository;
        this.fieldRepository = fieldRepository;
        this.userRepository = userRepository;
    }

    /**
     * Get all time slots for a field
     */
    public List<FieldSlots> getFieldSlots(Integer fieldId) {
        if (fieldId == null) {
            throw new IllegalArgumentException("Field ID is required");
        }

        Optional<Fields> fieldOpt = fieldRepository.findById(fieldId);
        if (fieldOpt.isEmpty()) {
            throw new IllegalArgumentException("Field not found");
        }

        return fieldSlotRepository.findByField(fieldOpt.get());
    }

    /**
     * Create a new field slot (admin only)
     */
    public FieldSlots createFieldSlot(Integer adminId, Integer fieldId, Integer weekDayId, 
                                     String fromTime, String toTime, Double price) {
        validateAdminAccess(adminId);
        
        if (fieldId == null) {
            throw new IllegalArgumentException("Field ID is required");
        }
        
        if (weekDayId == null) {
            throw new IllegalArgumentException("Week day ID is required");
        }

        Optional<Fields> fieldOpt = fieldRepository.findById(fieldId);
        if (fieldOpt.isEmpty()) {
            throw new IllegalArgumentException("Field not found");
        }

        LocalTime fromTimeObj = parseTime(fromTime);
        LocalTime toTimeObj = parseTime(toTime);
        
        if (fromTimeObj.isAfter(toTimeObj) || fromTimeObj.equals(toTimeObj)) {
            throw new IllegalArgumentException("From time must be before to time");
        }

        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }

        // For now, create a simple WeekDays object - in a real system you'd fetch from repository
        WeekDays weekDay = WeekDays.builder()
                .id(weekDayId)
                .build();

        FieldSlots slot = FieldSlots.builder()
                .field(fieldOpt.get())
                .weekDay(weekDay)
                .fromTime(fromTimeObj)
                .toTime(toTimeObj)
                .price(BigDecimal.valueOf(price))
                .build();

        return fieldSlotRepository.save(slot);
    }

    /**
     * Update a field slot (admin only)
     */
    public FieldSlots updateFieldSlot(Integer adminId, Integer slotId, String fromTime, String toTime, Double price) {
        validateAdminAccess(adminId);
        
        if (slotId == null) {
            throw new IllegalArgumentException("Slot ID is required");
        }

        Optional<FieldSlots> slotOpt = fieldSlotRepository.findById(slotId);
        if (slotOpt.isEmpty()) {
            throw new IllegalArgumentException("Field slot not found");
        }

        FieldSlots slot = slotOpt.get();
        
        if (fromTime != null && !fromTime.trim().isEmpty()) {
            LocalTime fromTimeObj = parseTime(fromTime);
            slot.setFromTime(fromTimeObj);
        }
        
        if (toTime != null && !toTime.trim().isEmpty()) {
            LocalTime toTimeObj = parseTime(toTime);
            slot.setToTime(toTimeObj);
        }
        
        // Validate time range after updates
        if (slot.getFromTime().isAfter(slot.getToTime()) || slot.getFromTime().equals(slot.getToTime())) {
            throw new IllegalArgumentException("From time must be before to time");
        }
        
        if (price != null && price > 0) {
            slot.setPrice(BigDecimal.valueOf(price));
        }

        return fieldSlotRepository.save(slot);
    }

    /**
     * Delete a field slot (admin only)
     */
    public void deleteFieldSlot(Integer adminId, Integer slotId) {
        validateAdminAccess(adminId);
        
        if (slotId == null) {
            throw new IllegalArgumentException("Slot ID is required");
        }

        Optional<FieldSlots> slotOpt = fieldSlotRepository.findById(slotId);
        if (slotOpt.isEmpty()) {
            throw new IllegalArgumentException("Field slot not found");
        }

        fieldSlotRepository.deleteById(slotId);
    }

    /**
     * Parse time string to LocalTime
     */
    private LocalTime parseTime(String timeStr) {
        if (timeStr == null || timeStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Time cannot be empty");
        }
        
        try {
            // Try different time formats
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            return LocalTime.parse(timeStr.trim(), formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid time format. Use HH:mm format (e.g., 14:30)");
        }
    }

    /**
     * Validate admin access
     */
    private void validateAdminAccess(Integer adminId) {
        if (adminId == null) {
            throw new IllegalArgumentException("Admin ID is required");
        }

        Optional<Users> adminOpt = userRepository.findById(adminId);
        if (adminOpt.isEmpty()) {
            throw new IllegalArgumentException("Admin not found");
        }

        Users admin = adminOpt.get();
        if (admin.getRole() != Users.Role.admin) {
            throw new IllegalArgumentException("Access denied. Admin role required.");
        }
    }
} 