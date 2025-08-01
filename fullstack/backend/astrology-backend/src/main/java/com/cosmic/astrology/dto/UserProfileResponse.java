package com.cosmic.astrology.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * User Profile Response DTO for complete profile information
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Complete user profile information")
public class UserProfileResponse {
    
    @Schema(description = "User ID", example = "123")
    private Long id;
    
    @Schema(description = "Username", example = "testuser")
    private String username;
    
    @Schema(description = "Email address", example = "user@example.com")
    private String email;
    
    @Schema(description = "First name", example = "John")
    private String firstName;
    
    @Schema(description = "Last name", example = "Doe")
    private String lastName;
    
    @Schema(description = "Full name", example = "John Doe")
    private String fullName;
    
    @Schema(description = "Phone number", example = "+1234567890")
    private String phoneNumber;
    
    @Schema(description = "Gender", example = "Male")
    private String gender;
    
    @Schema(description = "Occupation", example = "Software Engineer")
    private String occupation;
    
    @Schema(description = "Nationality", example = "American")
    private String nationality;
    
    @Schema(description = "User role", example = "CLIENT")
    private String role;
    
    @Schema(description = "Account enabled status", example = "true")
    private Boolean enabled;
    
    @Schema(description = "Email verification status", example = "true")
    private Boolean emailVerified;
    
    @Schema(description = "Profile completion percentage", example = "85")
    private Integer profileCompletionPercentage;
    
    @Schema(description = "Has complete birth data", example = "true")
    private Boolean hasCompleteBirthData;
    
    @Schema(description = "Has generated birth chart", example = "true")
    private Boolean hasGeneratedChart;
    
    @Schema(description = "Subscription type", example = "PREMIUM")
    private String subscriptionType;
    
    @Schema(description = "Subscription active status", example = "true")
    private Boolean subscriptionActive;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Member since date")
    private LocalDateTime memberSince;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Last login date")
    private LocalDateTime lastLogin;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Last updated date")
    private LocalDateTime lastUpdated;
    
    @Schema(description = "Profile picture URL")
    private String profilePictureUrl;
    
    // Astrology fields
    @Schema(description = "Sun sign", example = "Aries")
    private String sunSign;
    
    @Schema(description = "Moon sign", example = "Cancer")
    private String moonSign;
    
    @Schema(description = "Rising sign", example = "Leo")
    private String risingSign;
    
    @Schema(description = "Dominant element", example = "Fire")
    private String dominantElement;
    
    @Schema(description = "Moon Nakshatra", example = "Rohini")
    private String moonNakshatra;
    
    @Schema(description = "Moon Pada", example = "2")
    private Integer moonPada;
    
    // Constructors
    public UserProfileResponse() {}
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    public String getOccupation() { return occupation; }
    public void setOccupation(String occupation) { this.occupation = occupation; }
    
    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }
    
    public Boolean getEmailVerified() { return emailVerified; }
    public void setEmailVerified(Boolean emailVerified) { this.emailVerified = emailVerified; }
    
    public Integer getProfileCompletionPercentage() { return profileCompletionPercentage; }
    public void setProfileCompletionPercentage(Integer profileCompletionPercentage) { 
        this.profileCompletionPercentage = profileCompletionPercentage; 
    }
    
    public Boolean getHasCompleteBirthData() { return hasCompleteBirthData; }
    public void setHasCompleteBirthData(Boolean hasCompleteBirthData) { 
        this.hasCompleteBirthData = hasCompleteBirthData; 
    }
    
    public Boolean getHasGeneratedChart() { return hasGeneratedChart; }
    public void setHasGeneratedChart(Boolean hasGeneratedChart) { 
        this.hasGeneratedChart = hasGeneratedChart; 
    }
    
    public String getSubscriptionType() { return subscriptionType; }
    public void setSubscriptionType(String subscriptionType) { this.subscriptionType = subscriptionType; }
    
    public Boolean getSubscriptionActive() { return subscriptionActive; }
    public void setSubscriptionActive(Boolean subscriptionActive) { this.subscriptionActive = subscriptionActive; }
    
    public LocalDateTime getMemberSince() { return memberSince; }
    public void setMemberSince(LocalDateTime memberSince) { this.memberSince = memberSince; }
    
    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }
    
    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
    
    public String getProfilePictureUrl() { return profilePictureUrl; }
    public void setProfilePictureUrl(String profilePictureUrl) { this.profilePictureUrl = profilePictureUrl; }
    
    public String getSunSign() { return sunSign; }
    public void setSunSign(String sunSign) { this.sunSign = sunSign; }
    
    public String getMoonSign() { return moonSign; }
    public void setMoonSign(String moonSign) { this.moonSign = moonSign; }
    
    public String getRisingSign() { return risingSign; }
    public void setRisingSign(String risingSign) { this.risingSign = risingSign; }
    
    public String getDominantElement() { return dominantElement; }
    public void setDominantElement(String dominantElement) { this.dominantElement = dominantElement; }
    
    public String getMoonNakshatra() { return moonNakshatra; }
    public void setMoonNakshatra(String moonNakshatra) { this.moonNakshatra = moonNakshatra; }
    
    public Integer getMoonPada() { return moonPada; }
    public void setMoonPada(Integer moonPada) { this.moonPada = moonPada; }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UserProfileResponse that = (UserProfileResponse) obj;
        return Objects.equals(id, that.id) && Objects.equals(username, that.username);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }
    
    @Override
    public String toString() {
        return String.format("UserProfileResponse{id=%d, username='%s', email='%s', fullName='%s'}", 
                           id, username, email, fullName);
    }
}
