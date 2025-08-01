package com.cosmic.astrology.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Comprehensive Transit Response DTO for Vedic Astrology
 * Contains all planetary transit information including Vedic-specific data
 */
public class TransitResponse {
    
    // Basic planetary information
    private String planet;
    private Double position;
    private String sign;
    private String aspect;
    
    // Vedic-specific information
    private String nakshatra;
    private Integer pada;
    private String rashi; // Sanskrit name for sign
    private String house; // Which house the planet is transiting
    
    // Movement and timing information
    private Boolean isRetrograde;
    private Double speed; // Daily motion in degrees
    private String movement; // "Direct", "Retrograde", "Stationary"
    
    // Influence and interpretation
    private String influence;
    private String significance;
    private Integer intensityLevel; // 1-5 scale
    private String vedicInterpretation;
    
    // Technical astronomical data
    private Double longitude;
    private Double latitude;
    private Double distance; // From Earth in AU
    
    // Timing information
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime calculatedAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime nextSignChange;
    
    private Long daysInCurrentSign;
    
    // Aspects and relationships
    private List<String> currentAspects;
    private List<String> upcomingAspects;
    
    // User-friendly information
    private String element; // Fire, Earth, Air, Water
    private String quality; // Cardinal, Fixed, Mutable
    private String rulingPlanet;
    private String gemstone;
    private String mantra;
    private String color;
    
    // Default constructor
    public TransitResponse() {}
    
    // Basic constructor
    public TransitResponse(String planet, Double position, String sign) {
        this.planet = planet;
        this.position = position;
        this.sign = sign;
        this.calculatedAt = LocalDateTime.now();
    }
    
