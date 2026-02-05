package com.cosmic.astrology.service;

import com.cosmic.astrology.dto.*;
import com.cosmic.astrology.entity.User;
import com.cosmic.astrology.entity.UserRole;
import com.cosmic.astrology.entity.ActivityLog;
import com.cosmic.astrology.exception.UserNotFoundException;
import com.cosmic.astrology.repository.UserRepository;
import com.cosmic.astrology.repository.ActivityLogRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.regex.Pattern;


@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final ActivityLogRepository activityLogRepository;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;
    
    @Value("${app.upload.profile-pictures:/uploads/profile-pictures}")
    private String profilePictureUploadPath;
    
    @Value("${app.upload.max-file-size:5242880}") // 5MB default
    private long maxFileSize;
    
    @Value("${app.user.profile-completion.required-fields:8}")
    private int requiredFieldsForCompletion;
    
    @Value("${app.user.activity.page-size:20}")
    private int defaultActivityPageSize;
    
    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
        "image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp"
    );
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
   
    public User findByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        
        return userRepository.findByUsernameIgnoreCase(username.trim())
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));
    }
    
    
    public User findByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        
        return userRepository.findByEmailIgnoreCase(email.trim())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }
    
    
    public User findByUsernameOrEmail(String identifier) {
        if (identifier == null || identifier.trim().isEmpty()) {
            throw new IllegalArgumentException("User identifier cannot be null or empty");
        }
        
        return userRepository.findByUsernameOrEmailIgnoreCase(identifier.trim())
                .orElseThrow(() -> new UserNotFoundException("User not found: " + identifier));
    }
    
    
    public boolean existsByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        return userRepository.existsByUsernameIgnoreCase(username.trim());
    }
    public User getUserByUsername(String username) {
        try {
            Optional<User> userOptional = userRepository.findByUsername(username);
            return userOptional.orElse(null);
        } catch (Exception e) {
            System.err.println("❌ Error finding user by username: " + username + " - " + e.getMessage());
            return null;
        }
    }
    public User saveUser(User user) {
        try {
            if (user == null) {
                throw new IllegalArgumentException("User cannot be null");
            }
            
            user.setUpdatedAt(LocalDateTime.now());
            
            if (user.getId() == null) {
                // This is a new user
                System.out.println("💾 Creating new user: " + user.getUsername());
            } else {
                System.out.println("💾 Updating existing user: " + user.getUsername());
            }
            
            return userRepository.save(user);
            
        } catch (Exception e) {
            System.err.println("❌ Error saving user: " + e.getMessage());
            throw new RuntimeException("Failed to save user", e);
        }
    }
     public User getUserById(Long id) {
        try {
            Optional<User> userOptional = userRepository.findById(id);
            return userOptional.orElse(null);
        } catch (Exception e) {
            System.err.println("❌ Error finding user by ID: " + id + " - " + e.getMessage());
            return null;
        }
    }
     public boolean userExists(String username) {
        try {
            return userRepository.existsByUsername(username);
        } catch (Exception e) {
            System.err.println("❌ Error checking if user exists: " + username + " - " + e.getMessage());
            return false;
        }
    }
    
    
    public boolean existsByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return userRepository.existsByEmailIgnoreCase(email.trim());
    }
    
