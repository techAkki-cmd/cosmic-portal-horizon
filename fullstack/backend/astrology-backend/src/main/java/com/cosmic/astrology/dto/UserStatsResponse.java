package com.cosmic.astrology.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 🌟 COMPREHENSIVE USER STATISTICS RESPONSE DTO
 * Enhanced for Vedic Astrology Application with complete user engagement and cosmic insights
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Complete user statistics including charts, activity, cosmic insights, and Vedic astrology data")
public class UserStatsResponse {
    
    // ================ CORE STATISTICS (MATCHING FRONTEND) ================
    
    @Min(value = 0, message = "Charts created cannot be negative")
    @Schema(description = "Total number of birth charts created by user", example = "15", minimum = "0")
    private Integer chartsCreated;
    
    @Min(value = 0, message = "Accuracy rate cannot be negative")
    @Max(value = 100, message = "Accuracy rate cannot exceed 100")
    @Schema(description = "Chart calculation accuracy percentage", example = "95", minimum = "0", maximum = "100")
    private Integer accuracyRate;
    
    @Size(max = 50, message = "Cosmic energy description cannot exceed 50 characters")
    @Schema(description = "User's current cosmic energy state", 
            example = "Harmonious",
            allowableValues = {"Very Low", "Low", "Moderate", "High", "Very High", "Harmonious", "Dynamic", "Balanced"})
    private String cosmicEnergy;
    
    @Min(value = 0, message = "Streak days cannot be negative")
    @Schema(description = "Current login streak in days", example = "7", minimum = "0")
    private Integer streakDays;
    
    @Min(value = 0, message = "Total readings cannot be negative")
    @Schema(description = "Total readings and analyses performed", example = "42", minimum = "0")
    private Integer totalReadings;
    
    @Size(max = 30, message = "Favorite chart type cannot exceed 30 characters")
    @Schema(description = "User's most frequently used chart type", example = "Natal")
    private String favoriteChartType;
    
    @Size(max = 20, message = "Most active time cannot exceed 20 characters")
    @Schema(description = "User's most active time of day", example = "Morning")
    private String mostActiveTimeOfDay;
    
    @Min(value = 0, message = "Average session duration cannot be negative")
    @Schema(description = "Average session duration in minutes", example = "25", minimum = "0")
    private Integer averageSessionDuration;
    
    @Min(value = 0, message = "Total predictions cannot be negative")
    @Schema(description = "Total predictions made", example = "8", minimum = "0")
    private Integer totalPredictions;
    
    @Min(value = 0, message = "Correct predictions cannot be negative")
    @Schema(description = "Number of correct predictions", example = "7", minimum = "0")
    private Integer correctPredictions;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Last chart generation timestamp", example = "2024-01-14T14:20:00")
    private LocalDateTime lastChartGenerated;
    
    // ================ ENHANCED ENGAGEMENT STATISTICS ================
    
    @Min(value = 0, message = "Total logins cannot be negative")
    @Schema(description = "Total number of user logins", example = "142", minimum = "0")
    private Integer totalLogins;
    
    @Min(value = 0, message = "Profile completion cannot be negative")
    @Max(value = 100, message = "Profile completion cannot exceed 100")
    @Schema(description = "Profile completion percentage", example = "85", minimum = "0", maximum = "100")
    private Integer profileCompletionPercentage;
    
    @Size(max = 20, message = "Activity level cannot exceed 20 characters")
    @Schema(description = "User activity level", 
            example = "High",
            allowableValues = {"Very Low", "Low", "Medium", "High", "Very High", "Exceptional"})
    private String activityLevel;
    
    @Min(value = 0, message = "Engagement score cannot be negative")
    @Max(value = 100, message = "Engagement score cannot exceed 100")
    @Schema(description = "User engagement score (0-100)", example = "78", minimum = "0", maximum = "100")
    private Integer engagementScore;
    
    // ================ VEDIC ASTROLOGY PROFILE ================
    
