package com.cosmic.astrology.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Account Status Response DTO with comprehensive account information
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Complete account status and statistics")
public class AccountStatusResponse {
    
    @Schema(description = "Username", example = "testuser")
    private String username;
    
    @Schema(description = "Email address", example = "user@example.com")
    private String email;
    
    @Schema(description = "Account enabled status", example = "true")
    private Boolean enabled;
    
    @Schema(description = "Email verification status", example = "true")
    private Boolean emailVerified;
    
    @Schema(description = "User role", example = "CLIENT")
    private String role;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Member since date")
    private LocalDateTime memberSince;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Last login timestamp")
    private LocalDateTime lastLogin;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Last active timestamp")
    private LocalDateTime lastActiveDate;
    
    @Schema(description = "Profile completion percentage", example = "85")
    private Integer profileCompletionPercentage;
    
    @Schema(description = "Total charts generated", example = "15")
    private Integer chartsGenerated;
    
    @Schema(description = "Current login streak", example = "7")
    private Integer loginStreak;
    
    @Schema(description = "Total number of logins", example = "142")
    private Integer totalLogins;
    
    @Schema(description = "Subscription type", example = "PREMIUM")
    private String subscriptionType;
    
    @Schema(description = "Subscription active status", example = "true")
    private Boolean subscriptionActive;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Subscription end date")
    private LocalDateTime subscriptionEndDate;
    
    @Schema(description = "Remaining premium credits", example = "25")
    private Integer creditsRemaining;
    
    @Schema(description = "Account locked status", example = "false")
    private Boolean accountLocked;
    
    @Schema(description = "Days since last login", example = "1")
    private Long daysSinceLastLogin;
    
    @Schema(description = "Reports generated count", example = "5")
    private Integer reportsGenerated;
    
    @Schema(description = "Consultations booked count", example = "2")
    private Integer consultationsBooked;
    
    @Schema(description = "Account security score (0-100)", example = "92")
    private Integer securityScore;
    
    @Schema(description = "Two-factor authentication enabled", example = "false")
    private Boolean twoFactorEnabled;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Last password change date")
    private LocalDateTime lastPasswordChange;
    
    // Constructors
    public AccountStatusResponse() {}
    
    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }
    
    public Boolean getEmailVerified() { return emailVerified; }
    public void setEmailVerified(Boolean emailVerified) { this.emailVerified = emailVerified; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public LocalDateTime getMemberSince() { return memberSince; }
    public void setMemberSince(LocalDateTime memberSince) { this.memberSince = memberSince; }
    
    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }
    
    public LocalDateTime getLastActiveDate() { return lastActiveDate; }
    public void setLastActiveDate(LocalDateTime lastActiveDate) { this.lastActiveDate = lastActiveDate; }
    
    public Integer getProfileCompletionPercentage() { return profileCompletionPercentage; }
    public void setProfileCompletionPercentage(Integer profileCompletionPercentage) { 
        this.profileCompletionPercentage = profileCompletionPercentage; 
    }
    
    public Integer getChartsGenerated() { return chartsGenerated; }
    public void setChartsGenerated(Integer chartsGenerated) { this.chartsGenerated = chartsGenerated; }
    
    public Integer getLoginStreak() { return loginStreak; }
    public void setLoginStreak(Integer loginStreak) { this.loginStreak = loginStreak; }
    
    public Integer getTotalLogins() { return totalLogins; }
    public void setTotalLogins(Integer totalLogins) { this.totalLogins = totalLogins; }
    
    public String getSubscriptionType() { return subscriptionType; }
    public void setSubscriptionType(String subscriptionType) { this.subscriptionType = subscriptionType; }
    
    public Boolean getSubscriptionActive() { return subscriptionActive; }
    public void setSubscriptionActive(Boolean subscriptionActive) { this.subscriptionActive = subscriptionActive; }
    
    public LocalDateTime getSubscriptionEndDate() { return subscriptionEndDate; }
    public void setSubscriptionEndDate(LocalDateTime subscriptionEndDate) { 
        this.subscriptionEndDate = subscriptionEndDate; 
    }
    
    public Integer getCreditsRemaining() { return creditsRemaining; }
    public void setCreditsRemaining(Integer creditsRemaining) { this.creditsRemaining = creditsRemaining; }
    
    public Boolean getAccountLocked() { return accountLocked; }
    public void setAccountLocked(Boolean accountLocked) { this.accountLocked = accountLocked; }
    
    public Long getDaysSinceLastLogin() { return daysSinceLastLogin; }
    public void setDaysSinceLastLogin(Long daysSinceLastLogin) { this.daysSinceLastLogin = daysSinceLastLogin; }
    
    public Integer getReportsGenerated() { return reportsGenerated; }
    public void setReportsGenerated(Integer reportsGenerated) { this.reportsGenerated = reportsGenerated; }
    
    public Integer getConsultationsBooked() { return consultationsBooked; }
    public void setConsultationsBooked(Integer consultationsBooked) { this.consultationsBooked = consultationsBooked; }
    
    public Integer getSecurityScore() { return securityScore; }
    public void setSecurityScore(Integer securityScore) { this.securityScore = securityScore; }
    
    public Boolean getTwoFactorEnabled() { return twoFactorEnabled; }
    public void setTwoFactorEnabled(Boolean twoFactorEnabled) { this.twoFactorEnabled = twoFactorEnabled; }
    
    public LocalDateTime getLastPasswordChange() { return lastPasswordChange; }
    public void setLastPasswordChange(LocalDateTime lastPasswordChange) { 
        this.lastPasswordChange = lastPasswordChange; 
    }
    
    // Utility Methods
    @JsonProperty("accountAge")
    public String getAccountAge() {
        if (memberSince == null) return "Unknown";
        
        long days = java.time.temporal.ChronoUnit.DAYS.between(memberSince, LocalDateTime.now());
        if (days < 30) return days + " days";
        if (days < 365) return (days / 30) + " months";
        return (days / 365) + " years";
    }
    
    @JsonProperty("subscriptionStatus")
    public String getSubscriptionStatus() {
        if (!Boolean.TRUE.equals(subscriptionActive)) return "Free";
        if (subscriptionEndDate != null && subscriptionEndDate.isBefore(LocalDateTime.now())) {
            return "Expired";
        }
        return subscriptionType != null ? subscriptionType : "Active";
    }
    
    @JsonProperty("activityLevel")
    public String getActivityLevel() {
        if (daysSinceLastLogin == null) return "New User";
        if (daysSinceLastLogin == 0) return "Very Active";
        if (daysSinceLastLogin <= 7) return "Active";
        if (daysSinceLastLogin <= 30) return "Moderate";
        return "Inactive";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AccountStatusResponse that = (AccountStatusResponse) obj;
        return Objects.equals(username, that.username) && Objects.equals(email, that.email);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(username, email);
    }
    
    @Override
    public String toString() {
        return String.format("AccountStatusResponse{username='%s', email='%s', enabled=%s, subscriptionType='%s'}", 
                           username, email, enabled, subscriptionType);
    }
}
