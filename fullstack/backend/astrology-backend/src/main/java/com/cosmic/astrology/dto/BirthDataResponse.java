package com.cosmic.astrology.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * Birth Data Response DTO
 */
@Schema(description = "User birth data response")
public class BirthDataResponse {
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Birth date and time")
    private LocalDateTime birthDateTime;
    
    @Schema(description = "Birth location", example = "New York, NY, USA")
    private String birthLocation;
    
    @Schema(description = "Birth latitude", example = "40.7128")
    private Double birthLatitude;
    
    @Schema(description = "Birth longitude", example = "-74.0060")
    private Double birthLongitude;
    
    @Schema(description = "Timezone", example = "America/New_York")
    private String timezone;
    
    @Schema(description = "Birth city", example = "New York")
    private String birthCity;
    
    @Schema(description = "Birth state", example = "NY")
    private String birthState;
    
    @Schema(description = "Birth country", example = "USA")
    private String birthCountry;
    
    @Schema(description = "Data completeness status", example = "true")
    private Boolean dataComplete;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Last updated timestamp")
    private LocalDateTime lastUpdated;
    
    // Constructors
    public BirthDataResponse() {}
    
    // Getters and Setters
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
    
    public Boolean getDataComplete() { return dataComplete; }
    public void setDataComplete(Boolean dataComplete) { this.dataComplete = dataComplete; }
    
    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
}
