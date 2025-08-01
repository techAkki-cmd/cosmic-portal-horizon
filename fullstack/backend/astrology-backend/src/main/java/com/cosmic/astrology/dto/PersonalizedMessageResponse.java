package com.cosmic.astrology.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Personalized Message Response DTO for Vedic Astrology Application
 * Contains personalized astrological insights, recommendations, and cosmic guidance
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Personalized astrological message with cosmic insights and recommendations")
public class PersonalizedMessageResponse {
    
    @NotBlank(message = "Message cannot be blank")
    @Size(max = 1000, message = "Message cannot exceed 1000 characters")
    @Schema(description = "Main personalized message", 
            example = "Today brings favorable cosmic energies for new beginnings and creative endeavors.",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String message;
    
    @NotBlank(message = "Transit influence cannot be blank")
    @Size(max = 500, message = "Transit influence cannot exceed 500 characters")
    @Schema(description = "Current planetary transit influences", 
            example = "Jupiter in your 5th house enhances creativity and learning opportunities")
    private String transitInfluence;
    
    @NotBlank(message = "Recommendation cannot be blank")
    @Size(max = 500, message = "Recommendation cannot exceed 500 characters")
    @Schema(description = "Personalized recommendation for the day", 
            example = "Focus on artistic pursuits and spend time in meditation during evening hours")
    private String recommendation;
    
    @Min(value = 1, message = "Intensity must be between 1 and 5")
    @Max(value = 5, message = "Intensity must be between 1 and 5")
    @Schema(description = "Cosmic intensity level (1-5)", example = "4", minimum = "1", maximum = "5")
    private int intensity;
    
    @Size(max = 50, message = "Dominant planet name cannot exceed 50 characters")
    @Schema(description = "Most influential planet for the day", example = "Jupiter")
    private String dominantPlanet;
    
    @Size(max = 30, message = "Lucky color cannot exceed 30 characters")
    @Schema(description = "Auspicious color for the day", example = "Golden Yellow")
    private String luckyColor;
    
    @Size(max = 50, message = "Best time cannot exceed 50 characters")
    @Schema(description = "Most favorable time period", example = "6:00 AM - 8:00 AM")
    private String bestTimeOfDay;
    
    @Size(max = 50, message = "Moon phase cannot exceed 50 characters")
    @Schema(description = "Current lunar phase and its influence", example = "Waxing Crescent")
    private String moonPhase;
    
    // ================ ENHANCED VEDIC ASTROLOGY FIELDS ================
    
    @Schema(description = "Current Dasha (planetary period)", example = "Venus Mahadasha, Mars Antardasha")
    private String currentDasha;
    
    @Schema(description = "Recommended mantras for the day")
    private List<String> recommendedMantras;
    
    @Schema(description = "Suggested gemstones for enhancement", example = "Blue Sapphire, Pearl")
    private String suggestedGemstone;
    
    @Schema(description = "Favorable directions for activities")
    private List<String> favorableDirections;
    
    @Schema(description = "Auspicious numbers for the day")
    private List<Integer> luckyNumbers;
    
    @Schema(description = "Specific Vedic remedies and rituals")
    private List<String> vedicRemedies;
    
    @Schema(description = "Planetary hours (Hora) for different activities")
    private Map<String, String> planetaryHours;
    
    @Schema(description = "Nakshatra (lunar mansion) influence", example = "Rohini - Favorable for material pursuits")
    private String nakshatraInfluence;
    
    @Schema(description = "Yoga combinations present today")
    private List<String> yogaCombinations;
    
    @Schema(description = "Areas of life to focus on")
    private List<String> focusAreas;
    
    @Schema(description = "Activities to avoid during the day")
    private List<String> activitiesToAvoid;
    
    @Schema(description = "Spiritual guidance and insights")
    private String spiritualGuidance;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "When this message was generated", example = "2024-01-15T08:30:00")
    private LocalDateTime generatedAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Date this message is valid for", example = "2024-01-15")
    private LocalDateTime validFor;
    
