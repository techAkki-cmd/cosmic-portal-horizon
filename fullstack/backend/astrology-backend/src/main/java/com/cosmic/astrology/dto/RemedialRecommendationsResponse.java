package com.cosmic.astrology.dto;

import java.util.List;
import java.util.Map;

/**
 * 💎 REMEDIAL RECOMMENDATIONS RESPONSE
 * Complete response for personalized astrological remedies
 */
public class RemedialRecommendationsResponse {
    
    private int totalRemedies;
    private List<Map<String, Object>> gemstoneRemedies;
    private List<Map<String, Object>> mantraRemedies;
    private List<Map<String, Object>> healthRemedies;
    private List<Map<String, Object>> careerRemedies;
    private List<Map<String, Object>> relationshipRemedies;
    private List<Map<String, Object>> lifestyleRemedies;
    private List<Map<String, Object>> priorityRemedies;
    private String overallGuidance;
    
    // Constructors
    public RemedialRecommendationsResponse() {}
    
    public RemedialRecommendationsResponse(int totalRemedies, String overallGuidance) {
        this.totalRemedies = totalRemedies;
        this.overallGuidance = overallGuidance;
    }
    
    // Getters and Setters
    public int getTotalRemedies() {
        return totalRemedies;
    }
    
    public void setTotalRemedies(int totalRemedies) {
        this.totalRemedies = totalRemedies;
    }
    
    public List<Map<String, Object>> getGemstoneRemedies() {
        return gemstoneRemedies;
    }
    
    public void setGemstoneRemedies(List<Map<String, Object>> gemstoneRemedies) {
        this.gemstoneRemedies = gemstoneRemedies;
    }
    
    public List<Map<String, Object>> getMantraRemedies() {
        return mantraRemedies;
    }
    
    public void setMantraRemedies(List<Map<String, Object>> mantraRemedies) {
        this.mantraRemedies = mantraRemedies;
    }
    
    public List<Map<String, Object>> getHealthRemedies() {
        return healthRemedies;
    }
    
    public void setHealthRemedies(List<Map<String, Object>> healthRemedies) {
        this.healthRemedies = healthRemedies;
    }
    
    public List<Map<String, Object>> getCareerRemedies() {
        return careerRemedies;
    }
    
    public void setCareerRemedies(List<Map<String, Object>> careerRemedies) {
        this.careerRemedies = careerRemedies;
    }
    
    public List<Map<String, Object>> getRelationshipRemedies() {
        return relationshipRemedies;
    }
    
    public void setRelationshipRemedies(List<Map<String, Object>> relationshipRemedies) {
        this.relationshipRemedies = relationshipRemedies;
    }
    
    public List<Map<String, Object>> getLifestyleRemedies() {
        return lifestyleRemedies;
    }
    
    public void setLifestyleRemedies(List<Map<String, Object>> lifestyleRemedies) {
        this.lifestyleRemedies = lifestyleRemedies;
    }
    
    public List<Map<String, Object>> getPriorityRemedies() {
        return priorityRemedies;
    }
    
    public void setPriorityRemedies(List<Map<String, Object>> priorityRemedies) {
        this.priorityRemedies = priorityRemedies;
    }
    
    public String getOverallGuidance() {
        return overallGuidance;
    }
    
    public void setOverallGuidance(String overallGuidance) {
        this.overallGuidance = overallGuidance;
    }
    
    @Override
    public String toString() {
        return "RemedialRecommendationsResponse{" +
                "totalRemedies=" + totalRemedies +
                ", overallGuidance='" + overallGuidance + '\'' +
                ", priorityRemedies=" + (priorityRemedies != null ? priorityRemedies.size() : 0) +
                '}';
    }
}
