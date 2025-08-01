package com.cosmic.astrology.dto;

import java.time.LocalDateTime;

public class BirthChartRequest {
    private LocalDateTime birthDateTime;
    private Double birthLatitude;
    private Double birthLongitude;
    private String birthLocation;
    private String timezone;

    public BirthChartRequest() {}

    public LocalDateTime getBirthDateTime() {
        return birthDateTime;
    }

    public void setBirthDateTime(LocalDateTime birthDateTime) {
        this.birthDateTime = birthDateTime;
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

    public String getBirthLocation() {
        return birthLocation;
    }

    public void setBirthLocation(String birthLocation) {
        this.birthLocation = birthLocation;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
}
