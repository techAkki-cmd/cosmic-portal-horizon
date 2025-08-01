package com.cosmic.astrology.dto;

import java.util.List;
import java.util.Map;

public class PersonalizedBirthChartResponse {
    // Core chart data
    private Map<String, Double> siderealPositions;
    private Map<String, String> planetSigns;
    private Map<String, NakshatraInfo> planetNakshatras;
    private Map<String, Double> planetStrengths;
    private String ascendantSign;
    private String sunSign;
    private String moonSign;
    private Double ayanamsa;
    
    // Unique Vedic features
    private List<RareYoga> rareYogas;
    private List<DashaPeriod> dashaTable;
    private DashaPeriod currentDasha;
    private List<DivisionalChart> divisionalCharts;
    private ElementAnalysis elementAnalysis;
    private String uniquenessHighlight;
    private List<PersonalizedRemedy> personalizedRemedies;
    
    // Advanced features
    private List<PlanetaryAspect> vedicAspects;
    private Map<String, Double> shadbalaStrengths;
    private String dominantPlanet;
    private String chartPersonality;
    private List<String> karmaLessons;
    
    // Interactive data
    private ChartVisualizationData chartData;
    private List<TransitAlert> upcomingTransits;

    // Default constructor
    public PersonalizedBirthChartResponse() {
    }

    // Getters and Setters for all fields

    public Map<String, Double> getSiderealPositions() {
        return siderealPositions;
    }

    public void setSiderealPositions(Map<String, Double> siderealPositions) {
        this.siderealPositions = siderealPositions;
    }

    public Map<String, String> getPlanetSigns() {
        return planetSigns;
    }

    public void setPlanetSigns(Map<String, String> planetSigns) {
        this.planetSigns = planetSigns;
    }

    public Map<String, NakshatraInfo> getPlanetNakshatras() {
        return planetNakshatras;
    }

    public void setPlanetNakshatras(Map<String, NakshatraInfo> planetNakshatras) {
        this.planetNakshatras = planetNakshatras;
    }

    public Map<String, Double> getPlanetStrengths() {
        return planetStrengths;
    }

    public void setPlanetStrengths(Map<String, Double> planetStrengths) {
        this.planetStrengths = planetStrengths;
    }

    public String getAscendantSign() {
        return ascendantSign;
    }

    public void setAscendantSign(String ascendantSign) {
        this.ascendantSign = ascendantSign;
    }

    public String getSunSign() {
        return sunSign;
    }

    public void setSunSign(String sunSign) {
        this.sunSign = sunSign;
    }

    public String getMoonSign() {
        return moonSign;
    }

    public void setMoonSign(String moonSign) {
        this.moonSign = moonSign;
    }

    public Double getAyanamsa() {
        return ayanamsa;
    }

    public void setAyanamsa(Double ayanamsa) {
        this.ayanamsa = ayanamsa;
    }

    public List<RareYoga> getRareYogas() {
        return rareYogas;
    }

    public void setRareYogas(List<RareYoga> rareYogas) {
        this.rareYogas = rareYogas;
    }

    public List<DashaPeriod> getDashaTable() {
        return dashaTable;
    }

    public void setDashaTable(List<DashaPeriod> dashaTable) {
        this.dashaTable = dashaTable;
    }

    public DashaPeriod getCurrentDasha() {
        return currentDasha;
    }

    public void setCurrentDasha(DashaPeriod currentDasha) {
        this.currentDasha = currentDasha;
    }

    public List<DivisionalChart> getDivisionalCharts() {
        return divisionalCharts;
    }

    public void setDivisionalCharts(List<DivisionalChart> divisionalCharts) {
        this.divisionalCharts = divisionalCharts;
    }

    public ElementAnalysis getElementAnalysis() {
        return elementAnalysis;
    }

    public void setElementAnalysis(ElementAnalysis elementAnalysis) {
        this.elementAnalysis = elementAnalysis;
    }

    public String getUniquenessHighlight() {
        return uniquenessHighlight;
    }

    public void setUniquenessHighlight(String uniquenessHighlight) {
        this.uniquenessHighlight = uniquenessHighlight;
    }

    public List<PersonalizedRemedy> getPersonalizedRemedies() {
        return personalizedRemedies;
    }

    public void setPersonalizedRemedies(List<PersonalizedRemedy> personalizedRemedies) {
        this.personalizedRemedies = personalizedRemedies;
    }

    public List<PlanetaryAspect> getVedicAspects() {
        return vedicAspects;
    }

    public void setVedicAspects(List<PlanetaryAspect> vedicAspects) {
        this.vedicAspects = vedicAspects;
    }

    public Map<String, Double> getShadbalaStrengths() {
        return shadbalaStrengths;
    }

    public void setShadbalaStrengths(Map<String, Double> shadbalaStrengths) {
        this.shadbalaStrengths = shadbalaStrengths;
    }

    public String getDominantPlanet() {
        return dominantPlanet;
    }

    public void setDominantPlanet(String dominantPlanet) {
        this.dominantPlanet = dominantPlanet;
    }

    public String getChartPersonality() {
        return chartPersonality;
    }

    public void setChartPersonality(String chartPersonality) {
        this.chartPersonality = chartPersonality;
    }

    public List<String> getKarmaLessons() {
        return karmaLessons;
    }

    public void setKarmaLessons(List<String> karmaLessons) {
        this.karmaLessons = karmaLessons;
    }

    public ChartVisualizationData getChartData() {
        return chartData;
    }

    public void setChartData(ChartVisualizationData chartData) {
        this.chartData = chartData;
    }

    public List<TransitAlert> getUpcomingTransits() {
        return upcomingTransits;
    }

    public void setUpcomingTransits(List<TransitAlert> upcomingTransits) {
        this.upcomingTransits = upcomingTransits;
    }
}
