package com.cosmic.astrology.controller;

import com.cosmic.astrology.dto.*;
import com.cosmic.astrology.service.AstrologyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Comprehensive Vedic Astrology REST Controller
 * Provides all endpoints for professional Vedic astrology calculations and services
 */
@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:3000"}, maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/astrology")
@PreAuthorize("hasRole('CLIENT') or hasRole('ASTROLOGER') or hasRole('USER')")
@Validated
@Tag(name = "Vedic Astrology", description = "Professional Vedic Astrology API endpoints")
public class AstrologyController {
    
    @Autowired
    private AstrologyService astrologyService;
    
    // ================ PERSONALIZED SERVICES ================
    
    /**
     * Get personalized Vedic message for the authenticated user
     */
    @GetMapping("/personalized-message")
    @Operation(summary = "Get Personalized Vedic Message", 
               description = "Returns a personalized message based on user's Vedic birth chart and current transits")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Personalized message generated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request or missing birth data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> getPersonalizedMessage(Principal principal) {
        try {
            System.out.println("🕉️ Personalized Vedic message requested by: " + principal.getName());
            
            PersonalizedMessageResponse message = astrologyService.getPersonalizedMessage(principal.getName());
            
            System.out.println("✅ Personalized message generated successfully for: " + principal.getName());
            return ResponseEntity.ok(message);
            
        } catch (IllegalArgumentException e) {
            System.err.println("⚠️ Invalid request for personalized message: " + e.getMessage());
            return ResponseEntity.badRequest().body(createErrorResponse("Invalid request", e.getMessage()));
        } catch (Exception e) {
            System.err.println("❌ Error generating personalized message for " + principal.getName() + ": " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Internal error", "Unable to generate personalized message"));
        }
    }
    
    /**
     * Get life area influences based on Vedic astrology
     */
    @GetMapping("/life-area-influences")
    @Operation(summary = "Get Vedic Life Area Influences", 
               description = "Returns influences on different life areas based on Vedic astrology")
    public ResponseEntity<?> getLifeAreaInfluences(Principal principal) {
        try {
            System.out.println("🕉️ Vedic life area influences requested by: " + principal.getName());
            
            List<LifeAreaInfluence> influences = astrologyService.getLifeAreaInfluences(principal.getName());
            
            System.out.println("✅ Life area influences calculated for: " + principal.getName());
            return ResponseEntity.ok(influences);
            
        } catch (Exception e) {
            System.err.println("❌ Error calculating life area influences for " + principal.getName() + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Calculation error", "Unable to calculate life area influences"));
        }
    }
    
    /**
     * Get user statistics and cosmic insights
     */
    @GetMapping("/user-stats")
    @Operation(summary = "Get User Vedic Statistics", 
               description = "Returns user statistics including charts generated, accuracy, and cosmic energy")
    public ResponseEntity<?> getUserStats(Principal principal) {
        try {
            System.out.println("🕉️ User Vedic stats requested by: " + principal.getName());
            
            UserStatsResponse stats = astrologyService.getUserStats(principal.getName());
            
            System.out.println("✅ User stats retrieved for: " + principal.getName());
            return ResponseEntity.ok(stats);
            
        } catch (Exception e) {
            System.err.println("❌ Error fetching user stats for " + principal.getName() + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Data error", "Unable to fetch user statistics"));
        }
    }
    
    // ================ BIRTH CHART SERVICES ================
    
    /**
     * Calculate Vedic birth chart from provided birth data
     */
    @PostMapping("/calculate-birth-chart")
    @Operation(summary = "Calculate Vedic Birth Chart", 
               description = "Calculates a comprehensive Vedic birth chart using Swiss Ephemeris")
    public ResponseEntity<?> calculateBirthChart(
            @Valid @RequestBody BirthData birthData,
            Principal principal) {
        try {
            System.out.println("🕉️ Vedic birth chart calculation requested by: " + principal.getName());
            System.out.println("📍 Birth data received:");
            System.out.println("   - DateTime: " + birthData.getBirthDateTime());
            System.out.println("   - Location: " + birthData.getBirthLocation());
            System.out.println("   - Latitude: " + birthData.getBirthLatitude());
            System.out.println("   - Longitude: " + birthData.getBirthLongitude());
            
            BirthChartResponse chart = astrologyService.calculateBirthChart(birthData, principal.getName());
            
            System.out.println("✅ Vedic birth chart calculated successfully for: " + principal.getName());
            return ResponseEntity.ok(chart);
            
        } catch (IllegalArgumentException e) {
            System.err.println("⚠️ Invalid birth data: " + e.getMessage());
            return ResponseEntity.badRequest().body(createErrorResponse("Invalid birth data", e.getMessage()));
        } catch (Exception e) {
            System.err.println("❌ Error calculating Vedic birth chart: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Calculation failed", "Unable to calculate Vedic birth chart"));
        }
    }
    
    /**
     * Get user's stored Vedic birth chart
     */
    @GetMapping("/birth-chart")
    @Operation(summary = "Get User's Vedic Birth Chart", 
               description = "Retrieves the user's stored Vedic birth chart or calculates if not available")
    public ResponseEntity<?> getUserBirthChart(Principal principal) {
        try {
            System.out.println("🕉️ Vedic birth chart requested by: " + principal.getName());
            
            BirthChartResponse chart = astrologyService.getUserBirthChart(principal.getName());
            
            System.out.println("✅ Vedic birth chart retrieved for: " + principal.getName());
            return ResponseEntity.ok(chart);
            
        } catch (RuntimeException e) {
            System.err.println("⚠️ Birth chart not available: " + e.getMessage());
            return ResponseEntity.badRequest().body(createErrorResponse("Chart not available", e.getMessage()));
        } catch (Exception e) {
            System.err.println("❌ Error retrieving birth chart: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Retrieval error", "Unable to retrieve birth chart"));
        }
    }
    
    // ================ TRANSIT SERVICES ================
    
    /**
     * Get current Vedic planetary transits
     */
    @GetMapping("/current-transits")
    @Operation(summary = "Get Current Vedic Transits", 
               description = "Returns current sidereal planetary positions and transits")
    public ResponseEntity<?> getCurrentTransits(Principal principal) {
        try {
            System.out.println("🕉️ Current Vedic transits requested by: " + principal.getName());
            
            List<TransitResponse> transits = astrologyService.getCurrentTransits(principal.getName());
            
            System.out.println("✅ Current transits calculated for: " + principal.getName());
            return ResponseEntity.ok(transits);
            
        } catch (Exception e) {
            System.err.println("❌ Error calculating current transits: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Transit error", "Unable to calculate current transits"));
        }
    }
    