    @Size(max = 30, message = "Sun sign cannot exceed 30 characters")
    @Schema(description = "User's Vedic Sun sign", example = "Aries")
    private String sunSign;
    
    @Size(max = 30, message = "Moon sign cannot exceed 30 characters")
    @Schema(description = "User's Vedic Moon sign", example = "Cancer")
    private String moonSign;
    
    @Size(max = 30, message = "Rising sign cannot exceed 30 characters")
    @Schema(description = "User's Vedic Ascendant sign", example = "Leo")
    private String risingSign;
    
    @Size(max = 20, message = "Dominant element cannot exceed 20 characters")
    @Schema(description = "User's dominant element", 
            example = "Fire",
            allowableValues = {"Fire", "Earth", "Air", "Water"})
    private String dominantElement;
    
    @Size(max = 30, message = "Moon nakshatra cannot exceed 30 characters")
    @Schema(description = "User's Moon Nakshatra", example = "Rohini")
    private String moonNakshatra;
    
    @Size(max = 100, message = "Current dasha cannot exceed 100 characters")
    @Schema(description = "Current planetary period (Dasha)", example = "Venus Mahadasha, Mars Antardasha")
    private String currentDasha;
    
    // ================ PREMIUM SUBSCRIPTION DATA ================
    
    @Size(max = 20, message = "Subscription type cannot exceed 20 characters")
    @Schema(description = "User's subscription type", 
            example = "PREMIUM",
            allowableValues = {"FREE", "BASIC", "PREMIUM", "PROFESSIONAL", "ENTERPRISE"})
    private String subscriptionType;
    
    @Schema(description = "Whether subscription is currently active", example = "true")
    private Boolean subscriptionActive;
    
    @Min(value = 0, message = "Credits remaining cannot be negative")
    @Schema(description = "Remaining premium credits", example = "25", minimum = "0")
    private Integer creditsRemaining;
    
    @Min(value = 0, message = "Premium features used cannot be negative")
    @Schema(description = "Premium features accessed count", example = "8", minimum = "0")
    private Integer premiumFeaturesUsed;
    
