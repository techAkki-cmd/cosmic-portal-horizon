package com.cosmic.astrology.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

/**
 * Account Settings Update Request DTO
 */
@Schema(description = "Account settings update request")
public class AccountSettingsRequest {
    
    @Schema(description = "Email notifications preference", example = "true")
    private Boolean emailNotifications;
    
    @Schema(description = "Daily horoscope preference", example = "true")
    private Boolean dailyHoroscope;
    
    @Schema(description = "Transit alerts preference", example = "false")
    private Boolean transitAlerts;
    
    @Schema(description = "Marketing emails preference", example = "false")
    private Boolean marketingEmails;
    
    @Schema(description = "Push notifications preference", example = "true")
    private Boolean pushNotifications;
    
    @Schema(description = "Privacy level", example = "PRIVATE")
    private String privacyLevel; // PUBLIC, FRIENDS, PRIVATE
    
    @Schema(description = "Preferred language", example = "en")
    private String preferredLanguage;
    
    @Schema(description = "Time format preference", example = "12h")
    private String timeFormat;
    
    @Schema(description = "Date format preference", example = "MM/dd/yyyy")
    private String dateFormat;
    
    @Schema(description = "Timezone", example = "America/New_York")
    private String timezone;
    
    @Schema(description = "Theme preference", example = "DARK")
    private String theme; // LIGHT, DARK, AUTO
    
    @Size(max = 500, message = "Bio cannot exceed 500 characters")
    @Schema(description = "User bio", example = "Passionate about Vedic astrology and cosmic insights")
    private String bio;
    
    @Schema(description = "Show birth data publicly", example = "false")
    private Boolean publicBirthData;
    
    @Schema(description = "Allow friend requests", example = "true")
    private Boolean allowFriendRequests;
    
    @Schema(description = "Show online status", example = "true")
    private Boolean showOnlineStatus;
    
    @Schema(description = "Two-factor authentication preference", example = "false")
    private Boolean twoFactorAuth;
    
    // Constructors
    public AccountSettingsRequest() {}
    
    // Getters and Setters
    public Boolean getEmailNotifications() { return emailNotifications; }
    public void setEmailNotifications(Boolean emailNotifications) { this.emailNotifications = emailNotifications; }
    
    public Boolean getDailyHoroscope() { return dailyHoroscope; }
    public void setDailyHoroscope(Boolean dailyHoroscope) { this.dailyHoroscope = dailyHoroscope; }
    
    public Boolean getTransitAlerts() { return transitAlerts; }
    public void setTransitAlerts(Boolean transitAlerts) { this.transitAlerts = transitAlerts; }
    
    public Boolean getMarketingEmails() { return marketingEmails; }
    public void setMarketingEmails(Boolean marketingEmails) { this.marketingEmails = marketingEmails; }
    
    public Boolean getPushNotifications() { return pushNotifications; }
    public void setPushNotifications(Boolean pushNotifications) { this.pushNotifications = pushNotifications; }
    
    public String getPrivacyLevel() { return privacyLevel; }
    public void setPrivacyLevel(String privacyLevel) { this.privacyLevel = privacyLevel; }
    
    public String getPreferredLanguage() { return preferredLanguage; }
    public void setPreferredLanguage(String preferredLanguage) { this.preferredLanguage = preferredLanguage; }
    
    public String getTimeFormat() { return timeFormat; }
    public void setTimeFormat(String timeFormat) { this.timeFormat = timeFormat; }
    
    public String getDateFormat() { return dateFormat; }
    public void setDateFormat(String dateFormat) { this.dateFormat = dateFormat; }
    
    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }
    
    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }
    
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    
    public Boolean getPublicBirthData() { return publicBirthData; }
    public void setPublicBirthData(Boolean publicBirthData) { this.publicBirthData = publicBirthData; }
    
    public Boolean getAllowFriendRequests() { return allowFriendRequests; }
    public void setAllowFriendRequests(Boolean allowFriendRequests) { this.allowFriendRequests = allowFriendRequests; }
    
    public Boolean getShowOnlineStatus() { return showOnlineStatus; }
    public void setShowOnlineStatus(Boolean showOnlineStatus) { this.showOnlineStatus = showOnlineStatus; }
    
    public Boolean getTwoFactorAuth() { return twoFactorAuth; }
    public void setTwoFactorAuth(Boolean twoFactorAuth) { this.twoFactorAuth = twoFactorAuth; }
    
    @Override
    public String toString() {
        return String.format("AccountSettingsRequest{emailNotifications=%s, dailyHoroscope=%s, theme='%s'}", 
                           emailNotifications, dailyHoroscope, theme);
    }
}
