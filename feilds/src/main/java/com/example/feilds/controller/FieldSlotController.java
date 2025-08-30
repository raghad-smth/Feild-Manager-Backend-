package com.example.feilds.controller;

import com.example.feilds.model.FieldSlots;
import com.example.feilds.service.FieldSlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/field-slots")
@CrossOrigin(origins = "*")
public class FieldSlotController {

    private final FieldSlotService fieldSlotService;
    
    public FieldSlotController(FieldSlotService fieldSlotService) {
        this.fieldSlotService = fieldSlotService;
    }

    /**
     * Get all time slots for a field
     * GET /api/field-slots/field/{fieldId}
     */
    @GetMapping("/field/{fieldId}")
    public ResponseEntity<?> getFieldSlots(@PathVariable Integer fieldId) {
        try {
            List<FieldSlots> slots = fieldSlotService.getFieldSlots(fieldId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Field slots retrieved successfully");
            response.put("data", slots);
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve field slots: " + e.getMessage()));
        }
    }

    /**
     * Create a new time slot for a field (admin only)
     * POST /api/field-slots
     */
    @PostMapping
    public ResponseEntity<?> createFieldSlot(@RequestBody Map<String, Object> request,
                                            @RequestHeader("X-Admin-ID") Integer adminId) {
        try {
            Integer fieldId = (Integer) request.get("fieldId");
            Integer weekDayId = (Integer) request.get("weekDayId");
            String fromTime = (String) request.get("fromTime");
            String toTime = (String) request.get("toTime");
            Double price = request.get("price") instanceof Number ? 
                          ((Number) request.get("price")).doubleValue() : null;

            if (fieldId == null || weekDayId == null || fromTime == null || toTime == null || price == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "All fields are required: fieldId, weekDayId, fromTime, toTime, price"));
            }

            FieldSlots slot = fieldSlotService.createFieldSlot(adminId, fieldId, weekDayId, fromTime, toTime, price);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Field slot created successfully");
            response.put("data", slot);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to create field slot: " + e.getMessage()));
        }
    }

    /**
     * Update a field slot (admin only)
     * PUT /api/field-slots/{slotId}
     */
    @PutMapping("/{slotId}")
    public ResponseEntity<?> updateFieldSlot(@PathVariable Integer slotId,
                                           @RequestBody Map<String, Object> request,
                                           @RequestHeader("X-Admin-ID") Integer adminId) {
        try {
            String fromTime = (String) request.get("fromTime");
            String toTime = (String) request.get("toTime");
            Double price = request.get("price") instanceof Number ? 
                          ((Number) request.get("price")).doubleValue() : null;

            FieldSlots slot = fieldSlotService.updateFieldSlot(adminId, slotId, fromTime, toTime, price);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Field slot updated successfully");
            response.put("data", slot);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update field slot: " + e.getMessage()));
        }
    }

    /**
     * Delete a field slot (admin only)
     * DELETE /api/field-slots/{slotId}
     */
    @DeleteMapping("/{slotId}")
    public ResponseEntity<?> deleteFieldSlot(@PathVariable Integer slotId,
                                            @RequestHeader("X-Admin-ID") Integer adminId) {
        try {
            fieldSlotService.deleteFieldSlot(adminId, slotId);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Field slot deleted successfully");

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete field slot: " + e.getMessage()));
        }
    }
} 