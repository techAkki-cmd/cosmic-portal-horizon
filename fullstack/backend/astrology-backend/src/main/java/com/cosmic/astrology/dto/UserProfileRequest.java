package com.cosmic.astrology.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

/**
 * User Profile Update Request DTO
 */
@Schema(description = "User profile update request")
public class UserProfileRequest {
    
    @Size(max = 50, message = "First name cannot exceed 50 characters")
    @Schema(description = "First name", example = "John")
    private String firstName;
    
    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    @Schema(description = "Last name", example = "Doe")
    private String lastName;
    
    @Email(message = "Invalid email format")
    @Schema(description = "Email address", example = "user@example.com")
    private String email;
    
    @Size(max = 20, message = "Phone number cannot exceed 20 characters")
    @Schema(description = "Phone number", example = "+1234567890")
    private String phoneNumber;
    
    @Schema(description = "Gender", example = "Male")
    private String gender;
    
    @Size(max = 100, message = "Occupation cannot exceed 100 characters")
    @Schema(description = "Occupation", example = "Software Engineer")
    private String occupation;
    
    @Size(max = 100, message = "Nationality cannot exceed 100 characters")
    @Schema(description = "Nationality", example = "American")
    private String nationality;
    
    @Schema(description = "Preferred language", example = "en")
    private String preferredLanguage;
    
    @Schema(description = "Time format preference", example = "12h")
    private String timeFormat;
    
    @Schema(description = "Date format preference", example = "MM/dd/yyyy")
    private String dateFormat;
    
    // Constructors
    public UserProfileRequest() {}
    
    // Getters and Setters
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
    
    public String getPreferredLanguage() { return preferredLanguage; }
    public void setPreferredLanguage(String preferredLanguage) { this.preferredLanguage = preferredLanguage; }
    
    public String getTimeFormat() { return timeFormat; }
    public void setTimeFormat(String timeFormat) { this.timeFormat = timeFormat; }
    
    public String getDateFormat() { return dateFormat; }
    public void setDateFormat(String dateFormat) { this.dateFormat = dateFormat; }
}