public UserProfileResponse getUserProfile(String username) {
    try {
        log.info("📋 Retrieving profile for user: {}", username);
        
        User user = findByUsername(username);
        UserProfileResponse profile = createUserProfileResponse(user);
        
        log.info("🔍 Created profile response: id={}, username={}, email={}", 
            profile.getId(), profile.getUsername(), profile.getEmail());
        
        return profile;
        
    } catch (Exception e) {
        log.error("❌ Error retrieving profile for {}: {}", username, e.getMessage(), e);
        throw new RuntimeException("Failed to retrieve user profile", e);
    }
}
public boolean deleteUser(String username) {
        try {
            User user = getUserByUsername(username);
            if (user != null) {
                userRepository.delete(user);
                System.out.println("🗑️ Deleted user: " + username);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("❌ Error deleting user: " + username + " - " + e.getMessage());
            return false;
        }
    }
    
    
    public UserProfileResponse updateProfile(String username, UserProfileRequest request, String clientIp) {
        try {
            log.info("📝 Updating profile for user: {} from IP: {}", username, clientIp);
            
            User user = findByUsername(username);
            User originalUser = cloneUser(user); // For audit trail
            
            // Update basic information
            updateBasicProfile(user, request);
            
            // Update contact information
            updateContactInformation(user, request);
            
            // Update personal details
            updatePersonalDetails(user, request);
            
            // Update preferences
            updateUserPreferencesFromProfile(user, request);
            
            // Update timestamps
            user.setUpdatedAt(LocalDateTime.now());
            
            // Save and recalculate profile completion
            User savedUser = userRepository.save(user);
            
            // Log profile update
            logProfileUpdate(originalUser, savedUser, clientIp);
            
            UserProfileResponse response = createUserProfileResponse(savedUser);
            
            log.info("✅ Profile updated successfully for: {}", username);
            return response;
            
        } catch (Exception e) {
            log.error("❌ Error updating profile for {}: {}", username, e.getMessage(), e);
            throw new RuntimeException("Failed to update user profile", e);
        }
    }
    
    /
    public Map<String, Object> getBasicUserInfo(String username) {
        try {
            User user = findByUsername(username);
            
            Map<String, Object> info = new HashMap<>();
            info.put("id", user.getId());
            info.put("username", user.getUsername());
            info.put("email", user.getEmail());
            info.put("fullName", user.getFullName());
            info.put("firstName", user.getFirstName());
            info.put("lastName", user.getLastName());
            info.put("initials", user.getInitials());
            info.put("role", user.getRole().name());
            info.put("enabled", user.isEnabled());
            info.put("emailVerified", user.isEmailVerified());
            info.put("profileCompletion", user.getProfileCompletionPercentage());
            info.put("hasCompleteBirthData", user.hasCompleteBirthDataForChart());
            info.put("hasGeneratedChart", user.isHasGeneratedChart());
            info.put("subscriptionType", user.getSubscriptionType());
            info.put("subscriptionActive", user.hasActiveSubscription());
            info.put("lastLogin", user.getLastLogin());
            info.put("memberSince", user.getCreatedAt());
            
            return info;
            
        } catch (Exception e) {
            log.error("❌ Error getting basic user info: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get user information", e);
        }
    }
    
    
    public BirthDataResponse getBirthData(String username) {
        try {
            log.info("🌟 Retrieving birth data for user: {}", username);
            
            User user = findByUsername(username);
            
            if (!user.hasCompleteBirthDataForChart()) {
                throw new RuntimeException("Incomplete birth data for chart calculations");
            }
            
            BirthDataResponse response = new BirthDataResponse();
            response.setBirthDateTime(user.getBirthDateTime());
            response.setBirthLocation(user.getBirthLocation());
            response.setBirthLatitude(user.getBirthLatitude());
            response.setBirthLongitude(user.getBirthLongitude());
            response.setTimezone(user.getTimezone());
            response.setBirthCity(user.getBirthCity());
            response.setBirthState(user.getBirthState());
            response.setBirthCountry(user.getBirthCountry());
            response.setDataComplete(true);
            response.setLastUpdated(user.getUpdatedAt());
            
            log.info("✅ Birth data retrieved for: {}", username);
            return response;
            
        } catch (Exception e) {
            log.error("❌ Error retrieving birth data for {}: {}", username, e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve birth data", e);
        }
    }
    
  
    public BirthDataResponse updateBirthData(String username, BirthData birthData, String clientIp) {
        try {
            log.info("🌟 Updating birth data for user: {} from IP: {}", username, clientIp);
            
            // Validate birth data
            validateBirthDataInternal(birthData);
            
            User user = findByUsername(username);
            
            // Store original data for audit
            BirthData originalData = extractCurrentBirthData(user);
            
            // Update birth data
            user.setBirthDateTime(birthData.getBirthDateTime());
            user.setBirthLocation(birthData.getBirthLocation());
            user.setBirthLatitude(birthData.getBirthLatitude());
            user.setBirthLongitude(birthData.getBirthLongitude());
            user.setTimezone(birthData.getTimezone());
            
            // Extract city, state, country from location if provided
            parseAndSetLocationDetails(user, birthData.getBirthLocation());
            
            // Clear cached chart data as birth data changed
            clearCachedChartData(user);
            
            // Update timestamps
            user.setUpdatedAt(LocalDateTime.now());
            
            User savedUser = userRepository.save(user);
            
            // Log birth data update
            logBirthDataUpdate(username, originalData, birthData, clientIp);
            
            BirthDataResponse response = new BirthDataResponse();
            response.setBirthDateTime(savedUser.getBirthDateTime());
            response.setBirthLocation(savedUser.getBirthLocation());
            response.setBirthLatitude(savedUser.getBirthLatitude());
            response.setBirthLongitude(savedUser.getBirthLongitude());
            response.setTimezone(savedUser.getTimezone());
            response.setBirthCity(savedUser.getBirthCity());
            response.setBirthState(savedUser.getBirthState());
            response.setBirthCountry(savedUser.getBirthCountry());
            response.setDataComplete(savedUser.hasCompleteBirthDataForChart());
            response.setLastUpdated(savedUser.getUpdatedAt());
            
            log.info("✅ Birth data updated successfully for: {}", username);
            return response;
            
        } catch (Exception e) {
            log.error("❌ Error updating birth data for {}: {}", username, e.getMessage(), e);
            throw new RuntimeException("Failed to update birth data", e);
        }
    }
    
   
    public Map<String, Object> validateBirthData(BirthData birthData) {
        Map<String, Object> validation = new HashMap<>();
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        
        try {
            // Required field validation
            if (birthData.getBirthDateTime() == null) {
                errors.add("Birth date and time are required");
            } else {
                // Check if birth date is reasonable
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime birthTime = birthData.getBirthDateTime();
                
                if (birthTime.isAfter(now)) {
                    errors.add("Birth date cannot be in the future");
                } else if (birthTime.isBefore(LocalDateTime.now().minusYears(150))) {
                    warnings.add("Birth date seems very old, please verify");
                }
            }
            
            if (birthData.getBirthLocation() == null || birthData.getBirthLocation().trim().isEmpty()) {
                errors.add("Birth location is required");
            }
            
            if (birthData.getBirthLatitude() == null || birthData.getBirthLongitude() == null) {
                errors.add("Birth coordinates (latitude and longitude) are required");
            } else {
                // Validate coordinate ranges
                if (Math.abs(birthData.getBirthLatitude()) > 90) {
                    errors.add("Latitude must be between -90 and 90 degrees");
                }
                if (Math.abs(birthData.getBirthLongitude()) > 180) {
                    errors.add("Longitude must be between -180 and 180 degrees");
                }
            }
            
            if (birthData.getTimezone() == null || birthData.getTimezone().trim().isEmpty()) {
                warnings.add("Timezone information is recommended for accurate calculations");
            }
            
            validation.put("valid", errors.isEmpty());
            validation.put("errors", errors);
            validation.put("warnings", warnings);
            validation.put("canGenerateChart", errors.isEmpty());
            
        } catch (Exception e) {
            errors.add("Validation error: " + e.getMessage());
            validation.put("valid", false);
            validation.put("errors", errors);
            validation.put("warnings", warnings);
            validation.put("canGenerateChart", false);
        }
        
        return validation;
    }
    
    
    public UserPreferencesResponse getUserPreferences(String username) {
        try {
            User user = findByUsername(username);
            
            UserPreferencesResponse preferences = new UserPreferencesResponse();
            preferences.setPreferredLanguage(user.getPreferredLanguage());
            preferences.setTimeFormat(user.getTimeFormat());
            preferences.setDateFormat(user.getDateFormat());
            preferences.setEmailNotifications(user.isEmailNotifications());
            preferences.setDailyHoroscope(user.isDailyHoroscope());
            preferences.setTransitAlerts(user.isTransitAlerts());
            preferences.setTimezone(user.getTimezone());
            
            return preferences;
            
        } catch (Exception e) {
            log.error("❌ Error retrieving preferences: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve user preferences", e);
        }
    }
    
    /**
     * Update user preferences
     */
    public UserPreferencesResponse updatePreferences(String username, UserPreferencesRequest request, String clientIp) {
        try {
            log.info("⚙️ Updating preferences for user: {} from IP: {}", username, clientIp);
            
            User user = findByUsername(username);
            
            // Update preferences
            if (request.getPreferredLanguage() != null) {
                user.setPreferredLanguage(request.getPreferredLanguage());
            }
            if (request.getTimeFormat() != null) {
                user.setTimeFormat(request.getTimeFormat());
            }
            if (request.getDateFormat() != null) {
                user.setDateFormat(request.getDateFormat());
            }
            if (request.getEmailNotifications() != null) {
                user.setEmailNotifications(request.getEmailNotifications());
            }
            if (request.getDailyHoroscope() != null) {
                user.setDailyHoroscope(request.getDailyHoroscope());
            }
            if (request.getTransitAlerts() != null) {
                user.setTransitAlerts(request.getTransitAlerts());
            }
            if (request.getTimezone() != null) {
                user.setTimezone(request.getTimezone());
            }
            
            user.setUpdatedAt(LocalDateTime.now());
            User savedUser = userRepository.save(user);
            
            log.info("✅ Preferences updated for: {}", username);
            
            // Return updated preferences
            return getUserPreferences(username);
            
        } catch (Exception e) {
            log.error("❌ Error updating preferences: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update preferences", e);
        }
    }
    
    // ================ ACCOUNT MANAGEMENT ================
    
    /**
     * Get account status with detailed information
     */
    public AccountStatusResponse getAccountStatus(String username) {
        try {
            User user = findByUsername(username);
            
            AccountStatusResponse status = new AccountStatusResponse();
            status.setUsername(user.getUsername());
            status.setEmail(user.getEmail());
            status.setEnabled(user.isEnabled());
            status.setEmailVerified(user.isEmailVerified());
            status.setRole(user.getRole().name());
            status.setMemberSince(user.getCreatedAt());
            status.setLastLogin(user.getLastLogin());
            status.setLastActiveDate(user.getLastActiveDate());
            status.setProfileCompletionPercentage(user.getProfileCompletionPercentage());
            status.setChartsGenerated(user.getChartsGenerated());
            status.setLoginStreak(user.getLoginStreak());
            status.setTotalLogins(user.getTotalLogins());
            status.setSubscriptionType(user.getSubscriptionType());
            status.setSubscriptionActive(user.hasActiveSubscription());
            status.setSubscriptionEndDate(user.getSubscriptionEndDate());
            status.setCreditsRemaining(user.getCreditsRemaining());
            status.setAccountLocked(user.isAccountLocked());
            status.setDaysSinceLastLogin(user.getDaysSinceLastLogin());
            
            return status;
            
        } catch (Exception e) {
            log.error("❌ Error retrieving account status: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve account status", e);
        }
    }
    
    /**
     * Update account settings
     */
    public AccountSettingsResponse updateAccountSettings(String username, AccountSettingsRequest request, String clientIp) {
        try {
            log.info("⚙️ Updating account settings for user: {} from IP: {}", username, clientIp);
            
            User user = findByUsername(username);
            
            // Update settings that don't require special handling
            if (request.getEmailNotifications() != null) {
                user.setEmailNotifications(request.getEmailNotifications());
            }
            if (request.getDailyHoroscope() != null) {
                user.setDailyHoroscope(request.getDailyHoroscope());
            }
            if (request.getTransitAlerts() != null) {
                user.setTransitAlerts(request.getTransitAlerts());
            }
            
            user.setUpdatedAt(LocalDateTime.now());
            User savedUser = userRepository.save(user);
            
            AccountSettingsResponse response = new AccountSettingsResponse();
            response.setEmailNotifications(savedUser.isEmailNotifications());
            response.setDailyHoroscope(savedUser.isDailyHoroscope());
            response.setTransitAlerts(savedUser.isTransitAlerts());
            response.setLastUpdated(savedUser.getUpdatedAt());
            
            log.info("✅ Account settings updated for: {}", username);
            return response;
            
        } catch (Exception e) {
            log.error("❌ Error updating account settings: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update account settings", e);
        }
    }
    
    /**
     * Soft delete user account
     */
    public MessageResponse deleteAccount(String username, String confirmationPassword, String reason, String clientIp) {
        try {
            log.info("🗑️ Processing account deletion for: {} from IP: {}", username, clientIp);
            
            User user = findByUsername(username);
            
            // Verify password
            if (!passwordEncoder.matches(confirmationPassword, user.getPassword())) {
                throw new SecurityException("Invalid password confirmation");
            }
            
            // Soft delete - disable account but keep data
            user.setEnabled(false);
            user.setUpdatedAt(LocalDateTime.now());
            
            // Log deletion reason
            logAccountDeletion(username, reason, clientIp);
            
            userRepository.save(user);
            
            log.info("✅ Account soft deleted for: {}", username);
            
            return new MessageResponse("Account has been deactivated successfully. Your data will be retained for 30 days and can be restored by contacting support.");
            
        } catch (SecurityException e) {
            log.error("🔒 Account deletion denied: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("❌ Error deleting account: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete account", e);
        }
    }
    
    // ================ PROFILE PICTURE MANAGEMENT ================
    
    /**
     * Upload profile picture
     */
    public ProfilePictureResponse uploadProfilePicture(String username, MultipartFile file, String clientIp) {
        try {
            log.info("📸 Uploading profile picture for: {} (Size: {} bytes)", username, file.getSize());
            
            // Validate file
            validateProfilePictureFile(file);
            
            User user = findByUsername(username);
            
            // Create upload directory if it doesn't exist
            Path uploadDir = Paths.get(profilePictureUploadPath);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            
            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String filename = username + "_" + System.currentTimeMillis() + extension;
            Path filePath = uploadDir.resolve(filename);
            
            // Save file
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            // Delete old profile picture if exists
            if (user.getProfilePictureUrl() != null) {
                deleteOldProfilePicture(user.getProfilePictureUrl());
            }
            
            // Update user profile picture URL
            String profilePictureUrl = "/profile-pictures/" + filename;
            user.setProfilePictureUrl(profilePictureUrl);
            user.setUpdatedAt(LocalDateTime.now());
            
            userRepository.save(user);
            
            ProfilePictureResponse response = new ProfilePictureResponse();
            response.setProfilePictureUrl(profilePictureUrl);
            response.setFilename(filename);
            response.setFileSize(file.getSize());
            response.setUploadedAt(LocalDateTime.now());
            
            log.info("✅ Profile picture uploaded successfully for: {}", username);
            return response;
            
        } catch (Exception e) {
            log.error("❌ Error uploading profile picture: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to upload profile picture", e);
        }
    }
    
    /**
     * Delete profile picture
     */
    public MessageResponse deleteProfilePicture(String username, String clientIp) {
        try {
            log.info("🗑️ Deleting profile picture for: {} from IP: {}", username, clientIp);
            
            User user = findByUsername(username);
            
            if (user.getProfilePictureUrl() != null) {
                deleteOldProfilePicture(user.getProfilePictureUrl());
                user.setProfilePictureUrl(null);
                user.setUpdatedAt(LocalDateTime.now());
                userRepository.save(user);
            }
            
            log.info("✅ Profile picture deleted for: {}", username);
            return new MessageResponse("Profile picture deleted successfully");
            
        } catch (Exception e) {
            log.error("❌ Error deleting profile picture: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete profile picture", e);
        }
    }
    
    // ================ ACTIVITY AND HISTORY ================
    
    /**
     * Get user activity history with proper type conversion
     */
    public UserActivityResponse getUserActivity(String username, int page, int size) {
        try {
            log.info("🔍 Retrieving activity data for user: {} (page: {}, size: {})", username, page, size);
            
            // Get user
            User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));
            
            // Get activities with pagination
            Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
            Page<ActivityLog> activityPage = activityLogRepository.findByUsername(username, pageable);
            
            // Convert to ActivityRecord DTOs
            List<UserActivityResponse.ActivityRecord> activities = activityPage.getContent().stream()
                .map(this::convertActivityLogToRecord)
                .collect(Collectors.toList());
            
            // Get recent activities (last 24 hours)
            LocalDateTime yesterday = LocalDateTime.now().minusHours(24);
            List<ActivityLog> recentActivityLogs = activityLogRepository
                .findByUsernameAndTimestampAfterOrderByTimestampDesc(username, yesterday);
            
            List<UserActivityResponse.ActivityRecord> recentActivities = recentActivityLogs.stream()
                .map(this::convertActivityLogToRecord)
                .collect(Collectors.toList());
            
            // Create response
            UserActivityResponse activity = new UserActivityResponse(
                activities, 
                page, 
                size, 
                (int) activityPage.getTotalElements()
            );
            
            // Set user properties
            activity.setUsername(username);
            activity.setTotalLogins(user.getTotalLogins());
            activity.setLoginStreak(user.getLoginStreak());
            activity.setLastLogin(user.getLastLogin());
            activity.setLastActiveDate(user.getLastActiveDate());
            activity.setLastLoginIp(user.getLastLoginIp());
            activity.setChartsGenerated(user.getChartsGenerated());
            activity.setMemberSince(user.getCreatedAt());
            
            // Set recent activities with proper type
            activity.setRecentActivities(recentActivities);
            activity.setActivityCount(recentActivities.size());
            
            // Set additional user context
            activity.setDisplayName(user.getDisplayName());
            activity.setEmail(user.getEmail());
            activity.setUserStatus(user.getStatus());
            activity.setSubscriptionType(user.getSubscriptionType());
            activity.setTimezone(user.getTimezone());
            activity.setPreferredLanguage(user.getPreferredLanguage());
            
            // Generate activity summary
            UserActivityResponse.ActivitySummary summary = generateActivitySummary(activities, recentActivities, user);
            activity.setSummary(summary);
            
            // Set analytics
            activity.setActivityCategorization(generateActivityCategorization(activities));
            activity.setTimeDistribution(generateTimeDistribution(activities));
            activity.setDeviceAnalytics(generateDeviceAnalytics(activities));
            
            log.info("✅ Retrieved activity data for user: {} (Total: {}, Recent: {})", 
                    username, activities.size(), recentActivities.size());
            
            return activity;
            
        } catch (Exception e) {
            log.error("❌ Error retrieving user activity for {}: {}", username, e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve user activity", e);
        }
    }
    
    /**
     * Convert ActivityLog entity to ActivityRecord DTO
     */
    private UserActivityResponse.ActivityRecord convertActivityLogToRecord(ActivityLog activityLog) {
        UserActivityResponse.ActivityRecord record = new UserActivityResponse.ActivityRecord();
        
        try {
            record.setId(activityLog.getId());
            record.setActivityType(activityLog.getActivityType() != null ? activityLog.getActivityType() : "UNKNOWN");
            record.setDescription(activityLog.getDescription() != null ? activityLog.getDescription() : "No description");
            record.setTimestamp(activityLog.getTimestamp() != null ? activityLog.getTimestamp() : LocalDateTime.now());
            record.setIpAddress(activityLog.getIpAddress());
            record.setDeviceType(activityLog.getDeviceType() != null ? activityLog.getDeviceType() : "UNKNOWN");
            record.setStatus(activityLog.getStatus() != null ? activityLog.getStatus() : "SUCCESS");
            record.setSessionId(activityLog.getSessionId());
            record.setUserAgent(activityLog.getUserAgent());
            record.setLocation(activityLog.getLocation());
            record.setDurationMs(activityLog.getDurationMs());
            record.setCategory(categorizeActivity(activityLog.getActivityType()));
            record.setPriority(activityLog.getPriority() != null ? activityLog.getPriority() : "NORMAL");
            record.setErrorDetails(activityLog.getErrorDetails());
            
            // Handle metadata
            if (activityLog.getMetadata() != null) {
                record.setMetadata(activityLog.getMetadata());
            } else {
                record.setMetadata(new HashMap<>());
            }
            
        } catch (Exception e) {
            log.warn("⚠️ Error converting ActivityLog to ActivityRecord: {}", e.getMessage());
            // Set safe defaults
            record.setActivityType("CONVERSION_ERROR");
            record.setDescription("Error converting activity data");
            record.setTimestamp(LocalDateTime.now());
            record.setStatus("ERROR");
        }
        
        return record;
    }
    
    /**
     * Generate activity summary
     */
    private UserActivityResponse.ActivitySummary generateActivitySummary(
            List<UserActivityResponse.ActivityRecord> activities,
            List<UserActivityResponse.ActivityRecord> recentActivities,
            User user) {
        
        UserActivityResponse.ActivitySummary summary = new UserActivityResponse.ActivitySummary();
        
        if (activities.isEmpty()) {
            summary.setMostCommonActivity("NONE");
            summary.setAverageActivitiesPerDay(0.0);
            summary.setSuccessRate(100.0);
            summary.setEngagementLevel("LOW");
            return summary;
        }
        
        // Calculate most common activity
        Map<String, Long> activityTypes = activities.stream()
            .collect(Collectors.groupingBy(
                UserActivityResponse.ActivityRecord::getActivityType,
                Collectors.counting()
            ));
        
        String mostCommon = activityTypes.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("UNKNOWN");
        
        summary.setMostCommonActivity(mostCommon);
        
        // Calculate success rate
        long successfulActivities = activities.stream()
            .filter(a -> "SUCCESS".equals(a.getStatus()))
            .count();
        
        double successRate = (double) successfulActivities / activities.size() * 100;
        summary.setSuccessRate(successRate);
        
        // Set totals from user
        summary.setTotalLogins(user.getTotalLogins() != null ? user.getTotalLogins() : 0);
        summary.setTotalCharts(user.getChartsGenerated() != null ? user.getChartsGenerated() : 0);
        
        // Calculate engagement level
        summary.setEngagementLevel(calculateEngagementLevel(user, recentActivities));
        
        return summary;
    }
    
    /**
     * Calculate user engagement level
     */
    private String calculateEngagementLevel(User user, List<UserActivityResponse.ActivityRecord> recentActivities) {
        int score = 0;
        
        if (user.getLoginStreak() != null && user.getLoginStreak() >= 7) score += 25;
        if (user.getChartsGenerated() != null && user.getChartsGenerated() >= 10) score += 25;
        if (recentActivities.size() >= 5) score += 25;
        if (user.getLastActiveDate() != null && 
            user.getLastActiveDate().isAfter(LocalDateTime.now().minusDays(1))) score += 25;
        
        if (score >= 75) return "VERY_HIGH";
        if (score >= 50) return "HIGH";
        if (score >= 25) return "MEDIUM";
        return "LOW";
    }
    
    /**
     * Generate activity categorization
     */
    private Map<String, Integer> generateActivityCategorization(List<UserActivityResponse.ActivityRecord> activities) {
        Map<String, Integer> categorization = new HashMap<>();
        
        for (UserActivityResponse.ActivityRecord activity : activities) {
            String category = categorizeActivity(activity.getActivityType());
            categorization.merge(category, 1, Integer::sum);
        }
        
        return categorization;
    }
    
    /**
     * Categorize activity type
     */
    private String categorizeActivity(String activityType) {
        if (activityType == null) return "UNKNOWN";
        
        return switch (activityType.toUpperCase()) {
            case "LOGIN", "LOGOUT" -> "AUTHENTICATION";
            case "CHART_GENERATED", "CHART_VIEWED" -> "ASTROLOGY";
            case "PROFILE_UPDATED", "SETTINGS_CHANGED" -> "ACCOUNT_MANAGEMENT";
            case "CONSULTATION_BOOKED" -> "CONSULTATIONS";
            case "REPORT_DOWNLOADED" -> "REPORTS";
            default -> "OTHER";
        };
    }
    
    /**
     * Generate time distribution
     */
    private Map<String, Integer> generateTimeDistribution(List<UserActivityResponse.ActivityRecord> activities) {
        Map<String, Integer> distribution = new HashMap<>();
        
        for (UserActivityResponse.ActivityRecord activity : activities) {
            if (activity.getTimestamp() != null) {
                int hour = activity.getTimestamp().getHour();
                String timeSlot = getTimeSlot(hour);
                distribution.merge(timeSlot, 1, Integer::sum);
            }
        }
        
        return distribution;
    }
    
    /**
     * Get time slot for hour
     */
    private String getTimeSlot(int hour) {
        if (hour >= 6 && hour < 12) return "MORNING";
        if (hour >= 12 && hour < 17) return "AFTERNOON";
        if (hour >= 17 && hour < 21) return "EVENING";
        return "NIGHT";
    }
    
    /**
     * Generate device analytics
     */
    private Map<String, Integer> generateDeviceAnalytics(List<UserActivityResponse.ActivityRecord> activities) {
        return activities.stream()
            .collect(Collectors.groupingBy(
                activity -> activity.getDeviceType() != null ? activity.getDeviceType() : "UNKNOWN",
                Collectors.collectingAndThen(Collectors.counting(), Math::toIntExact)
            ));
    }
    
    /**
     * Get chart generation history
     */
    public ChartHistoryResponse getChartHistory(String username, int page, int size) {
        try {
            User user = findByUsername(username);
            
            ChartHistoryResponse history = new ChartHistoryResponse();
            history.setUsername(username);
            history.setTotalChartsGenerated(user.getChartsGenerated());
            history.setLastChartGenerated(user.getChartCalculatedAt());
            
            // Mock chart history (in real implementation, query from chart history table)
            List<Map<String, Object>> chartHistory = new ArrayList<>();
            if (user.isChartCalculated()) {
                Map<String, Object> chartRecord = new HashMap<>();
                chartRecord.put("type", "NATAL_CHART");
                chartRecord.put("calculatedAt", user.getChartCalculatedAt());
                chartRecord.put("accuracy", user.getCalculationAccuracy());
                chartRecord.put("ayanamsa", user.getAyanamsa());
                chartHistory.add(chartRecord);
            }
            
            history.setChartHistory(chartHistory);
            history.setHistoryCount(chartHistory.size());
            
            return history;
            
        } catch (Exception e) {
            log.error("❌ Error retrieving chart history: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve chart history", e);
        }
    }
    
    // ================ UTILITY METHODS ================
    
    /**
     * Get profile completion status
     */
    public ProfileCompletionResponse getProfileCompletion(String username) {
        try {
            User user = findByUsername(username);
            
            ProfileCompletionResponse completion = new ProfileCompletionResponse();
            completion.setUsername(username);
            completion.setCompletionPercentage(user.getProfileCompletionPercentage());
            completion.setMissingFields(calculateMissingFields(user));
            completion.setCompletedFields(calculateCompletedFields(user));
            completion.setIsComplete(user.getProfileCompletionPercentage() >= 90);
            
            return completion;
            
        } catch (Exception e) {
            log.error("❌ Error getting profile completion: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get profile completion", e);
        }
    }
    
    /**
     * Export all user data (GDPR compliance)
     */
    public Map<String, Object> exportUserData(String username) {
        try {
            log.info("📤 Exporting data for user: {}", username);
            
            User user = findByUsername(username);
            
            Map<String, Object> exportData = new HashMap<>();
            exportData.put("exportDate", LocalDateTime.now().format(TIMESTAMP_FORMATTER));
            exportData.put("username", user.getUsername());
            exportData.put("email", user.getEmail());
            exportData.put("personalInfo", createPersonalInfoExport(user));
            exportData.put("birthData", createBirthDataExport(user));
            exportData.put("preferences", createPreferencesExport(user));
            exportData.put("statistics", createStatisticsExport(user));
            exportData.put("chartData", createChartDataExport(user));
            exportData.put("accountInfo", createAccountInfoExport(user));
            
            log.info("✅ Data export completed for: {}", username);
            return exportData;
            
        } catch (Exception e) {
            log.error("❌ Error exporting user data: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to export user data", e);
        }
    }
    
    // ================ PRIVATE HELPER METHODS ================
    
    private UserProfileResponse createUserProfileResponse(User user) {
        UserProfileResponse response = new UserProfileResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setFullName(user.getFullName());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setGender(user.getGender());
        response.setOccupation(user.getOccupation());
        response.setNationality(user.getNationality());
        response.setRole(user.getRole().name());
        response.setEnabled(user.isEnabled());
        response.setEmailVerified(user.isEmailVerified());
        response.setProfileCompletionPercentage(user.getProfileCompletionPercentage());
        response.setHasCompleteBirthData(user.hasCompleteBirthDataForChart());
        response.setHasGeneratedChart(user.isHasGeneratedChart());
        response.setSubscriptionType(user.getSubscriptionType());
        response.setSubscriptionActive(user.hasActiveSubscription());
        response.setMemberSince(user.getCreatedAt());
        response.setLastLogin(user.getLastLogin());
        response.setLastUpdated(user.getUpdatedAt());
        response.setProfilePictureUrl(user.getProfilePictureUrl());
        
        // Astrology data
        response.setSunSign(user.getSunSign());
        response.setMoonSign(user.getMoonSign());
        response.setRisingSign(user.getRisingSign());
        response.setDominantElement(user.getDominantElement());
        response.setMoonNakshatra(user.getMoonNakshatra());
        response.setMoonPada(user.getMoonPada());
        
        return response;
    }
    
    private User cloneUser(User user) {
        // Simple cloning for audit purposes
        User clone = new User();
        clone.setId(user.getId());
        clone.setUsername(user.getUsername());
        clone.setEmail(user.getEmail());
        clone.setFirstName(user.getFirstName());
        clone.setLastName(user.getLastName());
        clone.setPhoneNumber(user.getPhoneNumber());
        clone.setGender(user.getGender());
        clone.setOccupation(user.getOccupation());
        clone.setNationality(user.getNationality());
        return clone;
    }
    
    private void updateBasicProfile(User user, UserProfileRequest request) {
        if (request.getFirstName() != null && !request.getFirstName().trim().isEmpty()) {
            user.setFirstName(request.getFirstName().trim());
        }
        if (request.getLastName() != null && !request.getLastName().trim().isEmpty()) {
            user.setLastName(request.getLastName().trim());
        }
        if (request.getPhoneNumber() != null) {
            user.setPhoneNumber(request.getPhoneNumber().trim());
        }
        if (request.getGender() != null) {
            user.setGender(request.getGender());
        }
        if (request.getOccupation() != null) {
            user.setOccupation(request.getOccupation().trim());
        }
        if (request.getNationality() != null) {
            user.setNationality(request.getNationality().trim());
        }
    }
    
    private void updateContactInformation(User user, UserProfileRequest request) {
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            String newEmail = request.getEmail().toLowerCase().trim();
            
            // Validate email format
            if (!EMAIL_PATTERN.matcher(newEmail).matches()) {
                throw new IllegalArgumentException("Invalid email format");
            }
            
            // Check if email is already taken
            if (userRepository.existsByEmailIgnoreCase(newEmail)) {
                throw new IllegalArgumentException("Email is already in use");
            }
            
            user.setEmail(newEmail);
            user.setEmailVerified(false); // Require re-verification
        }
    }
    
    private void updatePersonalDetails(User user, UserProfileRequest request) {
        // Additional personal details can be added here
        // For now, basic fields are handled in updateBasicProfile
    }
    
    private void updateUserPreferencesFromProfile(User user, UserProfileRequest request) {
        if (request.getPreferredLanguage() != null) {
            user.setPreferredLanguage(request.getPreferredLanguage());
        }
        if (request.getTimeFormat() != null) {
            user.setTimeFormat(request.getTimeFormat());
        }
        if (request.getDateFormat() != null) {
            user.setDateFormat(request.getDateFormat());
        }
    }
    
    private void validateBirthDataInternal(BirthData birthData) {
        Map<String, Object> validation = validateBirthData(birthData);
        Boolean isValid = (Boolean) validation.get("valid");
        
        if (!isValid) {
            @SuppressWarnings("unchecked")
            List<String> errors = (List<String>) validation.get("errors");
            throw new IllegalArgumentException("Invalid birth data: " + String.join(", ", errors));
        }
    }
    
    private BirthData extractCurrentBirthData(User user) {
        BirthData data = new BirthData();
        data.setBirthDateTime(user.getBirthDateTime());
        data.setBirthLocation(user.getBirthLocation());
        data.setBirthLatitude(user.getBirthLatitude());
        data.setBirthLongitude(user.getBirthLongitude());
        data.setTimezone(user.getTimezone());
        return data;
    }
    
    private void parseAndSetLocationDetails(User user, String location) {
        if (location != null && !location.trim().isEmpty()) {
            // Simple parsing - in real implementation, you might use a geocoding service
            String[] parts = location.split(",");
            if (parts.length >= 3) {
                user.setBirthCity(parts[0].trim());
                user.setBirthState(parts[1].trim());
                user.setBirthCountry(parts[2].trim());
            } else if (parts.length >= 2) {
                user.setBirthCity(parts[0].trim());
                user.setBirthCountry(parts[1].trim());
            } else {
                user.setBirthCity(location.trim());
            }
        }
    }
    
    private void clearCachedChartData(User user) {
        user.setNatalChart(null);
        user.setNatalHouses(null);
        user.setNatalAspects(null);
        user.setChartCalculated(false);
        user.setChartCalculatedAt(null);
        user.setSunSign(null);
        user.setMoonSign(null);
        user.setRisingSign(null);
        user.setDominantElement(null);
        user.setMoonNakshatra(null);
        user.setMoonPada(null);
        user.setAyanamsa(null);
    }
    
    private void validateProfilePictureFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Profile picture file cannot be empty");
        }
        
        if (file.getSize() > maxFileSize) {
            throw new IllegalArgumentException("File size exceeds maximum allowed size of " + (maxFileSize / 1024 / 1024) + "MB");
        }
        
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType.toLowerCase())) {
            throw new IllegalArgumentException("Invalid file type. Allowed types: " + String.join(", ", ALLOWED_IMAGE_TYPES));
        }
    }
    
    private void deleteOldProfilePicture(String profilePictureUrl) {
        try {
            if (profilePictureUrl != null && profilePictureUrl.startsWith("/profile-pictures/")) {
                String filename = profilePictureUrl.substring("/profile-pictures/".length());
                Path filePath = Paths.get(profilePictureUploadPath, filename);
                Files.deleteIfExists(filePath);
            }
        } catch (Exception e) {
            log.warn("⚠️ Failed to delete old profile picture: {}", e.getMessage());
        }
    }
    
    private List<String> calculateMissingFields(User user) {
        List<String> missing = new ArrayList<>();
        
        if (user.getFirstName() == null || user.getFirstName().trim().isEmpty()) {
            missing.add("First Name");
        }
        if (user.getLastName() == null || user.getLastName().trim().isEmpty()) {
            missing.add("Last Name");
        }
        if (user.getPhoneNumber() == null || user.getPhoneNumber().trim().isEmpty()) {
            missing.add("Phone Number");
        }
        if (user.getGender() == null || user.getGender().trim().isEmpty()) {
            missing.add("Gender");
        }
        if (user.getBirthDateTime() == null) {
            missing.add("Birth Date & Time");
        }
        if (user.getBirthLocation() == null || user.getBirthLocation().trim().isEmpty()) {
            missing.add("Birth Location");
        }
        if (user.getBirthLatitude() == null || user.getBirthLongitude() == null) {
            missing.add("Birth Coordinates");
        }
        if (user.getTimezone() == null || user.getTimezone().trim().isEmpty()) {
            missing.add("Timezone");
        }
        if (user.getOccupation() == null || user.getOccupation().trim().isEmpty()) {
            missing.add("Occupation");
        }
        if (!user.isEmailVerified()) {
            missing.add("Email Verification");
        }
        
        return missing;
    }
    
    private List<String> calculateCompletedFields(User user) {
        List<String> completed = new ArrayList<>();
        
        if (user.getFirstName() != null && !user.getFirstName().trim().isEmpty()) {
            completed.add("First Name");
        }
        if (user.getLastName() != null && !user.getLastName().trim().isEmpty()) {
            completed.add("Last Name");
        }
        if (user.getPhoneNumber() != null && !user.getPhoneNumber().trim().isEmpty()) {
            completed.add("Phone Number");
        }
        if (user.getGender() != null && !user.getGender().trim().isEmpty()) {
            completed.add("Gender");
        }
        if (user.getBirthDateTime() != null) {
            completed.add("Birth Date & Time");
        }
        if (user.getBirthLocation() != null && !user.getBirthLocation().trim().isEmpty()) {
            completed.add("Birth Location");
        }
        if (user.getBirthLatitude() != null && user.getBirthLongitude() != null) {
            completed.add("Birth Coordinates");
        }
        if (user.getTimezone() != null && !user.getTimezone().trim().isEmpty()) {
            completed.add("Timezone");
        }
        if (user.getOccupation() != null && !user.getOccupation().trim().isEmpty()) {
            completed.add("Occupation");
        }
        if (user.isEmailVerified()) {
            completed.add("Email Verification");
        }
        
        return completed;
    }
    
    // Export helper methods
    private Map<String, Object> createPersonalInfoExport(User user) {
        Map<String, Object> personalInfo = new HashMap<>();
        personalInfo.put("firstName", user.getFirstName());
        personalInfo.put("lastName", user.getLastName());
        personalInfo.put("phoneNumber", user.getPhoneNumber());
        personalInfo.put("gender", user.getGender());
        personalInfo.put("occupation", user.getOccupation());
        personalInfo.put("nationality", user.getNationality());
        return personalInfo;
    }
    
    private Map<String, Object> createBirthDataExport(User user) {
        Map<String, Object> birthData = new HashMap<>();
        birthData.put("birthDateTime", user.getBirthDateTime());
        birthData.put("birthLocation", user.getBirthLocation());
        birthData.put("birthLatitude", user.getBirthLatitude());
        birthData.put("birthLongitude", user.getBirthLongitude());
        birthData.put("timezone", user.getTimezone());
        birthData.put("birthCity", user.getBirthCity());
        birthData.put("birthState", user.getBirthState());
        birthData.put("birthCountry", user.getBirthCountry());
        return birthData;
    }
    
    private Map<String, Object> createPreferencesExport(User user) {
        Map<String, Object> preferences = new HashMap<>();
        preferences.put("preferredLanguage", user.getPreferredLanguage());
        preferences.put("timeFormat", user.getTimeFormat());
        preferences.put("dateFormat", user.getDateFormat());
        preferences.put("emailNotifications", user.isEmailNotifications());
        preferences.put("dailyHoroscope", user.isDailyHoroscope());
        preferences.put("transitAlerts", user.isTransitAlerts());
        return preferences;
    }
    
    private Map<String, Object> createStatisticsExport(User user) {
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("chartsGenerated", user.getChartsGenerated());
        statistics.put("loginStreak", user.getLoginStreak());
        statistics.put("totalLogins", user.getTotalLogins());
        statistics.put("profileCompletionPercentage", user.getProfileCompletionPercentage());
        statistics.put("memberSince", user.getCreatedAt());
        statistics.put("lastLogin", user.getLastLogin());
        statistics.put("lastActiveDate", user.getLastActiveDate());
        return statistics;
    }
    
    private Map<String, Object> createChartDataExport(User user) {
        Map<String, Object> chartData = new HashMap<>();
        chartData.put("sunSign", user.getSunSign());
        chartData.put("moonSign", user.getMoonSign());
        chartData.put("risingSign", user.getRisingSign());
        chartData.put("dominantElement", user.getDominantElement());
        chartData.put("moonNakshatra", user.getMoonNakshatra());
        chartData.put("moonPada", user.getMoonPada());
        chartData.put("ayanamsa", user.getAyanamsa());
        chartData.put("chartCalculated", user.isChartCalculated());
        chartData.put("chartCalculatedAt", user.getChartCalculatedAt());
        chartData.put("calculationAccuracy", user.getCalculationAccuracy());
        return chartData;
    }
    
    private Map<String, Object> createAccountInfoExport(User user) {
        Map<String, Object> accountInfo = new HashMap<>();
        accountInfo.put("username", user.getUsername());
        accountInfo.put("email", user.getEmail());
        accountInfo.put("role", user.getRole().name());
        accountInfo.put("enabled", user.isEnabled());
        accountInfo.put("emailVerified", user.isEmailVerified());
        accountInfo.put("subscriptionType", user.getSubscriptionType());
        accountInfo.put("subscriptionActive", user.hasActiveSubscription());
        accountInfo.put("subscriptionStartDate", user.getSubscriptionStartDate());
        accountInfo.put("subscriptionEndDate", user.getSubscriptionEndDate());
        accountInfo.put("creditsRemaining", user.getCreditsRemaining());
        return accountInfo;
    }
    
    // Audit logging methods
    private void logProfileUpdate(User originalUser, User updatedUser, String clientIp) {
        log.info("📝 Profile update audit log:");
        log.info("   User: {}", updatedUser.getUsername());
        log.info("   IP: {}", clientIp);
        log.info("   Timestamp: {}", LocalDateTime.now().format(TIMESTAMP_FORMATTER));
        
        // In a real implementation, you'd save this to an audit log table
    }
    
    private void logBirthDataUpdate(String username, BirthData originalData, BirthData newData, String clientIp) {
        log.info("🌟 Birth data update audit log:");
        log.info("   User: {}", username);
        log.info("   IP: {}", clientIp);
        log.info("   Old Location: {}", originalData.getBirthLocation());
        log.info("   New Location: {}", newData.getBirthLocation());
        log.info("   Timestamp: {}", LocalDateTime.now().format(TIMESTAMP_FORMATTER));
        
        // In a real implementation, you'd save this to an audit log table
    }
    
    private void logAccountDeletion(String username, String reason, String clientIp) {
        log.info("🗑️ Account deletion audit log:");
        log.info("   User: {}", username);
        log.info("   Reason: {}", reason);
        log.info("   IP: {}", clientIp);
        log.info("   Timestamp: {}", LocalDateTime.now().format(TIMESTAMP_FORMATTER));
        
        // In a real implementation, you'd save this to an audit log table
    }
}
