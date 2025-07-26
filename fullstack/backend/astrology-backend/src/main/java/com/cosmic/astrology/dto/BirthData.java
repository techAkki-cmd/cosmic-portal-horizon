package com.cosmic.astrology.dto;

import java.time.LocalDateTime;

public class BirthData {
    private LocalDateTime birthDateTime;
    private String birthLocation;
    private Double birthLatitude;
    private Double birthLongitude;
    private String timezone;
    
    public BirthData() {}
    
    public BirthData(LocalDateTime birthDateTime, String birthLocation, Double birthLatitude, Double birthLongitude, String timezone) {
        this.birthDateTime = birthDateTime;
        this.birthLocation = birthLocation;
        this.birthLatitude = birthLatitude;
        this.birthLongitude = birthLongitude;
        this.timezone = timezone;
    }
    
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
}
