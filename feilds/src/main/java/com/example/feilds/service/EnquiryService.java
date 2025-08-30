package com.example.feilds.service;

import com.example.feilds.model.Enquiries;
import com.example.feilds.model.Users;
import com.example.feilds.repository.EnquiryRepository;
import com.example.feilds.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EnquiryService {

    private final EnquiryRepository enquiryRepository;
    private final UserRepository userRepository;
    
    public EnquiryService(EnquiryRepository enquiryRepository, UserRepository userRepository) {
        this.enquiryRepository = enquiryRepository;
        this.userRepository = userRepository;
    }

    /**
     * Submit a new enquiry
     */
    public Enquiries submitEnquiry(Integer customerId, String content) {
        if (customerId == null) {
            throw new IllegalArgumentException("Customer ID is required");
        }
        
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Enquiry content cannot be empty");
        }

        Optional<Users> customerOpt = userRepository.findById(customerId);
        if (customerOpt.isEmpty()) {
            throw new IllegalArgumentException("Customer not found");
        }

        Users customer = customerOpt.get();
        if (customer.getRole() != Users.Role.player) {
            throw new IllegalArgumentException("Only players can submit enquiries");
        }

        Enquiries enquiry = Enquiries.builder()
                .content(content.trim())
                .customer(customer)
                .status(Enquiries.Status.open)
                .adminResponse(null)
                .isHidden(false)
                .createdAt(LocalDateTime.now())
                .build();

        return enquiryRepository.save(enquiry);
    }

    /**
     * Get all enquiries for a specific customer
     */
    public List<Enquiries> getCustomerEnquiries(Integer customerId) {
        if (customerId == null) {
            throw new IllegalArgumentException("Customer ID is required");
        }

        Optional<Users> customerOpt = userRepository.findById(customerId);
        if (customerOpt.isEmpty()) {
            throw new IllegalArgumentException("Customer not found");
        }

        return enquiryRepository.findByCustomer(customerOpt.get());
    }

    /**
     * Get all visible enquiries (admin only)
     */
    public List<Enquiries> getVisibleEnquiries(Integer adminId) {
        validateAdminAccess(adminId);
        return enquiryRepository.findByIsHiddenFalse();
    }

    /**
     * Get all enquiries including hidden ones (admin only)
     */
    public List<Enquiries> getAllEnquiries(Integer adminId) {
        validateAdminAccess(adminId);
        return enquiryRepository.findAll();
    }

    /**
     * Toggle enquiry visibility (admin only)
     */
    public Enquiries toggleVisibility(Integer adminId, Integer enquiryId, Boolean isHidden) {
        validateAdminAccess(adminId);
        
        if (enquiryId == null) {
            throw new IllegalArgumentException("Enquiry ID is required");
        }
        
        if (isHidden == null) {
            throw new IllegalArgumentException("Visibility status is required");
        }

        Optional<Enquiries> enquiryOpt = enquiryRepository.findById(enquiryId);
        if (enquiryOpt.isEmpty()) {
            throw new IllegalArgumentException("Enquiry not found");
        }

        Enquiries enquiry = enquiryOpt.get();
        enquiry.setIsHidden(isHidden);

        return enquiryRepository.save(enquiry);
    }

    /**
     * Update enquiry status (admin only)
     */
    public Enquiries updateStatus(Integer adminId, Integer enquiryId, String status) {
        validateAdminAccess(adminId);
        
        if (enquiryId == null) {
            throw new IllegalArgumentException("Enquiry ID is required");
        }
        
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status is required");
        }

        // Validate status enum
        Enquiries.Status enquiryStatus;
        try {
            enquiryStatus = Enquiries.Status.valueOf(status.toLowerCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status. Valid values are: open, in_progress, closed");
        }

        Optional<Enquiries> enquiryOpt = enquiryRepository.findById(enquiryId);
        if (enquiryOpt.isEmpty()) {
            throw new IllegalArgumentException("Enquiry not found");
        }

        Enquiries enquiry = enquiryOpt.get();
        enquiry.setStatus(enquiryStatus);

        return enquiryRepository.save(enquiry);
    }

    /**
     * Add admin response to enquiry (admin only)
     */
    public Enquiries addAdminResponse(Integer adminId, Integer enquiryId, String response) {
        validateAdminAccess(adminId);
        
        if (enquiryId == null) {
            throw new IllegalArgumentException("Enquiry ID is required");
        }
        
        if (response == null || response.trim().isEmpty()) {
            throw new IllegalArgumentException("Admin response cannot be empty");
        }

        Optional<Enquiries> enquiryOpt = enquiryRepository.findById(enquiryId);
        if (enquiryOpt.isEmpty()) {
            throw new IllegalArgumentException("Enquiry not found");
        }

        Enquiries enquiry = enquiryOpt.get();
        enquiry.setAdminResponse(response.trim());
        
        // Automatically update status to in_progress when admin responds
        if (enquiry.getStatus() == Enquiries.Status.open) {
            enquiry.setStatus(Enquiries.Status.in_progress);
        }

        return enquiryRepository.save(enquiry);
    }

    /**
     * Get enquiries by status (admin only)
     */
    public List<Enquiries> getEnquiriesByStatus(Integer adminId, String status) {
        validateAdminAccess(adminId);
        
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status is required");
        }

        // Validate status enum
        Enquiries.Status enquiryStatus;
        try {
            enquiryStatus = Enquiries.Status.valueOf(status.toLowerCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status. Valid values are: open, in_progress, closed");
        }

        return enquiryRepository.findByStatus(enquiryStatus);
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