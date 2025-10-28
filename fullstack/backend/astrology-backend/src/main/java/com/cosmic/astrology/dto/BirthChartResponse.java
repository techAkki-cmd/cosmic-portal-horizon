package com.cosmic.astrology.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * ✅ COMPLETE Birth Chart Response DTO for Vedic Astrology Platform
 * Contains ALL properties needed for PersonalizedBirthChart frontend integration
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BirthChartResponse {
    
    // ================ BASIC ASTROLOGICAL SIGNS ================
    private String sunSign;
    private String moonSign;
    private String risingSign;
    private String dominantElement;
    private String dominantPlanet; // ✅ REQUIRED by frontend
    
    // ================ PLANETARY DATA ================
    private Map<String, Double> planetaryPositions;
    private Map<String, Map<String, Object>> planetaryDetails;
    private Map<String, Double> planetaryStrengths;
    private Map<String, String> planetaryStates;
    
    // ================ CHART METADATA ================
    private String chartId;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime calculatedAt;
    
    // ================ BIRTH INFORMATION ================
    private String birthLocation;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime birthDateTime;
    
    private Double birthLatitude;
    private Double birthLongitude;
    private String timezone;
    
    // ================ VEDIC-SPECIFIC DATA ================
    private Double ayanamsa;
    private String ayanamsaType;
    private String system;
    private String accuracy;
    
    // ================ HOUSE SYSTEM ================
    private Map<String, Double> houses;
    private Map<String, Map<String, Object>> vedicHouses;
    private String houseSystem;
    private List<Map<String, Object>> houseAnalysis; // ✅ REQUIRED by frontend
    
    // ================ NAKSHATRA SYSTEM ================
    private Map<String, Map<String, Object>> nakshatras;
    private String moonNakshatra;
    private Integer moonPada;
    
    // ================ ASPECTS & RELATIONSHIPS ================
    private List<Map<String, Object>> aspects;
    private List<Map<String, Object>> vedicAspects;
    
    // ================ VEDIC LORDSHIPS ================
    private String lagnaLord;
    private String nakshatraLord;
    private String chandraLagna;
    private String suryaLagna;
    
    // ================ YOGAS & COMBINATIONS ================
    private List<String> yogas;
    private List<String> doshas;
    private String vimsottariDasha;
    private List<Map<String, Object>> rareYogas; // ✅ REQUIRED by frontend
    
    // ================ DASHA & REMEDIES ================
    private List<Map<String, Object>> dashaTable; // ✅ REQUIRED by frontend
    private List<Map<String, Object>> personalizedRemedies; // ✅ REQUIRED by frontend
    
    // ================ ELEMENTAL ANALYSIS ================
    private Map<String, Integer> elementDistribution;
    private Map<String, Integer> qualityDistribution;
    
    // ================ REMEDIES & RECOMMENDATIONS ================
    private List<String> gemstoneRecommendations;
    private List<String> mantraRecommendations;
    private String luckyColor;
    private List<Integer> luckyNumbers;
    private String favorableDirection;
    
    // ================ INTERPRETATIONS ================
    private String personalityProfile;
    private String careerIndications;
    private String relationshipTendencies;
    private String healthIndications;
    private String spiritualPath;
    
    // ================ TECHNICAL DATA ================
    private Double julianDay;
    private String ephemerisUsed;
    private String calculationSource;
    
    // ================ PERSONAL INFORMATION ================
    private Map<String, Object> personalInfo; // ✅ REQUIRED by frontend
    
    // ================ VALIDATION & QUALITY ================
    private Boolean isValid;
    private List<String> validationMessages;
    private String chartQuality;
    
    // ================ CONSTRUCTORS ================
    
    /**
     * Default constructor with complete initialization
     */
    public BirthChartResponse() {
        this.calculatedAt = LocalDateTime.now();
        
        // Initialize all Maps
        this.planetaryPositions = new HashMap<>();
        this.planetaryDetails = new HashMap<>();
        this.planetaryStrengths = new HashMap<>();
        this.planetaryStates = new HashMap<>();
        this.houses = new HashMap<>();
        this.vedicHouses = new HashMap<>();
        this.nakshatras = new HashMap<>();
        this.elementDistribution = new HashMap<>();
        this.qualityDistribution = new HashMap<>();
        this.personalInfo = new HashMap<>(); // ✅ INITIALIZE
        
        // Initialize all Lists
        this.aspects = new ArrayList<>();
        this.vedicAspects = new ArrayList<>();
        this.validationMessages = new ArrayList<>();
        this.yogas = new ArrayList<>();
        this.doshas = new ArrayList<>();
        this.gemstoneRecommendations = new ArrayList<>();
        this.mantraRecommendations = new ArrayList<>();
        this.luckyNumbers = new ArrayList<>();
        
        // ✅ INITIALIZE FRONTEND REQUIRED LISTS
        this.houseAnalysis = new ArrayList<>();
        this.rareYogas = new ArrayList<>();
        this.dashaTable = new ArrayList<>();
        this.personalizedRemedies = new ArrayList<>();
        
        this.isValid = true;
    }
    
    /**
     * Enhanced constructor for basic chart creation
     */
    public BirthChartResponse(String sunSign, String moonSign, String risingSign, 
                             String dominantElement, Map<String, Double> planetaryPositions) {
        this();
        this.sunSign = sunSign;
        this.moonSign = moonSign;
        this.risingSign = risingSign;
        this.dominantElement = dominantElement;
        if (planetaryPositions != null) {
            this.planetaryPositions.putAll(planetaryPositions);
        }
    }
    
    /**
     * Comprehensive constructor for Vedic charts
     */
    public BirthChartResponse(String sunSign, String moonSign, String risingSign, 
                             String dominantElement, Map<String, Double> planetaryPositions,
                             Double ayanamsa, String ayanamsaType, String system, 
                             Map<String, Double> houses) {
        this(sunSign, moonSign, risingSign, dominantElement, planetaryPositions);
        this.ayanamsa = ayanamsa;
        this.ayanamsaType = ayanamsaType;
        this.system = system;
        if (houses != null) {
            this.houses.putAll(houses);
        }
    }
    
    // ================ GETTERS AND SETTERS ================
    
    public String getSunSign() { return sunSign; }
    public void setSunSign(String sunSign) { this.sunSign = sunSign; }
    
    public String getMoonSign() { return moonSign; }
    public void setMoonSign(String moonSign) { this.moonSign = moonSign; }
    
    public String getRisingSign() { return risingSign; }
    public void setRisingSign(String risingSign) { this.risingSign = risingSign; }
    
    // ✅ FRONTEND COMPATIBILITY - ascendantSign maps to risingSign
    @JsonProperty("ascendantSign")
    public String getAscendantSign() {
        return this.risingSign;
    }
    
    public String getDominantElement() { return dominantElement; }
    public void setDominantElement(String dominantElement) { this.dominantElement = dominantElement; }
    
    // ✅ REQUIRED BY FRONTEND
    public String getDominantPlanet() { return dominantPlanet; }
    public void setDominantPlanet(String dominantPlanet) { this.dominantPlanet = dominantPlanet; }
    
    public Map<String, Double> getPlanetaryPositions() { return planetaryPositions; }
    public void setPlanetaryPositions(Map<String, Double> planetaryPositions) { 
        this.planetaryPositions = planetaryPositions != null ? planetaryPositions : new HashMap<>();
    }
    
    public Map<String, Map<String, Object>> getPlanetaryDetails() { return planetaryDetails; }
    public void setPlanetaryDetails(Map<String, Map<String, Object>> planetaryDetails) {
        this.planetaryDetails = planetaryDetails != null ? planetaryDetails : new HashMap<>();
    }
    
    public String getChartId() { return chartId; }
    public void setChartId(String chartId) { this.chartId = chartId; }
    
    public LocalDateTime getCalculatedAt() { return calculatedAt; }
    public void setCalculatedAt(LocalDateTime calculatedAt) { 
        this.calculatedAt = calculatedAt != null ? calculatedAt : LocalDateTime.now(); 
    }
    
    /**
     * Enhanced string setter with better error handling
     */
    public void setCalculatedAt(String calculatedAtString) {
        if (calculatedAtString == null || calculatedAtString.trim().isEmpty()) {
            this.calculatedAt = LocalDateTime.now();
            return;
        }
        
        try {
            this.calculatedAt = LocalDateTime.parse(calculatedAtString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (DateTimeParseException e) {
            try {
                this.calculatedAt = LocalDateTime.parse(calculatedAtString);
            } catch (DateTimeParseException e2) {
                this.calculatedAt = LocalDateTime.now();
                addValidationMessage("Invalid calculatedAt format: " + calculatedAtString);
            }
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
        this.vedicHouses = vedicHouses != null ? vedicHouses : new HashMap<>();
    }
    
    public String getHouseSystem() { return houseSystem; }
    public void setHouseSystem(String houseSystem) { this.houseSystem = houseSystem; }
    
    // ✅ REQUIRED BY FRONTEND - House Analysis
    public List<Map<String, Object>> getHouseAnalysis() { 
        return houseAnalysis; 
    }
    
    public void setHouseAnalysis(List<Map<String, Object>> houseAnalysis) { 
        this.houseAnalysis = houseAnalysis != null ? houseAnalysis : new ArrayList<>();
    }
    
    public Map<String, Map<String, Object>> getNakshatras() { return nakshatras; }
    public void setNakshatras(Map<String, Map<String, Object>> nakshatras) {
        this.nakshatras = nakshatras != null ? nakshatras : new HashMap<>();
        
        if (nakshatras != null && nakshatras.containsKey("Moon")) {
            Map<String, Object> moonNakshatraData = nakshatras.get("Moon");
            if (moonNakshatraData != null) {
                Object nakshatraObj = moonNakshatraData.get("nakshatra");
                if (nakshatraObj instanceof String) {
                    this.moonNakshatra = (String) nakshatraObj;
                }
                
                Object padaObj = moonNakshatraData.get("pada");
                if (padaObj instanceof Integer) {
                    this.moonPada = (Integer) padaObj;
                } else if (padaObj instanceof Number) {
                    this.moonPada = ((Number) padaObj).intValue();
                }
            }
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
        this.vedicAspects = vedicAspects != null ? vedicAspects : new ArrayList<>();
    }
    
    public Map<String, Double> getPlanetaryStrengths() { return planetaryStrengths; }
    public void setPlanetaryStrengths(Map<String, Double> planetaryStrengths) {
        this.planetaryStrengths = planetaryStrengths != null ? planetaryStrengths : new HashMap<>();
    }
    
    public Map<String, String> getPlanetaryStates() { return planetaryStates; }
    public void setPlanetaryStates(Map<String, String> planetaryStates) {
        this.planetaryStates = planetaryStates != null ? planetaryStates : new HashMap<>();
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
    public void setYogas(List<String> yogas) { 
        this.yogas = yogas != null ? yogas : new ArrayList<>();
    }
    
    public List<String> getDoshas() { return doshas; }
    public void setDoshas(List<String> doshas) { 
        this.doshas = doshas != null ? doshas : new ArrayList<>();
    }
    
    public String getVimsottariDasha() { return vimsottariDasha; }
    public void setVimsottariDasha(String vimsottariDasha) { this.vimsottariDasha = vimsottariDasha; }
    
    // ✅ REQUIRED BY FRONTEND - Rare Yogas
    public List<Map<String, Object>> getRareYogas() { 
        return rareYogas; 
    }
    
    public void setRareYogas(List<Map<String, Object>> rareYogas) { 
        this.rareYogas = rareYogas != null ? rareYogas : new ArrayList<>();
    }
    
    // ✅ REQUIRED BY FRONTEND - Dasha Table
    public List<Map<String, Object>> getDashaTable() { 
        return dashaTable; 
    }
    
    public void setDashaTable(List<Map<String, Object>> dashaTable) { 
        this.dashaTable = dashaTable != null ? dashaTable : new ArrayList<>();
    }
    
    // ✅ REQUIRED BY FRONTEND - Personalized Remedies
    public List<Map<String, Object>> getPersonalizedRemedies() { 
        return personalizedRemedies; 
    }
    
    public void setPersonalizedRemedies(List<Map<String, Object>> personalizedRemedies) { 
        this.personalizedRemedies = personalizedRemedies != null ? personalizedRemedies : new ArrayList<>();
    }
    
    public Map<String, Integer> getElementDistribution() { return elementDistribution; }
    public void setElementDistribution(Map<String, Integer> elementDistribution) {
        this.elementDistribution = elementDistribution != null ? elementDistribution : new HashMap<>();
    }
    
    public Map<String, Integer> getQualityDistribution() { return qualityDistribution; }
    public void setQualityDistribution(Map<String, Integer> qualityDistribution) {
        this.qualityDistribution = qualityDistribution != null ? qualityDistribution : new HashMap<>();
    }
    
    public List<String> getGemstoneRecommendations() { return gemstoneRecommendations; }
    public void setGemstoneRecommendations(List<String> gemstoneRecommendations) {
        this.gemstoneRecommendations = gemstoneRecommendations != null ? gemstoneRecommendations : new ArrayList<>();
    }
    
    public List<String> getMantraRecommendations() { return mantraRecommendations; }
    public void setMantraRecommendations(List<String> mantraRecommendations) {
        this.mantraRecommendations = mantraRecommendations != null ? mantraRecommendations : new ArrayList<>();
    }
    
    public String getLuckyColor() { return luckyColor; }
    public void setLuckyColor(String luckyColor) { this.luckyColor = luckyColor; }
    
    public List<Integer> getLuckyNumbers() { return luckyNumbers; }
    public void setLuckyNumbers(List<Integer> luckyNumbers) {
        this.luckyNumbers = luckyNumbers != null ? luckyNumbers : new ArrayList<>();
    }
    
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
    
    // ✅ REQUIRED BY FRONTEND - Personal Information
    public Map<String, Object> getPersonalInfo() { 
        return personalInfo; 
    }
    
    public void setPersonalInfo(Map<String, Object> personalInfo) { 
        this.personalInfo = personalInfo != null ? personalInfo : new HashMap<>();
    }
    
    // Enhanced isValid getter with multiple naming support
    public Boolean getIsValid() { return isValid; }
    public void setIsValid(Boolean isValid) { this.isValid = isValid != null ? isValid : true; }
    
    @JsonProperty("valid")
    public Boolean isValid() { return isValid; }
    
    public List<String> getValidationMessages() { return validationMessages; }
    public void setValidationMessages(List<String> validationMessages) {
        this.validationMessages = validationMessages != null ? validationMessages : new ArrayList<>();
    }
    
    public String getChartQuality() { return chartQuality; }
    public void setChartQuality(String chartQuality) { this.chartQuality = chartQuality; }
    
    // ================ ENHANCED UTILITY METHODS ================
    
    /**
     * Add a validation message with timestamp
     */
    public void addValidationMessage(String message) {
        if (validationMessages == null) {
            validationMessages = new ArrayList<>();
        }
        String timestampedMessage = String.format("[%s] %s", 
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")), message);
        validationMessages.add(timestampedMessage);
    }
    
    /**
     * Enhanced coordinate formatting
     */
    public String getFormattedCoordinates() {
        if (birthLatitude == null || birthLongitude == null) {
            return "Coordinates not available";
        }
        
        String latDir = birthLatitude >= 0 ? "N" : "S";
        String lonDir = birthLongitude >= 0 ? "E" : "W";
        
        return String.format("%.6f°%s, %.6f°%s", 
                           Math.abs(birthLatitude), latDir, 
                           Math.abs(birthLongitude), lonDir);
    }
    
    /**
     * Get planet's zodiac sign with enhanced error handling
     */
    public String getPlanetSign(String planetName) {
        if (planetName == null || planetaryPositions == null || !planetaryPositions.containsKey(planetName)) {
            return "Unknown";
        }
        
        try {
            double position = planetaryPositions.get(planetName);
            // Normalize to 0-360 range
            position = ((position % 360) + 360) % 360;
            int signIndex = (int) (position / 30);
            
            String[] signs = {"Aries", "Taurus", "Gemini", "Cancer", "Leo", "Virgo",
                             "Libra", "Scorpio", "Sagittarius", "Capricorn", "Aquarius", "Pisces"};
            
            return signs[Math.min(signIndex, 11)];
        } catch (Exception e) {
            addValidationMessage("Error calculating sign for planet: " + planetName);
            return "Unknown";
        }
    }
    
    /**
     * Enhanced completeness check
     */
    public boolean hasCompleteVedicData() {
        boolean hasBasics = planetaryPositions != null && !planetaryPositions.isEmpty() &&
                           houses != null && !houses.isEmpty() &&
                           ayanamsa != null &&
                           nakshatras != null && !nakshatras.isEmpty();
        
        if (!hasBasics) {
            return false;
        }
        
        // Check for essential planets
        String[] essentialPlanets = {"Sun", "Moon", "Mars", "Mercury", "Jupiter", "Venus", "Saturn"};
        for (String planet : essentialPlanets) {
            if (!planetaryPositions.containsKey(planet)) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Get calculation timestamp in milliseconds
     */
    @JsonIgnore
    public long getCalculationTimestamp() {
        return calculatedAt != null ? 
               calculatedAt.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli() : 
               System.currentTimeMillis();
    }
    
    // ================ ENHANCED OBJECT METHODS ================
    
    @Override
    public String toString() {
        return String.format(
            "BirthChartResponse{sunSign='%s', moonSign='%s', risingSign='%s', " +
            "dominantElement='%s', dominantPlanet='%s', ayanamsa=%.4f, system='%s', " +
            "quality='%s', calculatedAt=%s}",
            sunSign, moonSign, risingSign, dominantElement, dominantPlanet, 
            ayanamsa != null ? ayanamsa : 0.0, system, chartQuality, calculatedAt);
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
               Objects.equals(risingSign, that.risingSign) &&
               Objects.equals(ayanamsa, that.ayanamsa);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(chartId, birthDateTime, birthLatitude, birthLongitude, 
                          sunSign, moonSign, risingSign, ayanamsa);
    }
}