    // ================ TIMING AND DATES ================
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "User registration date", example = "2023-01-15T10:30:00")
    private LocalDateTime memberSince;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Last login timestamp", example = "2024-01-14T16:45:00")
    private LocalDateTime lastLogin;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Statistics calculation timestamp", example = "2024-01-15T08:00:00")
    private LocalDateTime calculatedAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Subscription expiry date", example = "2024-12-31T23:59:59")
    private LocalDateTime subscriptionExpiryDate;
    
    // ================ ENHANCED VEDIC FEATURES ================
    
    @Schema(description = "User's birth chart quality score (0-100)", example = "92")
    private Integer chartQualityScore;
    
    @Schema(description = "Current yogas active in user's chart")
    private List<String> activeYogas;
    
    @Schema(description = "Current transit influences affecting user")
    private List<String> currentTransitInfluences;
    
    @Schema(description = "User's karmic lessons based on chart analysis")
    private List<String> karmicLessons;
    
    @Schema(description = "Planetary strength scores for major planets")
    private Map<String, Integer> planetaryStrengths;
    
    @Schema(description = "User's Vedic element balance")
    private Map<String, Double> elementBalance;
    
    // ================ ACHIEVEMENT DATA ================
    
    @Schema(description = "User achievements and badges earned")
    private List<String> achievements;
    
    @Schema(description = "Milestones reached by user")
    private List<String> milestones;
    
    @Size(max = 50, message = "User level cannot exceed 50 characters")
    @Schema(description = "User level based on activity", example = "Intermediate Seeker")
    private String userLevel;
    
    @Min(value = 0, message = "Total points cannot be negative")
    @Schema(description = "Points earned through activities", example = "1250", minimum = "0")
    private Integer totalPoints;
    
    // ================ ANALYTICS AND BREAKDOWNS ================
    
    @Schema(description = "Chart types generated breakdown")
    private Map<String, Integer> chartTypeBreakdown;
    
    @Schema(description = "Feature usage statistics")
    private Map<String, Integer> featureUsage;
    
    @Schema(description = "Favorite astrological topics based on usage")
    private List<String> favoriteTopics;
    
    @Schema(description = "Recommended features based on usage patterns")
    private List<String> recommendedFeatures;
    
    // ================ UTILITY FIELDS ================
    
    @Schema(description = "Statistics reliability score (0-100)", example = "95")
    private Integer statisticsReliability;
    
    @Schema(description = "Data freshness indicator")
    private String dataFreshness;
    
    @Schema(description = "Account health score (0-100)", example = "88")
    private Integer accountHealthScore;
    
    // ================ COMPUTED PROPERTIES FOR FRONTEND ================
    
    /**
     * Get comprehensive Vedic astrological profile summary
     */
    @JsonProperty("vedicProfileSummary")
    public String getVedicProfileSummary() {
        if (sunSign != null || moonSign != null || risingSign != null) {
            StringBuilder profile = new StringBuilder();
            if (sunSign != null) profile.append("Sun: ").append(sunSign);
            if (moonSign != null) {
                if (profile.length() > 0) profile.append(", ");
                profile.append("Moon: ").append(moonSign);
            }
            if (risingSign != null) {
                if (profile.length() > 0) profile.append(", ");
                profile.append("Ascendant: ").append(risingSign);
            }
            if (moonNakshatra != null) {
                profile.append(" (").append(moonNakshatra).append(" Nakshatra)");
            }
            return profile.toString();
        }
        return "Vedic profile pending birth chart calculation";
    }
    
    /**
     * Check if user is a premium subscriber
     */
    @JsonProperty("isPremiumUser")
    public boolean isPremiumUser() {
        return subscriptionActive != null && subscriptionActive && 
               subscriptionType != null && 
               ("PREMIUM".equals(subscriptionType) || 
                "PROFESSIONAL".equals(subscriptionType) || 
                "ENTERPRISE".equals(subscriptionType));
    }
    
    /**
     * Check if user is highly engaged
     */
    @JsonProperty("isHighlyEngaged")
    public boolean isHighlyEngaged() {
        int engagementIndicators = 0;
        
        if (streakDays != null && streakDays >= 7) engagementIndicators++;
        if (chartsCreated != null && chartsCreated >= 10) engagementIndicators++;
        if (engagementScore != null && engagementScore >= 70) engagementIndicators++;
        if (profileCompletionPercentage != null && profileCompletionPercentage >= 80) engagementIndicators++;
        if (totalLogins != null && totalLogins >= 30) engagementIndicators++;
        
        return engagementIndicators >= 3;
    }
    
    /**
     * Get user tier based on activity and subscription
     */
    @JsonProperty("userTier")
    public String getUserTier() {
        if (isPremiumUser()) {
            if (chartsCreated != null && chartsCreated >= 50) return "Master Astrologer";
            if (chartsCreated != null && chartsCreated >= 20) return "Advanced Seeker";
            return "Premium Member";
        } else {
            if (chartsCreated != null && chartsCreated >= 20) return "Dedicated Explorer";
            if (chartsCreated != null && chartsCreated >= 5) return "Active Seeker";
            return "Cosmic Novice";
        }
    }
    
    /**
     * Get cosmic energy level (1-5 scale)
     */
    @JsonProperty("cosmicEnergyLevel")
    public int getCosmicEnergyLevel() {
        if (cosmicEnergy == null) return 3;
        
        return switch (cosmicEnergy.toLowerCase()) {
            case "very low", "depleted", "exhausted" -> 1;
            case "low", "weak", "diminished" -> 2;
            case "moderate", "balanced", "stable", "neutral" -> 3;
            case "high", "strong", "harmonious", "elevated" -> 4;
            case "very high", "dynamic", "powerful", "transcendent" -> 5;
            default -> 3;
        };
    }
    
    /**
     * Get enhanced dashboard summary for frontend
     */
    @JsonProperty("dashboardSummary")
    public Map<String, Object> getDashboardSummary() {
        Map<String, Object> summary = new HashMap<>();
        summary.put("chartsCreated", chartsCreated != null ? chartsCreated : 0);
        summary.put("streakDays", streakDays != null ? streakDays : 0);
        summary.put("cosmicEnergy", cosmicEnergy != null ? cosmicEnergy : "Balanced");
        summary.put("cosmicEnergyLevel", getCosmicEnergyLevel());
        summary.put("userTier", getUserTier());
        summary.put("profileCompletion", profileCompletionPercentage != null ? profileCompletionPercentage : 0);
        summary.put("subscriptionStatus", isPremiumUser() ? "Premium" : "Free");
        summary.put("activityLevel", activityLevel != null ? activityLevel : "Medium");
        summary.put("engagementScore", engagementScore != null ? engagementScore : 50);
        summary.put("accountHealth", accountHealthScore != null ? accountHealthScore : 75);
        summary.put("isHighlyEngaged", isHighlyEngaged());
        summary.put("dataFreshness", dataFreshness != null ? dataFreshness : "Live");
        summary.put("lastCalculated", calculatedAt);
        return summary;
    }
    
    /**
     * Get profile completion analysis
     */
    @JsonProperty("profileAnalysis")
    public Map<String, Object> getProfileAnalysis() {
        Map<String, Object> analysis = new HashMap<>();
        int completionScore = profileCompletionPercentage != null ? profileCompletionPercentage : 0;
        
        analysis.put("completionPercentage", completionScore);
        analysis.put("needsCompletion", completionScore < 80);
        
        List<String> missingElements = new ArrayList<>();
        if (sunSign == null) missingElements.add("Birth Chart Analysis");
        if (favoriteTopics == null || favoriteTopics.isEmpty()) missingElements.add("Interests & Preferences");
        if (subscriptionType == null || "FREE".equals(subscriptionType)) missingElements.add("Subscription Selection");
        
        analysis.put("missingElements", missingElements);
        analysis.put("priority", completionScore < 50 ? "HIGH" : completionScore < 80 ? "MEDIUM" : "LOW");
        
        return analysis;
    }
    
    /**
     * Get cosmic greeting based on time and user data
     */
    @JsonProperty("cosmicGreeting")
    public Map<String, String> getCosmicGreeting() {
        Map<String, String> greeting = new HashMap<>();
        int hour = LocalDateTime.now().getHour();
        String timeGreeting;
        String cosmicMessage;
        
        if (hour < 6) {
            timeGreeting = "🌅 Sacred Dawn";
            cosmicMessage = "The pre-dawn hours hold powerful spiritual energy";
        } else if (hour < 12) {
            timeGreeting = "🌞 Blessed Morning";
            cosmicMessage = "Solar energy peaks, perfect for new beginnings";
        } else if (hour < 17) {
            timeGreeting = "🌤️ Radiant Afternoon";
            cosmicMessage = "Active planetary hours support material pursuits";
        } else if (hour < 21) {
            timeGreeting = "🌅 Tranquil Evening";
            cosmicMessage = "Twilight brings reflection and inner wisdom";
        } else {
            timeGreeting = "🌙 Mystical Night";
            cosmicMessage = "Lunar energies enhance intuition and dreams";
        }
        
        String tier = getUserTier();
        greeting.put("greeting", timeGreeting + ", " + tier + "!");
        greeting.put("cosmicMessage", cosmicMessage);
        greeting.put("personalizedNote", getPersonalizedNote());
        
        return greeting;
    }
    
    private String getPersonalizedNote() {
        if (currentDasha != null) {
            return "Your current " + currentDasha + " brings unique opportunities.";
        }
        if (sunSign != null) {
            return "Your " + sunSign + " Sun sign guides today's cosmic flow.";
        }
        return "The cosmos aligns to support your spiritual journey.";
    }
    
    // ================ INITIALIZATION ================
    
    /**
     * Initialize with default values
     */
    public void initializeDefaults() {
        if (calculatedAt == null) {
            calculatedAt = LocalDateTime.now();
        }
        if (dataFreshness == null) {
            dataFreshness = "Live";
        }
        if (statisticsReliability == null) {
            statisticsReliability = 100;
        }
        if (chartsCreated == null) {
            chartsCreated = 0;
        }
        if (accuracyRate == null) {
            accuracyRate = 95;
        }
        if (streakDays == null) {
            streakDays = 0;
        }
        if (totalReadings == null) {
            totalReadings = 0;
        }
        if (favoriteChartType == null) {
            favoriteChartType = "Natal";
        }
        if (mostActiveTimeOfDay == null) {
            mostActiveTimeOfDay = "Morning";
        }
        if (averageSessionDuration == null) {
            averageSessionDuration = 0;
        }
        if (totalPredictions == null) {
            totalPredictions = 0;
        }
        if (correctPredictions == null) {
            correctPredictions = 0;
        }
        if (cosmicEnergy == null) {
            cosmicEnergy = "Harmonious";
        }
    }
    
    // ================ BUILDER PATTERN ENHANCEMENTS ================
    
    public static class UserStatsResponseBuilder {
        public UserStatsResponseBuilder withDefaults() {
            return this
                .calculatedAt(LocalDateTime.now())
                .dataFreshness("Live")
                .statisticsReliability(100)
                .chartsCreated(0)
                .accuracyRate(95)
                .streakDays(0)
                .totalReadings(0)
                .favoriteChartType("Natal")
                .mostActiveTimeOfDay("Morning")
                .averageSessionDuration(0)
                .totalPredictions(0)
                .correctPredictions(0)
                .cosmicEnergy("Harmonious");
        }
        
        public UserStatsResponseBuilder withVedicProfile(String sunSign, String moonSign, String risingSign, String dominantElement) {
            return this
                .sunSign(sunSign)
                .moonSign(moonSign)
                .risingSign(risingSign)
                .dominantElement(dominantElement);
        }
        
        public UserStatsResponseBuilder withPremiumFeatures(String subscriptionType, Integer creditsRemaining) {
            return this
                .subscriptionType(subscriptionType)
                .subscriptionActive(true)
                .creditsRemaining(creditsRemaining);
        }
    }
    
    // ================ VALIDATION AND UTILITY METHODS ================
    
    /**
     * Validate the statistics data
     */
    public boolean isValid() {
        return chartsCreated != null && chartsCreated >= 0 &&
               accuracyRate != null && accuracyRate >= 0 && accuracyRate <= 100 &&
               streakDays != null && streakDays >= 0;
    }
    
    /**
     * Get a safe copy for API responses
     */
    public UserStatsResponse createSafeCopy() {
        UserStatsResponse copy = new UserStatsResponse();
        copy.chartsCreated = this.chartsCreated;
        copy.accuracyRate = this.accuracyRate;
        copy.cosmicEnergy = this.cosmicEnergy;
        copy.streakDays = this.streakDays;
        copy.totalReadings = this.totalReadings;
        copy.favoriteChartType = this.favoriteChartType;
        copy.mostActiveTimeOfDay = this.mostActiveTimeOfDay;
        copy.averageSessionDuration = this.averageSessionDuration;
        copy.totalPredictions = this.totalPredictions;
        copy.correctPredictions = this.correctPredictions;
        copy.calculatedAt = LocalDateTime.now();
        copy.dataFreshness = "Live";
        copy.statisticsReliability = 100;
        return copy;
    }
    
    @Override
    public String toString() {
        return String.format(
            "UserStatsResponse{charts=%d, accuracy=%d%%, energy='%s', streak=%d, tier='%s', subscription='%s'}",
            chartsCreated != null ? chartsCreated : 0,
            accuracyRate != null ? accuracyRate : 0,
            cosmicEnergy != null ? cosmicEnergy : "Unknown",
            streakDays != null ? streakDays : 0,
            getUserTier(),
            subscriptionType != null ? subscriptionType : "FREE"
        );
    }
}
