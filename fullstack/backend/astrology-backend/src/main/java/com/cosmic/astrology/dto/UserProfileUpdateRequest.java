package com.cosmic.astrology.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * User Profile Update Request DTO for comprehensive profile updates
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "User profile update request with comprehensive validation")
public class UserProfileUpdateRequest {
    
    // ================ BASIC PERSONAL INFORMATION ================
    
    @Size(min = 1, max = 50, message = "First name must be between 1 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s'-]+$", message = "First name can only contain letters, spaces, hyphens, and apostrophes")
    @Schema(description = "User's first name", example = "John")
    private String firstName;
    
    @Size(min = 1, max = 50, message = "Last name must be between 1 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s'-]+$", message = "Last name can only contain letters, spaces, hyphens, and apostrophes")
    @Schema(description = "User's last name", example = "Doe")
    private String lastName;
    
    @Email(message = "Please provide a valid email address")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    @Schema(description = "User's email address", example = "john.doe@example.com")
    private String email;
    
    @Pattern(regexp = "^[+]?[1-9]\\d{1,14}$", message = "Please provide a valid phone number")
    @Schema(description = "User's phone number with country code", example = "+1234567890")
    private String phoneNumber;
    
    @Pattern(regexp = "^(Male|Female|Other|Prefer not to say)$", message = "Gender must be Male, Female, Other, or Prefer not to say")
    @Schema(description = "User's gender", example = "Male", allowableValues = {"Male", "Female", "Other", "Prefer not to say"})
    private String gender;
    
    @Size(max = 100, message = "Occupation cannot exceed 100 characters")
    @Schema(description = "User's occupation or profession", example = "Software Engineer")
    private String occupation;
    
    @Size(max = 100, message = "Nationality cannot exceed 100 characters")
    @Schema(description = "User's nationality", example = "American")
    private String nationality;
    
    @Size(max = 500, message = "Bio cannot exceed 500 characters")
    @Schema(description = "User's personal bio or description", example = "Passionate about Vedic astrology and spiritual growth")
    private String bio;
    
    // ================ CONTACT AND LOCATION ================
    
    @Size(max = 200, message = "Address cannot exceed 200 characters")
    @Schema(description = "User's current address", example = "123 Main St, New York, NY 10001")
    private String currentAddress;
    
    @Size(max = 100, message = "City cannot exceed 100 characters")
    @Schema(description = "User's current city", example = "New York")
    private String currentCity;
    
    @Size(max = 100, message = "State cannot exceed 100 characters")
    @Schema(description = "User's current state/province", example = "New York")
    private String currentState;
    
    @Size(max = 100, message = "Country cannot exceed 100 characters")
    @Schema(description = "User's current country", example = "United States")
    private String currentCountry;
    
    @Pattern(regexp = "^\\d{5}(-\\d{4})?$", message = "Please provide a valid ZIP code")
    @Schema(description = "User's ZIP/postal code", example = "10001")
    private String zipCode;
    
    // ================ PREFERENCES AND SETTINGS ================
    
    @Pattern(regexp = "^(en|es|fr|de|hi|sa)$", message = "Language must be en, es, fr, de, hi, or sa")
    @Schema(description = "User's preferred language", example = "en", allowableValues = {"en", "es", "fr", "de", "hi", "sa"})
    private String preferredLanguage;
    
    @Pattern(regexp = "^(12h|24h)$", message = "Time format must be 12h or 24h")
    @Schema(description = "User's preferred time format", example = "12h", allowableValues = {"12h", "24h"})
    private String timeFormat;
    
    @Pattern(regexp = "^(MM/dd/yyyy|dd/MM/yyyy|yyyy-MM-dd)$", message = "Date format must be MM/dd/yyyy, dd/MM/yyyy, or yyyy-MM-dd")
    @Schema(description = "User's preferred date format", example = "MM/dd/yyyy", allowableValues = {"MM/dd/yyyy", "dd/MM/yyyy", "yyyy-MM-dd"})
    private String dateFormat;
    
    @Size(max = 50, message = "Timezone cannot exceed 50 characters")
    @Schema(description = "User's timezone", example = "America/New_York")
    private String timezone;
    
    @Pattern(regexp = "^(LIGHT|DARK|AUTO)$", message = "Theme must be LIGHT, DARK, or AUTO")
    @Schema(description = "User's UI theme preference", example = "DARK", allowableValues = {"LIGHT", "DARK", "AUTO"})
    private String theme;
    
