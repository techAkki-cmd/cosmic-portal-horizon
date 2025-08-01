package com.cosmic.astrology.dto;

import java.time.LocalDate;

public class TransitAlert {
    private String planet;
    private String transitType; // conjunction, opposition, return, etc.
    private String natalPlanet; // planet being aspected in natal chart
    private LocalDate date;
    private String description;
    private String influence; // positive, challenging, neutral
    private int intensity; // 1-5
    private String recommendations;

    public TransitAlert() { }

    // Getters and Setters
    public String getPlanet() { return planet; }
    public void setPlanet(String planet) { this.planet = planet; }

    public String getTransitType() { return transitType; }
    public void setTransitType(String transitType) { this.transitType = transitType; }

    public String getNatalPlanet() { return natalPlanet; }
    public void setNatalPlanet(String natalPlanet) { this.natalPlanet = natalPlanet; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getInfluence() { return influence; }
    public void setInfluence(String influence) { this.influence = influence; }

    public int getIntensity() { return intensity; }
    public void setIntensity(int intensity) { this.intensity = intensity; }

    public String getRecommendations() { return recommendations; }
    public void setRecommendations(String recommendations) { this.recommendations = recommendations; }
}
