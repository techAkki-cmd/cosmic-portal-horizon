package com.cosmic.astrology.dto;

import java.util.List;
import java.util.Map;

/**
 * 📅 DASHA ANALYSIS RESPONSE
 * Complete response for Vimshottari Dasha analysis
 */
public class DashaAnalysisResponse {
    
    private String currentMahadasha;
    private String currentAntardasha;
    private String currentPratyantardasha;
    private String mahadashaRemaining;
    private String dashaInterpretation;
    private List<Map<String, Object>> upcomingPeriods;
    private List<String> dashaRemedies;
    private List<String> favorablePeriods;
    private int intensity;
    private String dashaStrength;
    
    // Constructors
    public DashaAnalysisResponse() {}
    
    public DashaAnalysisResponse(String currentMahadasha, String currentAntardasha, String dashaInterpretation) {
        this.currentMahadasha = currentMahadasha;
        this.currentAntardasha = currentAntardasha;
        this.dashaInterpretation = dashaInterpretation;
    }
    
    // Getters and Setters
    public String getCurrentMahadasha() {
        return currentMahadasha;
    }
    
    public void setCurrentMahadasha(String currentMahadasha) {
        this.currentMahadasha = currentMahadasha;
    }
    
    public String getCurrentAntardasha() {
        return currentAntardasha;
    }
    
    public void setCurrentAntardasha(String currentAntardasha) {
        this.currentAntardasha = currentAntardasha;
    }
    
    public String getCurrentPratyantardasha() {
        return currentPratyantardasha;
    }
    
    public void setCurrentPratyantardasha(String currentPratyantardasha) {
        this.currentPratyantardasha = currentPratyantardasha;
    }
    
    public String getMahadashaRemaining() {
        return mahadashaRemaining;
    }
    
    public void setMahadashaRemaining(String mahadashaRemaining) {
        this.mahadashaRemaining = mahadashaRemaining;
    }
    
    public String getDashaInterpretation() {
        return dashaInterpretation;
    }
    
    public void setDashaInterpretation(String dashaInterpretation) {
        this.dashaInterpretation = dashaInterpretation;
    }
    
    public List<Map<String, Object>> getUpcomingPeriods() {
        return upcomingPeriods;
    }
    
    public void setUpcomingPeriods(List<Map<String, Object>> upcomingPeriods) {
        this.upcomingPeriods = upcomingPeriods;
    }
    
    public List<String> getDashaRemedies() {
        return dashaRemedies;
    }
    
    public void setDashaRemedies(List<String> dashaRemedies) {
        this.dashaRemedies = dashaRemedies;
    }
    
    public List<String> getFavorablePeriods() {
        return favorablePeriods;
    }
    
    public void setFavorablePeriods(List<String> favorablePeriods) {
        this.favorablePeriods = favorablePeriods;
    }
    
    public int getIntensity() {
        return intensity;
    }
    
    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }
    
    public String getDashaStrength() {
        return dashaStrength;
    }
    
    public void setDashaStrength(String dashaStrength) {
        this.dashaStrength = dashaStrength;
    }
    
    @Override
    public String toString() {
        return "DashaAnalysisResponse{" +
                "currentMahadasha='" + currentMahadasha + '\'' +
                ", currentAntardasha='" + currentAntardasha + '\'' +
                ", currentPratyantardasha='" + currentPratyantardasha + '\'' +
                ", mahadashaRemaining='" + mahadashaRemaining + '\'' +
                ", intensity=" + intensity +
                '}';
    }
}
