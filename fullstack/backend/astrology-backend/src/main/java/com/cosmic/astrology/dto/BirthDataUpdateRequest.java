package com.cosmic.astrology.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Objects;

/**
 * Birth Data Update Request DTO for updating user's birth information
 * Used for Vedic astrology chart calculations and personalized insights
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Birth data update request for astrological calculations")
public class BirthDataUpdateRequest {
    
    @NotBlank(message = "Birth date and time is required")
    @Schema(description = "Birth date and time in ISO format", 
            example = "1990-05-15T14:30:00", 
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String birthDateTime;
    
    @NotBlank(message = "Birth location is required")
    @Size(max = 200, message = "Birth location cannot exceed 200 characters")
    @Schema(description = "Birth location (city, state/province, country)", 
            example = "Mumbai, Maharashtra, India", 
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String birthLocation;
    
    @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90 degrees")
    @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90 degrees")
    @Schema(description = "Birth location latitude", 
            example = "19.0760",
            minimum = "-90.0",
            maximum = "90.0")
    private Double birthLatitude;
    
    @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180 degrees")
    @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180 degrees")
    @Schema(description = "Birth location longitude", 
            example = "72.8777",
            minimum = "-180.0",
            maximum = "180.0")
    private Double birthLongitude;
    
    @Size(max = 50, message = "Timezone cannot exceed 50 characters")
    @Schema(description = "Birth location timezone", 
            example = "Asia/Kolkata",
            defaultValue = "UTC")
    private String timezone = "UTC";
    
    @Size(max = 100, message = "Birth city cannot exceed 100 characters")
    @Schema(description = "Birth city name", example = "Mumbai")
    private String birthCity;
    
    @Size(max = 100, message = "Birth state cannot exceed 100 characters")
    @Schema(description = "Birth state/province", example = "Maharashtra")
    private String birthState;
    
    @Size(max = 100, message = "Birth country cannot exceed 100 characters")
    @Schema(description = "Birth country", example = "India")
    private String birthCountry;
    
    // ✅ NEW: Missing fields for frontend integration
    @Schema(description = "Whether the birth time is accurate", example = "true")
    private Boolean isTimeAccurate;
    
    @Pattern(regexp = "BIRTH_CERTIFICATE|HOSPITAL_RECORD|FAMILY_KNOWLEDGE|ESTIMATED|USER_PROVIDED", 
             message = "Invalid time source")
    @Schema(description = "Source of the birth time information", 
            example = "USER_PROVIDED",
            allowableValues = {"BIRTH_CERTIFICATE", "HOSPITAL_RECORD", "FAMILY_KNOWLEDGE", "ESTIMATED", "USER_PROVIDED"})
    private String timeSource;
    
    @Schema(description = "Update reason for audit trail", example = "Correcting birth time")
    private String updateReason;
    
    @Schema(description = "Source of birth data", example = "BIRTH_CERTIFICATE")
    private String dataSource;
    
    // ================ CONSTRUCTORS ================
    
    public BirthDataUpdateRequest() {}
    
    public BirthDataUpdateRequest(String birthDateTime, String birthLocation) {
        this.birthDateTime = birthDateTime;
        this.birthLocation = birthLocation;
    }
    
    public BirthDataUpdateRequest(String birthDateTime, String birthLocation, 
                                 Double birthLatitude, Double birthLongitude, String timezone) {
        this.birthDateTime = birthDateTime;
        this.birthLocation = birthLocation;
        this.birthLatitude = birthLatitude;
        this.birthLongitude = birthLongitude;
        this.timezone = timezone;
    }
    
    // ✅ ENHANCED: Constructor with all fields
    public BirthDataUpdateRequest(String birthDateTime, String birthLocation, 
                                 Double birthLatitude, Double birthLongitude, String timezone,
                                 Boolean isTimeAccurate, String timeSource) {
        this.birthDateTime = birthDateTime;
        this.birthLocation = birthLocation;
        this.birthLatitude = birthLatitude;
        this.birthLongitude = birthLongitude;
        this.timezone = timezone;
        this.isTimeAccurate = isTimeAccurate;
        this.timeSource = timeSource;
    }
    
    // ================ GETTERS AND SETTERS ================
    
    public String getBirthDateTime() { 
        return birthDateTime; 
    }
    
    public void setBirthDateTime(String birthDateTime) { 
        this.birthDateTime = birthDateTime; 
    }
    
    public String getBirthLocation() { 
        return birthLocation; 
    }
    
    public void setBirthLocation(String birthLocation) { 
        this.birthLocation = birthLocation != null ? birthLocation.trim() : null; 
    }
    
    public Double getBirthLatitude() { 
        return birthLatitude; 
    }
    
    public void setBirthLatitude(Double birthLatitude) { 
        this.birthLatitude = birthLatitude; 
    }
    
    public Double getBirthLongitude() { 
        return birthLongitude; 
    }
    
    public void setBirthLongitude(Double birthLongitude) { 
        this.birthLongitude = birthLongitude; 
    }
    
    public String getTimezone() { 
        return timezone; 
    }
    
    public void setTimezone(String timezone) { 
        this.timezone = timezone != null ? timezone : "UTC"; 
    }
    
    public String getBirthCity() { 
        return birthCity; 
    }
    
    public void setBirthCity(String birthCity) { 
        this.birthCity = birthCity != null ? birthCity.trim() : null; 
    }
    
    public String getBirthState() { 
        return birthState; 
    }
    
    public void setBirthState(String birthState) { 
        this.birthState = birthState != null ? birthState.trim() : null; 
    }
    
    public String getBirthCountry() { 
        return birthCountry; 
    }
    
    public void setBirthCountry(String birthCountry) { 
        this.birthCountry = birthCountry != null ? birthCountry.trim() : null; 
    }
    
    // ✅ NEW: Missing getters and setters
    public Boolean getIsTimeAccurate() { 
        return isTimeAccurate; 
    }
    
    public void setIsTimeAccurate(Boolean isTimeAccurate) { 
        this.isTimeAccurate = isTimeAccurate; 
    }
    
    public String getTimeSource() { 
        return timeSource; 
    }
    
    public void setTimeSource(String timeSource) { 
        this.timeSource = timeSource; 
    }
    
    public String getUpdateReason() { 
        return updateReason; 
    }
    
    public void setUpdateReason(String updateReason) { 
        this.updateReason = updateReason; 
    }
    
    // ✅ ENHANCED: Improved getDataSource method
    public String getDataSource() { 
        // Use timeSource if available, fallback to dataSource, then default
        if (timeSource != null && !timeSource.trim().isEmpty()) {
            return timeSource;
        }
        return dataSource != null ? dataSource : "USER_PROVIDED"; 
    }
    
    public void setDataSource(String dataSource) { 
        this.dataSource = dataSource; 
    }
    
    // ================ UTILITY METHODS ================
    
    /**
     * Parse the birth date time string to LocalDateTime
     */
    @JsonProperty("parsedBirthDateTime")
    public LocalDateTime getParsedBirthDateTime() {
        if (birthDateTime == null || birthDateTime.trim().isEmpty()) {
            return null;
        }
        
        try {
            // Handle different formats
            if (birthDateTime.contains("T")) {
                return LocalDateTime.parse(birthDateTime);
            } else {
                // Assume date only, add default time
                return LocalDateTime.parse(birthDateTime + "T12:00:00");
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid birth date time format: " + birthDateTime, e);
        }
    }
    
    /**
     * Check if coordinates are provided
     */
    @JsonProperty("hasCoordinates")
    public boolean hasCoordinates() {
        return birthLatitude != null && birthLongitude != null;
    }
    
    /**
     * Validate coordinate ranges
     */
    @JsonProperty("hasValidCoordinates")
    public boolean hasValidCoordinates() {
        if (!hasCoordinates()) return false;
        
        return birthLatitude >= -90.0 && birthLatitude <= 90.0 &&
               birthLongitude >= -180.0 && birthLongitude <= 180.0;
    }
    
    /**
     * ✅ ENHANCED: Check if birth data is complete for chart calculations
     */
    @JsonProperty("isDataComplete")
    public boolean isDataComplete() {
        return birthDateTime != null && !birthDateTime.trim().isEmpty() &&
               birthLocation != null && !birthLocation.trim().isEmpty() &&
               hasValidCoordinates() &&
               timeSource != null && !timeSource.trim().isEmpty();
    }
    
    /**
     * ✅ NEW: Validate time source
     */
    @JsonProperty("isTimeSourceValid")
    public boolean isTimeSourceValid() {
        if (timeSource == null || timeSource.trim().isEmpty()) return false;
        
        return Arrays.asList("BIRTH_CERTIFICATE", "HOSPITAL_RECORD", 
                            "FAMILY_KNOWLEDGE", "ESTIMATED", "USER_PROVIDED")
                     .contains(timeSource.toUpperCase());
    }
    
    /**
     * Get formatted birth location for display
     */
    @JsonProperty("formattedLocation")
    public String getFormattedLocation() {
        if (birthLocation == null || birthLocation.trim().isEmpty()) {
            return "Unknown Location";
        }
        
        StringBuilder formatted = new StringBuilder();
        
        if (birthCity != null && !birthCity.trim().isEmpty()) {
            formatted.append(birthCity);
        }
        
        if (birthState != null && !birthState.trim().isEmpty()) {
            if (formatted.length() > 0) formatted.append(", ");
            formatted.append(birthState);
        }
        
        if (birthCountry != null && !birthCountry.trim().isEmpty()) {
            if (formatted.length() > 0) formatted.append(", ");
            formatted.append(birthCountry);
        }
        
        return formatted.length() > 0 ? formatted.toString() : birthLocation;
    }
    
    /**
     * Parse location components from birth location string
     */
    public void parseLocationComponents() {
        if (birthLocation == null || birthLocation.trim().isEmpty()) {
            return;
        }
        
        String[] parts = birthLocation.split(",");
        if (parts.length >= 1) {
            setBirthCity(parts[0].trim());
        }
        if (parts.length >= 2) {
            setBirthState(parts[1].trim());
        }
        if (parts.length >= 3) {
            setBirthCountry(parts[2].trim());
        }
    }
    
    /**
     * Get coordinate precision for display
     */
    @JsonProperty("coordinatePrecision")
    public String getCoordinatePrecision() {
        if (!hasCoordinates()) return "No coordinates";
        
        // Calculate precision based on decimal places
        String latStr = birthLatitude.toString();
        String lonStr = birthLongitude.toString();
        
        int latPrecision = latStr.contains(".") ? latStr.length() - latStr.indexOf(".") - 1 : 0;
        int lonPrecision = lonStr.contains(".") ? lonStr.length() - lonStr.indexOf(".") - 1 : 0;
        
        int avgPrecision = (latPrecision + lonPrecision) / 2;
        
        if (avgPrecision >= 4) return "Very High (±10m)";
        if (avgPrecision >= 3) return "High (±100m)";
        if (avgPrecision >= 2) return "Medium (±1km)";
        return "Low (±10km)";
    }
    
    /**
     * Validate birth date is not in the future
     */
    @JsonProperty("isBirthDateValid")
    public boolean isBirthDateValid() {
        try {
            LocalDateTime birthDT = getParsedBirthDateTime();
            return birthDT != null && birthDT.isBefore(LocalDateTime.now());
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Get age from birth date
     */
    @JsonProperty("ageInYears")
    public Integer getAgeInYears() {
        try {
            LocalDateTime birthDT = getParsedBirthDateTime();
            if (birthDT == null) return null;
            
            return (int) java.time.temporal.ChronoUnit.YEARS.between(birthDT, LocalDateTime.now());
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Check if timezone is valid
     */
    @JsonProperty("isTimezoneValid")
    public boolean isTimezoneValid() {
        if (timezone == null || timezone.trim().isEmpty()) return false;
        
        try {
            java.time.ZoneId.of(timezone);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * ✅ ENHANCED: Create a sanitized version for logging (no sensitive data)
     */
    public BirthDataUpdateRequest createSafeLogVersion() {
        BirthDataUpdateRequest safe = new BirthDataUpdateRequest();
        safe.setBirthDateTime(birthDateTime != null ? "***" : null);
        safe.setBirthLocation(birthLocation != null ? 
            birthLocation.length() > 10 ? birthLocation.substring(0, 10) + "..." : birthLocation : null);
        safe.setTimezone(timezone);
        safe.setUpdateReason(updateReason);
        safe.setDataSource(getDataSource()); // Use enhanced method
        safe.setTimeSource(timeSource);
        safe.setIsTimeAccurate(isTimeAccurate);
        return safe;
    }
    
    /**
     * ✅ ENHANCED: Validate the entire request
     */
    public boolean isValidRequest() {
        return birthDateTime != null && !birthDateTime.trim().isEmpty() &&
               birthLocation != null && !birthLocation.trim().isEmpty() &&
               isBirthDateValid() &&
               (timezone == null || isTimezoneValid()) &&
               (!hasCoordinates() || hasValidCoordinates()) &&
               (timeSource == null || isTimeSourceValid());
    }
    
    /**
     * ✅ NEW: Get data quality score
     */
    @JsonProperty("dataQualityScore")
    public int getDataQualityScore() {
        int score = 0;
        
        // Basic data (40 points)
        if (birthDateTime != null && !birthDateTime.trim().isEmpty()) score += 20;
        if (birthLocation != null && !birthLocation.trim().isEmpty()) score += 20;
        
        // Coordinates (30 points)
        if (hasValidCoordinates()) score += 30;
        
        // Time accuracy (20 points)
        if (isTimeAccurate != null && isTimeAccurate) score += 20;
        
        // Timezone (10 points)
        if (isTimezoneValid()) score += 10;
        
        return Math.min(score, 100); // Cap at 100
    }
    
    /**
     * ✅ NEW: Get data quality description
     */
    @JsonProperty("dataQualityDescription")
    public String getDataQualityDescription() {
        int score = getDataQualityScore();
        
        if (score >= 90) return "Excellent - Highly accurate chart calculations possible";
        if (score >= 70) return "Good - Accurate chart calculations possible";
        if (score >= 50) return "Fair - Basic chart calculations possible";
        if (score >= 30) return "Poor - Limited chart accuracy";
        return "Insufficient - More data needed for accurate charts";
    }
    
    // ================ OBJECT METHODS ================
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        BirthDataUpdateRequest that = (BirthDataUpdateRequest) obj;
        return Objects.equals(birthDateTime, that.birthDateTime) &&
               Objects.equals(birthLocation, that.birthLocation) &&
               Objects.equals(birthLatitude, that.birthLatitude) &&
               Objects.equals(birthLongitude, that.birthLongitude) &&
               Objects.equals(timezone, that.timezone) &&
               Objects.equals(isTimeAccurate, that.isTimeAccurate) &&
               Objects.equals(timeSource, that.timeSource);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(birthDateTime, birthLocation, birthLatitude, birthLongitude, 
                           timezone, isTimeAccurate, timeSource);
    }
    
    @Override
    public String toString() {
        return String.format(
            "BirthDataUpdateRequest{birthDateTime='%s', location='%s', coordinates=[%.4f,%.4f], timezone='%s', timeAccurate=%s, source='%s', quality=%d%%}",
            birthDateTime != null ? birthDateTime : "null",
            birthLocation != null ? birthLocation : "null",
            birthLatitude != null ? birthLatitude : 0.0,
            birthLongitude != null ? birthLongitude : 0.0,
            timezone != null ? timezone : "null",
            isTimeAccurate != null ? isTimeAccurate : "null",
            timeSource != null ? timeSource : "null",
            getDataQualityScore()
        );
    }
}
