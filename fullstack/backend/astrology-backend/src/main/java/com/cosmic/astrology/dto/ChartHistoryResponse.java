package com.cosmic.astrology.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.HashMap;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Objects;

/**
 * Chart History Response DTO with comprehensive chart generation tracking
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "User's birth chart generation history and statistics")
public class ChartHistoryResponse {
    
    @Schema(description = "Username", example = "testuser")
    private String username;
    
    @Schema(description = "Total charts generated", example = "15")
    private Integer totalChartsGenerated;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Last chart generation timestamp")
    private LocalDateTime lastChartGenerated;
    
    @Schema(description = "Chart generation history")
    private List<Map<String, Object>> chartHistory;
    
    @Schema(description = "Total history records count", example = "15")
    private Integer historyCount;
    
    @Schema(description = "Chart types breakdown")
    private Map<String, Integer> chartTypeBreakdown;
    
    @Schema(description = "Calculation systems used")
    private Map<String, Integer> calculationSystems;
    
    @Schema(description = "Most used chart type", example = "NATAL_CHART")
    private String mostUsedChartType;
    
    @Schema(description = "Average accuracy rate", example = "98.5")
    private Double averageAccuracy;
    
    @Schema(description = "Monthly chart generation breakdown")
    private Map<String, Integer> monthlyBreakdown;
    
    @Schema(description = "Favorite calculation time", example = "Evening")
    private String favoriteCalculationTime;
    
    @Schema(description = "Charts generated this month", example = "3")
    private Integer chartsThisMonth;
    
    @Schema(description = "Charts generated this year", example = "12")
    private Integer chartsThisYear;
    
    @Schema(description = "First chart generated date")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime firstChartDate;
    
    @Schema(description = "Chart generation streak (days)", example = "5")
    private Integer chartGenerationStreak;
    
    @Schema(description = "Locations used for chart calculations")
    private List<String> calculationLocations;
    
    @Schema(description = "Birth data versions history")
    private List<Map<String, Object>> birthDataHistory;
    
    // Constructors
    public ChartHistoryResponse() {}
    
    public ChartHistoryResponse(String username) {
        this.username = username;
    }
    
    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public Integer getTotalChartsGenerated() { return totalChartsGenerated; }
    public void setTotalChartsGenerated(Integer totalChartsGenerated) { 
        this.totalChartsGenerated = totalChartsGenerated; 
    }
    
    public LocalDateTime getLastChartGenerated() { return lastChartGenerated; }
    public void setLastChartGenerated(LocalDateTime lastChartGenerated) { 
        this.lastChartGenerated = lastChartGenerated; 
    }
    
    public List<Map<String, Object>> getChartHistory() { return chartHistory; }
    public void setChartHistory(List<Map<String, Object>> chartHistory) { this.chartHistory = chartHistory; }
    
    public Integer getHistoryCount() { return historyCount; }
    public void setHistoryCount(Integer historyCount) { this.historyCount = historyCount; }
    
    public Map<String, Integer> getChartTypeBreakdown() { return chartTypeBreakdown; }
    public void setChartTypeBreakdown(Map<String, Integer> chartTypeBreakdown) { 
        this.chartTypeBreakdown = chartTypeBreakdown; 
    }
    
    public Map<String, Integer> getCalculationSystems() { return calculationSystems; }
    public void setCalculationSystems(Map<String, Integer> calculationSystems) { 
        this.calculationSystems = calculationSystems; 
    }
    
    public String getMostUsedChartType() { return mostUsedChartType; }
    public void setMostUsedChartType(String mostUsedChartType) { this.mostUsedChartType = mostUsedChartType; }
    
    public Double getAverageAccuracy() { return averageAccuracy; }
    public void setAverageAccuracy(Double averageAccuracy) { this.averageAccuracy = averageAccuracy; }
    
    public Map<String, Integer> getMonthlyBreakdown() { return monthlyBreakdown; }
    public void setMonthlyBreakdown(Map<String, Integer> monthlyBreakdown) { this.monthlyBreakdown = monthlyBreakdown; }
    
    public String getFavoriteCalculationTime() { return favoriteCalculationTime; }
    public void setFavoriteCalculationTime(String favoriteCalculationTime) { 
        this.favoriteCalculationTime = favoriteCalculationTime; 
    }
    
    public Integer getChartsThisMonth() { return chartsThisMonth; }
    public void setChartsThisMonth(Integer chartsThisMonth) { this.chartsThisMonth = chartsThisMonth; }
    
    public Integer getChartsThisYear() { return chartsThisYear; }
    public void setChartsThisYear(Integer chartsThisYear) { this.chartsThisYear = chartsThisYear; }
    
    public LocalDateTime getFirstChartDate() { return firstChartDate; }
    public void setFirstChartDate(LocalDateTime firstChartDate) { this.firstChartDate = firstChartDate; }
    
    public Integer getChartGenerationStreak() { return chartGenerationStreak; }
    public void setChartGenerationStreak(Integer chartGenerationStreak) { 
        this.chartGenerationStreak = chartGenerationStreak; 
    }
    
    public List<String> getCalculationLocations() { return calculationLocations; }
    public void setCalculationLocations(List<String> calculationLocations) { 
        this.calculationLocations = calculationLocations; 
    }
    
    public List<Map<String, Object>> getBirthDataHistory() { return birthDataHistory; }
    public void setBirthDataHistory(List<Map<String, Object>> birthDataHistory) { 
        this.birthDataHistory = birthDataHistory; 
    }
    
    // Utility Methods
    @JsonProperty("chartingExperienceLevel")
    public String getChartingExperienceLevel() {
        if (totalChartsGenerated == null) return "Beginner";
        
        if (totalChartsGenerated >= 50) return "Expert";
        if (totalChartsGenerated >= 20) return "Advanced";
        if (totalChartsGenerated >= 5) return "Intermediate";
        return "Beginner";
    }
    
    @JsonProperty("averageChartsPerMonth")
    public double getAverageChartsPerMonth() {
        if (totalChartsGenerated == null || firstChartDate == null) return 0.0;
        
        long months = java.time.temporal.ChronoUnit.MONTHS.between(firstChartDate, LocalDateTime.now());
        if (months == 0) months = 1; // Avoid division by zero
        
        return (double) totalChartsGenerated / months;
    }
    
    @JsonProperty("daysSinceLastChart")
    public long getDaysSinceLastChart() {
        if (lastChartGenerated == null) return -1;
        return java.time.temporal.ChronoUnit.DAYS.between(lastChartGenerated, LocalDateTime.now());
    }
    
    @JsonProperty("isActiveChartGenerator") 
    public boolean isActiveChartGenerator() {
        return getDaysSinceLastChart() <= 30;
    }
    
    @JsonProperty("chartingJourneyDuration")
    public String getChartingJourneyDuration() {
        if (firstChartDate == null) return "Just started";
        
        long days = java.time.temporal.ChronoUnit.DAYS.between(firstChartDate, LocalDateTime.now());
        if (days < 30) return days + " days";
        if (days < 365) return (days / 30) + " months";
        return (days / 365) + " years";
    }
    
    @JsonProperty("preferredChartStyle")
    public String getPreferredChartStyle() {
        if (calculationSystems == null || calculationSystems.isEmpty()) return "Vedic (Default)";
        
        return calculationSystems.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Vedic (Default)");
    }
    
    @JsonProperty("nextMilestone")
    public Map<String, Object> getNextMilestone() {
        Map<String, Object> milestone = new HashMap<>();
        
        if (totalChartsGenerated == null) {
            milestone.put("target", 1);
            milestone.put("name", "First Chart");
            milestone.put("progress", 0);
            return milestone;
        }
        
        int current = totalChartsGenerated;
        int target;
        String name;
        
        if (current < 5) {
            target = 5;
            name = "Chart Explorer";
        } else if (current < 10) {
            target = 10;
            name = "Astrology Enthusiast";
        } else if (current < 25) {
            target = 25;
            name = "Chart Master";
        } else if (current < 50) {
            target = 50;
            name = "Vedic Expert";
        } else {
            target = 100;
            name = "Astrology Guru";
        }
        
        milestone.put("target", target);
        milestone.put("name", name);
        milestone.put("progress", Math.round((double) current / target * 100));
        milestone.put("remaining", target - current);
        
        return milestone;
    }
    
    @JsonProperty("chartQualityTrend")
    public String getChartQualityTrend() {
        if (averageAccuracy == null) return "Unknown";
        
        if (averageAccuracy >= 95) return "Excellent";
        if (averageAccuracy >= 90) return "Very Good";
        if (averageAccuracy >= 85) return "Good";
        if (averageAccuracy >= 80) return "Fair";
        return "Needs Improvement";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ChartHistoryResponse that = (ChartHistoryResponse) obj;
        return Objects.equals(username, that.username);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
    
    @Override
    public String toString() {
        return String.format("ChartHistoryResponse{username='%s', totalCharts=%d, experienceLevel='%s'}", 
                           username, totalChartsGenerated, getChartingExperienceLevel());
    }
}