    // Comprehensive constructor
    public TransitResponse(String planet, Double position, String sign, String nakshatra, 
                          Integer pada, Boolean isRetrograde, String influence) {
        this.planet = planet;
        this.position = position;
        this.sign = sign;
        this.nakshatra = nakshatra;
        this.pada = pada;
        this.isRetrograde = isRetrograde;
        this.influence = influence;
        this.calculatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public String getPlanet() {
        return planet;
    }
    
    public void setPlanet(String planet) {
        this.planet = planet;
    }
    
    public Double getPosition() {
        return position;
    }
    
    public void setPosition(Double position) {
        this.position = position;
    }
    
    public String getSign() {
        return sign;
    }
    
    public void setSign(String sign) {
        this.sign = sign;
    }
    
    public String getAspect() {
        return aspect;
    }
    
    public void setAspect(String aspect) {
        this.aspect = aspect;
    }
    
    public String getNakshatra() {
        return nakshatra;
    }
    
    public void setNakshatra(String nakshatra) {
        this.nakshatra = nakshatra;
    }
    
    public Integer getPada() {
        return pada;
    }
    
    public void setPada(Integer pada) {
        this.pada = pada;
    }
    
    public String getRashi() {
        return rashi;
    }
    
    public void setRashi(String rashi) {
        this.rashi = rashi;
    }
    
    public String getHouse() {
        return house;
    }
    
    public void setHouse(String house) {
        this.house = house;
    }
    
    public Boolean getIsRetrograde() {
        return isRetrograde;
    }
    
    public void setIsRetrograde(Boolean isRetrograde) {
        this.isRetrograde = isRetrograde;
    }
    
    // Alias for JSON compatibility
    @JsonProperty("retrograde")
    public Boolean isRetrograde() {
        return isRetrograde;
    }
    
    public Double getSpeed() {
        return speed;
    }
    
    public void setSpeed(Double speed) {
        this.speed = speed;
    }
    
    public String getMovement() {
        return movement;
    }
    
    public void setMovement(String movement) {
        this.movement = movement;
    }
    
    public String getInfluence() {
        return influence;
    }
    
    public void setInfluence(String influence) {
        this.influence = influence;
    }
    
    public String getSignificance() {
        return significance;
    }
    
    public void setSignificance(String significance) {
        this.significance = significance;
    }
    
    public Integer getIntensityLevel() {
        return intensityLevel;
    }
    
    public void setIntensityLevel(Integer intensityLevel) {
        this.intensityLevel = intensityLevel;
    }
    
    public String getVedicInterpretation() {
        return vedicInterpretation;
    }
    
    public void setVedicInterpretation(String vedicInterpretation) {
        this.vedicInterpretation = vedicInterpretation;
    }
    
    public Double getLongitude() {
        return longitude;
    }
    
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    
    public Double getLatitude() {
        return latitude;
    }
    
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    
    public Double getDistance() {
        return distance;
    }
    
    public void setDistance(Double distance) {
        this.distance = distance;
    }
    
    public LocalDateTime getCalculatedAt() {
        return calculatedAt;
    }
    
    public void setCalculatedAt(LocalDateTime calculatedAt) {
        this.calculatedAt = calculatedAt;
    }
    
    public LocalDateTime getNextSignChange() {
        return nextSignChange;
    }
    
    public void setNextSignChange(LocalDateTime nextSignChange) {
        this.nextSignChange = nextSignChange;
    }
    
    public Long getDaysInCurrentSign() {
        return daysInCurrentSign;
    }
    
    public void setDaysInCurrentSign(Long daysInCurrentSign) {
        this.daysInCurrentSign = daysInCurrentSign;
    }
    
    public List<String> getCurrentAspects() {
        return currentAspects;
    }
    
    public void setCurrentAspects(List<String> currentAspects) {
        this.currentAspects = currentAspects;
    }
    
    public List<String> getUpcomingAspects() {
        return upcomingAspects;
    }
    
    public void setUpcomingAspects(List<String> upcomingAspects) {
        this.upcomingAspects = upcomingAspects;
    }
    
    public String getElement() {
        return element;
    }
    
    public void setElement(String element) {
        this.element = element;
    }
    
    public String getQuality() {
        return quality;
    }
    
    public void setQuality(String quality) {
        this.quality = quality;
    }
    
    public String getRulingPlanet() {
        return rulingPlanet;
    }
    
    public void setRulingPlanet(String rulingPlanet) {
        this.rulingPlanet = rulingPlanet;
    }
    
    public String getGemstone() {
        return gemstone;
    }
    
    public void setGemstone(String gemstone) {
        this.gemstone = gemstone;
    }
    
    public String getMantra() {
        return mantra;
    }
    
    public void setMantra(String mantra) {
        this.mantra = mantra;
    }
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
    
    // Utility methods
    public String getFormattedPosition() {
        if (position == null) return "N/A";
        int degrees = (int) Math.floor(position);
        int minutes = (int) Math.floor((position - degrees) * 60);
        int seconds = (int) Math.floor(((position - degrees) * 60 - minutes) * 60);
        return String.format("%d°%02d'%02d\"", degrees, minutes, seconds);
    }
    
    public String getVedicDescription() {
        if (planet == null || sign == null) return "Position unknown";
        
        String retrogradeText = (isRetrograde != null && isRetrograde) ? " (Retrograde)" : "";
        String nakshatraText = (nakshatra != null) ? " in " + nakshatra + " Nakshatra" : "";
        String padaText = (pada != null) ? ", Pada " + pada : "";
        
        return String.format("%s in %s%s%s%s", planet, sign, nakshatraText, padaText, retrogradeText);
    }
    
    public String getPlanetaryStrength() {
        if (intensityLevel == null) return "Moderate";
        
        return switch (intensityLevel) {
            case 1 -> "Very Weak";
            case 2 -> "Weak";
            case 3 -> "Moderate";
            case 4 -> "Strong";
            case 5 -> "Very Strong";
            default -> "Moderate";
        };
    }
    
    public boolean isInOwnSign() {
        if (planet == null || sign == null) return false;
        
        // Basic own sign relationships in Vedic astrology
        return switch (planet.toLowerCase()) {
            case "sun" -> "Leo".equals(sign);
            case "moon" -> "Cancer".equals(sign);
            case "mercury" -> "Gemini".equals(sign) || "Virgo".equals(sign);
            case "venus" -> "Taurus".equals(sign) || "Libra".equals(sign);
            case "mars" -> "Aries".equals(sign) || "Scorpio".equals(sign);
            case "jupiter" -> "Sagittarius".equals(sign) || "Pisces".equals(sign);
            case "saturn" -> "Capricorn".equals(sign) || "Aquarius".equals(sign);
            default -> false;
        };
    }
    
    public boolean isExalted() {
        if (planet == null || sign == null) return false;
        
        // Exaltation signs in Vedic astrology
        return switch (planet.toLowerCase()) {
            case "sun" -> "Aries".equals(sign);
            case "moon" -> "Taurus".equals(sign);
            case "mercury" -> "Virgo".equals(sign);
            case "venus" -> "Pisces".equals(sign);
            case "mars" -> "Capricorn".equals(sign);
            case "jupiter" -> "Cancer".equals(sign);
            case "saturn" -> "Libra".equals(sign);
            default -> false;
        };
    }
    
    public boolean isDebilitated() {
        if (planet == null || sign == null) return false;
        
        // Debilitation signs in Vedic astrology
        return switch (planet.toLowerCase()) {
            case "sun" -> "Libra".equals(sign);
            case "moon" -> "Scorpio".equals(sign);
            case "mercury" -> "Pisces".equals(sign);
            case "venus" -> "Virgo".equals(sign);
            case "mars" -> "Cancer".equals(sign);
            case "jupiter" -> "Capricorn".equals(sign);
            case "saturn" -> "Aries".equals(sign);
            default -> false;
        };
    }
    
    @Override
    public String toString() {
        return String.format("TransitResponse{planet='%s', position=%.4f, sign='%s', nakshatra='%s', pada=%d, retrograde=%s}",
                planet, position, sign, nakshatra, pada, isRetrograde);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        TransitResponse that = (TransitResponse) obj;
        return Objects.equals(planet, that.planet) &&
               Objects.equals(position, that.position) &&
               Objects.equals(sign, that.sign) &&
               Objects.equals(nakshatra, that.nakshatra) &&
               Objects.equals(pada, that.pada);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(planet, position, sign, nakshatra, pada);
    }
}