    // ================ NOTIFICATION PREFERENCES ================
    
    @Schema(description = "Enable email notifications", example = "true")
    private Boolean emailNotifications;
    
    @Schema(description = "Enable daily horoscope notifications", example = "true")
    private Boolean dailyHoroscope;
    
    @Schema(description = "Enable transit alert notifications", example = "false")
    private Boolean transitAlerts;
    
    @Schema(description = "Enable marketing email notifications", example = "false")
    private Boolean marketingEmails;
    
    @Schema(description = "Enable push notifications", example = "true")
    private Boolean pushNotifications;
    
    @Schema(description = "Enable SMS notifications", example = "false")
    private Boolean smsNotifications;
    
    // ================ PRIVACY SETTINGS ================
    
    @Pattern(regexp = "^(PUBLIC|FRIENDS|PRIVATE)$", message = "Privacy level must be PUBLIC, FRIENDS, or PRIVATE")
    @Schema(description = "Profile privacy level", example = "PRIVATE", allowableValues = {"PUBLIC", "FRIENDS", "PRIVATE"})
    private String privacyLevel;
    
    @Schema(description = "Show birth data publicly", example = "false")
    private Boolean publicBirthData;
    
    @Schema(description = "Allow friend requests", example = "true")
    private Boolean allowFriendRequests;
    
    @Schema(description = "Show online status to others", example = "true")
    private Boolean showOnlineStatus;
    
    @Schema(description = "Allow search engines to index profile", example = "false")
    private Boolean searchableProfile;
    
    // ================ ASTROLOGICAL PREFERENCES ================
    
    @Pattern(regexp = "^(Lahiri|Raman|KP|Tropical)$", message = "Ayanamsa must be Lahiri, Raman, KP, or Tropical")
    @Schema(description = "Preferred ayanamsa system", example = "Lahiri", allowableValues = {"Lahiri", "Raman", "KP", "Tropical"})
    private String preferredAyanamsa;
    
    @Pattern(regexp = "^(Vedic|Western|Both)$", message = "Chart style must be Vedic, Western, or Both")
    @Schema(description = "Preferred chart calculation style", example = "Vedic", allowableValues = {"Vedic", "Western", "Both"})
    private String chartStyle;
    
    @Pattern(regexp = "^(North|South|East|Bengal)$", message = "Chart format must be North, South, East, or Bengal")
    @Schema(description = "Preferred chart display format", example = "North", allowableValues = {"North", "South", "East", "Bengal"})
    private String chartFormat;
    
    @Schema(description = "Include Uranus, Neptune, Pluto in calculations", example = "false")
    private Boolean includeOuterPlanets;
    
    @Schema(description = "Show Nakshatras in charts", example = "true")
    private Boolean showNakshatras;
    
    @Schema(description = "Show planetary aspects", example = "true")
    private Boolean showAspects;
    
    // ================ PROFESSIONAL INFORMATION ================
    
    @Size(max = 100, message = "Company name cannot exceed 100 characters")
    @Schema(description = "Company or organization name", example = "Tech Solutions Inc.")
    private String companyName;
    
    @Size(max = 100, message = "Job title cannot exceed 100 characters")
    @Schema(description = "Current job title", example = "Senior Software Engineer")
    private String jobTitle;
    
    @Size(max = 50, message = "Industry cannot exceed 50 characters")
    @Schema(description = "Industry or field of work", example = "Technology")
    private String industry;
    
    @Size(max = 100, message = "Education cannot exceed 100 characters")
    @Schema(description = "Highest education level", example = "Master's in Computer Science")
    private String education;
    
    @Size(max = 200, message = "Website URL cannot exceed 200 characters")
    @Pattern(regexp = "^(https?://)?(www\\.)?[a-zA-Z0-9-]+\\.[a-zA-Z]{2,}(/.*)?$", 
             message = "Please provide a valid website URL")
    @Schema(description = "Personal or professional website", example = "https://johndoe.com")
    private String website;
    
    // ================ SOCIAL MEDIA LINKS ================
    
    @Size(max = 100, message = "LinkedIn URL cannot exceed 100 characters")
    @Schema(description = "LinkedIn profile URL", example = "https://linkedin.com/in/johndoe")
    private String linkedinUrl;
    
