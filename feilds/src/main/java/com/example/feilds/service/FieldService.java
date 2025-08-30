package com.example.feilds.service;

import com.example.feilds.model.Fields;
import com.example.feilds.model.FieldSlots;
import com.example.feilds.model.Users;
import com.example.feilds.repository.FieldRepository;
import com.example.feilds.repository.FieldSlotRepository;
import com.example.feilds.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class FieldService {

    private final FieldRepository fieldRepository;
    private final FieldSlotRepository fieldSlotRepository;
    private final UserRepository userRepository;
    
    public FieldService(FieldRepository fieldRepository, FieldSlotRepository fieldSlotRepository, UserRepository userRepository) {
        this.fieldRepository = fieldRepository;
        this.fieldSlotRepository = fieldSlotRepository;
        this.userRepository = userRepository;
    }

    /**
     * Browse fields with filters
     */
    public Page<Fields> browseFieldsWithFilters(LocalDate date, String timeSlot, Integer capacity, String location, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        
        // For now, return all active fields - can be enhanced with custom query later
        return fieldRepository.findByIsActiveTrue(pageable);
    }

    /**
     * Get field details with available times
     */
    public Map<String, Object> getFieldWithAvailableTimes(Integer fieldId) {
        if (fieldId == null) {
            throw new IllegalArgumentException("Field ID is required");
        }

        Optional<Fields> fieldOpt = fieldRepository.findById(fieldId);
        if (fieldOpt.isEmpty()) {
            throw new IllegalArgumentException("Field not found");
        }

        Fields field = fieldOpt.get();
        if (!field.getIsActive()) {
            throw new IllegalArgumentException("Field is not active");
        }

        // Get available time slots for this field
        List<FieldSlots> timeSlots = fieldSlotRepository.findByField(field);

        Map<String, Object> result = new HashMap<>();
        result.put("field", field);
        result.put("availableTimeSlots", timeSlots);
        
        return result;
    }

    /**
     * Get all fields (admin only)
     */
    public List<Fields> getAllFields(Integer adminId) {
        validateAdminAccess(adminId);
        return fieldRepository.findAll();
    }

    /**
     * Create a new field (admin only)
     */
    public Fields createField(Integer adminId, String name, String images, Integer playersCapacity, String locationAddress) {
        validateAdminAccess(adminId);
        
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Field name cannot be empty");
        }
        
        if (playersCapacity == null || playersCapacity <= 0) {
            throw new IllegalArgumentException("Players capacity must be greater than 0");
        }
        
        if (locationAddress == null || locationAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("Location address cannot be empty");
        }

        Fields field = Fields.builder()
                .name(name.trim())
                .images(images != null ? images.trim() : "")
                .playersCapacity(playersCapacity)
                .locationAddress(locationAddress.trim())
                .isActive(true)
                .build();

        return fieldRepository.save(field);
    }

    /**
     * Update a field (admin only)
     */
    public Fields updateField(Integer adminId, Integer fieldId, String name, String images, Integer playersCapacity, String locationAddress, Boolean isActive) {
        validateAdminAccess(adminId);
        
        if (fieldId == null) {
            throw new IllegalArgumentException("Field ID is required");
        }

        Optional<Fields> fieldOpt = fieldRepository.findById(fieldId);
        if (fieldOpt.isEmpty()) {
            throw new IllegalArgumentException("Field not found");
        }

        Fields field = fieldOpt.get();
        
        if (name != null && !name.trim().isEmpty()) {
            field.setName(name.trim());
        }
        
        if (images != null) {
            field.setImages(images.trim());
        }
        
        if (playersCapacity != null && playersCapacity > 0) {
            field.setPlayersCapacity(playersCapacity);
        }
        
        if (locationAddress != null && !locationAddress.trim().isEmpty()) {
            field.setLocationAddress(locationAddress.trim());
        }
        
        if (isActive != null) {
            field.setIsActive(isActive);
        }

        return fieldRepository.save(field);
    }

    /**
     * Delete a field (admin only)
     */
    public void deleteField(Integer adminId, Integer fieldId) {
        validateAdminAccess(adminId);
        
        if (fieldId == null) {
            throw new IllegalArgumentException("Field ID is required");
        }

        Optional<Fields> fieldOpt = fieldRepository.findById(fieldId);
        if (fieldOpt.isEmpty()) {
            throw new IllegalArgumentException("Field not found");
        }

        // In a real system, you might want to check if there are active bookings
        // For now, just delete the field
        fieldRepository.deleteById(fieldId);
    }

    /**
     * Toggle field status (admin only)
     */
    public Fields toggleFieldStatus(Integer adminId, Integer fieldId, Boolean isActive) {
        validateAdminAccess(adminId);
        
        if (fieldId == null) {
            throw new IllegalArgumentException("Field ID is required");
        }
        
        if (isActive == null) {
            throw new IllegalArgumentException("Status is required");
        }

        Optional<Fields> fieldOpt = fieldRepository.findById(fieldId);
        if (fieldOpt.isEmpty()) {
            throw new IllegalArgumentException("Field not found");
        }

        Fields field = fieldOpt.get();
        field.setIsActive(isActive);

        return fieldRepository.save(field);
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