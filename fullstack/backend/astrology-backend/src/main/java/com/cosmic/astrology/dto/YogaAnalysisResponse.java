package com.cosmic.astrology.dto;

import java.util.List;
import java.util.Map;

/**
 * 🕉️ YOGA ANALYSIS RESPONSE
 * Complete response for Vedic Yoga analysis
 */
public class YogaAnalysisResponse {
    
    private int totalYogas;
    private List<Map<String, Object>> rajaYogas;
    private List<Map<String, Object>> dhanaYogas;
    private List<Map<String, Object>> spiritualYogas;
    private List<Map<String, Object>> mahapurushaYogas;
    private List<Map<String, Object>> challengingYogas;
    private int yogaStrength;
    private List<Map<String, Object>> topYogas;
    private String overallYogaAssessment;
    
    // Constructors
    public YogaAnalysisResponse() {}
    
    public YogaAnalysisResponse(int totalYogas, int yogaStrength) {
        this.totalYogas = totalYogas;
        this.yogaStrength = yogaStrength;
    }
    
    // Getters and Setters
    public int getTotalYogas() {
        return totalYogas;
    }
    
    public void setTotalYogas(int totalYogas) {
        this.totalYogas = totalYogas;
    }
    
    public List<Map<String, Object>> getRajaYogas() {
        return rajaYogas;
    }
    
    public void setRajaYogas(List<Map<String, Object>> rajaYogas) {
        this.rajaYogas = rajaYogas;
    }
    
    public List<Map<String, Object>> getDhanaYogas() {
        return dhanaYogas;
    }
    
    public void setDhanaYogas(List<Map<String, Object>> dhanaYogas) {
        this.dhanaYogas = dhanaYogas;
    }
    
    public List<Map<String, Object>> getSpiritualYogas() {
        return spiritualYogas;
    }
    
    public void setSpiritualYogas(List<Map<String, Object>> spiritualYogas) {
        this.spiritualYogas = spiritualYogas;
    }
    
    public List<Map<String, Object>> getMahapurushaYogas() {
        return mahapurushaYogas;
    }
    
    public void setMahapurushaYogas(List<Map<String, Object>> mahapurushaYogas) {
        this.mahapurushaYogas = mahapurushaYogas;
    }
    
    public List<Map<String, Object>> getChallengingYogas() {
        return challengingYogas;
    }
    
    public void setChallengingYogas(List<Map<String, Object>> challengingYogas) {
        this.challengingYogas = challengingYogas;
    }
    
    public int getYogaStrength() {
        return yogaStrength;
    }
    
    public void setYogaStrength(int yogaStrength) {
        this.yogaStrength = yogaStrength;
    }
    
    public List<Map<String, Object>> getTopYogas() {
        return topYogas;
    }
    
    public void setTopYogas(List<Map<String, Object>> topYogas) {
        this.topYogas = topYogas;
    }
    
    public String getOverallYogaAssessment() {
        return overallYogaAssessment;
    }
    
    public void setOverallYogaAssessment(String overallYogaAssessment) {
        this.overallYogaAssessment = overallYogaAssessment;
    }
    
    @Override
    public String toString() {
        return "YogaAnalysisResponse{" +
                "totalYogas=" + totalYogas +
                ", yogaStrength=" + yogaStrength +
                ", rajaYogas=" + (rajaYogas != null ? rajaYogas.size() : 0) +
                ", dhanaYogas=" + (dhanaYogas != null ? dhanaYogas.size() : 0) +
                ", spiritualYogas=" + (spiritualYogas != null ? spiritualYogas.size() : 0) +
                ", mahapurushaYogas=" + (mahapurushaYogas != null ? mahapurushaYogas.size() : 0) +
                ", challengingYogas=" + (challengingYogas != null ? challengingYogas.size() : 0) +
                '}';
    }
}