    @Size(max = 100, message = "Twitter handle cannot exceed 100 characters")
    @Schema(description = "Twitter/X handle", example = "@johndoe")
    private String twitterHandle;
    
    @Size(max = 100, message = "Instagram handle cannot exceed 100 characters")
    @Schema(description = "Instagram handle", example = "@johndoe")
    private String instagramHandle;
    
    // ================ EMERGENCY CONTACT ================
    
    @Size(max = 100, message = "Emergency contact name cannot exceed 100 characters")
    @Schema(description = "Emergency contact person name", example = "Jane Doe")
    private String emergencyContactName;
    
    @Pattern(regexp = "^[+]?[1-9]\\d{1,14}$", message = "Please provide a valid emergency contact phone")
    @Schema(description = "Emergency contact phone number", example = "+1234567891")
    private String emergencyContactPhone;
    
    @Size(max = 50, message = "Emergency contact relationship cannot exceed 50 characters")
    @Schema(description = "Relationship to emergency contact", example = "Spouse")
    private String emergencyContactRelationship;
    
    // ================ UPDATE METADATA ================
    
    @Schema(description = "Reason for profile update", example = "Updated contact information")
    private String updateReason;
    
    @Schema(description = "Request verification after email change", example = "true")
    private Boolean requestEmailVerification;
    
    @Schema(description = "Send update confirmation email", example = "true")
    private Boolean sendUpdateConfirmation;
    
    // ================ CONSTRUCTORS ================
    
    public UserProfileUpdateRequest() {}
    
