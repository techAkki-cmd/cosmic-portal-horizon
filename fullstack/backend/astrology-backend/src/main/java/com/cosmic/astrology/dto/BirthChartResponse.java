package com.cosmic.astrology.dto;

import java.time.LocalDateTime;
import java.util.Map;

public class BirthChartResponse {
    private String sunSign;
    private String moonSign;
    private String risingSign;
    private String dominantElement;
    private Map<String, Double> planetaryPositions;
    private String chartId;
    private LocalDateTime calculatedAt;
    
    // Birth data info
    private String birthLocation;
    private LocalDateTime birthDateTime;
    private Double birthLatitude;
    private Double birthLongitude;
    private String timezone;
    
    public BirthChartResponse() {
        this.calculatedAt = LocalDateTime.now();
    }
    
    public BirthChartResponse(String sunSign, String moonSign, String risingSign, 
                             String dominantElement, Map<String, Double> planetaryPositions) {
        this.sunSign = sunSign;
        this.moonSign = moonSign;
        this.risingSign = risingSign;
        this.dominantElement = dominantElement;
        this.planetaryPositions = planetaryPositions;
        this.calculatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public String getSunSign() { return sunSign; }
    public void setSunSign(String sunSign) { this.sunSign = sunSign; }
    
    public String getMoonSign() { return moonSign; }
    public void setMoonSign(String moonSign) { this.moonSign = moonSign; }
    
    public String getRisingSign() { return risingSign; }
    public void setRisingSign(String risingSign) { this.risingSign = risingSign; }
    
    public String getDominantElement() { return dominantElement; }
    public void setDominantElement(String dominantElement) { this.dominantElement = dominantElement; }
    
    public Map<String, Double> getPlanetaryPositions() { return planetaryPositions; }
    public void setPlanetaryPositions(Map<String, Double> planetaryPositions) { 
        this.planetaryPositions = planetaryPositions; 
    }
    
    public String getChartId() { return chartId; }
    public void setChartId(String chartId) { this.chartId = chartId; }
    
    public LocalDateTime getCalculatedAt() { return calculatedAt; }
    public void setCalculatedAt(LocalDateTime calculatedAt) { this.calculatedAt = calculatedAt; }
    
    public String getBirthLocation() { return birthLocation; }
    public void setBirthLocation(String birthLocation) { this.birthLocation = birthLocation; }
    
    public LocalDateTime getBirthDateTime() { return birthDateTime; }
    public void setBirthDateTime(LocalDateTime birthDateTime) { this.birthDateTime = birthDateTime; }
    
    public Double getBirthLatitude() { return birthLatitude; }
    public void setBirthLatitude(Double birthLatitude) { this.birthLatitude = birthLatitude; }
    
    public Double getBirthLongitude() { return birthLongitude; }
    public void setBirthLongitude(Double birthLongitude) { this.birthLongitude = birthLongitude; }
    
    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }
}
