package com.cosmic.astrology.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.HashMap;
/**
 * Profile Completion Response DTO with detailed completion tracking
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Profile completion status and recommendations")
public class ProfileCompletionResponse {
    
    @Schema(description = "Username", example = "testuser")
    private String username;
    
    @Schema(description = "Overall completion percentage", example = "85")
    private Integer completionPercentage;
    
    @Schema(description = "List of missing fields")
    private List<String> missingFields;
    
    @Schema(description = "List of completed fields")
    private List<String> completedFields;
    
    @Schema(description = "Whether profile is considered complete", example = "false")
    private Boolean isComplete;
    
    @Schema(description = "Profile completion level", example = "Good")
    private String completionLevel; // Poor, Fair, Good, Excellent
    
    @Schema(description = "Detailed field completion breakdown")
    private Map<String, Map<String, Object>> fieldBreakdown;
    
    @Schema(description = "Priority recommendations for completion")
    private List<Map<String, Object>> recommendations;
    
    @Schema(description = "Completion score (0-100)", example = "85")
    private Integer completionScore;
    
    @Schema(description = "Fields required for chart generation")
    private List<String> requiredForCharts;
    
    @Schema(description = "Optional enhancement fields")
    private List<String> optionalFields;
    
    @Schema(description = "Security-related completion items")
    private List<String> securityItems;
    
    @Schema(description = "Points earned for profile completion", example = "340")
    private Integer pointsEarned;
    
    @Schema(description = "Maximum possible points", example = "400")
    private Integer maxPoints;
    
    @Schema(description = "Next completion milestone")
    private String nextMilestone;
    
    @Schema(description = "Estimated time to complete (minutes)", example = "15")
    private Integer estimatedTimeToComplete;
    
    // Constructors
    public ProfileCompletionResponse() {}
    
    public ProfileCompletionResponse(String username, Integer completionPercentage) {
        this.username = username;
        this.completionPercentage = completionPercentage;
    }
    
    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public Integer getCompletionPercentage() { return completionPercentage; }
    public void setCompletionPercentage(Integer completionPercentage) { 
        this.completionPercentage = completionPercentage; 
    }
    
    public List<String> getMissingFields() { return missingFields; }
    public void setMissingFields(List<String> missingFields) { this.missingFields = missingFields; }
    
    public List<String> getCompletedFields() { return completedFields; }
    public void setCompletedFields(List<String> completedFields) { this.completedFields = completedFields; }
    
    public Boolean getIsComplete() { return isComplete; }
    public void setIsComplete(Boolean isComplete) { this.isComplete = isComplete; }
    
    public String getCompletionLevel() { return completionLevel; }
    public void setCompletionLevel(String completionLevel) { this.completionLevel = completionLevel; }
    
    public Map<String, Map<String, Object>> getFieldBreakdown() { return fieldBreakdown; }
    public void setFieldBreakdown(Map<String, Map<String, Object>> fieldBreakdown) { 
        this.fieldBreakdown = fieldBreakdown; 
    }
    
    public List<Map<String, Object>> getRecommendations() { return recommendations; }
    public void setRecommendations(List<Map<String, Object>> recommendations) { 
        this.recommendations = recommendations; 
    }
    
    public Integer getCompletionScore() { return completionScore; }
    public void setCompletionScore(Integer completionScore) { this.completionScore = completionScore; }
    
    public List<String> getRequiredForCharts() { return requiredForCharts; }
    public void setRequiredForCharts(List<String> requiredForCharts) { this.requiredForCharts = requiredForCharts; }
    
    public List<String> getOptionalFields() { return optionalFields; }
    public void setOptionalFields(List<String> optionalFields) { this.optionalFields = optionalFields; }
    
    public List<String> getSecurityItems() { return securityItems; }
    public void setSecurityItems(List<String> securityItems) { this.securityItems = securityItems; }
    
    public Integer getPointsEarned() { return pointsEarned; }
    public void setPointsEarned(Integer pointsEarned) { this.pointsEarned = pointsEarned; }
    
    public Integer getMaxPoints() { return maxPoints; }
    public void setMaxPoints(Integer maxPoints) { this.maxPoints = maxPoints; }
    
    public String getNextMilestone() { return nextMilestone; }
    public void setNextMilestone(String nextMilestone) { this.nextMilestone = nextMilestone; }
    
    public Integer getEstimatedTimeToComplete() { return estimatedTimeToComplete; }
    public void setEstimatedTimeToComplete(Integer estimatedTimeToComplete) { 
        this.estimatedTimeToComplete = estimatedTimeToComplete; 
    }
    
    // Utility Methods
    @JsonProperty("completionGrade")
    public String getCompletionGrade() {
        if (completionPercentage == null) return "F";
        
        if (completionPercentage >= 90) return "A+";
        if (completionPercentage >= 85) return "A";
        if (completionPercentage >= 80) return "B+";
        if (completionPercentage >= 75) return "B";
        if (completionPercentage >= 70) return "C+";
        if (completionPercentage >= 60) return "C";
        if (completionPercentage >= 50) return "D";
        return "F";
    }
    
    @JsonProperty("progressBar")
    public Map<String, Object> getProgressBar() {
        Map<String, Object> progress = new HashMap<>();
        progress.put("percentage", completionPercentage != null ? completionPercentage : 0);
        progress.put("color", getProgressColor());
        progress.put("label", completionPercentage + "% Complete");
        progress.put("showPercentage", true);
        return progress;
    }
    
    @JsonProperty("completionStatus")
    public String getCompletionStatus() {
        if (completionPercentage == null) return "Not Started";
        
        if (completionPercentage >= 95) return "Excellent Profile";
        if (completionPercentage >= 85) return "Well Completed";
        if (completionPercentage >= 70) return "Good Progress";
        if (completionPercentage >= 50) return "Getting There";
        if (completionPercentage >= 25) return "Good Start";
        return "Just Beginning";
    }
    
    @JsonProperty("canGenerateCharts")
    public boolean canGenerateCharts() {
        return requiredForCharts == null || requiredForCharts.isEmpty();
    }
    
    @JsonProperty("priorityAction")
    public String getPriorityAction() {
        if (missingFields == null || missingFields.isEmpty()) {
            return "Profile Complete! 🎉";
        }
        
        // Prioritize essential fields for chart generation
        if (missingFields.contains("Birth Date & Time")) {
            return "Add your birth date and time to unlock your cosmic profile";
        }
        if (missingFields.contains("Birth Location")) {
            return "Add your birth location for accurate chart calculations";
        }
        if (missingFields.contains("Birth Coordinates")) {
            return "Complete location details for precise astrological insights";
        }
        if (missingFields.contains("Email Verification")) {
            return "Verify your email to secure your account";
        }
        
        return "Complete your " + missingFields.get(0).toLowerCase() + " to improve your profile";
    }
    
    @JsonProperty("motivationalMessage")
    public String getMotivationalMessage() {
        if (completionPercentage == null || completionPercentage < 25) {
            return "🌟 Your cosmic journey begins with completing your profile!";
        }
        if (completionPercentage < 50) {
            return "🚀 Great start! Keep adding details to unlock more insights.";
        }
        if (completionPercentage < 75) {
            return "✨ You're doing amazing! Your cosmic profile is taking shape.";
        }
        if (completionPercentage < 95) {
            return "🌙 Almost there! Complete your profile for the full experience.";
        }
        return "🕉️ Perfect! Your cosmic profile is complete and ready for deep insights.";
    }
    
    @JsonProperty("completionBenefits")
    public List<String> getCompletionBenefits() {
        return List.of(
            "Generate accurate Vedic birth charts",
            "Receive personalized cosmic insights",
            "Access premium astrological features",
            "Get tailored remedial recommendations",
            "Join the community of serious seekers",
            "Unlock advanced transit predictions"
        );
    }
    
    private String getProgressColor() {
        if (completionPercentage == null) return "#gray";
        
        if (completionPercentage >= 90) return "#10B981"; // Green
        if (completionPercentage >= 70) return "#3B82F6"; // Blue
        if (completionPercentage >= 50) return "#F59E0B"; // Yellow
        return "#EF4444"; // Red
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ProfileCompletionResponse that = (ProfileCompletionResponse) obj;
        return Objects.equals(username, that.username) &&
               Objects.equals(completionPercentage, that.completionPercentage);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(username, completionPercentage);
    }
    
    @Override
    public String toString() {
        return String.format("ProfileCompletionResponse{username='%s', completion=%d%%, level='%s'}", 
                           username, completionPercentage, completionLevel);
    }
}
