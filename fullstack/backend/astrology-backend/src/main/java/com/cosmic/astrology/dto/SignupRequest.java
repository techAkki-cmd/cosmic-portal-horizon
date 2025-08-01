package com.cosmic.astrology.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Comprehensive User Registration Request DTO for Vedic Astrology Application
 * Includes user details, birth data, and preferences for complete profile setup
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Complete user registration request with personal and birth data")
public class SignupRequest {
    
    // ================ BASIC ACCOUNT INFORMATION ================
    
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain letters, numbers, and underscores")
    @Schema(description = "Unique username for the account", 
            example = "testuser", 
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;
    
    @NotBlank(message = "Email is required")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    @Email(message = "Please provide a valid email address")
    @Schema(description = "User's email address", 
            example = "user@cosmic-astrology.com", 
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;
    
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", 
             message = "Password must contain at least 8 characters with uppercase, lowercase, number, and special character")
    @Schema(description = "Strong password meeting security requirements", 
            example = "SecurePass123!", 
            requiredMode = Schema.RequiredMode.REQUIRED,
            format = "password")
    private String password;
    
    @NotBlank(message = "Password confirmation is required")
    @Schema(description = "Password confirmation for validation", 
            example = "SecurePass123!", 
            requiredMode = Schema.RequiredMode.REQUIRED,
            format = "password")
    private String confirmPassword;
    
    // ================ PERSONAL INFORMATION ================
    
    @NotBlank(message = "First name is required")
    @Size(min = 1, max = 50, message = "First name must be between 1 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s'-]+$", message = "First name can only contain letters, spaces, hyphens, and apostrophes")
    @Schema(description = "User's first name", 
            example = "John", 
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String firstName;
    
    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s'-]*$", message = "Last name can only contain letters, spaces, hyphens, and apostrophes")
    @Schema(description = "User's last name", example = "Doe")
    private String lastName;
    
    @Pattern(regexp = "^[+]?[1-9]\\d{1,14}$", message = "Please provide a valid phone number with country code")
    @Schema(description = "Phone number with country code", example = "+1234567890")
    private String phoneNumber;
    
    @Pattern(regexp = "^(Male|Female|Other|Prefer not to say)$", message = "Gender must be Male, Female, Other, or Prefer not to say")
    @Schema(description = "User's gender", example = "Male", 
            allowableValues = {"Male", "Female", "Other", "Prefer not to say"})
    private String gender;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Past(message = "Date of birth must be in the past")
    @Schema(description = "Date of birth (for age verification)", example = "1990-05-15")
    private LocalDateTime dateOfBirth;
    
    // ================ BIRTH DATA FOR VEDIC ASTROLOGY ================
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Past(message = "Birth date and time must be in the past")
    @Schema(description = "Exact birth date and time for chart calculations", 
            example = "1990-05-15T14:30:00")
    private LocalDateTime birthDateTime;
    
    @Size(max = 200, message = "Birth location cannot exceed 200 characters")
    @Schema(description = "Birth location (city, state, country)", 
            example = "New York, NY, USA")
    private String birthLocation;
    
    @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90 degrees")
    @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90 degrees")
    @Schema(description = "Birth location latitude", example = "40.7128")
    private Double birthLatitude;
    
    @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180 degrees")
    @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180 degrees")
    @Schema(description = "Birth location longitude", example = "-74.0060")
    private Double birthLongitude;
    
    @Size(max = 50, message = "Timezone cannot exceed 50 characters")
    @Schema(description = "Birth location timezone", example = "America/New_York")
    private String timezone;
    
    @Size(max = 100, message = "Birth city cannot exceed 100 characters")
    @Schema(description = "Birth city name", example = "New York")
    private String birthCity;
    
    @Size(max = 100, message = "Birth state cannot exceed 100 characters")
    @Schema(description = "Birth state/province", example = "New York")
    private String birthState;
    
    @Size(max = 100, message = "Birth country cannot exceed 100 characters")
    @Schema(description = "Birth country", example = "United States")
    private String birthCountry;
    
    // ================ PREFERENCES AND SETTINGS ================
    
    @Pattern(regexp = "^(en|es|fr|de|hi|sa)$", message = "Language must be en, es, fr, de, hi, or sa")
    @Schema(description = "Preferred language", example = "en", 
            allowableValues = {"en", "es", "fr", "de", "hi", "sa"})
    private String preferredLanguage = "en";
    
    @Pattern(regexp = "^(Lahiri|Raman|KP|Tropical)$", message = "Ayanamsa must be Lahiri, Raman, KP, or Tropical")
    @Schema(description = "Preferred ayanamsa calculation system", example = "Lahiri", 
            allowableValues = {"Lahiri", "Raman", "KP", "Tropical"})
    private String preferredAyanamsa = "Lahiri";
    
    @Pattern(regexp = "^(12h|24h)$", message = "Time format must be 12h or 24h")
    @Schema(description = "Preferred time format", example = "12h", 
            allowableValues = {"12h", "24h"})
    private String timeFormat = "12h";
    
    @Pattern(regexp = "^(MM/dd/yyyy|dd/MM/yyyy|yyyy-MM-dd)$", message = "Date format must be MM/dd/yyyy, dd/MM/yyyy, or yyyy-MM-dd")
    @Schema(description = "Preferred date format", example = "MM/dd/yyyy", 
            allowableValues = {"MM/dd/yyyy", "dd/MM/yyyy", "yyyy-MM-dd"})
    private String dateFormat = "MM/dd/yyyy";
    
    // ================ NOTIFICATION PREFERENCES ================
    
    @Schema(description = "Enable email notifications", example = "true")
    private Boolean emailNotifications = true;
    
    @Schema(description = "Enable daily horoscope notifications", example = "true")
    private Boolean dailyHoroscope = true;
    
    @Schema(description = "Enable transit alert notifications", example = "false")
    private Boolean transitAlerts = false;
    
    @Schema(description = "Enable marketing emails", example = "false")
    private Boolean marketingEmails = false;
    
    // ================ TERMS AND AGREEMENTS ================
    
    @NotNull(message = "You must agree to the terms of service")
    @AssertTrue(message = "You must agree to the terms of service")
    @Schema(description = "Agreement to terms of service", example = "true", 
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean agreeToTerms;
    
    @NotNull(message = "You must agree to the privacy policy")
    @AssertTrue(message = "You must agree to the privacy policy")
    @Schema(description = "Agreement to privacy policy", example = "true", 
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean agreeToPrivacyPolicy;
    
    @Schema(description = "Agreement to receive marketing communications", example = "false")
    private Boolean agreeToMarketing = false;
    
    @Schema(description = "User is 13 years or older", example = "true")
    private Boolean ageConfirmation = true;
    
    // ================ REGISTRATION METADATA ================
    
    @Schema(description = "Registration source/referrer", example = "ORGANIC")
    private String registrationSource;
    
    @Schema(description = "Referral code if applicable", example = "FRIEND2024")
    private String referralCode;
    
    @Schema(description = "How user heard about the service", example = "Google Search")
    private String hearAboutUs;
    
    @Schema(description = "Client IP address", example = "192.168.1.1")
    private String clientIp;
    
    @Schema(description = "User agent string", example = "Mozilla/5.0...")
    private String userAgent;
    
    @Schema(description = "Device type", example = "DESKTOP", 
            allowableValues = {"DESKTOP", "MOBILE", "TABLET"})
    private String deviceType;
    
    // ================ CONSTRUCTORS ================
    
    public SignupRequest() {}
    
    public SignupRequest(String username, String email, String password, String firstName) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
    }
    
    // ================ GETTERS AND SETTERS ================
    
    public String getUsername() { return username; }
    public void setUsername(String username) { 
        this.username = username != null ? username.trim().toLowerCase() : null; 
    }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { 
        this.email = email != null ? email.trim().toLowerCase() : null; 
    }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { 
        this.firstName = firstName != null ? firstName.trim() : null; 
    }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { 
        this.lastName = lastName != null ? lastName.trim() : null; 
    }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    public LocalDateTime getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDateTime dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    
    public LocalDateTime getBirthDateTime() { return birthDateTime; }
    public void setBirthDateTime(LocalDateTime birthDateTime) { this.birthDateTime = birthDateTime; }
    
    public String getBirthLocation() { return birthLocation; }
    public void setBirthLocation(String birthLocation) { 
        this.birthLocation = birthLocation != null ? birthLocation.trim() : null; 
    }
    
    public Double getBirthLatitude() { return birthLatitude; }
    public void setBirthLatitude(Double birthLatitude) { this.birthLatitude = birthLatitude; }
    
    public Double getBirthLongitude() { return birthLongitude; }
    public void setBirthLongitude(Double birthLongitude) { this.birthLongitude = birthLongitude; }
    
    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }
    
    public String getBirthCity() { return birthCity; }
    public void setBirthCity(String birthCity) { this.birthCity = birthCity; }
    
    public String getBirthState() { return birthState; }
    public void setBirthState(String birthState) { this.birthState = birthState; }
    
    public String getBirthCountry() { return birthCountry; }
    public void setBirthCountry(String birthCountry) { this.birthCountry = birthCountry; }
    
    public String getPreferredLanguage() { return preferredLanguage; }
    public void setPreferredLanguage(String preferredLanguage) { this.preferredLanguage = preferredLanguage; }
    
    public String getPreferredAyanamsa() { return preferredAyanamsa; }
    public void setPreferredAyanamsa(String preferredAyanamsa) { this.preferredAyanamsa = preferredAyanamsa; }
    
    public String getTimeFormat() { return timeFormat; }
    public void setTimeFormat(String timeFormat) { this.timeFormat = timeFormat; }
    
    public String getDateFormat() { return dateFormat; }
    public void setDateFormat(String dateFormat) { this.dateFormat = dateFormat; }
    
    public Boolean getEmailNotifications() { return emailNotifications; }
    public void setEmailNotifications(Boolean emailNotifications) { this.emailNotifications = emailNotifications; }
    
    public Boolean getDailyHoroscope() { return dailyHoroscope; }
    public void setDailyHoroscope(Boolean dailyHoroscope) { this.dailyHoroscope = dailyHoroscope; }
    
    public Boolean getTransitAlerts() { return transitAlerts; }
    public void setTransitAlerts(Boolean transitAlerts) { this.transitAlerts = transitAlerts; }
    
    public Boolean getMarketingEmails() { return marketingEmails; }
    public void setMarketingEmails(Boolean marketingEmails) { this.marketingEmails = marketingEmails; }
    
    public Boolean getAgreeToTerms() { return agreeToTerms; }
    public void setAgreeToTerms(Boolean agreeToTerms) { this.agreeToTerms = agreeToTerms; }
    
    public Boolean getAgreeToPrivacyPolicy() { return agreeToPrivacyPolicy; }
    public void setAgreeToPrivacyPolicy(Boolean agreeToPrivacyPolicy) { this.agreeToPrivacyPolicy = agreeToPrivacyPolicy; }
    
    public Boolean getAgreeToMarketing() { return agreeToMarketing; }
    public void setAgreeToMarketing(Boolean agreeToMarketing) { this.agreeToMarketing = agreeToMarketing; }
    
    public Boolean getAgeConfirmation() { return ageConfirmation; }
    public void setAgeConfirmation(Boolean ageConfirmation) { this.ageConfirmation = ageConfirmation; }
    
    public String getRegistrationSource() { return registrationSource; }
    public void setRegistrationSource(String registrationSource) { this.registrationSource = registrationSource; }
    
    public String getReferralCode() { return referralCode; }
    public void setReferralCode(String referralCode) { this.referralCode = referralCode; }
    
    public String getHearAboutUs() { return hearAboutUs; }
    public void setHearAboutUs(String hearAboutUs) { this.hearAboutUs = hearAboutUs; }
    
    public String getClientIp() { return clientIp; }
    public void setClientIp(String clientIp) { this.clientIp = clientIp; }
    
    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }
    
    public String getDeviceType() { return deviceType; }
    public void setDeviceType(String deviceType) { this.deviceType = deviceType; }
    
    // ================ UTILITY METHODS ================
    
    /**
     * Check if passwords match
     */
    @JsonProperty("passwordsMatch")
    public boolean doPasswordsMatch() {
        return password != null && password.equals(confirmPassword);
    }
    
    /**
     * Check if user has complete birth data for chart calculations
     */
    @JsonProperty("hasCompleteBirthData")
    public boolean hasCompleteBirthData() {
        return birthDateTime != null && 
               birthLatitude != null && 
               birthLongitude != null && 
               birthLocation != null && !birthLocation.trim().isEmpty();
    }
    
    /**
     * Check if all required agreements are accepted
     */
    @JsonProperty("agreementsComplete")
    public boolean areAgreementsComplete() {
        return Boolean.TRUE.equals(agreeToTerms) && 
               Boolean.TRUE.equals(agreeToPrivacyPolicy) &&
               Boolean.TRUE.equals(ageConfirmation);
    }
    
    /**
     * Get full name
     */
    @JsonProperty("fullName")
    public String getFullName() {
        if (firstName == null && lastName == null) return "";
        if (firstName == null) return lastName;
        if (lastName == null) return firstName;
        return firstName + " " + lastName;
    }
    
    /**
     * Check if registration is from mobile device
     */
    @JsonProperty("isMobileRegistration")
    public boolean isMobileRegistration() {
        return "MOBILE".equalsIgnoreCase(deviceType) || 
               (userAgent != null && (userAgent.contains("Mobile") || userAgent.contains("Android") || userAgent.contains("iPhone")));
    }
    
    /**
     * Check if user opted into marketing
     */
    @JsonProperty("marketingOptIn")
    public boolean isMarketingOptIn() {
        return Boolean.TRUE.equals(agreeToMarketing) || Boolean.TRUE.equals(marketingEmails);
    }
    
    /**
     * Calculate registration completion percentage
     */
    @JsonProperty("completionPercentage")
    public int getRegistrationCompletionPercentage() {
        int totalFields = 10;
        int completedFields = 0;
        
        if (username != null && !username.trim().isEmpty()) completedFields++;
        if (email != null && !email.trim().isEmpty()) completedFields++;
        if (password != null && !password.trim().isEmpty()) completedFields++;
        if (firstName != null && !firstName.trim().isEmpty()) completedFields++;
        if (areAgreementsComplete()) completedFields++;
        if (phoneNumber != null && !phoneNumber.trim().isEmpty()) completedFields++;
        if (gender != null) completedFields++;
        if (hasCompleteBirthData()) completedFields += 2; // Birth data counts as 2 fields
        if (preferredLanguage != null) completedFields++;
        
        return (completedFields * 100) / totalFields;
    }
    
    /**
     * Validate birth coordinates
     */
    public boolean hasValidCoordinates() {
        return birthLatitude != null && birthLongitude != null &&
               birthLatitude >= -90 && birthLatitude <= 90 &&
               birthLongitude >= -180 && birthLongitude <= 180;
    }
    
    /**
     * Get user initials
     */
    @JsonProperty("initials")
    public String getInitials() {
        StringBuilder initials = new StringBuilder();
        if (firstName != null && !firstName.isEmpty()) {
            initials.append(firstName.charAt(0));
        }
        if (lastName != null && !lastName.isEmpty()) {
            initials.append(lastName.charAt(0));
        }
        if (initials.length() == 0 && username != null && !username.isEmpty()) {
            initials.append(username.charAt(0));
        }
        return initials.toString().toUpperCase();
    }
    
    // ================ SECURITY METHODS ================
    
    /**
     * Clear sensitive data after processing
     */
    public void clearSensitiveData() {
        this.password = null;
        this.confirmPassword = null;
    }
    
    /**
     * Create a safe copy for logging (without sensitive data)
     */
    public SignupRequest createSafeLogCopy() {
        SignupRequest safeCopy = new SignupRequest();
        safeCopy.setUsername(this.username);
        safeCopy.setEmail(this.email);
        safeCopy.setPassword("***");
        safeCopy.setConfirmPassword("***");
        safeCopy.setFirstName(this.firstName);
        safeCopy.setLastName(this.lastName);
        safeCopy.setGender(this.gender);
        safeCopy.setBirthLocation(this.birthLocation);
        safeCopy.setPreferredLanguage(this.preferredLanguage);
        safeCopy.setRegistrationSource(this.registrationSource);
        safeCopy.setDeviceType(this.deviceType);
        safeCopy.setClientIp(this.clientIp);
        
        return safeCopy;
    }
    
    // ================ OBJECT METHODS ================
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        SignupRequest that = (SignupRequest) obj;
        return Objects.equals(username, that.username) &&
               Objects.equals(email, that.email);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(username, email);
    }
    
    @Override
    public String toString() {
        return String.format("SignupRequest{username='%s', email='%s', fullName='%s', hasCompleteBirthData=%s, completion=%d%%}", 
                           username, email, getFullName(), hasCompleteBirthData(), getRegistrationCompletionPercentage());
    }
}
