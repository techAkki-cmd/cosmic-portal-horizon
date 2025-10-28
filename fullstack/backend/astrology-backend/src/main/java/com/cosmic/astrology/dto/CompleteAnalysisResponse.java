package com.cosmic.astrology.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompleteAnalysisResponse {
    
    private BirthChartResponse birthChart;
    private List<Map<String, Object>> rareYogas;
    private List<Map<String, Object>> personalizedRemedies;
    private List<Map<String, Object>> dashaTable;
    private Map<String, Object> personalizedMessage;
    private List<Map<String, Object>> lifeAreaInfluences;
    private Map<String, Object> userStats;
    private List<Map<String, Object>> currentTransits;
    private String analysisTimestamp;
    private String analysisId;

    public CompleteAnalysisResponse() {
        this.rareYogas = new ArrayList<>();
        this.personalizedRemedies = new ArrayList<>();
        this.dashaTable = new ArrayList<>();
        this.lifeAreaInfluences = new ArrayList<>();
        this.currentTransits = new ArrayList<>();
    }

    // Getters and Setters
    public BirthChartResponse getBirthChart() { return birthChart; }
    public void setBirthChart(BirthChartResponse birthChart) { this.birthChart = birthChart; }

    public List<Map<String, Object>> getRareYogas() { return rareYogas; }
    public void setRareYogas(List<Map<String, Object>> rareYogas) { 
        this.rareYogas = rareYogas != null ? rareYogas : new ArrayList<>();
    }

    public List<Map<String, Object>> getPersonalizedRemedies() { return personalizedRemedies; }
    public void setPersonalizedRemedies(List<Map<String, Object>> personalizedRemedies) { 
        this.personalizedRemedies = personalizedRemedies != null ? personalizedRemedies : new ArrayList<>();
    }

    public List<Map<String, Object>> getDashaTable() { return dashaTable; }
    public void setDashaTable(List<Map<String, Object>> dashaTable) { 
        this.dashaTable = dashaTable != null ? dashaTable : new ArrayList<>();
    }

    public Map<String, Object> getPersonalizedMessage() { return personalizedMessage; }
    public void setPersonalizedMessage(Map<String, Object> personalizedMessage) { 
        this.personalizedMessage = personalizedMessage;
    }

    public List<Map<String, Object>> getLifeAreaInfluences() { return lifeAreaInfluences; }
    public void setLifeAreaInfluences(List<Map<String, Object>> lifeAreaInfluences) { 
        this.lifeAreaInfluences = lifeAreaInfluences != null ? lifeAreaInfluences : new ArrayList<>();
    }

    public Map<String, Object> getUserStats() { return userStats; }
    public void setUserStats(Map<String, Object> userStats) { this.userStats = userStats; }

    public List<Map<String, Object>> getCurrentTransits() { return currentTransits; }
    public void setCurrentTransits(List<Map<String, Object>> currentTransits) { 
        this.currentTransits = currentTransits != null ? currentTransits : new ArrayList<>();
    }

    public String getAnalysisTimestamp() { return analysisTimestamp; }
    public void setAnalysisTimestamp(String analysisTimestamp) { this.analysisTimestamp = analysisTimestamp; }

    public String getAnalysisId() { return analysisId; }
    public void setAnalysisId(String analysisId) { this.analysisId = analysisId; }
}
