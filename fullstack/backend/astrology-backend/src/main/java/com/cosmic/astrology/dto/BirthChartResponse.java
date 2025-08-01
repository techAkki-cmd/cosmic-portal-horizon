package com.cosmic.astrology.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Comprehensive Birth Chart Response DTO for Vedic Astrology
 * Contains complete natal chart data with Vedic-specific calculations
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BirthChartResponse {
    
    // Basic astrological signs
    private String sunSign;
    private String moonSign;
    private String risingSign;
    private String dominantElement;
    
    // Planetary positions and data
    private Map<String, Double> planetaryPositions;
    private Map<String, Map<String, Object>> planetaryDetails;
    
    // Chart identification and metadata
    private String chartId;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime calculatedAt;
    
    // Birth data information
    private String birthLocation;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime birthDateTime;
    
    private Double birthLatitude;
    private Double birthLongitude;
    private String timezone;
    
    // Vedic-specific data
    private Double ayanamsa;
    private String ayanamsaType;
    private String system;
    private String accuracy;
    
    // House system data
    private Map<String, Double> houses;
    private Map<String, Map<String, Object>> vedicHouses;
    private String houseSystem;
    
    // Nakshatra data
    private Map<String, Map<String, Object>> nakshatras;
    private String moonNakshatra;
    private Integer moonPada;
    
    // Aspects and relationships
    private List<Map<String, Object>> aspects;
    private List<Map<String, Object>> vedicAspects;
    
    // Planetary strengths and qualities
    private Map<String, Double> planetaryStrengths;
    private Map<String, String> planetaryStates; // exalted, debilitated, etc.
    
    // Additional Vedic calculations
    private String lagnaLord;
    private String nakshatraLord;
    private String chandraLagna; // Moon sign as ascendant
    private String suryaLagna;   // Sun sign as ascendant
    
    // Yoga and combinations
    private List<String> yogas;
    private List<String> doshas;
    private String vimsottariDasha;
    
    // Elements and qualities analysis
    private Map<String, Integer> elementDistribution;
    private Map<String, Integer> qualityDistribution;
    
    // Vedic remedies and recommendations
    private List<String> gemstoneRecommendations;
    private List<String> mantraRecommendations;
    private String luckyColor;
    private List<Integer> luckyNumbers;
    private String favorableDirection;
    
    // Chart interpretation
    private String personalityProfile;
    private String careerIndications;
    private String relationshipTendencies;
    private String healthIndications;
    private String spiritualPath;
    
    // Technical data
    private Double julianDay;
    private String ephemerisUsed;
    private String calculationSource;
    
    // Chart status and validation
    private Boolean isValidChart;
    private List<String> validationMessages;
    private String chartQuality; // "Excellent", "Good", "Fair", "Poor"
    
    // Default constructor
    public BirthChartResponse() {
        this.calculatedAt = LocalDateTime.now();
        this.planetaryPositions = new HashMap<>();
        this.houses = new HashMap<>();
        this.nakshatras = new HashMap<>();
        this.aspects = new ArrayList<>();
        this.planetaryStrengths = new HashMap<>();
        this.validationMessages = new ArrayList<>();
        this.isValidChart = true;
    }
    
    // Basic constructor
    public BirthChartResponse(String sunSign, String moonSign, String risingSign, 
                             String dominantElement, Map<String, Double> planetaryPositions) {
        this();
        this.sunSign = sunSign;
        this.moonSign = moonSign;
        this.risingSign = risingSign;
        this.dominantElement = dominantElement;
        this.planetaryPositions = planetaryPositions != null ? planetaryPositions : new HashMap<>();
    }
    
    // Comprehensive constructor for Vedic charts
    public BirthChartResponse(String sunSign, String moonSign, String risingSign, 
                             String dominantElement, Map<String, Double> planetaryPositions,
                             Double ayanamsa, String system, Map<String, Double> houses) {
        this(sunSign, moonSign, risingSign, dominantElement, planetaryPositions);
        this.ayanamsa = ayanamsa;
        this.system = system;
        this.houses = houses != null ? houses : new HashMap<>();
    }
    
    // ================ GETTERS AND SETTERS ================
    
    public String getSunSign() { return sunSign; }
    public void setSunSign(String sunSign) { this.sunSign = sunSign; }
    
    public String getMoonSign() { return moonSign; }
    public void setMoonSign(String moonSign) { this.moonSign = moonSign; }
    
    public String getRisingSign() { return risingSign; }
    public void setRisingSign(String risingSign) { this.risingSign = risingSign; }
    
    public String getDominantElement() { return dominantElement; }
    public void setDominantElement(String dominantElement) { this.dominantElement = dominantElement; }
    
    public Map<String, Double> getPlanetaryPositions() { return planetaryPositions; }
    public void setPlanetaryPositions(Map<String, Double> planetaryPositions) { 
        this.planetaryPositions = planetaryPositions != null ? planetaryPositions : new HashMap<>();
    }
    
    public Map<String, Map<String, Object>> getPlanetaryDetails() { return planetaryDetails; }
    public void setPlanetaryDetails(Map<String, Map<String, Object>> planetaryDetails) {
        this.planetaryDetails = planetaryDetails;
    }
    
    public String getChartId() { return chartId; }
    public void setChartId(String chartId) { this.chartId = chartId; }
    
    public LocalDateTime getCalculatedAt() { return calculatedAt; }
    public void setCalculatedAt(LocalDateTime calculatedAt) { this.calculatedAt = calculatedAt; }
    
    // String setter for compatibility with existing code
    public void setCalculatedAt(String calculatedAtString) {
        try {
            this.calculatedAt = LocalDateTime.parse(calculatedAtString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (Exception e) {
            this.calculatedAt = LocalDateTime.now();
        }
    }
    
    public String getBirthLocation() { return birthLocation; }
    public void setBirthLocation(String birthLocation) { this.birthLocation = birthLocation; }
    
    public LocalDateTime getBirthDateTime() { return birthDateTime; }
    public void setBirthDateTime(LocalDateTime birthDateTime) { this.birthDateTime = birthDateTime; }
    
    public Double getBirthLatitude() { return birthLatitude; }
    public void setBirthLatitude(Double birthLatitude) { this.birthLatitude = birthLatitude; }
    
    public Double getBirthLongitude() { return birthLongitude; }
    public void setBirthLongitude(Double birthLongitude) { this.birthLongitude = birthLongitude; }
    
    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }
    
    // Vedic-specific getters and setters
    public Double getAyanamsa() { return ayanamsa; }
    public void setAyanamsa(Double ayanamsa) { this.ayanamsa = ayanamsa; }
    
    public String getAyanamsaType() { return ayanamsaType; }
    public void setAyanamsaType(String ayanamsaType) { this.ayanamsaType = ayanamsaType; }
    
    public String getSystem() { return system; }
    public void setSystem(String system) { this.system = system; }
    
    public String getAccuracy() { return accuracy; }
    public void setAccuracy(String accuracy) { this.accuracy = accuracy; }
    
    public Map<String, Double> getHouses() { return houses; }
    public void setHouses(Map<String, Double> houses) { 
        this.houses = houses != null ? houses : new HashMap<>();
    }
    
    public Map<String, Map<String, Object>> getVedicHouses() { return vedicHouses; }
    public void setVedicHouses(Map<String, Map<String, Object>> vedicHouses) {
        this.vedicHouses = vedicHouses;
    }
    
    public String getHouseSystem() { return houseSystem; }
    public void setHouseSystem(String houseSystem) { this.houseSystem = houseSystem; }
    
    public Map<String, Map<String, Object>> getNakshatras() { return nakshatras; }
    public void setNakshatras(Map<String, Map<String, Object>> nakshatras) {
        this.nakshatras = nakshatras != null ? nakshatras : new HashMap<>();
        
        // Extract Moon's nakshatra if available
        if (nakshatras != null && nakshatras.containsKey("Moon")) {
            Map<String, Object> moonNakshatraData = nakshatras.get("Moon");
            this.moonNakshatra = (String) moonNakshatraData.get("nakshatra");
            this.moonPada = (Integer) moonNakshatraData.get("pada");
        }
    }
    
    public String getMoonNakshatra() { return moonNakshatra; }
    public void setMoonNakshatra(String moonNakshatra) { this.moonNakshatra = moonNakshatra; }
    
    public Integer getMoonPada() { return moonPada; }
    public void setMoonPada(Integer moonPada) { this.moonPada = moonPada; }
    
    public List<Map<String, Object>> getAspects() { return aspects; }
    public void setAspects(List<Map<String, Object>> aspects) {
        this.aspects = aspects != null ? aspects : new ArrayList<>();
    }
    
    public List<Map<String, Object>> getVedicAspects() { return vedicAspects; }
    public void setVedicAspects(List<Map<String, Object>> vedicAspects) {
        this.vedicAspects = vedicAspects;
    }
    
    public Map<String, Double> getPlanetaryStrengths() { return planetaryStrengths; }
    public void setPlanetaryStrengths(Map<String, Double> planetaryStrengths) {
        this.planetaryStrengths = planetaryStrengths != null ? planetaryStrengths : new HashMap<>();
    }
    
    public Map<String, String> getPlanetaryStates() { return planetaryStates; }
    public void setPlanetaryStates(Map<String, String> planetaryStates) {
        this.planetaryStates = planetaryStates;
    }
    
    public String getLagnaLord() { return lagnaLord; }
    public void setLagnaLord(String lagnaLord) { this.lagnaLord = lagnaLord; }
    
    public String getNakshatraLord() { return nakshatraLord; }
    public void setNakshatraLord(String nakshatraLord) { this.nakshatraLord = nakshatraLord; }
    
    public String getChandraLagna() { return chandraLagna; }
    public void setChandraLagna(String chandraLagna) { this.chandraLagna = chandraLagna; }
    
    public String getSuryaLagna() { return suryaLagna; }
    public void setSuryaLagna(String suryaLagna) { this.suryaLagna = suryaLagna; }
    
    public List<String> getYogas() { return yogas; }
    public void setYogas(List<String> yogas) { this.yogas = yogas; }
    
    public List<String> getDoshas() { return doshas; }
    public void setDoshas(List<String> doshas) { this.doshas = doshas; }
    
    public String getVimsottariDasha() { return vimsottariDasha; }
    public void setVimsottariDasha(String vimsottariDasha) { this.vimsottariDasha = vimsottariDasha; }
    
    public Map<String, Integer> getElementDistribution() { return elementDistribution; }
    public void setElementDistribution(Map<String, Integer> elementDistribution) {
        this.elementDistribution = elementDistribution;
    }
    
    public Map<String, Integer> getQualityDistribution() { return qualityDistribution; }
    public void setQualityDistribution(Map<String, Integer> qualityDistribution) {
        this.qualityDistribution = qualityDistribution;
    }
    
    public List<String> getGemstoneRecommendations() { return gemstoneRecommendations; }
    public void setGemstoneRecommendations(List<String> gemstoneRecommendations) {
        this.gemstoneRecommendations = gemstoneRecommendations;
    }
    
    public List<String> getMantraRecommendations() { return mantraRecommendations; }
    public void setMantraRecommendations(List<String> mantraRecommendations) {
        this.mantraRecommendations = mantraRecommendations;
    }
    
    public String getLuckyColor() { return luckyColor; }
    public void setLuckyColor(String luckyColor) { this.luckyColor = luckyColor; }
    
    public List<Integer> getLuckyNumbers() { return luckyNumbers; }
    public void setLuckyNumbers(List<Integer> luckyNumbers) { this.luckyNumbers = luckyNumbers; }
    
    public String getFavorableDirection() { return favorableDirection; }
    public void setFavorableDirection(String favorableDirection) { this.favorableDirection = favorableDirection; }
    
    public String getPersonalityProfile() { return personalityProfile; }
    public void setPersonalityProfile(String personalityProfile) { this.personalityProfile = personalityProfile; }
    
    public String getCareerIndications() { return careerIndications; }
    public void setCareerIndications(String careerIndications) { this.careerIndications = careerIndications; }
    
    public String getRelationshipTendencies() { return relationshipTendencies; }
    public void setRelationshipTendencies(String relationshipTendencies) { this.relationshipTendencies = relationshipTendencies; }
    
    public String getHealthIndications() { return healthIndications; }
    public void setHealthIndications(String healthIndications) { this.healthIndications = healthIndications; }
    
    public String getSpiritualPath() { return spiritualPath; }
    public void setSpiritualPath(String spiritualPath) { this.spiritualPath = spiritualPath; }
    
    public Double getJulianDay() { return julianDay; }
    public void setJulianDay(Double julianDay) { this.julianDay = julianDay; }
    
    public String getEphemerisUsed() { return ephemerisUsed; }
    public void setEphemerisUsed(String ephemerisUsed) { this.ephemerisUsed = ephemerisUsed; }
    
    public String getCalculationSource() { return calculationSource; }
    public void setCalculationSource(String calculationSource) { this.calculationSource = calculationSource; }
    
    public Boolean getIsValidChart() { return isValidChart; }
    public void setIsValidChart(Boolean isValidChart) { this.isValidChart = isValidChart; }
    
    @JsonProperty("valid")
    public Boolean isValid() { return isValidChart; }
    
    public List<String> getValidationMessages() { return validationMessages; }
    public void setValidationMessages(List<String> validationMessages) {
        this.validationMessages = validationMessages != null ? validationMessages : new ArrayList<>();
    }
    
    public String getChartQuality() { return chartQuality; }
    public void setChartQuality(String chartQuality) { this.chartQuality = chartQuality; }
    
    // ================ UTILITY METHODS ================
    
    /**
     * Add a validation message to the chart
     */
    public void addValidationMessage(String message) {
        if (validationMessages == null) {
            validationMessages = new ArrayList<>();
        }
        validationMessages.add(message);
    }
    
    /**
     * Get formatted birth coordinates
     */
    public String getFormattedCoordinates() {
        if (birthLatitude == null || birthLongitude == null) {
            return "Coordinates not available";
        }
        
        String latDir = birthLatitude >= 0 ? "N" : "S";
        String lonDir = birthLongitude >= 0 ? "E" : "W";
        
        return String.format("%.4f°%s, %.4f°%s", 
                           Math.abs(birthLatitude), latDir, 
                           Math.abs(birthLongitude), lonDir);
    }
    
    /**
     * Get Vedic chart summary
     */
    public String getVedicSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("🕉️ Vedic Chart Summary:\n");
        summary.append("Sun: ").append(sunSign != null ? sunSign : "Unknown").append("\n");
        summary.append("Moon: ").append(moonSign != null ? moonSign : "Unknown");
        
        if (moonNakshatra != null) {
            summary.append(" (").append(moonNakshatra).append(" Nakshatra");
            if (moonPada != null) {
                summary.append(", Pada ").append(moonPada);
            }
            summary.append(")");
        }
        summary.append("\n");
        
        summary.append("Lagna: ").append(risingSign != null ? risingSign : "Unknown").append("\n");
        summary.append("Dominant Element: ").append(dominantElement != null ? dominantElement : "Unknown");
        
        if (ayanamsa != null) {
            summary.append("\nAyanamsa: ").append(String.format("%.4f°", ayanamsa));
        }
        
        return summary.toString();
    }
    
    /**
     * Get planet in specific sign
     */
    public String getPlanetSign(String planetName) {
        if (planetaryPositions == null || !planetaryPositions.containsKey(planetName)) {
            return "Unknown";
        }
        
        double position = planetaryPositions.get(planetName);
        int signIndex = (int) (position / 30);
        
        String[] signs = {"Aries", "Taurus", "Gemini", "Cancer", "Leo", "Virgo",
                         "Libra", "Scorpio", "Sagittarius", "Capricorn", "Aquarius", "Pisces"};
        
        return signs[signIndex % 12];
    }
    
    /**
     * Check if chart has complete Vedic data
     */
    public boolean hasCompleteVedicData() {
        return planetaryPositions != null && !planetaryPositions.isEmpty() &&
               houses != null && !houses.isEmpty() &&
               ayanamsa != null &&
               nakshatras != null && !nakshatras.isEmpty();
    }
    
    /**
     * Get chart computation time in milliseconds since epoch
     */
    public long getCalculationTimestamp() {
        return calculatedAt != null ? 
               calculatedAt.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli() : 
               System.currentTimeMillis();
    }
    
    /**
     * Get Vedic chart type description
     */
    public String getChartTypeDescription() {
        if (system == null) return "Standard Chart";
        
        if (system.toLowerCase().contains("vedic") || system.toLowerCase().contains("sidereal")) {
            return "Vedic Sidereal Chart";
        } else if (system.toLowerCase().contains("tropical")) {
            return "Tropical Western Chart";
        } else {
            return "Custom Chart System";
        }
    }
    
    @Override
    public String toString() {
        return String.format("BirthChartResponse{sunSign='%s', moonSign='%s', risingSign='%s', " +
                           "dominantElement='%s', ayanamsa=%.4f, system='%s', calculatedAt=%s}",
                           sunSign, moonSign, risingSign, dominantElement, ayanamsa, system, calculatedAt);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        BirthChartResponse that = (BirthChartResponse) obj;
        return Objects.equals(chartId, that.chartId) &&
               Objects.equals(birthDateTime, that.birthDateTime) &&
               Objects.equals(birthLatitude, that.birthLatitude) &&
               Objects.equals(birthLongitude, that.birthLongitude) &&
               Objects.equals(sunSign, that.sunSign) &&
               Objects.equals(moonSign, that.moonSign) &&
               Objects.equals(risingSign, that.risingSign);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(chartId, birthDateTime, birthLatitude, birthLongitude, 
                          sunSign, moonSign, risingSign);
    }
}
