package com.example.feilds.controller;

import com.example.feilds.model.Settings;
import com.example.feilds.service.SettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/settings")
@CrossOrigin(origins = "*")
public class SettingsController {

    private final SettingsService settingsService;
    
    public SettingsController(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    /**
     * Get current platform settings (public)
     * GET /api/settings
     */
    @GetMapping
    public ResponseEntity<?> getPlatformSettings() {
        try {
            Settings settings = settingsService.getCurrentSettings();
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Platform settings retrieved successfully");
            response.put("data", settings);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve settings: " + e.getMessage()));
        }
    }

    /**
     * Get platform name and logo (public)
     * GET /api/settings/basic
     */
    @GetMapping("/basic")
    public ResponseEntity<?> getBasicSettings() {
        try {
            Map<String, Object> basicSettings = settingsService.getBasicSettings();
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Basic settings retrieved successfully");
            response.put("data", basicSettings);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve basic settings: " + e.getMessage()));
        }
    }

    /**
     * Get about section (public)
     * GET /api/settings/about
     */
    @GetMapping("/about")
    public ResponseEntity<?> getAboutSettings() {
        try {
            Map<String, Object> aboutSettings = settingsService.getAboutSettings();
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "About settings retrieved successfully");
            response.put("data", aboutSettings);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve about settings: " + e.getMessage()));
        }
    }

    /**
     * Get terms and conditions (public)
     * GET /api/settings/terms
     */
    @GetMapping("/terms")
    public ResponseEntity<?> getTermsAndConditions() {
        try {
            Map<String, Object> terms = settingsService.getTermsAndConditions();
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Terms and conditions retrieved successfully");
            response.put("data", terms);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve terms and conditions: " + e.getMessage()));
        }
    }

    /**
     * Get contact/social info (public)
     * GET /api/settings/contact
     */
    @GetMapping("/contact")
    public ResponseEntity<?> getContactInfo() {
        try {
            Map<String, Object> contactInfo = settingsService.getContactInfo();
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Contact information retrieved successfully");
            response.put("data", contactInfo);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve contact information: " + e.getMessage()));
        }
    }

    /**
     * Update platform settings (admin only)
     * PUT /api/settings
     */
    @PutMapping
    public ResponseEntity<?> updateSettings(@RequestBody Map<String, Object> request,
                                          @RequestHeader("X-Admin-ID") Integer adminId) {
        try {
            String name = (String) request.get("name");
            String logoUrl = (String) request.get("logoUrl");
            String aboutImageUrl = (String) request.get("aboutImageUrl");
            String aboutDescription = (String) request.get("aboutDescription");
            String termsAndConditions = (String) request.get("termsAndConditions");
            String facebookUrl = (String) request.get("facebookUrl");
            String whatsappNumber = (String) request.get("whatsappNumber");
            String phoneNumber = (String) request.get("phoneNumber");
            String secondPhoneNumber = (String) request.get("secondPhoneNumber");

            Settings updatedSettings = settingsService.updateSettings(adminId, name, logoUrl, aboutImageUrl, 
                    aboutDescription, termsAndConditions, facebookUrl, whatsappNumber, phoneNumber, secondPhoneNumber);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Settings updated successfully");
            response.put("data", updatedSettings);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update settings: " + e.getMessage()));
        }
    }

    /**
     * Preview settings changes before saving (admin only)
     * POST /api/settings/preview
     */
    @PostMapping("/preview")
    public ResponseEntity<?> previewSettings(@RequestBody Map<String, Object> request,
                                            @RequestHeader("X-Admin-ID") Integer adminId) {
        try {
            Map<String, Object> previewData = settingsService.previewSettings(adminId, request);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Settings preview generated successfully");
            response.put("data", previewData);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to generate preview: " + e.getMessage()));
        }
    }

    /**
     * Upload/update logo (admin only)
     * POST /api/settings/logo
     */
    @PostMapping("/logo")
    public ResponseEntity<?> uploadLogo(@RequestBody Map<String, String> request,
                                      @RequestHeader("X-Admin-ID") Integer adminId) {
        try {
            String logoUrl = request.get("logoUrl");
            
            if (logoUrl == null || logoUrl.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Logo URL is required"));
            }

            Settings updatedSettings = settingsService.updateLogo(adminId, logoUrl);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Logo updated successfully");
            response.put("data", updatedSettings);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update logo: " + e.getMessage()));
        }
    }
} 