    @Schema(description = "Source of astrological calculations", example = "Lahiri Ayanamsa")
    private String calculationSource;
    
    @Schema(description = "Confidence level of predictions (0-100)", example = "87")
    private Integer confidenceLevel;
    
    @Schema(description = "Message priority level", allowableValues = {"LOW", "MEDIUM", "HIGH", "URGENT"})
    private String priority = "MEDIUM";
    
    @Schema(description = "Message category", allowableValues = {"DAILY", "WEEKLY", "MONTHLY", "SPECIAL", "TRANSIT"})
    private String category = "DAILY";
    
    // ================ CONSTRUCTORS ================
    
    public PersonalizedMessageResponse() {
        this.generatedAt = LocalDateTime.now();
        this.validFor = LocalDateTime.now().plusDays(1);
    }
    
    public PersonalizedMessageResponse(String message, String transitInfluence, 
                                     String recommendation, int intensity) {
        this();
        this.message = message;
        this.transitInfluence = transitInfluence;
        this.recommendation = recommendation;
        this.intensity = intensity;
    }
    
    public PersonalizedMessageResponse(String message, String transitInfluence, 
                                     String recommendation, int intensity,
                                     String dominantPlanet, String luckyColor, 
                                     String bestTimeOfDay, String moonPhase) {
        this(message, transitInfluence, recommendation, intensity);
        this.dominantPlanet = dominantPlanet;
        this.luckyColor = luckyColor;
        this.bestTimeOfDay = bestTimeOfDay;
        this.moonPhase = moonPhase;
    }
    
    // ================ GETTERS AND SETTERS ================
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public String getTransitInfluence() { return transitInfluence; }
    public void setTransitInfluence(String transitInfluence) { this.transitInfluence = transitInfluence; }
    
    public String getRecommendation() { return recommendation; }
    public void setRecommendation(String recommendation) { this.recommendation = recommendation; }
    
    public int getIntensity() { return intensity; }
    public void setIntensity(int intensity) { this.intensity = intensity; }
    
    public String getDominantPlanet() { return dominantPlanet; }
    public void setDominantPlanet(String dominantPlanet) { this.dominantPlanet = dominantPlanet; }
    
    public String getLuckyColor() { return luckyColor; }
    public void setLuckyColor(String luckyColor) { this.luckyColor = luckyColor; }
    
    public String getBestTimeOfDay() { return bestTimeOfDay; }
    public void setBestTimeOfDay(String bestTimeOfDay) { this.bestTimeOfDay = bestTimeOfDay; }
    
    public String getMoonPhase() { return moonPhase; }
    public void setMoonPhase(String moonPhase) { this.moonPhase = moonPhase; }
    
    public String getCurrentDasha() { return currentDasha; }
    public void setCurrentDasha(String currentDasha) { this.currentDasha = currentDasha; }
    
    public List<String> getRecommendedMantras() { return recommendedMantras; }
    public void setRecommendedMantras(List<String> recommendedMantras) { this.recommendedMantras = recommendedMantras; }
    
    public String getSuggestedGemstone() { return suggestedGemstone; }
    public void setSuggestedGemstone(String suggestedGemstone) { this.suggestedGemstone = suggestedGemstone; }
    
    public List<String> getFavorableDirections() { return favorableDirections; }
    public void setFavorableDirections(List<String> favorableDirections) { this.favorableDirections = favorableDirections; }
    
    public List<Integer> getLuckyNumbers() { return luckyNumbers; }
    public void setLuckyNumbers(List<Integer> luckyNumbers) { this.luckyNumbers = luckyNumbers; }
    
    public List<String> getVedicRemedies() { return vedicRemedies; }
    public void setVedicRemedies(List<String> vedicRemedies) { this.vedicRemedies = vedicRemedies; }
    
    public Map<String, String> getPlanetaryHours() { return planetaryHours; }
    public void setPlanetaryHours(Map<String, String> planetaryHours) { this.planetaryHours = planetaryHours; }
    
