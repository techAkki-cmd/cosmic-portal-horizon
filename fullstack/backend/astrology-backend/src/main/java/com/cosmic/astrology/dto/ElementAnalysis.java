package com.cosmic.astrology.dto;

public class ElementAnalysis {
    private int fireCount;
    private int earthCount;
    private int airCount;
    private int waterCount;
    private String dominantElement;
    private String personality;
    private String strengths;
    private String challenges;

    // Default constructor
    public ElementAnalysis() { }

    // Getters and Setters
    public int getFireCount() { return fireCount; }
    public void setFireCount(int fireCount) { this.fireCount = fireCount; }

    public int getEarthCount() { return earthCount; }
    public void setEarthCount(int earthCount) { this.earthCount = earthCount; }

    public int getAirCount() { return airCount; }
    public void setAirCount(int airCount) { this.airCount = airCount; }

    public int getWaterCount() { return waterCount; }
    public void setWaterCount(int waterCount) { this.waterCount = waterCount; }

    public String getDominantElement() { return dominantElement; }
    public void setDominantElement(String dominantElement) { this.dominantElement = dominantElement; }

    public String getPersonality() { return personality; }
    public void setPersonality(String personality) { this.personality = personality; }

    public String getStrengths() { return strengths; }
    public void setStrengths(String strengths) { this.strengths = strengths; }

    public String getChallenges() { return challenges; }
    public void setChallenges(String challenges) { this.challenges = challenges; }
}
