package com.cosmic.astrology.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Comprehensive Transit Response DTO for Vedic Astrology
 * Contains all planetary transit information including Vedic-specific data
 * 
 * @version 2.0
 * @since 2025-08-02
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransitResponse {
    
    // ================ BASIC PLANETARY INFORMATION ================
    
    @NotBlank(message = "Planet name is required")
    private String planet;
    
    @NotNull(message = "Position is required")
    @DecimalMin(value = "0.0", message = "Position must be between 0 and 360 degrees")
    @DecimalMax(value = "360.0", message = "Position must be between 0 and 360 degrees")
    private Double position;
    
    @NotBlank(message = "Sign is required")
    private String sign;
    
    private String aspect;
    private String transitType;
    
    // ================ VEDIC-SPECIFIC INFORMATION ================
    
    private String nakshatra;
    private Integer pada;
    private String rashi; // Sanskrit name for sign
    private String house; // Which house the planet is transiting
    private String nakshatraLord;
    private String signLord;
    
    // ================ MOVEMENT AND TIMING INFORMATION ================
    
    @JsonProperty("isRetrograde")
    private Boolean isRetrograde = false;
    
    private Double speed; // Daily motion in degrees
    private String movement; // "Direct", "Retrograde", "Stationary"
    private String combustion; // "Combust", "Not Combust"
    
    // ================ INFLUENCE AND INTERPRETATION ================
    
    private String influence;
    private String significance;
    private Integer intensityLevel = 3; // 1-5 scale, default moderate
    private String vedicInterpretation;
    private String beneficMalefic; // "Benefic", "Malefic", "Neutral"
    
    // ================ TECHNICAL ASTRONOMICAL DATA ================
    
    private Double longitude;
    private Double latitude;
    private Double distance; // From Earth in AU
    private String ayanamsa;
    
    // ================ TIMING INFORMATION ================
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime calculatedAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime nextSignChange;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime nextNakshatraChange;
    
    private Long daysInCurrentSign;
    private Long daysInCurrentNakshatra;
    
    // ================ ASPECTS AND RELATIONSHIPS ================
    
    private List<String> currentAspects = new ArrayList<>();
    private List<String> upcomingAspects = new ArrayList<>();
    private List<String> aspectingPlanets = new ArrayList<>();
    private List<String> aspectedBy = new ArrayList<>();
    
    // ================ USER-FRIENDLY INFORMATION ================
    
    private String element; // Fire, Earth, Air, Water
    private String quality; // Cardinal, Fixed, Mutable
    private String rulingPlanet;
    private String gemstone;
    private String mantra;
    private String color;
    private String metal;
    private String deity;
    private String yantra;
    
    // ================ ADDITIONAL VEDIC PROPERTIES ================
    
    private String varna; // Brahmin, Kshatriya, Vaishya, Shudra
    private String gana; // Deva, Manushya, Rakshasa
    private String nadi; // Adi, Madhya, Antya
    private String yoniAnimal; // Animal associated with nakshatra
    private String planetaryState; // Exalted, Own, Debilitated, Friend, Enemy, Neutral
    
    // ================ TRANSIT PREDICTIONS ================
    
    private List<String> predictions = new ArrayList<>();
    private List<String> remedies = new ArrayList<>();
    private List<String> favorableActivities = new ArrayList<>();
    private List<String> unfavorableActivities = new ArrayList<>();
    
    // ================ CONSTRUCTORS ================
    
    /**
     * Default constructor
     */
    public TransitResponse() {
        this.calculatedAt = LocalDateTime.now();
    }
    
    /**
     * Basic constructor
     */
    public TransitResponse(String planet, Double position, String sign) {
        this();
        this.planet = planet;
        this.position = position;
        this.sign = sign;
        calculateDerivedProperties();
    }
    
    /**
     * Enhanced constructor
     */
    public TransitResponse(String planet, Double position, String sign, String nakshatra, 
                          Integer pada, Boolean isRetrograde, String influence) {
        this();
        this.planet = planet;
        this.position = position;
        this.sign = sign;
        this.nakshatra = nakshatra;
        this.pada = pada;
        this.isRetrograde = isRetrograde;
        this.influence = influence;
        calculateDerivedProperties();
    }
    
    /**
     * Comprehensive constructor
     */
    public TransitResponse(String planet, Double position, String sign, String nakshatra, 
                          Integer pada, String house, Boolean isRetrograde, Double speed,
                          String influence, Integer intensityLevel) {
        this();
        this.planet = planet;
        this.position = position;
        this.sign = sign;
        this.nakshatra = nakshatra;
        this.pada = pada;
        this.house = house;
        this.isRetrograde = isRetrograde;
        this.speed = speed;
        this.influence = influence;
        this.intensityLevel = intensityLevel;
        calculateDerivedProperties();
    }
    
    // ================ CALCULATION METHODS ================
    
    /**
     * Calculate all derived properties based on basic planetary data
     */
    private void calculateDerivedProperties() {
        if (position != null && sign != null) {
            calculateNakshatraAndPada();
            calculatePlanetaryState();
            calculateElement();
            calculateQuality();
            calculateRulingPlanet();
            calculateVedicProperties();
            calculateMovementStatus();
            calculateBeneficMalefic();
        }
    }
    
    /**
     * Calculate Nakshatra and Pada from position
     */
    private void calculateNakshatraAndPada() {
        if (position == null) return;
        
        String[] nakshatras = {
            "Ashwini", "Bharani", "Krittika", "Rohini", "Mrigashira", "Ardra", "Punarvasu",
            "Pushya", "Ashlesha", "Magha", "Purva Phalguni", "Uttara Phalguni", "Hasta",
            "Chitra", "Swati", "Vishakha", "Anuradha", "Jyeshtha", "Mula", "Purva Ashadha",
            "Uttara Ashadha", "Shravana", "Dhanishta", "Shatabhisha", "Purva Bhadrapada",
            "Uttara Bhadrapada", "Revati"
        };
        
        // Each nakshatra is 13.333... degrees
        double nakshatraDegrees = 360.0 / 27.0;
        int nakshatraIndex = (int) (position / nakshatraDegrees);
        
        if (this.nakshatra == null && nakshatraIndex < nakshatras.length) {
            this.nakshatra = nakshatras[nakshatraIndex];
        }
        
        if (this.pada == null) {
            // Each nakshatra has 4 padas of 3.333... degrees each
            double padaDegrees = nakshatraDegrees / 4.0;
            double positionInNakshatra = position % nakshatraDegrees;
            this.pada = (int) (positionInNakshatra / padaDegrees) + 1;
        }
        
        // Set Nakshatra Lord
        if (this.nakshatraLord == null) {
            this.nakshatraLord = getNakshatraLordByName(this.nakshatra);
        }
    }
    
    /**
     * Calculate planetary state (Exalted, Own, Debilitated, etc.)
     */
    private void calculatePlanetaryState() {
        if (planet == null || sign == null) return;
        
        if (isExalted()) {
            this.planetaryState = "Exalted";
        } else if (isDebilitated()) {
            this.planetaryState = "Debilitated";
        } else if (isInOwnSign()) {
            this.planetaryState = "Own Sign";
        } else if (isInFriendSign()) {
            this.planetaryState = "Friend Sign";
        } else if (isInEnemySign()) {
            this.planetaryState = "Enemy Sign";
        } else {
            this.planetaryState = "Neutral";
        }
    }
    
    /**
     * Calculate element based on sign
     */
    private void calculateElement() {
        if (sign == null || element != null) return;
        
        element = switch (sign) {
            case "Aries", "Leo", "Sagittarius" -> "Fire";
            case "Taurus", "Virgo", "Capricorn" -> "Earth";
            case "Gemini", "Libra", "Aquarius" -> "Air";
            case "Cancer", "Scorpio", "Pisces" -> "Water";
            default -> "Unknown";
        };
    }
    
    /**
     * Calculate quality based on sign
     */
    private void calculateQuality() {
        if (sign == null || quality != null) return;
        
        quality = switch (sign) {
            case "Aries", "Cancer", "Libra", "Capricorn" -> "Cardinal";
            case "Taurus", "Leo", "Scorpio", "Aquarius" -> "Fixed";
            case "Gemini", "Virgo", "Sagittarius", "Pisces" -> "Mutable";
            default -> "Unknown";
        };
    }
    
    /**
     * Calculate ruling planet of the sign
     */
    private void calculateRulingPlanet() {
        if (sign == null || rulingPlanet != null) return;
        
        rulingPlanet = switch (sign) {
            case "Aries", "Scorpio" -> "Mars";
            case "Taurus", "Libra" -> "Venus";
            case "Gemini", "Virgo" -> "Mercury";
            case "Cancer" -> "Moon";
            case "Leo" -> "Sun";
            case "Sagittarius", "Pisces" -> "Jupiter";
            case "Capricorn", "Aquarius" -> "Saturn";
            default -> "Unknown";
        };
        
        this.signLord = this.rulingPlanet;
    }
    
    /**
     * Calculate Vedic properties
     */
    private void calculateVedicProperties() {
        calculateGemstoneAndMantra();
        calculateColorAndMetal();
        calculateVarnaGanaNadi();
        calculateDeityAndYantra();
    }
    
    private void calculateGemstoneAndMantra() {
        if (planet == null) return;
        
        Map<String, String> planetGems = Map.of(
            "Sun", "Ruby", "Moon", "Pearl", "Mars", "Red Coral",
            "Mercury", "Emerald", "Jupiter", "Yellow Sapphire",
            "Venus", "Diamond", "Saturn", "Blue Sapphire"
        );
        
        Map<String, String> planetMantras = Map.of(
            "Sun", "Om Suryaya Namaha",
            "Moon", "Om Chandraya Namaha",
            "Mars", "Om Angarakaya Namaha",
            "Mercury", "Om Budhaya Namaha",
            "Jupiter", "Om Gurave Namaha",
            "Venus", "Om Shukraya Namaha",
            "Saturn", "Om Shanicharaya Namaha"
        );
        
        this.gemstone = planetGems.getOrDefault(planet, "Quartz");
        this.mantra = planetMantras.getOrDefault(planet, "Om Namah Shivaya");
    }
    
    private void calculateColorAndMetal() {
        if (planet == null) return;
        
        Map<String, String> planetColors = Map.of(
            "Sun", "Orange", "Moon", "White", "Mars", "Red",
            "Mercury", "Green", "Jupiter", "Yellow",
            "Venus", "White", "Saturn", "Blue"
        );
        
        Map<String, String> planetMetals = Map.of(
            "Sun", "Gold", "Moon", "Silver", "Mars", "Copper",
            "Mercury", "Bronze", "Jupiter", "Gold",
            "Venus", "Silver", "Saturn", "Iron"
        );
        
        this.color = planetColors.getOrDefault(planet, "White");
        this.metal = planetMetals.getOrDefault(planet, "Copper");
    }
    
    private void calculateVarnaGanaNadi() {
        if (nakshatra == null) return;
        
        // Simplified varna calculation based on nakshatra
        int nakshatraNum = getNakshatraNumber(nakshatra);
        
        this.varna = switch ((nakshatraNum - 1) % 4) {
            case 0 -> "Brahmin";
            case 1 -> "Kshatriya";
            case 2 -> "Vaishya";
            case 3 -> "Shudra";
            default -> "Unknown";
        };
        
        this.gana = switch ((nakshatraNum - 1) % 3) {
            case 0 -> "Deva";
            case 1 -> "Manushya";
            case 2 -> "Rakshasa";
            default -> "Unknown";
        };
        
        this.nadi = switch ((nakshatraNum - 1) % 3) {
            case 0 -> "Adi";
            case 1 -> "Madhya";
            case 2 -> "Antya";
            default -> "Unknown";
        };
    }
    
    private void calculateDeityAndYantra() {
        if (planet == null) return;
        
        Map<String, String> planetDeities = Map.of(
            "Sun", "Surya", "Moon", "Chandra", "Mars", "Mangal",
            "Mercury", "Budha", "Jupiter", "Brihaspati",
            "Venus", "Shukra", "Saturn", "Shani"
        );
        
        this.deity = planetDeities.getOrDefault(planet, "Ganesha");
        this.yantra = planet + " Yantra";
    }
    
    private void calculateMovementStatus() {
        if (isRetrograde == null) return;
        
        if (isRetrograde) {
            this.movement = "Retrograde";
        } else if (speed != null && Math.abs(speed) < 0.1) {
            this.movement = "Stationary";
        } else {
            this.movement = "Direct";
        }
    }
    
    private void calculateBeneficMalefic() {
        if (planet == null) return;
        
        this.beneficMalefic = switch (planet.toLowerCase()) {
            case "jupiter", "venus", "moon" -> "Benefic";
            case "saturn", "mars", "rahu", "ketu" -> "Malefic";
            case "mercury" -> "Neutral";
            case "sun" -> isRetrograde != null && isRetrograde ? "Malefic" : "Neutral";
            default -> "Neutral";
        };
    }
    
    // ================ UTILITY METHODS ================
    
    /**
     * Get formatted position in degrees, minutes, seconds
     */
    public String getFormattedPosition() {
        if (position == null) return "N/A";
        int degrees = (int) Math.floor(position);
        int minutes = (int) Math.floor((position - degrees) * 60);
        int seconds = (int) Math.floor(((position - degrees) * 60 - minutes) * 60);
        return String.format("%d°%02d'%02d\"", degrees, minutes, seconds);
    }
    
    /**
     * Get comprehensive Vedic description
     */
    public String getVedicDescription() {
        if (planet == null || sign == null) return "Position unknown";
        
        StringBuilder desc = new StringBuilder();
        desc.append(planet).append(" in ").append(sign);
        
        if (nakshatra != null) {
            desc.append(" (").append(nakshatra);
            if (pada != null) {
                desc.append(" Pada ").append(pada);
            }
            desc.append(")");
        }
        
        if (isRetrograde != null && isRetrograde) {
            desc.append(" - Retrograde");
        }
        
        if (planetaryState != null && !planetaryState.equals("Neutral")) {
            desc.append(" - ").append(planetaryState);
        }
        
        return desc.toString();
    }
    
    /**
     * Get planetary strength description
     */
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
    
    /**
     * Check if planet is in own sign
     */
    public boolean isInOwnSign() {
        if (planet == null || sign == null) return false;
        
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
    
    /**
     * Check if planet is exalted
     */
    public boolean isExalted() {
        if (planet == null || sign == null) return false;
        
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
    
    /**
     * Check if planet is debilitated
     */
    public boolean isDebilitated() {
        if (planet == null || sign == null) return false;
        
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
    
    /**
     * Check if planet is in friend sign
     */
    @JsonIgnore
    public boolean isInFriendSign() {
        if (planet == null || sign == null) return false;
        
        // Simplified friendship relationships
        Map<String, List<String>> friendSigns = Map.of(
            "Sun", List.of("Aries", "Sagittarius"),
            "Moon", List.of("Gemini", "Leo", "Virgo"),
            "Mars", List.of("Leo", "Sagittarius"),
            "Mercury", List.of("Aries", "Leo"),
            "Jupiter", List.of("Aries", "Leo", "Scorpio"),
            "Venus", List.of("Gemini", "Virgo"),
            "Saturn", List.of("Gemini", "Virgo")
        );
        
        return friendSigns.getOrDefault(planet, new ArrayList<>()).contains(sign);
    }
    
    /**
     * Check if planet is in enemy sign
     */
    @JsonIgnore
    public boolean isInEnemySign() {
        if (planet == null || sign == null) return false;
        
        // Simplified enemy relationships
        Map<String, List<String>> enemySigns = Map.of(
            "Sun", List.of("Aquarius", "Libra"),
            "Moon", List.of("Scorpio"),
            "Mars", List.of("Cancer", "Virgo"),
            "Mercury", List.of("Sagittarius"),
            "Jupiter", List.of("Gemini", "Virgo"),
            "Venus", List.of("Leo", "Scorpio"),
            "Saturn", List.of("Aries", "Leo", "Scorpio")
        );
        
        return enemySigns.getOrDefault(planet, new ArrayList<>()).contains(sign);
    }
    
    /**
     * Get nakshatra lord by nakshatra name
     */
    private String getNakshatraLordByName(String nakshatraName) {
    if (nakshatraName == null) return "Unknown";
    
    // ✅ Use HashMap to avoid argument limit
    Map<String, String> lords = new HashMap<>();
    lords.put("Ashwini", "Ketu");
    lords.put("Bharani", "Venus");
    lords.put("Krittika", "Sun");
    lords.put("Rohini", "Moon");
    lords.put("Mrigashira", "Mars");
    lords.put("Ardra", "Rahu");
    lords.put("Punarvasu", "Jupiter");
    lords.put("Pushya", "Saturn");
    lords.put("Ashlesha", "Mercury");
    lords.put("Magha", "Ketu");
    lords.put("Purva Phalguni", "Venus");
    lords.put("Uttara Phalguni", "Sun");
    lords.put("Hasta", "Moon");
    lords.put("Chitra", "Mars");
    lords.put("Swati", "Rahu");
    lords.put("Vishakha", "Jupiter");
    lords.put("Anuradha", "Saturn");
    lords.put("Jyeshtha", "Mercury");
    lords.put("Mula", "Ketu");
    lords.put("Purva Ashadha", "Venus");
    lords.put("Uttara Ashadha", "Sun");
    lords.put("Shravana", "Moon");
    lords.put("Dhanishta", "Mars");
    lords.put("Shatabhisha", "Rahu");
    lords.put("Purva Bhadrapada", "Jupiter");
    lords.put("Uttara Bhadrapada", "Saturn");
    lords.put("Revati", "Mercury");
    
    return lords.getOrDefault(nakshatraName, "Unknown");
}

    
    /**
     * Get nakshatra number (1-27)
     */
    private int getNakshatraNumber(String nakshatraName) {
        if (nakshatraName == null) return 1;
        
        String[] nakshatras = {
            "Ashwini", "Bharani", "Krittika", "Rohini", "Mrigashira", "Ardra", "Punarvasu",
            "Pushya", "Ashlesha", "Magha", "Purva Phalguni", "Uttara Phalguni", "Hasta",
            "Chitra", "Swati", "Vishakha", "Anuradha", "Jyeshtha", "Mula", "Purva Ashadha",
            "Uttara Ashadha", "Shravana", "Dhanishta", "Shatabhisha", "Purva Bhadrapada",
            "Uttara Bhadrapada", "Revati"
        };
        
        for (int i = 0; i < nakshatras.length; i++) {
            if (nakshatras[i].equals(nakshatraName)) {
                return i + 1;
            }
        }
        
        return 1;
    }
    
    /**
     * Calculate days until next sign change
     */
    public long getDaysUntilSignChange() {
        if (nextSignChange == null) return -1;
        return ChronoUnit.DAYS.between(LocalDateTime.now(), nextSignChange);
    }
    
    /**
     * Get transit summary
     */
    @JsonIgnore
    public String getTransitSummary() {
        StringBuilder summary = new StringBuilder();
        
        summary.append(getVedicDescription());
        
        if (influence != null) {
            summary.append(" - ").append(influence);
        }
        
        if (intensityLevel != null && intensityLevel >= 4) {
            summary.append(" (Strong influence)");
        }
        
        return summary.toString();
    }
    
    // ================ VALIDATION METHODS ================
    
    /**
     * Validate transit data
     */
    @JsonIgnore
    public boolean isValid() {
        return planet != null && !planet.trim().isEmpty() &&
               position != null && position >= 0 && position <= 360 &&
               sign != null && !sign.trim().isEmpty();
    }
    
    /**
     * Get validation errors
     */
    @JsonIgnore
    public List<String> getValidationErrors() {
        List<String> errors = new ArrayList<>();
        
        if (planet == null || planet.trim().isEmpty()) {
            errors.add("Planet name is required");
        }
        
        if (position == null) {
            errors.add("Position is required");
        } else if (position < 0 || position > 360) {
            errors.add("Position must be between 0 and 360 degrees");
        }
        
        if (sign == null || sign.trim().isEmpty()) {
            errors.add("Sign is required");
        }
        
        return errors;
    }
    
    // ================ GETTERS AND SETTERS ================
    // [All your existing getters and setters remain the same]
    
    public String getPlanet() { return planet; }
    public void setPlanet(String planet) { 
        this.planet = planet;
        calculateDerivedProperties();
    }
    
    public Double getPosition() { return position; }
    public void setPosition(Double position) { 
        this.position = position;
        calculateDerivedProperties();
    }
    
    public String getSign() { return sign; }
    public void setSign(String sign) { 
        this.sign = sign;
        calculateDerivedProperties();
    }
    
    public String getAspect() { return aspect; }
    public void setAspect(String aspect) { this.aspect = aspect; }
    
    public String getNakshatra() { return nakshatra; }
    public void setNakshatra(String nakshatra) { this.nakshatra = nakshatra; }
    
    public Integer getPada() { return pada; }
    public void setPada(Integer pada) { this.pada = pada; }
    
    public String getRashi() { return rashi; }
    public void setRashi(String rashi) { this.rashi = rashi; }
    
    public String getHouse() { return house; }
    public void setHouse(String house) { this.house = house; }
    
    public String getNakshatraLord() { return nakshatraLord; }
    public void setNakshatraLord(String nakshatraLord) { this.nakshatraLord = nakshatraLord; }
    
    public String getSignLord() { return signLord; }
    public void setSignLord(String signLord) { this.signLord = signLord; }
    
    public Boolean getIsRetrograde() { return isRetrograde; }
    public void setIsRetrograde(Boolean isRetrograde) { 
        this.isRetrograde = isRetrograde;
        calculateMovementStatus();
    }
    
    @JsonProperty("retrograde")
    public Boolean isRetrograde() { return isRetrograde; }
    
    public Double getSpeed() { return speed; }
    public void setSpeed(Double speed) { 
        this.speed = speed;
        calculateMovementStatus();
    }
    
    public String getMovement() { return movement; }
    public void setMovement(String movement) { this.movement = movement; }
    
    public String getCombustion() { return combustion; }
    public void setCombustion(String combustion) { this.combustion = combustion; }
    
    public String getInfluence() { return influence; }
    public void setInfluence(String influence) { this.influence = influence; }
    
    public String getSignificance() { return significance; }
    public void setSignificance(String significance) { this.significance = significance; }
    
    public Integer getIntensityLevel() { return intensityLevel; }
    public void setIntensityLevel(Integer intensityLevel) { this.intensityLevel = intensityLevel; }
    
    public String getVedicInterpretation() { return vedicInterpretation; }
    public void setVedicInterpretation(String vedicInterpretation) { this.vedicInterpretation = vedicInterpretation; }
    
    public String getBeneficMalefic() { return beneficMalefic; }
    public void setBeneficMalefic(String beneficMalefic) { this.beneficMalefic = beneficMalefic; }
    
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    
    public Double getDistance() { return distance; }
    public void setDistance(Double distance) { this.distance = distance; }
    
    public String getAyanamsa() { return ayanamsa; }
    public void setAyanamsa(String ayanamsa) { this.ayanamsa = ayanamsa; }
    
    public LocalDateTime getCalculatedAt() { return calculatedAt; }
    public void setCalculatedAt(LocalDateTime calculatedAt) { this.calculatedAt = calculatedAt; }
    
    public LocalDateTime getNextSignChange() { return nextSignChange; }
    public void setNextSignChange(LocalDateTime nextSignChange) { this.nextSignChange = nextSignChange; }
    
    public LocalDateTime getNextNakshatraChange() { return nextNakshatraChange; }
    public void setNextNakshatraChange(LocalDateTime nextNakshatraChange) { this.nextNakshatraChange = nextNakshatraChange; }
    
    public Long getDaysInCurrentSign() { return daysInCurrentSign; }
    public void setDaysInCurrentSign(Long daysInCurrentSign) { this.daysInCurrentSign = daysInCurrentSign; }
    
    public Long getDaysInCurrentNakshatra() { return daysInCurrentNakshatra; }
    public void setDaysInCurrentNakshatra(Long daysInCurrentNakshatra) { this.daysInCurrentNakshatra = daysInCurrentNakshatra; }
    
    public List<String> getCurrentAspects() { return currentAspects; }
    public void setCurrentAspects(List<String> currentAspects) { this.currentAspects = currentAspects; }
    
    public List<String> getUpcomingAspects() { return upcomingAspects; }
    public void setUpcomingAspects(List<String> upcomingAspects) { this.upcomingAspects = upcomingAspects; }
    
    public List<String> getAspectingPlanets() { return aspectingPlanets; }
    public void setAspectingPlanets(List<String> aspectingPlanets) { this.aspectingPlanets = aspectingPlanets; }
    
    public List<String> getAspectedBy() { return aspectedBy; }
    public void setAspectedBy(List<String> aspectedBy) { this.aspectedBy = aspectedBy; }
    
    public String getElement() { return element; }
    public void setElement(String element) { this.element = element; }
    
    public String getQuality() { return quality; }
    public void setQuality(String quality) { this.quality = quality; }
    
    public String getRulingPlanet() { return rulingPlanet; }
    public void setRulingPlanet(String rulingPlanet) { this.rulingPlanet = rulingPlanet; }
    
    public String getGemstone() { return gemstone; }
    public void setGemstone(String gemstone) { this.gemstone = gemstone; }
    
    public String getMantra() { return mantra; }
    public void setMantra(String mantra) { this.mantra = mantra; }
    
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    
    public String getMetal() { return metal; }
    public void setMetal(String metal) { this.metal = metal; }
    
    public String getDeity() { return deity; }
    public void setDeity(String deity) { this.deity = deity; }
    
    public String getYantra() { return yantra; }
    public void setYantra(String yantra) { this.yantra = yantra; }
    
    public String getVarna() { return varna; }
    public void setVarna(String varna) { this.varna = varna; }
    
    public String getGana() { return gana; }
    public void setGana(String gana) { this.gana = gana; }
    
    public String getNadi() { return nadi; }
    public void setNadi(String nadi) { this.nadi = nadi; }
    
    public String getYoniAnimal() { return yoniAnimal; }
    public void setYoniAnimal(String yoniAnimal) { this.yoniAnimal = yoniAnimal; }
    
    public String getPlanetaryState() { return planetaryState; }
    public void setPlanetaryState(String planetaryState) { this.planetaryState = planetaryState; }
    
    public List<String> getPredictions() { return predictions; }
    public void setPredictions(List<String> predictions) { this.predictions = predictions; }
    
    public List<String> getRemedies() { return remedies; }
    public void setRemedies(List<String> remedies) { this.remedies = remedies; }
    
    public List<String> getFavorableActivities() { return favorableActivities; }
    public void setFavorableActivities(List<String> favorableActivities) { this.favorableActivities = favorableActivities; }
    
    public List<String> getUnfavorableActivities() { return unfavorableActivities; }
    public void setUnfavorableActivities(List<String> unfavorableActivities) { this.unfavorableActivities = unfavorableActivities; }
    
    // ================ OBJECT METHODS ================
    
    @Override
    public String toString() {
        return String.format(
            "TransitResponse{planet='%s', position=%.4f°, sign='%s', nakshatra='%s', pada=%d, retrograde=%s, state='%s'}",
            planet, position, sign, nakshatra, pada, isRetrograde, planetaryState
        );
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
               Objects.equals(pada, that.pada) &&
               Objects.equals(calculatedAt, that.calculatedAt);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(planet, position, sign, nakshatra, pada, calculatedAt);
    }
    public String getTransitType() { 
    return transitType; 
}

public void setTransitType(String transitType) { 
    this.transitType = transitType; 
}
}