    public String getNakshatraInfluence() { return nakshatraInfluence; }
    public void setNakshatraInfluence(String nakshatraInfluence) { this.nakshatraInfluence = nakshatraInfluence; }
    
    public List<String> getYogaCombinations() { return yogaCombinations; }
    public void setYogaCombinations(List<String> yogaCombinations) { this.yogaCombinations = yogaCombinations; }
    
    public List<String> getFocusAreas() { return focusAreas; }
    public void setFocusAreas(List<String> focusAreas) { this.focusAreas = focusAreas; }
    
    public List<String> getActivitiesToAvoid() { return activitiesToAvoid; }
    public void setActivitiesToAvoid(List<String> activitiesToAvoid) { this.activitiesToAvoid = activitiesToAvoid; }
    
    public String getSpiritualGuidance() { return spiritualGuidance; }
    public void setSpiritualGuidance(String spiritualGuidance) { this.spiritualGuidance = spiritualGuidance; }
    
    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }
    
    public LocalDateTime getValidFor() { return validFor; }
    public void setValidFor(LocalDateTime validFor) { this.validFor = validFor; }
    
    public String getCalculationSource() { return calculationSource; }
    public void setCalculationSource(String calculationSource) { this.calculationSource = calculationSource; }
    
    public Integer getConfidenceLevel() { return confidenceLevel; }
    public void setConfidenceLevel(Integer confidenceLevel) { this.confidenceLevel = confidenceLevel; }
    
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    // ================ UTILITY METHODS ================
    
    /**
     * Get formatted generation timestamp
     */
    @JsonProperty("formattedGeneratedAt")
    public String getFormattedGeneratedAt() {
        return generatedAt != null ? 
            generatedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null;
    }
    
    /**
     * Get intensity as descriptive text
     */
    @JsonProperty("intensityDescription")
    public String getIntensityDescription() {
        switch (intensity) {
            case 1: return "Very Low - Subtle influences";
            case 2: return "Low - Gentle cosmic currents";
            case 3: return "Moderate - Balanced energies";
            case 4: return "High - Strong planetary forces";
            case 5: return "Very High - Intense cosmic activity";
            default: return "Unknown intensity";
        }
    }
    
    /**
     * Check if the message is still valid
     */
    @JsonProperty("isValid")
    public boolean isValid() {
        return validFor != null && LocalDateTime.now().isBefore(validFor);
    }
    
    /**
     * Check if this is a high-priority message
     */
    @JsonProperty("isHighPriority")
    public boolean isHighPriority() {
        return "HIGH".equals(priority) || "URGENT".equals(priority);
    }
    
    /**
     * Get the number of hours until expiry
     */
    @JsonProperty("hoursUntilExpiry")
    public Long getHoursUntilExpiry() {
        if (validFor == null) return null;
        return java.time.temporal.ChronoUnit.HOURS.between(LocalDateTime.now(), validFor);
    }
    
    /**
     * Check if the message has comprehensive details
     */
    @JsonProperty("isComprehensive")
    public boolean isComprehensive() {
        return dominantPlanet != null && luckyColor != null && 
               bestTimeOfDay != null && moonPhase != null;
    }
    
    /**
     * Get message completeness score (0-100)
     */
    @JsonProperty("completenessScore")
    public int getCompletenessScore() {
        int score = 0;
        int totalFields = 16;
        
        if (message != null && !message.trim().isEmpty()) score++;
        if (transitInfluence != null && !transitInfluence.trim().isEmpty()) score++;
        if (recommendation != null && !recommendation.trim().isEmpty()) score++;
        if (intensity > 0) score++;
        if (dominantPlanet != null && !dominantPlanet.trim().isEmpty()) score++;
        if (luckyColor != null && !luckyColor.trim().isEmpty()) score++;
        if (bestTimeOfDay != null && !bestTimeOfDay.trim().isEmpty()) score++;
        if (moonPhase != null && !moonPhase.trim().isEmpty()) score++;
        if (currentDasha != null && !currentDasha.trim().isEmpty()) score++;
        if (recommendedMantras != null && !recommendedMantras.isEmpty()) score++;
        if (suggestedGemstone != null && !suggestedGemstone.trim().isEmpty()) score++;
        if (favorableDirections != null && !favorableDirections.isEmpty()) score++;
        if (luckyNumbers != null && !luckyNumbers.isEmpty()) score++;
        if (vedicRemedies != null && !vedicRemedies.isEmpty()) score++;
        if (nakshatraInfluence != null && !nakshatraInfluence.trim().isEmpty()) score++;
        if (spiritualGuidance != null && !spiritualGuidance.trim().isEmpty()) score++;
        
        return (score * 100) / totalFields;
    }
    
    /**
     * Get a summary of key highlights
     */
    @JsonProperty("keyHighlights")
    public Map<String, String> getKeyHighlights() {
        Map<String, String> highlights = new java.util.HashMap<>();
        
        if (dominantPlanet != null) {
            highlights.put("Dominant Planet", dominantPlanet);
        }
        if (luckyColor != null) {
            highlights.put("Lucky Color", luckyColor);
        }
        if (bestTimeOfDay != null) {
            highlights.put("Best Time", bestTimeOfDay);
        }
        if (moonPhase != null) {
            highlights.put("Moon Phase", moonPhase);
        }
        if (currentDasha != null) {
            highlights.put("Current Dasha", currentDasha);
        }
        
        highlights.put("Intensity", getIntensityDescription());
        
        return highlights;
    }
    
    /**
     * Create a condensed version for notifications
     */
    public PersonalizedMessageResponse createCondensedVersion() {
        PersonalizedMessageResponse condensed = new PersonalizedMessageResponse();
        condensed.setMessage(message != null && message.length() > 100 ? 
            message.substring(0, 97) + "..." : message);
        condensed.setIntensity(intensity);
        condensed.setDominantPlanet(dominantPlanet);
        condensed.setLuckyColor(luckyColor);
        condensed.setBestTimeOfDay(bestTimeOfDay);
        condensed.setPriority(priority);
        condensed.setCategory(category);
        return condensed;
    }
    
    /**
     * Validate message content
     */
    public boolean isValidMessage() {
        return message != null && !message.trim().isEmpty() &&
               transitInfluence != null && !transitInfluence.trim().isEmpty() &&
               recommendation != null && !recommendation.trim().isEmpty() &&
               intensity >= 1 && intensity <= 5;
    }
    
    /**
     * Create safe version for logging (removes sensitive content)
     */
    public PersonalizedMessageResponse createSafeLogVersion() {
        PersonalizedMessageResponse safe = new PersonalizedMessageResponse();
        safe.setIntensity(intensity);
        safe.setDominantPlanet(dominantPlanet);
        safe.setPriority(priority);
        safe.setCategory(category);
        safe.setConfidenceLevel(confidenceLevel);
        safe.setGeneratedAt(generatedAt);
        safe.setValidFor(validFor);
        return safe;
    }
    
    // ================ OBJECT METHODS ================
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        PersonalizedMessageResponse that = (PersonalizedMessageResponse) obj;
        return intensity == that.intensity &&
               Objects.equals(message, that.message) &&
               Objects.equals(transitInfluence, that.transitInfluence) &&
               Objects.equals(recommendation, that.recommendation) &&
               Objects.equals(dominantPlanet, that.dominantPlanet) &&
               Objects.equals(generatedAt, that.generatedAt);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(message, transitInfluence, recommendation, intensity, 
                           dominantPlanet, generatedAt);
    }
    
    @Override
    public String toString() {
        return String.format(
            "PersonalizedMessageResponse{intensity=%d, planet='%s', priority='%s', category='%s', valid=%s, complete=%d%%}",
            intensity,
            dominantPlanet != null ? dominantPlanet : "Unknown",
            priority,
            category,
            isValid(),
            getCompletenessScore()
        );
    }
}