    // ================ GETTERS AND SETTERS ================
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    public String getOccupation() { return occupation; }
    public void setOccupation(String occupation) { this.occupation = occupation; }
    
    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }
    
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    
    public String getCurrentAddress() { return currentAddress; }
    public void setCurrentAddress(String currentAddress) { this.currentAddress = currentAddress; }
    
    public String getCurrentCity() { return currentCity; }
    public void setCurrentCity(String currentCity) { this.currentCity = currentCity; }
    
    public String getCurrentState() { return currentState; }
    public void setCurrentState(String currentState) { this.currentState = currentState; }
    
    public String getCurrentCountry() { return currentCountry; }
    public void setCurrentCountry(String currentCountry) { this.currentCountry = currentCountry; }
    
    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }
    
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
    
    public Boolean getSmsNotifications() { return smsNotifications; }
    public void setSmsNotifications(Boolean smsNotifications) { this.smsNotifications = smsNotifications; }
    
    public String getPrivacyLevel() { return privacyLevel; }
    public void setPrivacyLevel(String privacyLevel) { this.privacyLevel = privacyLevel; }
    
    public Boolean getPublicBirthData() { return publicBirthData; }
    public void setPublicBirthData(Boolean publicBirthData) { this.publicBirthData = publicBirthData; }
    
    public Boolean getAllowFriendRequests() { return allowFriendRequests; }
    public void setAllowFriendRequests(Boolean allowFriendRequests) { this.allowFriendRequests = allowFriendRequests; }
    
    public Boolean getShowOnlineStatus() { return showOnlineStatus; }
    public void setShowOnlineStatus(Boolean showOnlineStatus) { this.showOnlineStatus = showOnlineStatus; }
    
    public Boolean getSearchableProfile() { return searchableProfile; }
    public void setSearchableProfile(Boolean searchableProfile) { this.searchableProfile = searchableProfile; }
    
    public String getPreferredAyanamsa() { return preferredAyanamsa; }
    public void setPreferredAyanamsa(String preferredAyanamsa) { this.preferredAyanamsa = preferredAyanamsa; }
    
    public String getChartStyle() { return chartStyle; }
    public void setChartStyle(String chartStyle) { this.chartStyle = chartStyle; }
    
    public String getChartFormat() { return chartFormat; }
    public void setChartFormat(String chartFormat) { this.chartFormat = chartFormat; }
    
    public Boolean getIncludeOuterPlanets() { return includeOuterPlanets; }
    public void setIncludeOuterPlanets(Boolean includeOuterPlanets) { this.includeOuterPlanets = includeOuterPlanets; }
    
    public Boolean getShowNakshatras() { return showNakshatras; }
    public void setShowNakshatras(Boolean showNakshatras) { this.showNakshatras = showNakshatras; }
    
    public Boolean getShowAspects() { return showAspects; }
    public void setShowAspects(Boolean showAspects) { this.showAspects = showAspects; }
    
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    
    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
    
    public String getIndustry() { return industry; }
    public void setIndustry(String industry) { this.industry = industry; }
    
    public String getEducation() { return education; }
    public void setEducation(String education) { this.education = education; }
    
    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }
    
    public String getLinkedinUrl() { return linkedinUrl; }
    public void setLinkedinUrl(String linkedinUrl) { this.linkedinUrl = linkedinUrl; }
    
    public String getTwitterHandle() { return twitterHandle; }
    public void setTwitterHandle(String twitterHandle) { this.twitterHandle = twitterHandle; }
    
    public String getInstagramHandle() { return instagramHandle; }
    public void setInstagramHandle(String instagramHandle) { this.instagramHandle = instagramHandle; }
    
    public String getEmergencyContactName() { return emergencyContactName; }
    public void setEmergencyContactName(String emergencyContactName) { this.emergencyContactName = emergencyContactName; }
    
    public String getEmergencyContactPhone() { return emergencyContactPhone; }
    public void setEmergencyContactPhone(String emergencyContactPhone) { this.emergencyContactPhone = emergencyContactPhone; }
    
    public String getEmergencyContactRelationship() { return emergencyContactRelationship; }
    public void setEmergencyContactRelationship(String emergencyContactRelationship) { 
        this.emergencyContactRelationship = emergencyContactRelationship; 
    }
    
    public String getUpdateReason() { return updateReason; }
    public void setUpdateReason(String updateReason) { this.updateReason = updateReason; }
    
    public Boolean getRequestEmailVerification() { return requestEmailVerification; }
    public void setRequestEmailVerification(Boolean requestEmailVerification) { 
        this.requestEmailVerification = requestEmailVerification; 
    }
    
    public Boolean getSendUpdateConfirmation() { return sendUpdateConfirmation; }
    public void setSendUpdateConfirmation(Boolean sendUpdateConfirmation) { 
        this.sendUpdateConfirmation = sendUpdateConfirmation; 
    }
    
    // ================ UTILITY METHODS ================
    
    /**
     * Check if any personal information fields are being updated
     */
    public boolean hasPersonalInfoUpdates() {
        return firstName != null || lastName != null || phoneNumber != null || 
               gender != null || occupation != null || nationality != null || bio != null;
    }
    
    /**
     * Check if any notification preferences are being updated
     */
    public boolean hasNotificationUpdates() {
        return emailNotifications != null || dailyHoroscope != null || transitAlerts != null ||
               marketingEmails != null || pushNotifications != null || smsNotifications != null;
    }
    
    /**
     * Check if any privacy settings are being updated
     */
    public boolean hasPrivacyUpdates() {
        return privacyLevel != null || publicBirthData != null || allowFriendRequests != null ||
               showOnlineStatus != null || searchableProfile != null;
    }
    
    /**
     * Check if any astrological preferences are being updated
     */
    public boolean hasAstrologicalUpdates() {
        return preferredAyanamsa != null || chartStyle != null || chartFormat != null ||
               includeOuterPlanets != null || showNakshatras != null || showAspects != null;
    }
    
    /**
     * Check if email is being changed (requires verification)
     */
    public boolean isEmailBeingChanged() {
        return email != null && !email.trim().isEmpty();
    }
    
    /**
     * Check if any sensitive information is being updated
     */
    public boolean hasSensitiveUpdates() {
        return email != null || phoneNumber != null || emergencyContactPhone != null;
    }
    
    /**
     * Get count of fields being updated
     */
    public int getUpdateFieldCount() {
        int count = 0;
        
        if (firstName != null) count++;
        if (lastName != null) count++;
        if (email != null) count++;
        if (phoneNumber != null) count++;
        if (gender != null) count++;
        if (occupation != null) count++;
        if (nationality != null) count++;
        if (bio != null) count++;
        if (currentAddress != null) count++;
        if (preferredLanguage != null) count++;
        if (emailNotifications != null) count++;
        if (privacyLevel != null) count++;
        if (preferredAyanamsa != null) count++;
        
        return count;
    }
    
    @Override
    public String toString() {
        return String.format("UserProfileUpdateRequest{firstName='%s', lastName='%s', email='%s', fieldsUpdating=%d}", 
                           firstName, lastName, email, getUpdateFieldCount());
    }
}
