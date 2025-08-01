package com.cosmic.astrology.controller;

// ================ IMPORTS ================
import com.cosmic.astrology.dto.*;
import com.cosmic.astrology.entity.User;
import com.cosmic.astrology.service.UserService;
import com.cosmic.astrology.exception.UserNotFoundException; // ✅ Add missing exception import
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Comprehensive User Management Controller for Vedic Astrology Application
 * Handles user profiles, birth data, preferences, and account management
 */
@CrossOrigin(
    origins = {"http://localhost:8081", "http://localhost:3000", "https://yourdomain.com"}, 
    maxAge = 3600, 
    allowCredentials = "true",
    allowedHeaders = {"*"},
    methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}
)
@RestController
@RequestMapping("/api/users") // ✅ Changed to match our established pattern
@PreAuthorize("hasRole('CLIENT') or hasRole('ASTROLOGER') or hasRole('USER')")
@Validated
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "User Management", description = "User profile and account management endpoints")
@Slf4j // ✅ Fixed: Use Lombok's @Slf4j for proper logging
@RequiredArgsConstructor // ✅ Use constructor injection instead of @Autowired
public class UserController {
    
    private final UserService userService; // ✅ Final field with constructor injection
    
    // ================ PROFILE MANAGEMENT ================
    
    /**
     * Get complete user profile information
     */
    @GetMapping("/profile")
    @Operation(summary = "Get User Profile", 
               description = "Retrieve complete user profile information including astrology data")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Profile retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> getUserProfile(Principal principal, HttpServletRequest request) {
        try {
            String username = principal.getName();
            String clientIp = getClientIpAddress(request);
            
            log.info("👤 Profile request from user: {} at IP: {}", username, clientIp);
            
            UserProfileResponse profile = userService.getUserProfile(username);
            
            log.info("✅ Profile retrieved successfully for: {}", username);
            return ResponseEntity.ok()
                    .body(createSuccessResponse("PROFILE_RETRIEVED", "Profile retrieved successfully", profile));
            
        } catch (UserNotFoundException e) {
            log.error("❌ Profile retrieval error for {}: {}", principal.getName(), e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse("USER_NOT_FOUND", e.getMessage(), null));
        } catch (Exception e) {
            log.error("❌ Unexpected error retrieving profile for {}: {}", principal.getName(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("PROFILE_ERROR", "Unable to retrieve profile", null));
        }
    }
    
    /**
     * Update user profile information
     */
    @PutMapping("/profile")
    @Operation(summary = "Update User Profile", 
               description = "Update user profile information including personal details and preferences")
    public ResponseEntity<?> updateUserProfile(
            @Valid @RequestBody UserProfileRequest request, 
            BindingResult bindingResult,
            Principal principal,
            HttpServletRequest httpRequest) {
        
        try {
            String username = principal.getName();
            String clientIp = getClientIpAddress(httpRequest);
            
            log.info("👤 Profile update request from user: {} at IP: {}", username, clientIp);
            
            // Validate request
            if (bindingResult.hasErrors()) {
                Map<String, String> validationErrors = extractValidationErrors(bindingResult);
                log.warn("❌ Profile update validation errors for {}: {}", username, validationErrors);
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("VALIDATION_ERROR", "Invalid profile data", validationErrors));
            }
            
            UserProfileResponse updatedProfile = userService.updateProfile(username, request, clientIp);
            
            log.info("✅ Profile updated successfully for: {}", username);
            return ResponseEntity.ok()
                    .body(createSuccessResponse("PROFILE_UPDATED", "Profile updated successfully", updatedProfile));
            
        } catch (IllegalArgumentException e) {
            log.warn("⚠️ Invalid profile update data for {}: {}", principal.getName(), e.getMessage());
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("INVALID_DATA", e.getMessage(), null));
        } catch (Exception e) {
            log.error("❌ Profile update error for {}: {}", principal.getName(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("UPDATE_ERROR", "Unable to update profile", null));
        }
    }
    
