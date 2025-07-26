// src/main/java/com/cosmic/astrology/dto/LifeAreaInfluence.java
package com.cosmic.astrology.dto;

public class LifeAreaInfluence {
    private String title;
    private int rating;
    private String insight;
    private String icon;
    private String gradient;
    
    public LifeAreaInfluence() {}
    
    public LifeAreaInfluence(String title, int rating, String insight, String icon, String gradient) {
        this.title = title;
        this.rating = rating;
        this.insight = insight;
        this.icon = icon;
        this.gradient = gradient;
    }
    
    // Getters and setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    
    public String getInsight() { return insight; }
    public void setInsight(String insight) { this.insight = insight; }
    
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    
    public String getGradient() { return gradient; }
    public void setGradient(String gradient) { this.gradient = gradient; }
}
