package com.example.feilds.controller;

import com.example.feilds.model.Enquiries;
import com.example.feilds.service.EnquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enquiries")
@CrossOrigin(origins = "*")
public class EnquiryController {

    private final EnquiryService enquiryService;
    
    public EnquiryController(EnquiryService enquiryService) {
        this.enquiryService = enquiryService;
    }

    /**
     * Submit an enquiry (players)
     * POST /api/enquiries
     */
    @PostMapping
    public ResponseEntity<?> submitEnquiry(@RequestBody Map<String, Object> request) {
        try {
            String content = (String) request.get("content");
            Integer customerId = (Integer) request.get("customerId");

            if (content == null || content.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Enquiry content is required"));
            }

            if (customerId == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Customer ID is required"));
            }

            Enquiries enquiry = enquiryService.submitEnquiry(customerId, content.trim());

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Enquiry submitted successfully");
            response.put("data", enquiry);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to submit enquiry: " + e.getMessage()));
        }
    }

    /**
     * Get all enquiries for a customer
     * GET /api/enquiries/customer/{customerId}
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?> getCustomerEnquiries(@PathVariable Integer customerId) {
        try {
            List<Enquiries> enquiries = enquiryService.getCustomerEnquiries(customerId);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Customer enquiries retrieved successfully");
            response.put("data", enquiries);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve enquiries: " + e.getMessage()));
        }
    }

    /**
     * Get all visible enquiries (admin)
     * GET /api/enquiries/admin/visible
     */
    @GetMapping("/admin/visible")
    public ResponseEntity<?> getVisibleEnquiries(@RequestHeader("X-Admin-ID") Integer adminId) {
        try {
            List<Enquiries> enquiries = enquiryService.getVisibleEnquiries(adminId);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Visible enquiries retrieved successfully");
            response.put("data", enquiries);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve enquiries: " + e.getMessage()));
        }
    }

    /**
     * Get all enquiries including hidden ones (admin)
     * GET /api/enquiries/admin/all
     */
    @GetMapping("/admin/all")
    public ResponseEntity<?> getAllEnquiries(@RequestHeader("X-Admin-ID") Integer adminId) {
        try {
            List<Enquiries> enquiries = enquiryService.getAllEnquiries(adminId);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "All enquiries retrieved successfully");
            response.put("data", enquiries);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve enquiries: " + e.getMessage()));
        }
    }

    /**
     * Hide/unhide an enquiry (admin)
     * PUT /api/enquiries/{enquiryId}/visibility
     */
    @PutMapping("/{enquiryId}/visibility")
    public ResponseEntity<?> toggleEnquiryVisibility(@PathVariable Integer enquiryId,
                                                   @RequestBody Map<String, Boolean> request,
                                                   @RequestHeader("X-Admin-ID") Integer adminId) {
        try {
            Boolean isHidden = request.get("isHidden");
            
            if (isHidden == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Visibility status (isHidden) is required"));
            }

            Enquiries enquiry = enquiryService.toggleVisibility(adminId, enquiryId, isHidden);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Enquiry visibility updated successfully");
            response.put("data", enquiry);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update enquiry visibility: " + e.getMessage()));
        }
    }

    /**
     * Update enquiry status (admin)
     * PUT /api/enquiries/{enquiryId}/status
     */
    @PutMapping("/{enquiryId}/status")
    public ResponseEntity<?> updateEnquiryStatus(@PathVariable Integer enquiryId,
                                                @RequestBody Map<String, String> request,
                                                @RequestHeader("X-Admin-ID") Integer adminId) {
        try {
            String status = request.get("status");
            
            if (status == null || status.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Status is required"));
            }

            Enquiries enquiry = enquiryService.updateStatus(adminId, enquiryId, status);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Enquiry status updated successfully");
            response.put("data", enquiry);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update enquiry status: " + e.getMessage()));
        }
    }

    /**
     * Add admin response to enquiry (admin)
     * PUT /api/enquiries/{enquiryId}/response
     */
    @PutMapping("/{enquiryId}/response")
    public ResponseEntity<?> addAdminResponse(@PathVariable Integer enquiryId,
                                            @RequestBody Map<String, String> request,
                                            @RequestHeader("X-Admin-ID") Integer adminId) {
        try {
            String response = request.get("response");
            
            if (response == null || response.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Admin response is required"));
            }

            Enquiries enquiry = enquiryService.addAdminResponse(adminId, enquiryId, response.trim());

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("status", "success");
            responseMap.put("message", "Admin response added successfully");
            responseMap.put("data", enquiry);

            return ResponseEntity.ok(responseMap);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to add admin response: " + e.getMessage()));
        }
    }

    /**
     * Get enquiries by status (admin)
     * GET /api/enquiries/admin/status/{status}
     */
    @GetMapping("/admin/status/{status}")
    public ResponseEntity<?> getEnquiriesByStatus(@PathVariable String status,
                                                @RequestHeader("X-Admin-ID") Integer adminId) {
        try {
            List<Enquiries> enquiries = enquiryService.getEnquiriesByStatus(adminId, status);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Enquiries by status retrieved successfully");
            response.put("data", enquiries);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve enquiries: " + e.getMessage()));
        }
    }
} 