    /**
     * Get basic user information (lightweight endpoint)
     */
    @GetMapping("/info")
    @Operation(summary = "Get Basic User Info", 
               description = "Get basic user information for UI display")
    public ResponseEntity<?> getUserInfo(Principal principal) {
        try {
            String username = principal.getName();
            Map<String, Object> userInfo = userService.getBasicUserInfo(username);
            
            return ResponseEntity.ok()
                    .body(createSuccessResponse("USER_INFO", "User info retrieved", userInfo));
            
        } catch (Exception e) {
            log.error("❌ Error getting user info for {}: {}", principal.getName(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("INFO_ERROR", "Unable to retrieve user info", null));
        }
    }
    
    // ================ BIRTH DATA MANAGEMENT ================
    
    /**
     * Get user's birth data for chart calculations
     */
    @GetMapping("/birth-data")
    @Operation(summary = "Get Birth Data", 
               description = "Retrieve user's birth data including date, time, and location")
    public ResponseEntity<?> getBirthData(Principal principal) {
        try {
            String username = principal.getName();
            log.info("🌟 Birth data request from user: {}", username);
            
            BirthDataResponse birthData = userService.getBirthData(username);
            
            log.info("✅ Birth data retrieved for: {}", username);
            return ResponseEntity.ok()
                    .body(createSuccessResponse("BIRTH_DATA_RETRIEVED", "Birth data retrieved successfully", birthData));
            
        } catch (UserNotFoundException e) {
            log.warn("⚠️ Birth data not found for {}: {}", principal.getName(), e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse("BIRTH_DATA_NOT_FOUND", e.getMessage(), null));
        } catch (Exception e) {
            log.error("❌ Error retrieving birth data for {}: {}", principal.getName(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("BIRTH_DATA_ERROR", "Unable to retrieve birth data", null));
        }
    }
    
    /**
     * Update user's birth data
     */
    @PostMapping("/birth-data")
    @Operation(summary = "Update Birth Data", 
               description = "Update user's birth data for accurate chart calculations")
    public ResponseEntity<?> updateBirthData(
            @Valid @RequestBody BirthData birthData, 
            BindingResult bindingResult,
            Principal principal,
            HttpServletRequest request) {
        
        try {
            String username = principal.getName();
            String clientIp = getClientIpAddress(request);
            
            log.info("🌟 Birth data update from user: {} at IP: {}", username, clientIp);
            log.info("📍 New birth data: {} at {}", birthData.getBirthLocation(), birthData.getBirthDateTime());
            
            // Validate request
            if (bindingResult.hasErrors()) {
                Map<String, String> validationErrors = extractValidationErrors(bindingResult);
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("VALIDATION_ERROR", "Invalid birth data", validationErrors));
            }
            
            BirthDataResponse updatedBirthData = userService.updateBirthData(username, birthData, clientIp);
            
            log.info("✅ Birth data updated successfully for: {}", username);
            return ResponseEntity.ok()
                    .body(createSuccessResponse("BIRTH_DATA_UPDATED", "Birth data updated successfully", updatedBirthData));
            
        } catch (IllegalArgumentException e) {
            log.warn("⚠️ Invalid birth data for {}: {}", principal.getName(), e.getMessage());
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("INVALID_BIRTH_DATA", e.getMessage(), null));
        } catch (Exception e) {
            log.error("❌ Birth data update error for {}: {}", principal.getName(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("UPDATE_ERROR", "Unable to update birth data", null));
        }
    }
    
    /**
     * ✅ FIXED: Enhanced birth profile update endpoint (matches frontend expectation)
   /**
 * ✅ FIXED: Enhanced birth profile update endpoint (matches frontend expectation)
 */
@PutMapping("/birth-profile")
@Operation(summary = "Update Birth Profile", 
           description = "Update user's birth profile data for astrological calculations")
public ResponseEntity<?> updateBirthProfile(
        @RequestBody @Valid BirthDataUpdateRequest request,
        Principal principal, // ✅ FIXED: Use Principal instead of @AuthenticationPrincipal String
        HttpServletRequest httpRequest) {
    
    try {
        // ✅ FIXED: Get username from Principal
        String username = principal.getName();
        
        // ✅ DEBUG: Add logging to verify username
        if (username == null || username.trim().isEmpty()) {
            log.error("❌ Username is null or empty from principal: {}", principal);
            log.error("❌ Principal class: {}", principal != null ? principal.getClass().getName() : "null");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(createErrorResponse("AUTH_ERROR", "Invalid authentication - username is null", null));
        }
        
        String clientIp = getClientIpAddress(httpRequest);
        
        log.info("🌟 Birth profile update from user: {} at IP: {}", username, clientIp);
        
        // Validate the request data
        if (request.getBirthDateTime() == null || request.getBirthLocation() == null || request.getBirthLocation().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                .body(createErrorResponse("VALIDATION_ERROR", "Birth date and location are required", null));
        }
        
        // Convert request to BirthData
        BirthData birthData = convertToBirthData(request);
        
        // Update birth data
        BirthDataResponse birthDataResponse = userService.updateBirthData(username, birthData, clientIp);
        
        // Get updated user profile
        UserProfileResponse updatedProfile = userService.getUserProfile(username);
        
        log.info("✅ Birth profile updated successfully for user: {}", username);
        
        return ResponseEntity.ok()
            .body(createSuccessResponse("BIRTH_PROFILE_UPDATED", 
                "Birth profile updated successfully", updatedProfile));
        
    } catch (UserNotFoundException e) {
        log.error("❌ User not found: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(createErrorResponse("USER_NOT_FOUND", e.getMessage(), null));
        
    } catch (IllegalArgumentException e) {
        log.error("❌ Invalid birth data provided: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(createErrorResponse("INVALID_DATA", e.getMessage(), null));
        
    } catch (Exception e) {
        log.error("❌ Unexpected error updating birth profile for user: {}", 
            principal != null ? principal.getName() : "unknown", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(createErrorResponse("UPDATE_ERROR", "Failed to update birth profile", null));
    }
}

    
    // ================ PREFERENCES MANAGEMENT ================
    
    /**
     * Get user preferences
     */
    @GetMapping("/preferences")
    @Operation(summary = "Get User Preferences", 
               description = "Retrieve user's application preferences and settings")
    public ResponseEntity<?> getUserPreferences(Principal principal) {
        try {
            String username = principal.getName();
            UserPreferencesResponse preferences = userService.getUserPreferences(username);
            
            return ResponseEntity.ok()
                    .body(createSuccessResponse("PREFERENCES_RETRIEVED", "Preferences retrieved successfully", preferences));
            
        } catch (Exception e) {
            log.error("❌ Error retrieving preferences for {}: {}", principal.getName(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("PREFERENCES_ERROR", "Unable to retrieve preferences", null));
        }
    }
    
    /**
     * Update user preferences
     */
    @PutMapping("/preferences")
    @Operation(summary = "Update User Preferences", 
               description = "Update user's application preferences and notification settings")
    public ResponseEntity<?> updateUserPreferences(
            @Valid @RequestBody UserPreferencesRequest request,
            BindingResult bindingResult,
            Principal principal,
            HttpServletRequest httpRequest) {
        
        try {
            if (bindingResult.hasErrors()) {
                Map<String, String> validationErrors = extractValidationErrors(bindingResult);
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("VALIDATION_ERROR", "Invalid preferences data", validationErrors));
            }
            
            String username = principal.getName();
            String clientIp = getClientIpAddress(httpRequest);
            
            UserPreferencesResponse updatedPreferences = userService.updatePreferences(username, request, clientIp);
            
            log.info("✅ Preferences updated for: {}", username);
            return ResponseEntity.ok()
                    .body(createSuccessResponse("PREFERENCES_UPDATED", "Preferences updated successfully", updatedPreferences));
            
        } catch (Exception e) {
            log.error("❌ Preferences update error for {}: {}", principal.getName(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("UPDATE_ERROR", "Unable to update preferences", null));
        }
    }
    
    // ================ ACCOUNT MANAGEMENT ================
    
    /**
     * Get account status and statistics
     */
    @GetMapping("/account-status")
    @Operation(summary = "Get Account Status", 
               description = "Get detailed account status including subscription, usage, and statistics")
    public ResponseEntity<?> getAccountStatus(Principal principal) {
        try {
            String username = principal.getName();
            AccountStatusResponse status = userService.getAccountStatus(username);
            
            return ResponseEntity.ok()
                    .body(createSuccessResponse("ACCOUNT_STATUS", "Account status retrieved", status));
            
        } catch (Exception e) {
            log.error("❌ Error retrieving account status for {}: {}", principal.getName(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("STATUS_ERROR", "Unable to retrieve account status", null));
        }
    }
    
    /**
     * Update account settings
     */
    @PutMapping("/account-settings")
    @Operation(summary = "Update Account Settings", 
               description = "Update account-level settings and security preferences")
    public ResponseEntity<?> updateAccountSettings(
            @Valid @RequestBody AccountSettingsRequest request,
            BindingResult bindingResult,
            Principal principal,
            HttpServletRequest httpRequest) {
        
        try {
            if (bindingResult.hasErrors()) {
                Map<String, String> validationErrors = extractValidationErrors(bindingResult);
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("VALIDATION_ERROR", "Invalid settings data", validationErrors));
            }
            
            String username = principal.getName();
            String clientIp = getClientIpAddress(httpRequest);
            
            AccountSettingsResponse updatedSettings = userService.updateAccountSettings(username, request, clientIp);
            
            log.info("✅ Account settings updated for: {}", username);
            return ResponseEntity.ok()
                    .body(createSuccessResponse("SETTINGS_UPDATED", "Account settings updated successfully", updatedSettings));
            
        } catch (Exception e) {
            log.error("❌ Account settings update error for {}: {}", principal.getName(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("UPDATE_ERROR", "Unable to update account settings", null));
        }
    }
    
    /**
     * Delete user account (soft delete)
     */
    @DeleteMapping("/account")
    @Operation(summary = "Delete Account", 
               description = "Soft delete user account (can be restored within 30 days)")
    public ResponseEntity<?> deleteAccount(
            @RequestBody Map<String, String> confirmationData,
            Principal principal,
            HttpServletRequest request) {
        
        try {
            String username = principal.getName();
            String clientIp = getClientIpAddress(request);
            String confirmationPassword = confirmationData.get("password");
            String reason = confirmationData.get("reason");
            
            log.info("🗑️ Account deletion request from: {} at IP: {}", username, clientIp);
            
            if (confirmationPassword == null || confirmationPassword.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("PASSWORD_REQUIRED", "Password confirmation required for account deletion", null));
            }
            
            MessageResponse result = userService.deleteAccount(username, confirmationPassword, reason, clientIp);
            
            log.info("✅ Account deletion processed for: {}", username);
            return ResponseEntity.ok()
                    .body(createSuccessResponse("ACCOUNT_DELETED", result.getMessage(), null));
            
        } catch (SecurityException e) {
            log.warn("🔒 Account deletion denied for {}: {}", principal.getName(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(createErrorResponse("INVALID_PASSWORD", e.getMessage(), null));
        } catch (Exception e) {
            log.error("❌ Account deletion error for {}: {}", principal.getName(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("DELETION_ERROR", "Unable to process account deletion", null));
        }
    }
    
    // ================ PROFILE PICTURE MANAGEMENT ================
    
    /**
     * Upload profile picture
     */
    @PostMapping("/profile-picture")
    @Operation(summary = "Upload Profile Picture", 
               description = "Upload or update user profile picture")
    public ResponseEntity<?> uploadProfilePicture(
            @RequestParam("file") MultipartFile file,
            Principal principal,
            HttpServletRequest request) {
        
        try {
            String username = principal.getName();
            String clientIp = getClientIpAddress(request);
            
            log.info("📸 Profile picture upload from: {} (Size: {} bytes)", username, file.getSize());
            
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("EMPTY_FILE", "Profile picture file is required", null));
            }
            
            ProfilePictureResponse result = userService.uploadProfilePicture(username, file, clientIp);
            
            log.info("✅ Profile picture uploaded for: {}", username);
            return ResponseEntity.ok()
                    .body(createSuccessResponse("PICTURE_UPLOADED", "Profile picture uploaded successfully", result));
            
        } catch (IllegalArgumentException e) {
            log.warn("⚠️ Invalid profile picture for {}: {}", principal.getName(), e.getMessage());
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("INVALID_FILE", e.getMessage(), null));
        } catch (Exception e) {
            log.error("❌ Profile picture upload error for {}: {}", principal.getName(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("UPLOAD_ERROR", "Unable to upload profile picture", null));
        }
    }
    
    /**
     * Delete profile picture
     */
    @DeleteMapping("/profile-picture")
    @Operation(summary = "Delete Profile Picture", 
               description = "Remove user's profile picture")
    public ResponseEntity<?> deleteProfilePicture(Principal principal, HttpServletRequest request) {
        try {
            String username = principal.getName();
            String clientIp = getClientIpAddress(request);
            
            MessageResponse result = userService.deleteProfilePicture(username, clientIp);
            
            log.info("✅ Profile picture deleted for: {}", username);
            return ResponseEntity.ok()
                    .body(createSuccessResponse("PICTURE_DELETED", result.getMessage(), null));
            
        } catch (Exception e) {
            log.error("❌ Profile picture deletion error for {}: {}", principal.getName(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("DELETE_ERROR", "Unable to delete profile picture", null));
        }
    }
    
    // ================ ACTIVITY AND HISTORY ================
    
    /**
     * Get user activity history
     */
    @GetMapping("/activity")
    @Operation(summary = "Get User Activity", 
               description = "Get user's recent activity and login history")
    public ResponseEntity<?> getUserActivity(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            Principal principal) {
        
        try {
            String username = principal.getName();
            UserActivityResponse activity = userService.getUserActivity(username, page, size);
            
            return ResponseEntity.ok()
                    .body(createSuccessResponse("ACTIVITY_RETRIEVED", "User activity retrieved", activity));
            
        } catch (Exception e) {
            log.error("❌ Error retrieving user activity for {}: {}", principal.getName(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("ACTIVITY_ERROR", "Unable to retrieve user activity", null));
        }
    }
    
    /**
     * Get chart generation history
     */
    @GetMapping("/chart-history")
    @Operation(summary = "Get Chart History", 
               description = "Get user's birth chart generation history")
    public ResponseEntity<?> getChartHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Principal principal) {
        
        try {
            String username = principal.getName();
            ChartHistoryResponse history = userService.getChartHistory(username, page, size);
            
            return ResponseEntity.ok()
                    .body(createSuccessResponse("CHART_HISTORY", "Chart history retrieved", history));
            
        } catch (Exception e) {
            log.error("❌ Error retrieving chart history for {}: {}", principal.getName(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("HISTORY_ERROR", "Unable to retrieve chart history", null));
        }
    }
    
    // ================ UTILITY ENDPOINTS ================
    
    /**
     * Check profile completion status
     */
    @GetMapping("/profile-completion")
    @Operation(summary = "Get Profile Completion", 
               description = "Get profile completion status and missing fields")
    public ResponseEntity<?> getProfileCompletion(Principal principal) {
        try {
            String username = principal.getName();
            ProfileCompletionResponse completion = userService.getProfileCompletion(username);
            
            return ResponseEntity.ok()
                    .body(createSuccessResponse("COMPLETION_STATUS", "Profile completion status", completion));
            
        } catch (Exception e) {
            log.error("❌ Error getting profile completion for {}: {}", principal.getName(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("COMPLETION_ERROR", "Unable to get profile completion", null));
        }
    }
    
    /**
     * Export user data (GDPR compliance)
     */
    @GetMapping("/export-data")
    @Operation(summary = "Export User Data", 
               description = "Export all user data for GDPR compliance")
    public ResponseEntity<?> exportUserData(Principal principal) {
        try {
            String username = principal.getName();
            log.info("📤 Data export request from: {}", username);
            
            Map<String, Object> exportData = userService.exportUserData(username);
            
            log.info("✅ Data export completed for: {}", username);
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"" + username + "_data_export.json\"")
                    .body(exportData);
            
        } catch (Exception e) {
            log.error("❌ Data export error for {}: {}", principal.getName(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("EXPORT_ERROR", "Unable to export user data", null));
        }
    }
    
    // ================ PRIVATE HELPER METHODS ================
    
    /**
     * ✅ FIXED: Helper method to convert DTO
     */
   /**
 * ✅ ENHANCED: Helper method to convert DTO with better error handling
 */
/**
 * ✅ ENHANCED: Convert BirthDataUpdateRequest to BirthData entity with comprehensive validation
 */
private BirthData convertToBirthData(BirthDataUpdateRequest request) {
    try {
        // ✅ VALIDATION: Ensure request is not null
        if (request == null) {
            throw new IllegalArgumentException("Birth data request cannot be null");
        }
        
        // ✅ VALIDATION: Check if request is valid
        if (!request.isValidRequest()) {
            throw new IllegalArgumentException("Invalid birth data request: " + request.getDataQualityDescription());
        }
        
        log.debug("🔄 Converting birth data request: {}", request.createSafeLogVersion());
        
        BirthData birthData = new BirthData();
        
        // ✅ CORE DATA: Set essential birth information
        birthData.setBirthDateTime(request.getParsedBirthDateTime());
        birthData.setBirthLocation(request.getBirthLocation());
        
        // ✅ COORDINATES: Handle nullable coordinates
        birthData.setBirthLatitude(request.getBirthLatitude());
        birthData.setBirthLongitude(request.getBirthLongitude());
        
        // ✅ TIMEZONE: Set timezone with fallback
        birthData.setTimezone(request.getTimezone() != null ? request.getTimezone() : "UTC");
        
        // ✅ LOCATION COMPONENTS: Set parsed location components
        birthData.setBirthCity(request.getBirthCity());
        birthData.setBirthState(request.getBirthState());
        birthData.setBirthCountry(request.getBirthCountry());
        
        // ✅ ENHANCED: Handle data source with smart fallback
        String dataSource = request.getDataSource(); // This uses the enhanced getDataSource() method
        birthData.setDataSource(dataSource);
        
        // ✅ ENHANCED: Set time accuracy information
        if (request.getIsTimeAccurate() != null) {
            birthData.setTimeAccurate(request.getIsTimeAccurate());
        } else {
            // Default based on data source
            birthData.setTimeAccurate(isTimeAccurateFromSource(dataSource));
        }
        
        // ✅ NEW: Set time source if available
        if (request.getTimeSource() != null && !request.getTimeSource().trim().isEmpty()) {
            birthData.setTimeSource(request.getTimeSource());
        }
        
        // ✅ NEW: Set update reason for audit trail
        if (request.getUpdateReason() != null && !request.getUpdateReason().trim().isEmpty()) {
            birthData.setUpdateReason(request.getUpdateReason());
        }
        
        // ✅ ENHANCED: Set additional metadata
        birthData.setCoordinatesPrecision(request.getCoordinatePrecision());
        birthData.setFormattedLocation(request.getFormattedLocation());
        
        // ✅ VALIDATION: Parse location components if not already set
        if ((birthData.getBirthCity() == null || birthData.getBirthState() == null || birthData.getBirthCountry() == null)) {
            request.parseLocationComponents();
            if (birthData.getBirthCity() == null) birthData.setBirthCity(request.getBirthCity());
            if (birthData.getBirthState() == null) birthData.setBirthState(request.getBirthState());
            if (birthData.getBirthCountry() == null) birthData.setBirthCountry(request.getBirthCountry());
        }
        
        // ✅ VALIDATION: Ensure coordinates are valid if provided
        if (request.hasCoordinates() && !request.hasValidCoordinates()) {
            log.warn("⚠️ Invalid coordinates provided: lat={}, lon={}", 
                request.getBirthLatitude(), request.getBirthLongitude());
            // Set coordinates to null if invalid
            birthData.setBirthLatitude(null);
            birthData.setBirthLongitude(null);
        }
        
        // ✅ ENHANCED: Calculate and set quality score
        try {
            birthData.calculateQualityScore();
            log.debug("✅ Birth data quality score: {}%", birthData.getQualityScore());
        } catch (Exception qualityError) {
            log.warn("⚠️ Could not calculate quality score: {}", qualityError.getMessage());
            // Set default quality score based on available data
            birthData.setQualityScore(calculateFallbackQualityScore(request));
        }
        
        // ✅ VALIDATION: Final validation of converted data
        validateConvertedBirthData(birthData);
        
        log.debug("✅ Successfully converted birth data request");
        return birthData;
        
    } catch (IllegalArgumentException e) {
        log.error("❌ Validation error in birth data conversion: {}", e.getMessage());
        throw e;
    } catch (Exception e) {
        log.error("❌ Unexpected error converting birth data request: {}", e.getMessage(), e);
        throw new IllegalArgumentException("Invalid birth data format: " + e.getMessage(), e);
    }
}

// ================ HELPER METHODS ================

/**
 * ✅ NEW: Determine time accuracy based on data source
 */
private boolean isTimeAccurateFromSource(String dataSource) {
    if (dataSource == null) return false;
    
    switch (dataSource.toUpperCase()) {
        case "BIRTH_CERTIFICATE":
        case "HOSPITAL_RECORD":
            return true;
        case "FAMILY_KNOWLEDGE":
            return false; // Usually approximate
        case "ESTIMATED":
        case "USER_PROVIDED":
        default:
            return false; // Conservative assumption
    }
}

/**
 * ✅ NEW: Calculate fallback quality score if automatic calculation fails
 */
private int calculateFallbackQualityScore(BirthDataUpdateRequest request) {
    int score = 0;
    
    // Basic data (40 points)
    if (request.getBirthDateTime() != null && !request.getBirthDateTime().trim().isEmpty()) {
        score += 20;
    }
    if (request.getBirthLocation() != null && !request.getBirthLocation().trim().isEmpty()) {
        score += 20;
    }
    
    // Coordinates (30 points)
    if (request.hasValidCoordinates()) {
        score += 30;
    }
    
    // Time accuracy (20 points)
    if (request.getIsTimeAccurate() != null && request.getIsTimeAccurate()) {
        score += 20;
    }
    
    // Timezone (10 points)
    if (request.isTimezoneValid()) {
        score += 10;
    }
    
    return Math.min(score, 100);
}

/**
 * ✅ NEW: Validate the converted birth data
 */
private void validateConvertedBirthData(BirthData birthData) {
    if (birthData.getBirthDateTime() == null) {
        throw new IllegalArgumentException("Birth date and time cannot be null after conversion");
    }
    
    if (birthData.getBirthLocation() == null || birthData.getBirthLocation().trim().isEmpty()) {
        throw new IllegalArgumentException("Birth location cannot be null or empty after conversion");
    }
    
    // Validate date is not in the future
    if (birthData.getBirthDateTime().isAfter(java.time.LocalDateTime.now())) {
        throw new IllegalArgumentException("Birth date cannot be in the future");
    }
    
    // Validate coordinates if provided
    if (birthData.getBirthLatitude() != null || birthData.getBirthLongitude() != null) {
        if (birthData.getBirthLatitude() == null || birthData.getBirthLongitude() == null) {
            throw new IllegalArgumentException("Both latitude and longitude must be provided if coordinates are specified");
        }
        
        if (birthData.getBirthLatitude() < -90 || birthData.getBirthLatitude() > 90) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90 degrees");
        }
        
        if (birthData.getBirthLongitude() < -180 || birthData.getBirthLongitude() > 180) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180 degrees");
        }
    }
    
    // Validate timezone
    if (birthData.getTimezone() != null) {
        try {
            java.time.ZoneId.of(birthData.getTimezone());
        } catch (Exception e) {
            log.warn("⚠️ Invalid timezone '{}', using UTC as fallback", birthData.getTimezone());
            birthData.setTimezone("UTC");
        }
    }
}



    
    private Map<String, String> extractValidationErrors(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .collect(Collectors.toMap(
                    FieldError::getField,
                    error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "Invalid value",
                    (existing, replacement) -> existing
                ));
    }
    
    private Map<String, Object> createSuccessResponse(String code, String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("code", code);
        response.put("message", message);
        response.put("timestamp", LocalDateTime.now());
        if (data != null) {
            response.put("data", data);
        }
        return response;
    }
    
    private Map<String, Object> createErrorResponse(String code, String message, Object errors) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("code", code);
        response.put("message", message);
        response.put("timestamp", LocalDateTime.now());
        if (errors != null) {
            response.put("errors", errors);
        }
        return response;
    }
    
    /**
     * ✅ FIXED: Enhanced client IP extraction
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String[] headerNames = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
        };
        
        for (String header : headerNames) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                return ip.split(",")[0].trim();
            }
        }
        
        return request.getRemoteAddr();
    }
}