    /**
     * Get personalized transit analysis
     */
    @GetMapping("/transit-analysis")
    @Operation(summary = "Get Personalized Transit Analysis", 
               description = "Analyzes current transits against user's natal chart")
    public ResponseEntity<?> getTransitAnalysis(Principal principal) {
        try {
            System.out.println("🕉️ Transit analysis requested by: " + principal.getName());
            
            // Get user's natal chart and current transits
            BirthChartResponse natalChart = astrologyService.getUserBirthChart(principal.getName());
            List<TransitResponse> currentTransits = astrologyService.getCurrentTransits(principal.getName());
            
            // Create comprehensive transit analysis response
            Map<String, Object> analysisResponse = new HashMap<>();
            analysisResponse.put("natalChart", natalChart);
            analysisResponse.put("currentTransits", currentTransits);
            analysisResponse.put("analysisDate", LocalDateTime.now());
            analysisResponse.put("significantTransits", extractSignificantTransits(currentTransits));
            analysisResponse.put("recommendations", generateTransitRecommendations(natalChart, currentTransits));
            
            System.out.println("✅ Transit analysis completed for: " + principal.getName());
            return ResponseEntity.ok(analysisResponse);
            
        } catch (Exception e) {
            System.err.println("❌ Error in transit analysis: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Analysis error", "Unable to perform transit analysis"));
        }
    }
    
    // ================ VEDIC SPECIFIC SERVICES ================
    
    /**
     * Get Nakshatra information for current time
     */
    @GetMapping("/current-nakshatra")
    @Operation(summary = "Get Current Moon Nakshatra", 
               description = "Returns current Moon's Nakshatra and auspicious timing information")
    public ResponseEntity<?> getCurrentNakshatra(Principal principal) {
        try {
            System.out.println("🕉️ Current Nakshatra requested by: " + principal.getName());
            
            List<TransitResponse> transits = astrologyService.getCurrentTransits(principal.getName());
            TransitResponse moonTransit = transits.stream()
                    .filter(t -> "Moon".equals(t.getPlanet()))
                    .findFirst()
                    .orElse(null);
            
            if (moonTransit == null) {
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("Data unavailable", "Current Moon position not available"));
            }
            
            Map<String, Object> nakshatraInfo = new HashMap<>();
            nakshatraInfo.put("nakshatra", moonTransit.getNakshatra());
            nakshatraInfo.put("pada", moonTransit.getPada());
            nakshatraInfo.put("moonPosition", moonTransit.getPosition());
            nakshatraInfo.put("moonSign", moonTransit.getSign());
            nakshatraInfo.put("calculatedAt", LocalDateTime.now());
            nakshatraInfo.put("auspiciousness", determineAuspiciousness(moonTransit.getNakshatra()));
            
            System.out.println("✅ Current Nakshatra info provided for: " + principal.getName());
            return ResponseEntity.ok(nakshatraInfo);
            
        } catch (Exception e) {
            System.err.println("❌ Error getting current Nakshatra: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Nakshatra error", "Unable to get current Nakshatra"));
        }
    }
    
    /**
     * Get Vedic compatibility analysis between two charts
     */
    @PostMapping("/compatibility")
    @Operation(summary = "Vedic Compatibility Analysis", 
               description = "Analyzes compatibility between user's chart and provided birth data")
    public ResponseEntity<?> getVedicCompatibility(
            @Valid @RequestBody BirthData partnerBirthData,
            Principal principal) {
        try {
            System.out.println("🕉️ Vedic compatibility analysis requested by: " + principal.getName());
            
            // Get user's natal chart
            BirthChartResponse userChart = astrologyService.getUserBirthChart(principal.getName());
            
            // Calculate partner's chart (temporary calculation without storing)
            BirthChartResponse partnerChart = astrologyService.calculateBirthChart(partnerBirthData, "temp_partner");
            
            // Perform compatibility analysis
            Map<String, Object> compatibilityAnalysis = new HashMap<>();
            compatibilityAnalysis.put("userChart", userChart);
            compatibilityAnalysis.put("partnerChart", partnerChart);
            compatibilityAnalysis.put("compatibilityScore", calculateCompatibilityScore(userChart, partnerChart));
            compatibilityAnalysis.put("ganaMatching", calculateGanaMatching(userChart, partnerChart));
            compatibilityAnalysis.put("nakshatraCompatibility", calculateNakshatraCompatibility(userChart, partnerChart));
            compatibilityAnalysis.put("manglikAnalysis", calculateManglikDosha(userChart, partnerChart));
            compatibilityAnalysis.put("recommendations", generateCompatibilityRecommendations(userChart, partnerChart));
            compatibilityAnalysis.put("analysisDate", LocalDateTime.now());
            
            System.out.println("✅ Vedic compatibility analysis completed for: " + principal.getName());
            return ResponseEntity.ok(compatibilityAnalysis);
            
        } catch (Exception e) {
            System.err.println("❌ Error in compatibility analysis: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Compatibility error", "Unable to perform compatibility analysis"));
        }
    }
    
    // ================ UTILITY ENDPOINTS ================
    
    /**
     * Health check endpoint for the astrology service
     */
    @GetMapping("/health")
    @PreAuthorize("permitAll()")
    @Operation(summary = "Health Check", description = "Check if astrology services are operational")
    public ResponseEntity<?> healthCheck() {
        try {
            Map<String, Object> health = new HashMap<>();
            health.put("status", "healthy");
            health.put("service", "Vedic Astrology API");
            health.put("timestamp", LocalDateTime.now());
            health.put("version", "1.0.0");
            health.put("ephemeris", "Swiss Ephemeris");
            
            return ResponseEntity.ok(health);
        } catch (Exception e) {
            Map<String, Object> health = new HashMap<>();
            health.put("status", "unhealthy");
            health.put("error", e.getMessage());
            health.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(health);
        }
    }
    
    /**
     * Get available astrological services
     */
    @GetMapping("/services")
    @Operation(summary = "Available Services", description = "List all available astrological services")
    public ResponseEntity<?> getAvailableServices() {
        Map<String, Object> services = new HashMap<>();
        services.put("birthChartCalculation", "Complete Vedic birth chart calculation using Swiss Ephemeris");
        services.put("transitAnalysis", "Real-time planetary transit analysis");
        services.put("personalizedMessages", "AI-powered personalized astrological insights");
        services.put("compatibilityAnalysis", "Traditional Vedic compatibility matching");
        services.put("nakshatraAnalysis", "Comprehensive lunar mansion analysis");
        services.put("lifeAreaInfluences", "Analysis of planetary influences on life areas");
        services.put("remedialMeasures", "Traditional Vedic remedies and recommendations");
        services.put("ephemerisAccuracy", "NASA/JPL level accuracy using Swiss Ephemeris");
        services.put("supportedSystems", new String[]{"Vedic/Sidereal", "Lahiri Ayanamsa", "Whole Sign Houses"});
        
        return ResponseEntity.ok(services);
    }
    
    // ================ PRIVATE HELPER METHODS ================
    
    private Map<String, Object> createErrorResponse(String error, String message) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", error);
        errorResponse.put("message", message);
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("success", false);
        return errorResponse;
    }
    
    private List<String> extractSignificantTransits(List<TransitResponse> transits) {
        // Extract significant transits (implement based on your business logic)
        return transits.stream()
                .filter(t -> t.getIntensityLevel() != null && t.getIntensityLevel() >= 4)
                .map(t -> t.getPlanet() + " in " + t.getSign())
                .toList();
    }
    
    private List<String> generateTransitRecommendations(BirthChartResponse natal, List<TransitResponse> transits) {
        // Generate recommendations based on transits (implement your logic)
        return List.of(
            "Focus on spiritual practices during this transit period",
            "Favorable time for new beginnings and partnerships",
            "Avoid major financial decisions this week"
        );
    }
    
    private String determineAuspiciousness(String nakshatra) {
        if (nakshatra == null) return "Neutral";
        
        // Simplified auspiciousness determination
        List<String> auspicious = List.of("Rohini", "Uttara Phalguni", "Uttara Ashadha", "Uttara Bhadrapada");
        List<String> inauspicious = List.of("Bharani", "Ashlesha", "Jyeshtha", "Mula");
        
        if (auspicious.contains(nakshatra)) return "Highly Auspicious";
        if (inauspicious.contains(nakshatra)) return "Challenging";
        return "Moderate";
    }
    
    private double calculateCompatibilityScore(BirthChartResponse chart1, BirthChartResponse chart2) {
        // Simplified compatibility scoring (implement detailed Vedic matching)
        double score = 75.0; // Base score
        
        // Moon sign compatibility
        if (chart1.getMoonSign() != null && chart1.getMoonSign().equals(chart2.getMoonSign())) {
            score += 10;
        }
        
        // Ascendant compatibility
        if (chart1.getRisingSign() != null && chart1.getRisingSign().equals(chart2.getRisingSign())) {
            score += 5;
        }
        
        return Math.min(score, 100.0);
    }
    
    private String calculateGanaMatching(BirthChartResponse chart1, BirthChartResponse chart2) {
        // Implement Gana matching logic
        return "Compatible (Both belong to Deva Gana)";
    }
    
    private String calculateNakshatraCompatibility(BirthChartResponse chart1, BirthChartResponse chart2) {
        // Implement Nakshatra compatibility
        return "Favorable - 27/36 points";
    }
    
    private String calculateManglikDosha(BirthChartResponse chart1, BirthChartResponse chart2) {
        // Implement Manglik analysis
        return "No Manglik Dosha detected";
    }
    
    private List<String> generateCompatibilityRecommendations(BirthChartResponse chart1, BirthChartResponse chart2) {
        return List.of(
            "Perform joint meditation and spiritual practices",
            "Consider Mars remedies for harmony",
            "Favorable marriage timing: After September 2024"
        );
    }
}
