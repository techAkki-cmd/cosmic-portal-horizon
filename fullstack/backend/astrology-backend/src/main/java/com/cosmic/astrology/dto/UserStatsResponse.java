// src/main/java/com/cosmic/astrology/dto/UserStatsResponse.java
package com.cosmic.astrology.dto;

public class UserStatsResponse {
    private int chartsCreated;
    private int accuracyRate;
    private String cosmicEnergy;
    private int streakDays;
    
    public UserStatsResponse() {}
    
    public UserStatsResponse(int chartsCreated, int accuracyRate, String cosmicEnergy, int streakDays) {
        this.chartsCreated = chartsCreated;
        this.accuracyRate = accuracyRate;
        this.cosmicEnergy = cosmicEnergy;
        this.streakDays = streakDays;
    }
    
    // Getters and setters
    public int getChartsCreated() { return chartsCreated; }
    public void setChartsCreated(int chartsCreated) { this.chartsCreated = chartsCreated; }
    
    public int getAccuracyRate() { return accuracyRate; }
    public void setAccuracyRate(int accuracyRate) { this.accuracyRate = accuracyRate; }
    
    public String getCosmicEnergy() { return cosmicEnergy; }
    public void setCosmicEnergy(String cosmicEnergy) { this.cosmicEnergy = cosmicEnergy; }
    
    public int getStreakDays() { return streakDays; }
    public void setStreakDays(int streakDays) { this.streakDays = streakDays; }
}
