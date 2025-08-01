package com.cosmic.astrology.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Account Settings Response DTO
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Account settings information")
public class AccountSettingsResponse {
    
    @Schema(description = "Email notifications enabled", example = "true")
    private Boolean emailNotifications;
    
    @Schema(description = "Daily horoscope enabled", example = "true")
    private Boolean dailyHoroscope;
    
    @Schema(description = "Transit alerts enabled", example = "false")
    private Boolean transitAlerts;
    
    @Schema(description = "Marketing emails enabled", example = "false")
    private Boolean marketingEmails;
    
    @Schema(description = "Push notifications enabled", example = "true")
    private Boolean pushNotifications;
    
    @Schema(description = "Privacy level", example = "PRIVATE")
    private String privacyLevel;
    
    @Schema(description = "Preferred language", example = "en")
    private String preferredLanguage;
    
    @Schema(description = "Time format", example = "12h")
    private String timeFormat;
    
    @Schema(description = "Date format", example = "MM/dd/yyyy")
    private String dateFormat;
    
    @Schema(description = "Timezone", example = "America/New_York")
    private String timezone;
    
    @Schema(description = "Theme", example = "DARK")
    private String theme;
    
    @Schema(description = "User bio")
    private String bio;
    
    @Schema(description = "Public birth data", example = "false")
    private Boolean publicBirthData;
    
    @Schema(description = "Allow friend requests", example = "true")
    private Boolean allowFriendRequests;
    
    @Schema(description = "Show online status", example = "true")
    private Boolean showOnlineStatus;
    
    @Schema(description = "Two-factor authentication enabled", example = "false")
    private Boolean twoFactorAuth;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Last updated timestamp")
    private LocalDateTime lastUpdated;
    
    // Constructors
    public AccountSettingsResponse() {
        this.lastUpdated = LocalDateTime.now();
    }
    
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
    
    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AccountSettingsResponse that = (AccountSettingsResponse) obj;
        return Objects.equals(emailNotifications, that.emailNotifications) &&
               Objects.equals(theme, that.theme) &&
               Objects.equals(privacyLevel, that.privacyLevel);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(emailNotifications, theme, privacyLevel);
    }
    
    @Override
    public String toString() {
        return String.format("AccountSettingsResponse{emailNotifications=%s, theme='%s', lastUpdated=%s}", 
                           emailNotifications, theme, lastUpdated);
    }
}
