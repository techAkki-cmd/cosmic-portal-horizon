package com.cosmic.astrology.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;


@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_username", columnList = "username"),
    @Index(name = "idx_email", columnList = "email"),
    @Index(name = "idx_created_at", columnList = "createdAt"),
    @Index(name = "idx_last_login", columnList = "lastLogin"),
    @Index(name = "idx_enabled", columnList = "enabled"),
    @Index(name = "idx_status", columnList = "status"),
    @Index(name = "idx_subscription_type", columnList = "subscriptionType")
})
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    @Column(unique = true, nullable = false, length = 20)
    private String username;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(unique = true, nullable = false, length = 100)
    private String email;
    
    @NotBlank(message = "Password is required")
    @JsonIgnore
    @Column(nullable = false)
    private String password;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role = UserRole.CLIENT;
    
    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true;
    
    @Column(name = "email_verified", nullable = false)
    private Boolean emailVerified = false;
    
    private LocalDateTime emailVerifiedAt;
    
    
    @Size(max = 50, message = "First name cannot exceed 50 characters")
    @Column(length = 50)
    private String firstName;
    
    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    @Column(length = 50)
    private String lastName;
    
    @Size(max = 20, message = "Phone number cannot exceed 20 characters")
    @Column(length = 20)
    private String phoneNumber;
    
    @Column(length = 10)
    private String gender; 
    
    @Column(length = 100)
    private String occupation;
    
    @Column(length = 100)
    private String nationality;

    
    @Size(max = 100, message = "Display name cannot exceed 100 characters")
    @Column(name = "display_name", length = 100)
    private String displayName;

    @Size(max = 20, message = "Status cannot exceed 20 characters")
    @Column(name = "status", length = 20)
    private String status = "ACTIVE";

    @Size(max = 10, message = "Initials cannot exceed 10 characters")
    @Column(name = "initials", length = 10)
    private String initials;

    @Column(name = "account_locked", nullable = false)
    private Boolean accountLocked = false;

    @Column(name = "days_since_last_login")
    private Long daysSinceLastLogin;

    @Column(name = "has_active_subscription", nullable = false)
    private Boolean hasActiveSubscription = false;

