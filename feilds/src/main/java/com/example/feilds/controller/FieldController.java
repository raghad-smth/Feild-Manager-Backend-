package com.example.feilds.controller;

import com.example.feilds.model.Fields;
import com.example.feilds.service.FieldService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/fields")
@CrossOrigin(origins = "*")
public class FieldController {

    private final FieldService fieldService;
    
    public FieldController(FieldService fieldService) {
        this.fieldService = fieldService;
    }

    /**
     * Browse fields with filtration (available times)
     * GET /api/fields
     */
    @GetMapping
    public ResponseEntity<?> browseFields(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) String timeSlot,
            @RequestParam(required = false) Integer capacity,
            @RequestParam(required = false) String location,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<Fields> fields = fieldService.browseFieldsWithFilters(date, timeSlot, capacity, location, page, size);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Fields retrieved successfully");
            response.put("data", fields);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve fields: " + e.getMessage()));
        }
    }

    /**
     * Get field details with available times
     * GET /api/fields/{fieldId}
     */
    @GetMapping("/{fieldId}")
    public ResponseEntity<?> getFieldDetails(@PathVariable Integer fieldId) {
        try {
            Map<String, Object> fieldDetails = fieldService.getFieldWithAvailableTimes(fieldId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Field details retrieved successfully");
            response.put("data", fieldDetails);
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve field details: " + e.getMessage()));
        }
    }

    /**
     * Get all fields (admin only)
     * GET /api/fields/admin/all
     */
    @GetMapping("/admin/all")
    public ResponseEntity<?> getAllFields(@RequestHeader("X-Admin-ID") Integer adminId) {
        try {
            List<Fields> fields = fieldService.getAllFields(adminId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "All fields retrieved successfully");
            response.put("data", fields);
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve fields: " + e.getMessage()));
        }
    }

    /**
     * Create a new field (admin only)
     * POST /api/fields
     */
    @PostMapping
    public ResponseEntity<?> createField(@RequestBody Map<String, Object> request,
                                       @RequestHeader("X-Admin-ID") Integer adminId) {
        try {
            String name = (String) request.get("name");
            String images = (String) request.get("images");
            Integer playersCapacity = (Integer) request.get("playersCapacity");
            String locationAddress = (String) request.get("locationAddress");

            if (name == null || name.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Field name is required"));
            }

            if (playersCapacity == null || playersCapacity <= 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Valid players capacity is required"));
            }

            if (locationAddress == null || locationAddress.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Location address is required"));
            }

            Fields field = fieldService.createField(adminId, name, images, playersCapacity, locationAddress);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Field created successfully");
            response.put("data", field);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to create field: " + e.getMessage()));
        }
    }

    /**
     * Update a field (admin only)
     * PUT /api/fields/{fieldId}
     */
    @PutMapping("/{fieldId}")
    public ResponseEntity<?> updateField(@PathVariable Integer fieldId,
                                       @RequestBody Map<String, Object> request,
                                       @RequestHeader("X-Admin-ID") Integer adminId) {
        try {
            String name = (String) request.get("name");
            String images = (String) request.get("images");
            Integer playersCapacity = (Integer) request.get("playersCapacity");
            String locationAddress = (String) request.get("locationAddress");
            Boolean isActive = (Boolean) request.get("isActive");

            Fields field = fieldService.updateField(adminId, fieldId, name, images, playersCapacity, locationAddress, isActive);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Field updated successfully");
            response.put("data", field);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update field: " + e.getMessage()));
        }
    }

    /**
     * Delete a field (admin only)
     * DELETE /api/fields/{fieldId}
     */
    @DeleteMapping("/{fieldId}")
    public ResponseEntity<?> deleteField(@PathVariable Integer fieldId,
                                       @RequestHeader("X-Admin-ID") Integer adminId) {
        try {
            fieldService.deleteField(adminId, fieldId);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Field deleted successfully");

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete field: " + e.getMessage()));
        }
    }

    /**
     * Activate/Deactivate a field (admin only)
     * PUT /api/fields/{fieldId}/status
     */
    @PutMapping("/{fieldId}/status")
    public ResponseEntity<?> toggleFieldStatus(@PathVariable Integer fieldId,
                                              @RequestBody Map<String, Boolean> request,
                                              @RequestHeader("X-Admin-ID") Integer adminId) {
        try {
            Boolean isActive = request.get("isActive");
            if (isActive == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Status (isActive) is required"));
            }

            Fields field = fieldService.toggleFieldStatus(adminId, fieldId, isActive);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Field status updated successfully");
            response.put("data", field);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update field status: " + e.getMessage()));
        }
    }
} 