package com.example.feilds.service;

import com.example.feilds.model.Settings;
import com.example.feilds.model.Users;
import com.example.feilds.repository.SettingsRepository;
import com.example.feilds.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class SettingsService {

    private final SettingsRepository settingsRepository;
    private final UserRepository userRepository;
    
    public SettingsService(SettingsRepository settingsRepository, UserRepository userRepository) {
        this.settingsRepository = settingsRepository;
        this.userRepository = userRepository;
    }

    /**
     * Get current platform settings
     */
    public Settings getCurrentSettings() {
        // Return the first (and should be only) settings record
        return settingsRepository.findAll()
                .stream()
                .findFirst()
                .orElse(createDefaultSettings());
    }

    /**
     * Get basic settings (name and logo)
     */
    public Map<String, Object> getBasicSettings() {
        Settings settings = getCurrentSettings();
        
        Map<String, Object> basicSettings = new HashMap<>();
        basicSettings.put("name", settings.getName());
        basicSettings.put("logoUrl", settings.getLogoUrl());
        
        return basicSettings;
    }

    /**
     * Get about settings
     */
    public Map<String, Object> getAboutSettings() {
        Settings settings = getCurrentSettings();
        
        Map<String, Object> aboutSettings = new HashMap<>();
        aboutSettings.put("aboutImageUrl", settings.getAboutImageUrl());
        aboutSettings.put("aboutDescription", settings.getAboutDescription());
        
        return aboutSettings;
    }

    /**
     * Get terms and conditions
     */
    public Map<String, Object> getTermsAndConditions() {
        Settings settings = getCurrentSettings();
        
        Map<String, Object> terms = new HashMap<>();
        terms.put("termsAndConditions", settings.getTermsAndConditions());
        
        return terms;
    }

    /**
     * Get contact information
     */
    public Map<String, Object> getContactInfo() {
        Settings settings = getCurrentSettings();
        
        Map<String, Object> contactInfo = new HashMap<>();
        contactInfo.put("facebookUrl", settings.getFacebookUrl());
        contactInfo.put("whatsappNumber", settings.getWhatsappNumber());
        contactInfo.put("phoneNumber", settings.getPhoneNumber());
        contactInfo.put("secondPhoneNumber", settings.getSecondPhoneNumber());
        
        return contactInfo;
    }

    /**
     * Update platform settings (admin only)
     */
    public Settings updateSettings(Integer adminId, String name, String logoUrl, String aboutImageUrl,
                                 String aboutDescription, String termsAndConditions, String facebookUrl,
                                 String whatsappNumber, String phoneNumber, String secondPhoneNumber) {
        validateAdminAccess(adminId);
        
        Settings settings = getCurrentSettings();
        
        if (name != null && !name.trim().isEmpty()) {
            settings.setName(name.trim());
        }
        
        if (logoUrl != null) {
            settings.setLogoUrl(logoUrl.trim());
        }
        
        if (aboutImageUrl != null) {
            settings.setAboutImageUrl(aboutImageUrl.trim());
        }
        
        if (aboutDescription != null) {
            settings.setAboutDescription(aboutDescription.trim());
        }
        
        if (termsAndConditions != null) {
            settings.setTermsAndConditions(termsAndConditions.trim());
        }
        
        if (facebookUrl != null) {
            settings.setFacebookUrl(facebookUrl.trim());
        }
        
        if (whatsappNumber != null) {
            settings.setWhatsappNumber(whatsappNumber.trim());
        }
        
        if (phoneNumber != null) {
            settings.setPhoneNumber(phoneNumber.trim());
        }
        
        if (secondPhoneNumber != null) {
            settings.setSecondPhoneNumber(secondPhoneNumber.trim());
        }

        return settingsRepository.save(settings);
    }

    /**
     * Preview settings changes before saving
     */
    public Map<String, Object> previewSettings(Integer adminId, Map<String, Object> newSettings) {
        validateAdminAccess(adminId);
        
        Settings currentSettings = getCurrentSettings();
        
        Map<String, Object> preview = new HashMap<>();
        preview.put("current", currentSettings);
        
        // Create preview object with proposed changes
        Settings previewSettings = new Settings(
                currentSettings.getId(),
                currentSettings.getAdmin(),
                newSettings.getOrDefault("name", currentSettings.getName()).toString(),
                getStringOrDefault(newSettings, "logoUrl", currentSettings.getLogoUrl()),
                getStringOrDefault(newSettings, "aboutImageUrl", currentSettings.getAboutImageUrl()),
                getStringOrDefault(newSettings, "aboutDescription", currentSettings.getAboutDescription()),
                getStringOrDefault(newSettings, "termsAndConditions", currentSettings.getTermsAndConditions()),
                getStringOrDefault(newSettings, "facebookUrl", currentSettings.getFacebookUrl()),
                getStringOrDefault(newSettings, "whatsappNumber", currentSettings.getWhatsappNumber()),
                getStringOrDefault(newSettings, "phoneNumber", currentSettings.getPhoneNumber()),
                getStringOrDefault(newSettings, "secondPhoneNumber", currentSettings.getSecondPhoneNumber())
        );
                
        preview.put("preview", previewSettings);
        
        return preview;
    }

    /**
     * Update logo only
     */
    public Settings updateLogo(Integer adminId, String logoUrl) {
        validateAdminAccess(adminId);
        
        Settings settings = getCurrentSettings();
        settings.setLogoUrl(logoUrl);
        
        return settingsRepository.save(settings);
    }

    /**
     * Create default settings if none exist
     */
    private Settings createDefaultSettings() {
        Users defaultAdmin = userRepository.findByRole(Users.Role.admin)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No admin user found to create default settings"));

        Settings defaultSettings = new Settings(
                null, // id will be generated by database
                defaultAdmin,
                "Fields Manager",
                "",
                "",
                "Welcome to Fields Manager - your sports field booking platform",
                "Terms and conditions will be updated soon.",
                "",
                "",
                "",
                ""
        );

        return settingsRepository.save(defaultSettings);
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

    /**
     * Helper method to get string value or default
     */
    private String getStringOrDefault(Map<String, Object> map, String key, String defaultValue) {
        Object value = map.get(key);
        return value != null ? value.toString() : defaultValue;
    }
} 