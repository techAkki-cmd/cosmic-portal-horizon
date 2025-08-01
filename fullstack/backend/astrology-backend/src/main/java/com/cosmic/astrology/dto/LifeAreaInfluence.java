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
 * Life Area Influence DTO for Vedic Astrology Application
 * Represents astrological influences on specific areas of life like career, relationships, health, etc.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Astrological influence on specific life areas with detailed insights and recommendations")
public class LifeAreaInfluence {
    
    @NotBlank(message = "Title cannot be blank")
    @Size(max = 100, message = "Title cannot exceed 100 characters")
    @Schema(description = "Life area title", 
            example = "Career & Professional Growth",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;
    
    @Min(value = 1, message = "Rating must be between 1 and 10")
    @Max(value = 10, message = "Rating must be between 1 and 10")
    @Schema(description = "Influence rating (1-10)", example = "8", minimum = "1", maximum = "10")
    private int rating;
    
    @NotBlank(message = "Insight cannot be blank")
    @Size(max = 1000, message = "Insight cannot exceed 1000 characters")
    @Schema(description = "Detailed astrological insight", 
            example = "Jupiter's favorable position in your 10th house brings excellent opportunities for career advancement")
    private String insight;
    
    @Size(max = 50, message = "Icon cannot exceed 50 characters")
    @Schema(description = "Icon identifier for UI display", example = "briefcase")
    private String icon;
    
    @Size(max = 100, message = "Gradient cannot exceed 100 characters")
    @Schema(description = "CSS gradient for visual representation", 
            example = "from-blue-500 to-purple-600")
    private String gradient;
    
    // ================ ENHANCED VEDIC ASTROLOGY FIELDS ================
    
    @Schema(description = "Sub-areas within this life domain")
    private List<SubAreaInfluence> subAreas;
    
    @Schema(description = "Primary planetary influences affecting this area")
    private List<PlanetaryInfluence> planetaryInfluences;
    
    @Schema(description = "Specific recommendations for improvement")
    private List<String> recommendations;
    
    @Schema(description = "Favorable time periods for this life area")
    private List<String> favorablePeriods;
    
    @Schema(description = "Challenging periods to be cautious")
    private List<String> challengingPeriods;
    
    @Schema(description = "Vedic remedies for enhancing this life area")
    private List<String> vedicRemedies;
    
    @Schema(description = "Current trend direction", allowableValues = {"RISING", "STABLE", "DECLINING", "FLUCTUATING"})
    private String trend = "STABLE";
    
    @Schema(description = "Confidence level of this analysis (0-100)", example = "85")
    private Integer confidenceLevel;
    
    @Schema(description = "Detailed astrological explanation")
    private String astrologicalReasoning;
    
    @Schema(description = "Key strengths in this life area")
    private List<String> strengths;
    
    @Schema(description = "Areas needing attention or improvement")
    private List<String> challenges;
    
    @Schema(description = "Lucky elements for this life area")
    private Map<String, String> luckyElements;
    
    @Schema(description = "Specific mantras for this life area")
    private List<String> recommendedMantras;
    
    @Schema(description = "Gemstones beneficial for this area")
    private List<String> beneficialGemstones;
    
    @Schema(description = "Colors that enhance this life area")
    private List<String> favorableColors;
    
    @Schema(description = "Ideal days of week for activities in this area")
    private List<String> favorableDays;
    
    @Schema(description = "Priority level for attention", allowableValues = {"LOW", "MEDIUM", "HIGH", "URGENT"})
    private String priority = "MEDIUM";
    
    @Schema(description = "Time frame for these influences", allowableValues = {"DAILY", "WEEKLY", "MONTHLY", "YEARLY"})
    private String timeFrame = "MONTHLY";
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "When this analysis was calculated")
    private LocalDateTime calculatedAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "When this analysis expires")
    private LocalDateTime validUntil;
    
    @Schema(description = "Ayanamsa system used for calculations", example = "Lahiri")
    private String ayanamsaUsed;
    
    @Schema(description = "House positions relevant to this life area")
    private Map<String, Integer> relevantHouses;
    
    // ================ NESTED CLASSES ================
    
    @Schema(description = "Sub-area influence within a life domain")
    public static class SubAreaInfluence {
        @Schema(description = "Sub-area name", example = "Leadership Skills")
        private String name;
        
        @Schema(description = "Sub-area score (1-10)", example = "7")
        private int score;
        
        @Schema(description = "Specific description for this sub-area")
        private String description;
        
        @Schema(description = "Trend for this sub-area")
        private String trend;
        
        // Constructors, getters, and setters
        public SubAreaInfluence() {}
        
        public SubAreaInfluence(String name, int score, String description) {
            this.name = name;
            this.score = score;
            this.description = description;
        }
        
        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public int getScore() { return score; }
        public void setScore(int score) { this.score = score; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public String getTrend() { return trend; }
        public void setTrend(String trend) { this.trend = trend; }
    }
    
    @Schema(description = "Planetary influence on a life area")
    public static class PlanetaryInfluence {
        @Schema(description = "Planet name", example = "Jupiter")
        private String planet;
        
        @Schema(description = "Influence type", allowableValues = {"POSITIVE", "NEGATIVE", "NEUTRAL", "MIXED"})
        private String influenceType;
        
        @Schema(description = "Strength of influence (1-10)", example = "8")
        private int strength;
        
        @Schema(description = "Description of planetary influence")
        private String description;
        
        @Schema(description = "House position of the planet", example = "10")
        private Integer housePosition;
        
        // Constructors, getters, and setters
        public PlanetaryInfluence() {}
        
        public PlanetaryInfluence(String planet, String influenceType, int strength, String description) {
            this.planet = planet;
            this.influenceType = influenceType;
            this.strength = strength;
            this.description = description;
        }
        
        // Getters and setters
        public String getPlanet() { return planet; }
        public void setPlanet(String planet) { this.planet = planet; }
        
        public String getInfluenceType() { return influenceType; }
        public void setInfluenceType(String influenceType) { this.influenceType = influenceType; }
        
        public int getStrength() { return strength; }
        public void setStrength(int strength) { this.strength = strength; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public Integer getHousePosition() { return housePosition; }
        public void setHousePosition(Integer housePosition) { this.housePosition = housePosition; }
    }
    
    // ================ CONSTRUCTORS ================
    
    public LifeAreaInfluence() {
        this.calculatedAt = LocalDateTime.now();
        this.validUntil = LocalDateTime.now().plusDays(30); // Default 30-day validity
    }
    
    public LifeAreaInfluence(String title, int rating, String insight, String icon, String gradient) {
        this();
        this.title = title;
        this.rating = rating;
        this.insight = insight;
        this.icon = icon;
        this.gradient = gradient;
    }
    
    public LifeAreaInfluence(String title, int rating, String insight, String icon, String gradient,
                           String trend, Integer confidenceLevel, String priority) {
        this(title, rating, insight, icon, gradient);
        this.trend = trend;
        this.confidenceLevel = confidenceLevel;
        this.priority = priority;
    }
    
    // ================ GETTERS AND SETTERS ================
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    
    public String getInsight() { return insight; }
    public void setInsight(String insight) { this.insight = insight; }
    
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    
    public String getGradient() { return gradient; }
    public void setGradient(String gradient) { this.gradient = gradient; }
    
    public List<SubAreaInfluence> getSubAreas() { return subAreas; }
    public void setSubAreas(List<SubAreaInfluence> subAreas) { this.subAreas = subAreas; }
    
    public List<PlanetaryInfluence> getPlanetaryInfluences() { return planetaryInfluences; }
    public void setPlanetaryInfluences(List<PlanetaryInfluence> planetaryInfluences) { this.planetaryInfluences = planetaryInfluences; }
    
    public List<String> getRecommendations() { return recommendations; }
    public void setRecommendations(List<String> recommendations) { this.recommendations = recommendations; }
    
    public List<String> getFavorablePeriods() { return favorablePeriods; }
    public void setFavorablePeriods(List<String> favorablePeriods) { this.favorablePeriods = favorablePeriods; }
    
    public List<String> getChallengingPeriods() { return challengingPeriods; }
    public void setChallengingPeriods(List<String> challengingPeriods) { this.challengingPeriods = challengingPeriods; }
    
    public List<String> getVedicRemedies() { return vedicRemedies; }
    public void setVedicRemedies(List<String> vedicRemedies) { this.vedicRemedies = vedicRemedies; }
    
    public String getTrend() { return trend; }
    public void setTrend(String trend) { this.trend = trend; }
    
    public Integer getConfidenceLevel() { return confidenceLevel; }
    public void setConfidenceLevel(Integer confidenceLevel) { this.confidenceLevel = confidenceLevel; }
    
    public String getAstrologicalReasoning() { return astrologicalReasoning; }
    public void setAstrologicalReasoning(String astrologicalReasoning) { this.astrologicalReasoning = astrologicalReasoning; }
    
    public List<String> getStrengths() { return strengths; }
    public void setStrengths(List<String> strengths) { this.strengths = strengths; }
    
    public List<String> getChallenges() { return challenges; }
    public void setChallenges(List<String> challenges) { this.challenges = challenges; }
    
    public Map<String, String> getLuckyElements() { return luckyElements; }
    public void setLuckyElements(Map<String, String> luckyElements) { this.luckyElements = luckyElements; }
    
    public List<String> getRecommendedMantras() { return recommendedMantras; }
    public void setRecommendedMantras(List<String> recommendedMantras) { this.recommendedMantras = recommendedMantras; }
    
    public List<String> getBeneficialGemstones() { return beneficialGemstones; }
    public void setBeneficialGemstones(List<String> beneficialGemstones) { this.beneficialGemstones = beneficialGemstones; }
    
    public List<String> getFavorableColors() { return favorableColors; }
    public void setFavorableColors(List<String> favorableColors) { this.favorableColors = favorableColors; }
    
    public List<String> getFavorableDays() { return favorableDays; }
    public void setFavorableDays(List<String> favorableDays) { this.favorableDays = favorableDays; }
    
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    
    public String getTimeFrame() { return timeFrame; }
    public void setTimeFrame(String timeFrame) { this.timeFrame = timeFrame; }
    
    public LocalDateTime getCalculatedAt() { return calculatedAt; }
    public void setCalculatedAt(LocalDateTime calculatedAt) { this.calculatedAt = calculatedAt; }
    
    public LocalDateTime getValidUntil() { return validUntil; }
    public void setValidUntil(LocalDateTime validUntil) { this.validUntil = validUntil; }
    
    public String getAyanamsaUsed() { return ayanamsaUsed; }
    public void setAyanamsaUsed(String ayanamsaUsed) { this.ayanamsaUsed = ayanamsaUsed; }
    
    public Map<String, Integer> getRelevantHouses() { return relevantHouses; }
    public void setRelevantHouses(Map<String, Integer> relevantHouses) { this.relevantHouses = relevantHouses; }
    
    // ================ UTILITY METHODS ================
    
    /**
     * Get rating as descriptive text
     */
    @JsonProperty("ratingDescription")
    public String getRatingDescription() {
        if (rating >= 9) return "Excellent - Very favorable influences";
        if (rating >= 7) return "Good - Positive cosmic support";
        if (rating >= 5) return "Average - Balanced energies";
        if (rating >= 3) return "Below Average - Some challenges present";
        return "Poor - Significant obstacles to navigate";
    }
    
    /**
     * Get trend with emoji for better UI display
     */
    @JsonProperty("trendWithEmoji")
    public String getTrendWithEmoji() {
        switch (trend) {
            case "RISING": return "📈 Rising";
            case "STABLE": return "➡️ Stable";
            case "DECLINING": return "📉 Declining";
            case "FLUCTUATING": return "〰️ Fluctuating";
            default: return "➡️ " + trend;
        }
    }
    
    /**
     * Check if this influence is currently valid
     */
    @JsonProperty("isValid")
    public boolean isValid() {
        return validUntil != null && LocalDateTime.now().isBefore(validUntil);
    }
    
    /**
     * Check if this is a high-priority life area
     */
    @JsonProperty("isHighPriority")
    public boolean isHighPriority() {
        return "HIGH".equals(priority) || "URGENT".equals(priority);
    }
    
    /**
     * Get the number of days until this analysis expires
     */
    @JsonProperty("daysUntilExpiry")
    public Long getDaysUntilExpiry() {
        if (validUntil == null) return null;
        return java.time.temporal.ChronoUnit.DAYS.between(LocalDateTime.now(), validUntil);
    }
    
    /**
     * Check if this life area has detailed analysis
     */
    @JsonProperty("hasDetailedAnalysis")
    public boolean hasDetailedAnalysis() {
        return subAreas != null && !subAreas.isEmpty() &&
               planetaryInfluences != null && !planetaryInfluences.isEmpty() &&
               recommendations != null && !recommendations.isEmpty();
    }
    
    /**
     * Get overall influence strength based on rating and confidence
     */
    @JsonProperty("overallStrength")
    public String getOverallStrength() {
        if (confidenceLevel == null) return "Unknown";
        
        int combinedScore = (rating * 70 + confidenceLevel * 30) / 100;
        
        if (combinedScore >= 85) return "Very Strong";
        if (combinedScore >= 70) return "Strong";
        if (combinedScore >= 55) return "Moderate";
        if (combinedScore >= 40) return "Weak";
        return "Very Weak";
    }
    
    /**
     * Get average sub-area score
     */
    @JsonProperty("averageSubAreaScore")
    public Double getAverageSubAreaScore() {
        if (subAreas == null || subAreas.isEmpty()) return null;
        
        return subAreas.stream()
                .mapToInt(SubAreaInfluence::getScore)
                .average()
                .orElse(0.0);
    }
    
    /**
     * Count positive planetary influences
     */
    @JsonProperty("positiveInfluenceCount")
    public int getPositiveInfluenceCount() {
        if (planetaryInfluences == null) return 0;
        
        return (int) planetaryInfluences.stream()
                .filter(pi -> "POSITIVE".equals(pi.getInfluenceType()))
                .count();
    }
    
    /**
     * Count negative planetary influences
     */
    @JsonProperty("negativeInfluenceCount")
    public int getNegativeInfluenceCount() {
        if (planetaryInfluences == null) return 0;
        
        return (int) planetaryInfluences.stream()
                .filter(pi -> "NEGATIVE".equals(pi.getInfluenceType()))
                .count();
    }
    
    /**
     * Get data completeness score
     */
    @JsonProperty("completenessScore")
    public int getCompletenessScore() {
        int score = 0;
        int totalFields = 12;
        
        if (title != null && !title.trim().isEmpty()) score++;
        if (rating > 0) score++;
        if (insight != null && !insight.trim().isEmpty()) score++;
        if (subAreas != null && !subAreas.isEmpty()) score++;
        if (planetaryInfluences != null && !planetaryInfluences.isEmpty()) score++;
        if (recommendations != null && !recommendations.isEmpty()) score++;
        if (favorablePeriods != null && !favorablePeriods.isEmpty()) score++;
        if (vedicRemedies != null && !vedicRemedies.isEmpty()) score++;
        if (astrologicalReasoning != null && !astrologicalReasoning.trim().isEmpty()) score++;
        if (strengths != null && !strengths.isEmpty()) score++;
        if (challenges != null && !challenges.isEmpty()) score++;
        if (confidenceLevel != null && confidenceLevel > 0) score++;
        
        return (score * 100) / totalFields;
    }
    
    /**
     * Get color based on rating for UI display
     */
    @JsonProperty("ratingColor")
    public String getRatingColor() {
        if (rating >= 8) return "text-green-600";
        if (rating >= 6) return "text-blue-600";
        if (rating >= 4) return "text-yellow-600";
        if (rating >= 2) return "text-orange-600";
        return "text-red-600";
    }
    
    /**
     * Create a summary for notifications
     */
    public LifeAreaInfluence createSummaryVersion() {
        LifeAreaInfluence summary = new LifeAreaInfluence();
        summary.setTitle(title);
        summary.setRating(rating);
        summary.setInsight(insight != null && insight.length() > 150 ? 
            insight.substring(0, 147) + "..." : insight);
        summary.setIcon(icon);
        summary.setGradient(gradient);
        summary.setTrend(trend);
        summary.setPriority(priority);
        summary.setConfidenceLevel(confidenceLevel);
        return summary;
    }
    
    /**
     * Check if remedies are available
     */
    @JsonProperty("hasRemedies")
    public boolean hasRemedies() {
        return (vedicRemedies != null && !vedicRemedies.isEmpty()) ||
               (recommendedMantras != null && !recommendedMantras.isEmpty()) ||
               (beneficialGemstones != null && !beneficialGemstones.isEmpty());
    }
    
    /**
     * Get formatted calculation timestamp
     */
    @JsonProperty("formattedCalculatedAt")
    public String getFormattedCalculatedAt() {
        return calculatedAt != null ? 
            calculatedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null;
    }
    
    /**
     * Create safe version for logging
     */
    public LifeAreaInfluence createSafeLogVersion() {
        LifeAreaInfluence safe = new LifeAreaInfluence();
        safe.setTitle(title);
        safe.setRating(rating);
        safe.setTrend(trend);
        safe.setPriority(priority);
        safe.setConfidenceLevel(confidenceLevel);
        safe.setTimeFrame(timeFrame);
        safe.setCalculatedAt(calculatedAt);
        return safe;
    }
    
    // ================ OBJECT METHODS ================
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        LifeAreaInfluence that = (LifeAreaInfluence) obj;
        return rating == that.rating &&
               Objects.equals(title, that.title) &&
               Objects.equals(insight, that.insight) &&
               Objects.equals(trend, that.trend) &&
               Objects.equals(calculatedAt, that.calculatedAt);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(title, rating, insight, trend, calculatedAt);
    }
    
    @Override
    public String toString() {
        return String.format(
            "LifeAreaInfluence{title='%s', rating=%d, trend='%s', priority='%s', confidence=%d%%, complete=%d%%}",
            title,
            rating,
            trend,
            priority,
            confidenceLevel != null ? confidenceLevel : 0,
            getCompletenessScore()
        );
    }
}
