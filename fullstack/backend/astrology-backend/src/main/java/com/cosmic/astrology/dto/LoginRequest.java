package com.cosmic.astrology.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Objects;

/**
 * Login Request DTO for user authentication
 * Supports both username and email-based login
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "User login request with username/email and password")
public class LoginRequest {
    
    @NotBlank(message = "Username or email is required")
    @Size(min = 3, max = 100, message = "Username/email must be between 3 and 100 characters")
    @Schema(description = "Username or email address", 
            example = "testuser", 
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;
    
    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    @Schema(description = "User password", 
            example = "TestPass123!", 
            requiredMode = Schema.RequiredMode.REQUIRED,
            format = "password")
    private String password;
    
    @Schema(description = "Remember me option for extended session", 
            example = "false",
            defaultValue = "false")
    private Boolean rememberMe = false;
    
    @Size(max = 45, message = "Client IP cannot exceed 45 characters")
    @Schema(description = "Client IP address for security logging", 
            example = "192.168.1.1")
    private String clientIp;
    
    @Size(max = 500, message = "User agent cannot exceed 500 characters")
    @Schema(description = "Browser/device user agent", 
            example = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
    private String userAgent;
    
    @Pattern(regexp = "^[a-zA-Z]{2}$", message = "Language must be a valid 2-letter code")
    @Schema(description = "Preferred login language", 
            example = "en",
            allowableValues = {"en", "es", "fr", "de", "hi", "sa"})
    private String language = "en";
    
    @Schema(description = "Device type for responsive experience", 
            example = "DESKTOP",
            allowableValues = {"DESKTOP", "MOBILE", "TABLET"})
    private String deviceType;
    
    @Schema(description = "Two-factor authentication code (if enabled)", 
            example = "123456")
    @Pattern(regexp = "^\\d{6}$", message = "2FA code must be 6 digits")
    private String twoFactorCode;
    
    @Schema(description = "Time zone of the client", 
            example = "America/New_York")
    private String timezone;
    
    // ================ CONSTRUCTORS ================
    
    public LoginRequest() {}
    
    public LoginRequest(String username, String password) {
        this.username = username != null ? username.trim() : null;
        this.password = password;
    }
    
    public LoginRequest(String username, String password, Boolean rememberMe) {
        this(username, password);
        this.rememberMe = rememberMe != null ? rememberMe : false;
    }
    
    public LoginRequest(String username, String password, String clientIp, String userAgent) {
        this(username, password);
        this.clientIp = clientIp;
        this.userAgent = userAgent;
    }
    
    // ================ GETTERS AND SETTERS ================
    
    public String getUsername() { return username; }
    public void setUsername(String username) { 
        this.username = username != null ? username.trim() : null; 
    }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public Boolean getRememberMe() { return rememberMe; }
    public void setRememberMe(Boolean rememberMe) { 
        this.rememberMe = rememberMe != null ? rememberMe : false; 
    }
    
    public String getClientIp() { return clientIp; }
    public void setClientIp(String clientIp) { this.clientIp = clientIp; }
    
    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }
    
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    
    public String getDeviceType() { return deviceType; }
    public void setDeviceType(String deviceType) { this.deviceType = deviceType; }
    
    public String getTwoFactorCode() { return twoFactorCode; }
    public void setTwoFactorCode(String twoFactorCode) { this.twoFactorCode = twoFactorCode; }
    
    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }
    
    // ================ UTILITY METHODS ================
    
    /**
     * Check if the identifier is an email address
     */
    public boolean isEmailLogin() {
        return username != null && username.contains("@");
    }
    
    /**
     * Check if username is a valid format
     */
    public boolean isUsernameLogin() {
        return username != null && !username.contains("@") && username.length() >= 3;
    }
    
    /**
     * Get the login identifier type
     */
    public String getIdentifierType() {
        if (isEmailLogin()) {
            return "EMAIL";
        } else if (isUsernameLogin()) {
            return "USERNAME";
        }
        return "UNKNOWN";
    }
    
    /**
     * Check if this is a mobile device login
     */
    public boolean isMobileLogin() {
        return "MOBILE".equalsIgnoreCase(deviceType) || 
               (userAgent != null && (userAgent.contains("Mobile") || userAgent.contains("Android") || userAgent.contains("iPhone")));
    }
    
    /**
     * Check if remember me is enabled
     */
    public boolean shouldRememberUser() {
        return Boolean.TRUE.equals(rememberMe);
    }
    
    /**
     * Check if two-factor authentication is provided
     */
    public boolean hasTwoFactorCode() {
        return twoFactorCode != null && !twoFactorCode.trim().isEmpty();
    }
    
    /**
     * Get sanitized username for logging (hide sensitive parts of email)
     */
    public String getSanitizedUsername() {
        if (username == null) return "null";
        
        if (isEmailLogin()) {
            String[] parts = username.split("@");
            if (parts.length == 2) {
                String localPart = parts[0];
                String domain = parts[1];
                
                if (localPart.length() > 2) {
                    return localPart.charAt(0) + "***" + localPart.charAt(localPart.length() - 1) + "@" + domain;
                }
                return "***@" + domain;
            }
        } else {
            if (username.length() > 3) {
                return username.substring(0, 2) + "***" + username.charAt(username.length() - 1);
            }
        }
        
        return username;
    }
    
    /**
     * Validate login request data
     */
    public boolean isValidRequest() {
        return username != null && !username.trim().isEmpty() &&
               password != null && !password.trim().isEmpty() &&
               username.trim().length() >= 3;
    }
    
    // ================ SECURITY METHODS ================
    
    /**
     * Clear sensitive data after use
     */
    public void clearSensitiveData() {
        this.password = null;
        this.twoFactorCode = null;
    }
    
    /**
     * Create a copy without sensitive data for logging
     */
    public LoginRequest createSafeLogCopy() {
        LoginRequest safeCopy = new LoginRequest();
        safeCopy.setUsername(getSanitizedUsername());
        safeCopy.setPassword("***");
        safeCopy.setRememberMe(this.rememberMe);
        safeCopy.setClientIp(this.clientIp);
        safeCopy.setLanguage(this.language);
        safeCopy.setDeviceType(this.deviceType);
        safeCopy.setTimezone(this.timezone);
        safeCopy.setTwoFactorCode(hasTwoFactorCode() ? "***" : null);
        
        return safeCopy;
    }
    
    // ================ OBJECT METHODS ================
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        LoginRequest that = (LoginRequest) obj;
        return Objects.equals(username, that.username) &&
               Objects.equals(clientIp, that.clientIp);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(username, clientIp);
    }
    
    @Override
    public String toString() {
        return String.format("LoginRequest{username='%s', identifierType='%s', rememberMe=%s, deviceType='%s', hasTwoFactor=%s}", 
                           getSanitizedUsername(), getIdentifierType(), rememberMe, deviceType, hasTwoFactorCode());
    }
}
