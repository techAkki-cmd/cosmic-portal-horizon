package com.cosmic.astrology.dto;

public class PlanetPosition {
    private String name;
    private double longitude;
    private double latitude;
    private double speed;
    private String zodiacSign;
    private int house;
    private boolean retrograde;
    private double degree;
    private int minutes;
    private int seconds;
    
    public PlanetPosition() {}
    
    public PlanetPosition(String name, double longitude, double latitude, 
                         double speed, String zodiacSign, int house) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.speed = speed;
        this.zodiacSign = zodiacSign;
        this.house = house;
        this.retrograde = speed < 0;
        this.calculateDegreeMinutesSeconds(longitude);
    }
    
    private void calculateDegreeMinutesSeconds(double longitude) {
        double adjustedLongitude = longitude % 30;
        this.degree = (int) adjustedLongitude;
        double minutesDouble = (adjustedLongitude - degree) * 60;
        this.minutes = (int) minutesDouble;
        this.seconds = (int) ((minutesDouble - minutes) * 60);
    }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { 
        this.longitude = longitude;
        calculateDegreeMinutesSeconds(longitude);
    }
    
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    
    public double getSpeed() { return speed; }
    public void setSpeed(double speed) { 
        this.speed = speed;
        this.retrograde = speed < 0;
    }
    
    public String getZodiacSign() { return zodiacSign; }
    public void setZodiacSign(String zodiacSign) { this.zodiacSign = zodiacSign; }
    
    public int getHouse() { return house; }
    public void setHouse(int house) { this.house = house; }
    
    public boolean isRetrograde() { return retrograde; }
    public void setRetrograde(boolean retrograde) { this.retrograde = retrograde; }
    
    public double getDegree() { return degree; }
    public void setDegree(double degree) { this.degree = degree; }
    
    public int getMinutes() { return minutes; }
    public void setMinutes(int minutes) { this.minutes = minutes; }
    
    public int getSeconds() { return seconds; }
    public void setSeconds(int seconds) { this.seconds = seconds; }
}
