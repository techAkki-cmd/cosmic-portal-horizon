package com.cosmic.astrology.dto;

import java.util.Map;

public class DivisionalChart {
    private String chartType; // D1, D9, D10, etc.
    private String purpose;
    private Map<String, String> planetSigns;
    private String dominantSign;
    private String keyInsight;

    // Default constructor
    public DivisionalChart() { }

    // Getters and Setters
    public String getChartType() { return chartType; }
    public void setChartType(String chartType) { this.chartType = chartType; }

    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }

    public Map<String, String> getPlanetSigns() { return planetSigns; }
    public void setPlanetSigns(Map<String, String> planetSigns) { this.planetSigns = planetSigns; }

    public String getDominantSign() { return dominantSign; }
    public void setDominantSign(String dominantSign) { this.dominantSign = dominantSign; }

    public String getKeyInsight() { return keyInsight; }
    public void setKeyInsight(String keyInsight) { this.keyInsight = keyInsight; }
}