z    @Column(length = 500)
    private String profilePictureUrl;

    @Column(length = 100)
    private String profilePictureFileName;

    @Column
    private Long profilePictureFileSize;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime profilePictureUploadedAt;
    
    
    private LocalDateTime birthDateTime;
    
    @Size(max = 200, message = "Birth location cannot exceed 200 characters")
    @Column(length = 200)
    private String birthLocation;
    
    @Column(name = "birth_latitude", precision = 10)
    private Double birthLatitude;

    @Column(name = "birth_longitude", precision = 10)
    private Double birthLongitude;
    
    @Size(max = 50, message = "Timezone cannot exceed 50 characters")
    @Column(length = 50)
    private String timezone;
    
    @Column(length = 100)
    private String birthCity;
    
    @Column(length = 100)
    private String birthState;
    
    @Column(length = 100)
    private String birthCountry;
    
    
    @Column(length = 30)
    private String sunSign;
    
    @Column(length = 30)
    private String moonSign;
    
    @Column(length = 30)
    private String risingSign;
    
    @Column(length = 30)
    private String lagnaSign; 
    
    @Column(length = 20)
    private String dominantElement; 
    
    @Column(length = 50)
    private String moonNakshatra;
    
    private Integer moonPada;
    
    @Column(name = "ayanamsa", columnDefinition = "DECIMAL(8,4)")
    private Double ayanamsa;
    
    @Lob
    @Column(columnDefinition = "TEXT")
    private String natalChart; /
    
    @Lob
    @Column(columnDefinition = "TEXT")
    private String natalHouses;
    
    @Lob
    @Column(columnDefinition = "TEXT")
    private String natalAspects; 
    
    private LocalDateTime chartCalculatedAt;
    
    @Column(name = "chart_calculated", nullable = false)
    private Boolean chartCalculated = false;
    
    @Column(length = 50)
    private String calculationAccuracy; 
    
    
    @Column(length = 20)
    private String preferredLanguage = "en";
    
    @Column(length = 10)
    private String timeFormat = "12h"; 
    
    @Column(length = 10)
    private String dateFormat = "MM/dd/yyyy";
    
    @Column(name = "email_notifications", nullable = false)
    private Boolean emailNotifications = true;
    
    @Column(name = "daily_horoscope", nullable = false)
    private Boolean dailyHoroscope = true;
    
    @Column(name = "transit_alerts", nullable = false)
    private Boolean transitAlerts = true;
    
    
    @Column(nullable = false)
    private Integer chartsGenerated = 0;
    
    @Column(nullable = false)
    private Integer loginStreak = 0;
    
    @Column(nullable = false)
    private Integer totalLogins = 0;
    
    private Integer consultationsBooked = 0;
    
    private Integer reportsGenerated = 0;
    
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private LocalDateTime lastLogin;
    
    private LocalDateTime lastLogout;
    
    private LocalDateTime lastActiveDate;
    
    @Column(length = 45)
    private String lastLoginIp;
    
    @Column(length = 500)
    private String lastUserAgent;
    
    private LocalDateTime passwordChangedAt;
    
    private Integer failedLoginAttempts = 0;
    
    private LocalDateTime accountLockedAt;
    
    private LocalDateTime accountUnlockedAt;
    
    
    @Column(length = 20)
    private String subscriptionType = "FREE"; 
    
    private LocalDateTime subscriptionStartDate;
    
    private LocalDateTime subscriptionEndDate;
    
    @Column(name = "subscription_active", nullable = false)
    private Boolean subscriptionActive = false;
    
    private Integer creditsRemaining = 0;
    
    
    @Column(nullable = false)
    private Integer profileCompletionPercentage = 0;
    
    @Column(name = "has_complete_birth_data", nullable = false)
    private Boolean hasCompleteBirthData = false;
    
    @Column(name = "has_generated_chart", nullable = false)
    private Boolean hasGeneratedChart = false;
    
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        calculateProfileCompletion();
        updateDynamicFields();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        calculateProfileCompletion();
        checkBirthDataCompletion();
        updateDynamicFields();
    }
    
    
    public User() {}
    
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
    
    public User(String username, String email, String password, String firstName, String lastName) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    @JsonIgnore
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
    
    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { 
        this.enabled = enabled != null ? enabled : true;
    }
    public boolean isEnabled() { 
        return enabled != null && enabled;
    }
    
    public Boolean getEmailVerified() { return emailVerified; }
    public void setEmailVerified(Boolean emailVerified) { 
        this.emailVerified = emailVerified != null ? emailVerified : false;
    }
    public boolean isEmailVerified() { 
        return emailVerified != null && emailVerified;
    }
    
    public LocalDateTime getEmailVerifiedAt() { return emailVerifiedAt; }
    public void setEmailVerifiedAt(LocalDateTime emailVerifiedAt) { this.emailVerifiedAt = emailVerifiedAt; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    public String getOccupation() { return occupation; }
    public void setOccupation(String occupation) { this.occupation = occupation; }
    
    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }
    
    
    public String getDisplayName() { 
        if (displayName != null && !displayName.trim().isEmpty()) {
            return displayName;
        }
        return getFullName();
    }

    public void setDisplayName(String displayName) { 
        this.displayName = displayName != null ? displayName.trim() : null;
    }

    public String getStatus() { 
        return status != null ? status : "ACTIVE";
    }

    public void setStatus(String status) { 
        this.status = status != null ? status.trim().toUpperCase() : "ACTIVE";
    }

    public String getInitials() {
        if (initials != null && !initials.trim().isEmpty()) {
            return initials;
        }
        StringBuilder sb = new StringBuilder();
        if (firstName != null && !firstName.trim().isEmpty()) {
            sb.append(firstName.trim().charAt(0));
        }
        if (lastName != null && !lastName.trim().isEmpty()) {
            sb.append(lastName.trim().charAt(0));
        }
        if (sb.length() == 0 && username != null) {
            sb.append(username.charAt(0));
        }
        return sb.toString().toUpperCase();
    }

    public void setInitials(String initials) {
        this.initials = initials != null ? initials.trim().toUpperCase() : null;
    }

    public Boolean getAccountLocked() { return accountLocked; }
    public void setAccountLocked(Boolean accountLocked) { 
        this.accountLocked = accountLocked != null ? accountLocked : false;
        if (accountLocked != null && accountLocked) {
            this.accountLockedAt = LocalDateTime.now();
        }
    }
    public boolean isAccountLocked() { 
        return (accountLocked != null && accountLocked) || 
               (accountLockedAt != null && 
                (accountUnlockedAt == null || accountLockedAt.isAfter(accountUnlockedAt)));
    }

    public Long getDaysSinceLastLogin() { 
        if (lastLogin == null) return null;
        return ChronoUnit.DAYS.between(lastLogin, LocalDateTime.now());
    }

    public void setDaysSinceLastLogin(Long daysSinceLastLogin) { 
        this.daysSinceLastLogin = daysSinceLastLogin;
    }

    public Boolean getHasActiveSubscription() { return hasActiveSubscription; }
    public void setHasActiveSubscription(Boolean hasActiveSubscription) { 
        this.hasActiveSubscription = hasActiveSubscription != null ? hasActiveSubscription : false;
    }
    public boolean hasActiveSubscription() { 
        return (hasActiveSubscription != null && hasActiveSubscription) && 
               subscriptionEndDate != null && 
               subscriptionEndDate.isAfter(LocalDateTime.now());
    }
    
    public String getProfilePictureUrl() { return profilePictureUrl; }
    public void setProfilePictureUrl(String profilePictureUrl) { this.profilePictureUrl = profilePictureUrl; }
    
    public String getProfilePictureFileName() { return profilePictureFileName; }
    public void setProfilePictureFileName(String profilePictureFileName) { this.profilePictureFileName = profilePictureFileName; }
    
    public Long getProfilePictureFileSize() { return profilePictureFileSize; }
    public void setProfilePictureFileSize(Long profilePictureFileSize) { this.profilePictureFileSize = profilePictureFileSize; }
    
    public LocalDateTime getProfilePictureUploadedAt() { return profilePictureUploadedAt; }
    public void setProfilePictureUploadedAt(LocalDateTime profilePictureUploadedAt) { this.profilePictureUploadedAt = profilePictureUploadedAt; }
    
    
    public LocalDateTime getBirthDateTime() { return birthDateTime; }
    public void setBirthDateTime(LocalDateTime birthDateTime) { this.birthDateTime = birthDateTime; }
    
    public String getBirthLocation() { return birthLocation; }
    public void setBirthLocation(String birthLocation) { this.birthLocation = birthLocation; }
    
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
    
    
    public String getSunSign() { return sunSign; }
    public void setSunSign(String sunSign) { this.sunSign = sunSign; }
    
    public String getMoonSign() { return moonSign; }
    public void setMoonSign(String moonSign) { this.moonSign = moonSign; }
    
    public String getRisingSign() { return risingSign; }
    public void setRisingSign(String risingSign) { this.risingSign = risingSign; }
    
    public String getLagnaSign() { return lagnaSign; }
    public void setLagnaSign(String lagnaSign) { this.lagnaSign = lagnaSign; }
    
    public String getDominantElement() { return dominantElement; }
    public void setDominantElement(String dominantElement) { this.dominantElement = dominantElement; }
    
    public String getMoonNakshatra() { return moonNakshatra; }
    public void setMoonNakshatra(String moonNakshatra) { this.moonNakshatra = moonNakshatra; }
    
    public Integer getMoonPada() { return moonPada; }
    public void setMoonPada(Integer moonPada) { this.moonPada = moonPada; }
    
    public Double getAyanamsa() { return ayanamsa; }
    public void setAyanamsa(Double ayanamsa) { this.ayanamsa = ayanamsa; }
    
    public String getNatalChart() { return natalChart; }
    public void setNatalChart(String natalChart) { this.natalChart = natalChart; }
    
    public String getNatalHouses() { return natalHouses; }
    public void setNatalHouses(String natalHouses) { this.natalHouses = natalHouses; }
    
    public String getNatalAspects() { return natalAspects; }
    public void setNatalAspects(String natalAspects) { this.natalAspects = natalAspects; }
    
    public LocalDateTime getChartCalculatedAt() { return chartCalculatedAt; }
    public void setChartCalculatedAt(LocalDateTime chartCalculatedAt) { this.chartCalculatedAt = chartCalculatedAt; }
    
    public Boolean getChartCalculated() { return chartCalculated; }
    public void setChartCalculated(Boolean chartCalculated) { 
        this.chartCalculated = chartCalculated != null ? chartCalculated : false;
    }
    public boolean isChartCalculated() { 
        return chartCalculated != null && chartCalculated;
    }
    
    public String getCalculationAccuracy() { return calculationAccuracy; }
    public void setCalculationAccuracy(String calculationAccuracy) { this.calculationAccuracy = calculationAccuracy; }
    
    
    public String getPreferredLanguage() { return preferredLanguage; }
    public void setPreferredLanguage(String preferredLanguage) { this.preferredLanguage = preferredLanguage; }
    
    public String getTimeFormat() { return timeFormat; }
    public void setTimeFormat(String timeFormat) { this.timeFormat = timeFormat; }
    
    public String getDateFormat() { return dateFormat; }
    public void setDateFormat(String dateFormat) { this.dateFormat = dateFormat; }
    
    public Boolean getEmailNotifications() { return emailNotifications; }
    public void setEmailNotifications(Boolean emailNotifications) { 
        this.emailNotifications = emailNotifications != null ? emailNotifications : true;
    }
    public boolean isEmailNotifications() { 
        return emailNotifications != null && emailNotifications;
    }
    
    public Boolean getDailyHoroscope() { return dailyHoroscope; }
    public void setDailyHoroscope(Boolean dailyHoroscope) { 
        this.dailyHoroscope = dailyHoroscope != null ? dailyHoroscope : true;
    }
    public boolean isDailyHoroscope() { 
        return dailyHoroscope != null && dailyHoroscope;
    }
    
    public Boolean getTransitAlerts() { return transitAlerts; }
    public void setTransitAlerts(Boolean transitAlerts) { 
        this.transitAlerts = transitAlerts != null ? transitAlerts : true;
    }
    public boolean isTransitAlerts() { 
        return transitAlerts != null && transitAlerts;
    }
    
    
    public Integer getChartsGenerated() { return chartsGenerated; }
    public void setChartsGenerated(Integer chartsGenerated) { this.chartsGenerated = chartsGenerated; }
    
    public Integer getLoginStreak() { return loginStreak; }
    public void setLoginStreak(Integer loginStreak) { this.loginStreak = loginStreak; }
    
    public Integer getTotalLogins() { return totalLogins; }
    public void setTotalLogins(Integer totalLogins) { this.totalLogins = totalLogins; }
    
    public Integer getConsultationsBooked() { return consultationsBooked; }
    public void setConsultationsBooked(Integer consultationsBooked) { this.consultationsBooked = consultationsBooked; }
    
    public Integer getReportsGenerated() { return reportsGenerated; }
    public void setReportsGenerated(Integer reportsGenerated) { this.reportsGenerated = reportsGenerated; }
    
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }
    
    public LocalDateTime getLastLogout() { return lastLogout; }
    public void setLastLogout(LocalDateTime lastLogout) { this.lastLogout = lastLogout; }
    
    public LocalDateTime getLastActiveDate() { return lastActiveDate; }
    public void setLastActiveDate(LocalDateTime lastActiveDate) { this.lastActiveDate = lastActiveDate; }
    
    public String getLastLoginIp() { return lastLoginIp; }
    public void setLastLoginIp(String lastLoginIp) { this.lastLoginIp = lastLoginIp; }
    
    public String getLastUserAgent() { return lastUserAgent; }
    public void setLastUserAgent(String lastUserAgent) { this.lastUserAgent = lastUserAgent; }
    
    public LocalDateTime getPasswordChangedAt() { return passwordChangedAt; }
    public void setPasswordChangedAt(LocalDateTime passwordChangedAt) { this.passwordChangedAt = passwordChangedAt; }
    
    public Integer getFailedLoginAttempts() { return failedLoginAttempts; }
    public void setFailedLoginAttempts(Integer failedLoginAttempts) { this.failedLoginAttempts = failedLoginAttempts; }
    
    public LocalDateTime getAccountLockedAt() { return accountLockedAt; }
    public void setAccountLockedAt(LocalDateTime accountLockedAt) { this.accountLockedAt = accountLockedAt; }
    
    public LocalDateTime getAccountUnlockedAt() { return accountUnlockedAt; }
    public void setAccountUnlockedAt(LocalDateTime accountUnlockedAt) { this.accountUnlockedAt = accountUnlockedAt; }
    
    
    public String getSubscriptionType() { return subscriptionType; }
    public void setSubscriptionType(String subscriptionType) { this.subscriptionType = subscriptionType; }
    
    public LocalDateTime getSubscriptionStartDate() { return subscriptionStartDate; }
    public void setSubscriptionStartDate(LocalDateTime subscriptionStartDate) { this.subscriptionStartDate = subscriptionStartDate; }
    
    public LocalDateTime getSubscriptionEndDate() { return subscriptionEndDate; }
    public void setSubscriptionEndDate(LocalDateTime subscriptionEndDate) { this.subscriptionEndDate = subscriptionEndDate; }
    
    public Boolean getSubscriptionActive() { return subscriptionActive; }
    public void setSubscriptionActive(Boolean subscriptionActive) { 
        this.subscriptionActive = subscriptionActive != null ? subscriptionActive : false;
    }
    public boolean isSubscriptionActive() { 
        return subscriptionActive != null && subscriptionActive;
    }
    
    public Integer getCreditsRemaining() { return creditsRemaining; }
    public void setCreditsRemaining(Integer creditsRemaining) { this.creditsRemaining = creditsRemaining; }
    
    
    public Integer getProfileCompletionPercentage() { return profileCompletionPercentage; }
    public void setProfileCompletionPercentage(Integer profileCompletionPercentage) { this.profileCompletionPercentage = profileCompletionPercentage; }
    
    public Boolean getHasCompleteBirthData() { return hasCompleteBirthData; }
    public void setHasCompleteBirthData(Boolean hasCompleteBirthData) { 
        this.hasCompleteBirthData = hasCompleteBirthData != null ? hasCompleteBirthData : false;
    }
    public boolean isHasCompleteBirthData() { 
        return hasCompleteBirthData != null && hasCompleteBirthData;
    }
    
    public Boolean getHasGeneratedChart() { return hasGeneratedChart; }
    public void setHasGeneratedChart(Boolean hasGeneratedChart) { 
        this.hasGeneratedChart = hasGeneratedChart != null ? hasGeneratedChart : false;
    }
    public boolean isHasGeneratedChart() { 
        return hasGeneratedChart != null && hasGeneratedChart;
    }
    
    
    @JsonProperty("fullName")
    public String getFullName() {
        if (firstName == null && lastName == null) {
            return username;
        }
        if (firstName == null) {
            return lastName;
        }
        if (lastName == null) {
            return firstName;
        }
        return firstName + " " + lastName;
    }
    
    public boolean hasCompleteBirthDataForChart() {
        return birthDateTime != null && 
               birthLatitude != null && 
               birthLongitude != null &&
               birthLocation != null && 
               !birthLocation.trim().isEmpty();
    }
    
    @JsonProperty("cosmicProfileSummary")
    public String getCosmicProfileSummary() {
        if (sunSign != null || moonSign != null || risingSign != null) {
            StringBuilder profile = new StringBuilder();
            if (sunSign != null) profile.append("Sun: ").append(sunSign);
            if (moonSign != null) {
                if (profile.length() > 0) profile.append(", ");
                profile.append("Moon: ").append(moonSign);
            }
            if (risingSign != null) {
                if (profile.length() > 0) profile.append(", ");
                profile.append("Rising: ").append(risingSign);
            }
            return profile.toString();
        }
        return "Chart not calculated";
    }
    
    @JsonProperty("membershipDurationInDays")
    public long getMembershipDurationInDays() {
        return createdAt != null ? ChronoUnit.DAYS.between(createdAt, LocalDateTime.now()) : 0;
    }
    
    @JsonProperty("isNewUser")
    public boolean isNewUser() {
        return getMembershipDurationInDays() <= 7;
    }
    
    @JsonProperty("userTier")
    public String getUserTier() {
        if (hasActiveSubscription()) {
            if (chartsGenerated != null && chartsGenerated >= 50) return "Master Astrologer";
            if (chartsGenerated != null && chartsGenerated >= 20) return "Advanced Seeker";
            return "Premium Member";
        } else {
            if (chartsGenerated != null && chartsGenerated >= 20) return "Dedicated Explorer";
            if (chartsGenerated != null && chartsGenerated >= 5) return "Active Seeker";
            return "Cosmic Novice";
        }
    }
    
    public void resetChartData() {
        this.chartCalculated = false;
        this.chartCalculatedAt = null;
        this.natalChart = null;  
        this.natalHouses = null;
        this.natalAspects = null;
        this.sunSign = null;
        this.moonSign = null;
        this.risingSign = null;
        this.dominantElement = null;
        this.moonNakshatra = null;
        this.moonPada = null;
        this.ayanamsa = null;
        this.calculationAccuracy = null;
        this.hasGeneratedChart = false;
    }
    
    private void calculateProfileCompletion() {
        int totalFields = 12;
        int completedFields = 0;
        
        if (firstName != null && !firstName.trim().isEmpty()) completedFields++;
        if (lastName != null && !lastName.trim().isEmpty()) completedFields++;
        if (phoneNumber != null && !phoneNumber.trim().isEmpty()) completedFields++;
        if (gender != null && !gender.trim().isEmpty()) completedFields++;
        if (birthDateTime != null) completedFields++;
        if (birthLocation != null && !birthLocation.trim().isEmpty()) completedFields++;
        if (birthLatitude != null && birthLongitude != null) completedFields++;
        if (timezone != null && !timezone.trim().isEmpty()) completedFields++;
        if (occupation != null && !occupation.trim().isEmpty()) completedFields++;
        if (nationality != null && !nationality.trim().isEmpty()) completedFields++;
        if (emailVerified != null && emailVerified) completedFields++;
        if (profilePictureUrl != null) completedFields++;
        
        this.profileCompletionPercentage = (completedFields * 100) / totalFields;
    }
    
    private void checkBirthDataCompletion() {
        this.hasCompleteBirthData = hasCompleteBirthDataForChart();
    }
    
    private void updateDynamicFields() {
        this.daysSinceLastLogin = getDaysSinceLastLogin();
        this.hasActiveSubscription = hasActiveSubscription();
        
        if (this.displayName == null || this.displayName.trim().isEmpty()) {
            this.displayName = getFullName();
        }
        
        if (this.initials == null || this.initials.trim().isEmpty()) {
            this.initials = getInitials();
        }
    }
    
    public void incrementChartsGenerated() {
        this.chartsGenerated = (this.chartsGenerated == null) ? 1 : this.chartsGenerated + 1;
        this.hasGeneratedChart = true;
        this.chartCalculatedAt = LocalDateTime.now();
    }
    
    public void incrementLoginStats() {
        this.totalLogins = (this.totalLogins == null) ? 1 : this.totalLogins + 1;
        
        if (lastLogin != null && 
            ChronoUnit.HOURS.between(lastLogin, LocalDateTime.now()) <= 24) {
            this.loginStreak = (this.loginStreak == null) ? 1 : this.loginStreak + 1;
        } else {
            this.loginStreak = 1;
        }
        
        this.lastLogin = LocalDateTime.now();
        this.lastActiveDate = LocalDateTime.now();
    }
    
    public void updateLastActiveDate() {
        this.lastActiveDate = LocalDateTime.now();
    }
    
    public void lockAccount(String reason) {
        this.accountLocked = true;
        this.accountLockedAt = LocalDateTime.now();
        this.accountUnlockedAt = null;
    }
    
    public void unlockAccount() {
        this.accountLocked = false;
        this.accountUnlockedAt = LocalDateTime.now();
        this.failedLoginAttempts = 0;
    }
    
    public void incrementFailedLoginAttempts() {
        this.failedLoginAttempts = (this.failedLoginAttempts == null) ? 1 : this.failedLoginAttempts + 1;
    }
    
    public void resetFailedLoginAttempts() {
        this.failedLoginAttempts = 0;
    }
    
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        User user = (User) obj;
        return Objects.equals(id, user.id) && 
               Objects.equals(username, user.username) && 
               Objects.equals(email, user.email);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, username, email);
    }
    
    @Override
    public String toString() {
        return String.format("User{id=%d, username='%s', email='%s', role=%s, enabled=%s, status='%s', profileCompletion=%d%%, tier='%s'}",
                           id, username, email, role, isEnabled(), status, profileCompletionPercentage, getUserTier());
    }
}
