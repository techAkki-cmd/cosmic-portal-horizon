// src/main/java/com/cosmic/astrology/dto/PersonalizedMessageResponse.java
package com.cosmic.astrology.dto;

public class PersonalizedMessageResponse {
    private String message;
    private String transitInfluence;
    private String recommendation;
    private int intensity;
    private String dominantPlanet;
    private String luckyColor;
    private String bestTimeOfDay;
    private String moonPhase;
    
    public PersonalizedMessageResponse() {}
    
    public PersonalizedMessageResponse(String message, String transitInfluence, 
                                     String recommendation, int intensity) {
        this.message = message;
        this.transitInfluence = transitInfluence;
        this.recommendation = recommendation;
        this.intensity = intensity;
    }
    
    // All getters and setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public String getTransitInfluence() { return transitInfluence; }
    public void setTransitInfluence(String transitInfluence) { this.transitInfluence = transitInfluence; }
    
    public String getRecommendation() { return recommendation; }
    public void setRecommendation(String recommendation) { this.recommendation = recommendation; }
    
    public int getIntensity() { return intensity; }
    public void setIntensity(int intensity) { this.intensity = intensity; }
    
    public String getDominantPlanet() { return dominantPlanet; }
    public void setDominantPlanet(String dominantPlanet) { this.dominantPlanet = dominantPlanet; }
    
    public String getLuckyColor() { return luckyColor; }
    public void setLuckyColor(String luckyColor) { this.luckyColor = luckyColor; }
    
    public String getBestTimeOfDay() { return bestTimeOfDay; }
    public void setBestTimeOfDay(String bestTimeOfDay) { this.bestTimeOfDay = bestTimeOfDay; }
    
    public String getMoonPhase() { return moonPhase; }
    public void setMoonPhase(String moonPhase) { this.moonPhase = moonPhase; }
}
