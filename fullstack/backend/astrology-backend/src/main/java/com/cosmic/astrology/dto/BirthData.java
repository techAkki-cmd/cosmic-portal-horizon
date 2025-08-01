package com.cosmic.astrology.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Objects;

/**
 * Birth Data DTO for Vedic astrology calculations
 * Contains all necessary birth information for accurate chart generation
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Birth data for astrological chart calculations")
public class BirthData {
    
    @NotNull(message = "Birth date and time is required")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Birth date and time", example = "1990-05-15T14:30:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime birthDateTime;
    
    @NotBlank(message = "Birth location is required")
    @Size(max = 200, message = "Birth location cannot exceed 200 characters")
    @Schema(description = "Full birth location", example = "Mumbai, Maharashtra, India", requiredMode = Schema.RequiredMode.REQUIRED)
    private String birthLocation;
    
    @NotNull(message = "Birth latitude is required")
    @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90 degrees")
    @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90 degrees")
    @Schema(description = "Birth location latitude", example = "19.0760", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double birthLatitude;
    
    @NotNull(message = "Birth longitude is required")
    @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180 degrees")
    @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180 degrees")
    @Schema(description = "Birth location longitude", example = "72.8777", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double birthLongitude;
    
    @NotBlank(message = "Timezone is required")
    @Size(max = 50, message = "Timezone cannot exceed 50 characters")
    @Schema(description = "Birth location timezone", example = "Asia/Kolkata", requiredMode = Schema.RequiredMode.REQUIRED)
    private String timezone;
    
    // ✅ Location component fields
    @Size(max = 100, message = "Birth city cannot exceed 100 characters")
    @Schema(description = "Birth city", example = "Mumbai")
    private String birthCity;
    
    @Size(max = 100, message = "Birth state cannot exceed 100 characters")
    @Schema(description = "Birth state/province", example = "Maharashtra")
    private String birthState;
    
    @Size(max = 100, message = "Birth country cannot exceed 100 characters")
    @Schema(description = "Birth country", example = "India")
    private String birthCountry;
    
    // ✅ Data source and accuracy fields
    @Pattern(regexp = "BIRTH_CERTIFICATE|HOSPITAL_RECORD|FAMILY_KNOWLEDGE|ESTIMATED|USER_PROVIDED", 
             message = "Invalid data source")
    @Schema(description = "Source of birth data", 
            example = "USER_PROVIDED",
            allowableValues = {"BIRTH_CERTIFICATE", "HOSPITAL_RECORD", "FAMILY_KNOWLEDGE", "ESTIMATED", "USER_PROVIDED"})
    private String dataSource;
    
    @Schema(description = "Accuracy of coordinates in meters", example = "100")
    private Integer accuracy;
    
    // ✅ FIXED: Multiple time accuracy fields for compatibility
    @Schema(description = "Whether the time is exact or estimated")
    private Boolean isTimeExact = true;
    
    // ✅ NEW: Alternative field names for backward compatibility
    @Schema(description = "Whether the birth time is accurate (alias for isTimeExact)")
    private Boolean timeAccurate;
    
    @Schema(description = "Whether the birth time is accurate (alternative naming)")
    private Boolean isTimeAccurate;
    
    // ✅ NEW: Missing fields from conversion method
    @Pattern(regexp = "BIRTH_CERTIFICATE|HOSPITAL_RECORD|FAMILY_KNOWLEDGE|ESTIMATED|USER_PROVIDED", 
             message = "Invalid time source")
    @Schema(description = "Source of the birth time information", 
            example = "USER_PROVIDED",
            allowableValues = {"BIRTH_CERTIFICATE", "HOSPITAL_RECORD", "FAMILY_KNOWLEDGE", "ESTIMATED", "USER_PROVIDED"})
    private String timeSource;
    
    @Size(max = 500, message = "Update reason cannot exceed 500 characters")
    @Schema(description = "Reason for updating birth data", example = "Correcting birth time from birth certificate")
    private String updateReason;
    
    @Size(max = 1000, message = "Notes cannot exceed 1000 characters")
    @Schema(description = "Additional notes about birth data")
    private String notes;
    
    @Min(value = 0, message = "Quality score must be between 0 and 100")
    @Max(value = 100, message = "Quality score must be between 0 and 100")
    @Schema(description = "Data quality score (0-100)", example = "95")
    private Integer qualityScore;
    
    // ✅ NEW: Additional metadata fields
    @Schema(description = "Coordinate precision description", example = "High (±100m)")
    private String coordinatesPrecision;
    
    @Schema(description = "Formatted location for display", example = "Mumbai, Maharashtra, India")
    private String formattedLocation;
    
    @Schema(description = "Creation timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    
    @Schema(description = "Last update timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
    
    // ================ CONSTRUCTORS ================
    
    public BirthData() {
        this.createdAt = LocalDateTime.now();
    }
    
    public BirthData(LocalDateTime birthDateTime, String birthLocation, 
                    Double birthLatitude, Double birthLongitude, String timezone) {
        this();
        this.birthDateTime = birthDateTime;
        this.birthLocation = birthLocation;
        this.birthLatitude = birthLatitude;
        this.birthLongitude = birthLongitude;
        this.timezone = timezone;
        this.syncTimeAccuracyFields();
    }
    
    public BirthData(LocalDateTime birthDateTime, String birthLocation, 
                    Double birthLatitude, Double birthLongitude, String timezone,
                    String birthCity, String birthState, String birthCountry) {
        this(birthDateTime, birthLocation, birthLatitude, birthLongitude, timezone);
        this.birthCity = birthCity;
        this.birthState = birthState;
        this.birthCountry = birthCountry;
    }
    
    // ================ GETTERS AND SETTERS ================
    
    public LocalDateTime getBirthDateTime() { 
        return birthDateTime; 
    }
    
    public void setBirthDateTime(LocalDateTime birthDateTime) { 
        this.birthDateTime = birthDateTime;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getBirthLocation() { 
        return birthLocation; 
    }
    
    public void setBirthLocation(String birthLocation) { 
        this.birthLocation = birthLocation != null ? birthLocation.trim() : null;
        this.updatedAt = LocalDateTime.now();
    }
    
    public Double getBirthLatitude() { 
        return birthLatitude; 
    }
    
    public void setBirthLatitude(Double birthLatitude) { 
        this.birthLatitude = birthLatitude;
        this.updatedAt = LocalDateTime.now();
    }
    
    public Double getBirthLongitude() { 
        return birthLongitude; 
    }
    
    public void setBirthLongitude(Double birthLongitude) { 
        this.birthLongitude = birthLongitude;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getTimezone() { 
        return timezone; 
    }
    
    public void setTimezone(String timezone) { 
        this.timezone = timezone;
        this.updatedAt = LocalDateTime.now();
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
    
    public String getDataSource() { 
        return dataSource; 
    }
    
    public void setDataSource(String dataSource) { 
        this.dataSource = dataSource; 
    }
    
    public Integer getAccuracy() { 
        return accuracy; 
    }
    
    public void setAccuracy(Integer accuracy) { 
        this.accuracy = accuracy; 
    }
    
    // ✅ FIXED: Time accuracy getters/setters with sync
    public Boolean getIsTimeExact() { 
        return isTimeExact; 
    }
    
    public void setIsTimeExact(Boolean isTimeExact) { 
        this.isTimeExact = isTimeExact;
        this.syncTimeAccuracyFields();
    }
    
    // ✅ NEW: Additional time accuracy field methods for compatibility
    public Boolean getTimeAccurate() {
        return timeAccurate != null ? timeAccurate : isTimeExact;
    }
    
    public void setTimeAccurate(Boolean timeAccurate) {
        this.timeAccurate = timeAccurate;
        this.syncTimeAccuracyFields();
    }
    
    public Boolean getIsTimeAccurate() {
        return isTimeAccurate != null ? isTimeAccurate : isTimeExact;
    }
    
    public void setIsTimeAccurate(Boolean isTimeAccurate) {
        this.isTimeAccurate = isTimeAccurate;
        this.syncTimeAccuracyFields();
    }
    
    // ✅ NEW: Missing field getters/setters
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
    
    public String getNotes() { 
        return notes; 
    }
    
    public void setNotes(String notes) { 
        this.notes = notes; 
    }
    
    public Integer getQualityScore() { 
        return qualityScore; 
    }
    
    public void setQualityScore(Integer qualityScore) { 
        this.qualityScore = qualityScore; 
    }
    
    public String getCoordinatesPrecision() {
        return coordinatesPrecision;
    }
    
    public void setCoordinatesPrecision(String coordinatesPrecision) {
        this.coordinatesPrecision = coordinatesPrecision;
    }
    
    public String getFormattedLocation() {
        return formattedLocation;
    }
    
    public void setFormattedLocation(String formattedLocation) {
        this.formattedLocation = formattedLocation;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // ================ UTILITY METHODS ================
    
    /**
     * ✅ NEW: Sync time accuracy fields to maintain consistency
     */
    private void syncTimeAccuracyFields() {
        if (isTimeExact != null) {
            if (timeAccurate == null) timeAccurate = isTimeExact;
            if (isTimeAccurate == null) isTimeAccurate = isTimeExact;
        } else if (timeAccurate != null) {
            isTimeExact = timeAccurate;
            if (isTimeAccurate == null) isTimeAccurate = timeAccurate;
        } else if (isTimeAccurate != null) {
            isTimeExact = isTimeAccurate;
            if (timeAccurate == null) timeAccurate = isTimeAccurate;
        }
    }
    
    /**
     * Get formatted birth date and time for display
     */
    @JsonProperty("formattedBirthDateTime")
    public String getFormattedBirthDateTime() {
        if (birthDateTime == null) return null;
        return birthDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    
    /**
     * ✅ ENHANCED: Get formatted birth location for display
     */
    public String getFormattedLocationDisplay() {
        if (formattedLocation != null) return formattedLocation;
        
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
        
        String result = formatted.length() > 0 ? formatted.toString() : birthLocation;
        this.formattedLocation = result; // Cache the result
        return result;
    }
    
    /**
     * Check if coordinates are available
     */
    @JsonProperty("hasCoordinates")
    public boolean hasCoordinates() {
        return birthLatitude != null && birthLongitude != null;
    }
    
    /**
     * Check if coordinates are valid
     */
    @JsonProperty("hasValidCoordinates")
    public boolean hasValidCoordinates() {
        if (!hasCoordinates()) return false;
        
        return birthLatitude >= -90.0 && birthLatitude <= 90.0 &&
               birthLongitude >= -180.0 && birthLongitude <= 180.0;
    }
    
    /**
     * Check if birth data is complete for chart calculations
     */
    @JsonProperty("isComplete")
    public boolean isComplete() {
        return birthDateTime != null &&
               birthLocation != null && !birthLocation.trim().isEmpty() &&
               hasValidCoordinates() &&
               timezone != null && !timezone.trim().isEmpty();
    }
    
    /**
     * ✅ ENHANCED: Get coordinate precision description
     */
    public String getCoordinatePrecision() {
        if (coordinatesPrecision != null) return coordinatesPrecision;
        
        if (!hasCoordinates()) return "No coordinates";
        
        if (accuracy != null) {
            if (accuracy <= 10) return "Very High (±10m)";
            if (accuracy <= 100) return "High (±100m)";
            if (accuracy <= 1000) return "Medium (±1km)";
            return "Low (±" + accuracy + "m)";
        }
        
        // Calculate precision based on decimal places
        String latStr = birthLatitude.toString();
        String lonStr = birthLongitude.toString();
        
        int latPrecision = latStr.contains(".") ? latStr.length() - latStr.indexOf(".") - 1 : 0;
        int lonPrecision = lonStr.contains(".") ? lonStr.length() - lonStr.indexOf(".") - 1 : 0;
        
        int avgPrecision = (latPrecision + lonPrecision) / 2;
        
        String result;
        if (avgPrecision >= 4) result = "Very High (±10m)";
        else if (avgPrecision >= 3) result = "High (±100m)";
        else if (avgPrecision >= 2) result = "Medium (±1km)";
        else result = "Low (±10km)";
        
        this.coordinatesPrecision = result; // Cache the result
        return result;
    }
    
    /**
     * Validate timezone format
     */
    @JsonProperty("isTimezoneValid")
    public boolean isTimezoneValid() {
        if (timezone == null || timezone.trim().isEmpty()) return false;
        
        try {
            ZoneId.of(timezone);
            return true;
        } catch (Exception e) {
            return false;
        }
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
     * ✅ NEW: Validate data source
     */
    @JsonProperty("isDataSourceValid")
    public boolean isDataSourceValid() {
        if (dataSource == null || dataSource.trim().isEmpty()) return false;
        
        return Arrays.asList("BIRTH_CERTIFICATE", "HOSPITAL_RECORD", 
                            "FAMILY_KNOWLEDGE", "ESTIMATED", "USER_PROVIDED")
                     .contains(dataSource.toUpperCase());
    }
    
    /**
     * Calculate age from birth date
     */
    @JsonProperty("ageInYears")
    public Integer getAgeInYears() {
        if (birthDateTime == null) return null;
        
        return (int) java.time.temporal.ChronoUnit.YEARS.between(
            birthDateTime.toLocalDate(), 
            LocalDateTime.now().toLocalDate()
        );
    }
    
    /**
     * Check if birth date is valid (not in future)
     */
    @JsonProperty("isBirthDateValid")
    public boolean isBirthDateValid() {
        return birthDateTime != null && birthDateTime.isBefore(LocalDateTime.now());
    }
    
    /**
     * Parse location components from birth location string
     */
    public void parseLocationComponents() {
        if (birthLocation == null || birthLocation.trim().isEmpty()) {
            return;
        }
        
        String[] parts = birthLocation.split(",");
        if (parts.length >= 1 && (birthCity == null || birthCity.trim().isEmpty())) {
            setBirthCity(parts[0].trim());
        }
        if (parts.length >= 2 && (birthState == null || birthState.trim().isEmpty())) {
            setBirthState(parts[1].trim());
        }
        if (parts.length >= 3 && (birthCountry == null || birthCountry.trim().isEmpty())) {
            setBirthCountry(parts[2].trim());
        }
    }
    
    /**
     * ✅ ENHANCED: Calculate data quality score based on completeness and accuracy
     */
    public void calculateQualityScore() {
        int score = 0;
        
        // Basic required fields (60 points)
        if (birthDateTime != null) score += 20;
        if (birthLocation != null && !birthLocation.trim().isEmpty()) score += 15;
        if (hasValidCoordinates()) score += 15;
        if (timezone != null && isTimezoneValid()) score += 10;
        
        // Additional detail fields (25 points)
        if (birthCity != null && !birthCity.trim().isEmpty()) score += 3;
        if (birthState != null && !birthState.trim().isEmpty()) score += 3;
        if (birthCountry != null && !birthCountry.trim().isEmpty()) score += 4;
        if (getIsTimeExact() != null && getIsTimeExact()) score += 10;
        if (isTimeSourceValid()) score += 5;
        
        // Accuracy bonus (15 points)
        if (accuracy != null) {
            if (accuracy <= 10) score += 15;
            else if (accuracy <= 100) score += 10;
            else if (accuracy <= 1000) score += 5;
        } else {
            // Calculate based on coordinate precision
            String precision = getCoordinatePrecision();
            if (precision.contains("Very High")) score += 15;
            else if (precision.contains("High")) score += 10;
            else if (precision.contains("Medium")) score += 5;
        }
        
        this.qualityScore = Math.min(score, 100);
    }
    
    /**
     * ✅ NEW: Get data quality description
     */
    @JsonProperty("dataQualityDescription")
    public String getDataQualityDescription() {
        int score = qualityScore != null ? qualityScore : 0;
        
        if (score >= 90) return "Excellent - Highly accurate chart calculations possible";
        if (score >= 70) return "Good - Accurate chart calculations possible";
        if (score >= 50) return "Fair - Basic chart calculations possible";
        if (score >= 30) return "Poor - Limited chart accuracy";
        return "Insufficient - More data needed for accurate charts";
    }
    
    /**
     * Create a copy for safe logging (removes sensitive location details)
     */
    public BirthData createSafeLogVersion() {
        BirthData safe = new BirthData();
        safe.setBirthDateTime(birthDateTime);
        safe.setBirthLocation(birthLocation != null && birthLocation.length() > 10 ? 
            birthLocation.substring(0, 10) + "..." : birthLocation);
        safe.setTimezone(timezone);
        safe.setDataSource(dataSource);
        safe.setTimeSource(timeSource);
        safe.setIsTimeExact(isTimeExact);
        safe.setQualityScore(qualityScore);
        safe.setUpdateReason(updateReason);
        return safe;
    }
    
    /**
     * ✅ NEW: Validate the entire birth data object
     */
    @JsonProperty("isValidBirthData")
    public boolean isValidBirthData() {
        return birthDateTime != null &&
               birthLocation != null && !birthLocation.trim().isEmpty() &&
               isBirthDateValid() &&
               (timezone == null || isTimezoneValid()) &&
               (!hasCoordinates() || hasValidCoordinates()) &&
               (timeSource == null || isTimeSourceValid()) &&
               (dataSource == null || isDataSourceValid());
    }
    
    // ================ OBJECT METHODS ================
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        BirthData birthData = (BirthData) obj;
        return Objects.equals(birthDateTime, birthData.birthDateTime) &&
               Objects.equals(birthLocation, birthData.birthLocation) &&
               Objects.equals(birthLatitude, birthData.birthLatitude) &&
               Objects.equals(birthLongitude, birthData.birthLongitude) &&
               Objects.equals(timezone, birthData.timezone) &&
               Objects.equals(timeSource, birthData.timeSource) &&
               Objects.equals(isTimeExact, birthData.isTimeExact);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(birthDateTime, birthLocation, birthLatitude, 
                           birthLongitude, timezone, timeSource, isTimeExact);
    }
    
    @Override
    public String toString() {
        return String.format(
            "BirthData{dateTime='%s', location='%s', coordinates=[%.4f,%.4f], timezone='%s', timeAccurate=%s, source='%s', quality=%d%%}",
            birthDateTime != null ? birthDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : "null",
            birthLocation != null ? birthLocation : "null",
            birthLatitude != null ? birthLatitude : 0.0,
            birthLongitude != null ? birthLongitude : 0.0,
            timezone != null ? timezone : "null",
            getIsTimeExact() != null ? getIsTimeExact() : "null",
            timeSource != null ? timeSource : dataSource,
            qualityScore != null ? qualityScore : 0
        );
    }
}
