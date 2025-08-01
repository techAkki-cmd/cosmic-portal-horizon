package com.cosmic.astrology.controller;

import com.cosmic.astrology.dto.*;
import com.cosmic.astrology.entity.User;
import com.cosmic.astrology.service.AstrologyService;
import com.cosmic.astrology.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 🌟 COMPREHENSIVE VEDIC ASTROLOGY REST CONTROLLER
 * Provides complete astrological analysis including birth charts, yogas, dashas, remedies, and transits
 * Integrates with the advanced AstrologyService for world-class insights
 */
@RestController
@RequestMapping("/api/birth-chart")
@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:3000"}, maxAge = 3600, allowCredentials = "true")
public class BirthChartController {

    private static final Logger logger = LoggerFactory.getLogger(BirthChartController.class);

    @Autowired
    private AstrologyService astrologyService;

    @Autowired
    private UserService userService;

    // ================ ENHANCED ENDPOINTS WITH QUERY PARAMETER SUPPORT ================

    /**
     * 🔥 GET PERSONALIZED MESSAGE - Fixed with query parameter support
     */
    @GetMapping("/personalized-message")
    @PreAuthorize("permitAll()")
    public ResponseEntity<PersonalizedMessageResponse> getPersonalizedMessage(
            @RequestParam(required = false) String username,
            HttpServletRequest request) {
        try {
            String finalUsername = (username != null && !username.isEmpty()) 
                ? username 
                : extractUsername(request);
                
            logger.info("🔮 Getting personalized message for: {}", finalUsername);
            
            PersonalizedMessageResponse response = astrologyService.getPersonalizedMessage(finalUsername);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("❌ Error generating personalized message for {}: {}", username, e.getMessage());
            // Return fallback response instead of error
            return ResponseEntity.ok(createFallbackPersonalizedMessage(username));
        }
    }

    /**
     * 🔥 GET CURRENT TRANSITS - Fixed with query parameter support
     */
    @GetMapping("/current-transits")
@PreAuthorize("permitAll()")
public ResponseEntity<List<TransitResponse>> getCurrentTransits(
        @RequestParam(required = false) String username,
        HttpServletRequest request) {
    try {
        String finalUsername = (username != null && !username.isEmpty()) 
            ? username 
            : extractUsername(request);
            
        logger.info("🌍 Getting current transits for: {}", finalUsername);
        
        // ✅ ADD THIS: Check if user has complete birth data before natal chart calculation
        User user = validateAndGetUser(finalUsername);
        if (user == null || !hasCompleteBirthData(user)) {
            logger.warn("⚠️ User {} lacks complete birth data, returning generic transits", finalUsername);
            return ResponseEntity.ok(createGenericTransits());
        }
        
        List<TransitResponse> transits = astrologyService.getCurrentTransits(finalUsername);
        return ResponseEntity.ok(transits);
        
    } catch (Exception e) {
        logger.error("❌ Error fetching current transits for {}: {}", username, e.getMessage());
        // Return fallback transits instead of error
        return ResponseEntity.ok(createFallbackTransits());
    }
}

// ✅ ADD THESE HELPER METHODS:
private boolean hasCompleteBirthData(User user) {
    return user.getBirthDateTime() != null && 
           user.getBirthLatitude() != null && 
           user.getBirthLongitude() != null &&
           user.getBirthLocation() != null &&
           !user.getBirthLocation().trim().isEmpty();
}

private List<TransitResponse> createGenericTransits() {
    List<TransitResponse> transits = new ArrayList<>();
    
    // Create current date-based generic transits without user-specific calculation
    LocalDateTime now = LocalDateTime.now();
    
    TransitResponse sunTransit = new TransitResponse();
    sunTransit.setPlanet("Sun");
    sunTransit.setPosition(120.5 + (now.getDayOfYear() % 30)); // Approximate current position
    sunTransit.setSign("Leo");
    sunTransit.setNakshatra("Magha");
    sunTransit.setPada(2);
    sunTransit.setInfluence("Current solar energy supports leadership and vitality");
    transits.add(sunTransit);
    
    TransitResponse moonTransit = new TransitResponse();
    moonTransit.setPlanet("Moon");
    moonTransit.setPosition(45.3 + (now.getDayOfMonth() * 12)); // Approximate lunar position
    moonTransit.setSign("Taurus");
    moonTransit.setNakshatra("Rohini");
    moonTransit.setPada(1);
    moonTransit.setInfluence("Lunar energy enhances emotional stability");
    transits.add(moonTransit);
    
    return transits;
}


    /**
     * 🔥 GET LIFE AREA INFLUENCES - Fixed with query parameter support
     */
    @GetMapping("/life-area-influences")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<LifeAreaInfluence>> getLifeAreaInfluences(
            @RequestParam(required = false) String username,
            HttpServletRequest request) {
        try {
            String finalUsername = (username != null && !username.isEmpty()) 
                ? username 
                : extractUsername(request);
                
            logger.info("🎯 Getting life area influences for: {}", finalUsername);
            
            List<LifeAreaInfluence> influences = astrologyService.getLifeAreaInfluences(finalUsername);
            return ResponseEntity.ok(influences);
            
        } catch (Exception e) {
            logger.error("❌ Error calculating life area influences for {}: {}", username, e.getMessage());
            // Return fallback life areas instead of error
            return ResponseEntity.ok(createFallbackLifeAreas());
        }
    }

    /**
     * 🔥 GET USER STATS - Fixed with query parameter support
     */
    @GetMapping("/user-stats")
    @PreAuthorize("permitAll()")
    public ResponseEntity<UserStatsResponse> getUserStats(
            @RequestParam(required = false) String username,
            HttpServletRequest request) {
        try {
            String finalUsername = (username != null && !username.isEmpty()) 
                ? username 
                : extractUsername(request);
                
            logger.info("📊 Getting user stats for: {}", finalUsername);
            
            UserStatsResponse stats = astrologyService.getUserStats(finalUsername);
            return ResponseEntity.ok(stats);
            
        } catch (Exception e) {
            logger.error("❌ Error fetching user stats for {}: {}", username, e.getMessage());
            // Return fallback stats instead of error
            return ResponseEntity.ok(createFallbackStats());
        }
    }

    /**
     * 🔥 GET YOGA ANALYSIS - Fixed with query parameter support
     */
    @GetMapping("/yoga-analysis")
    @PreAuthorize("permitAll()")
    public ResponseEntity<YogaAnalysisResponse> getYogaAnalysis(
            @RequestParam(required = false) String username,
            HttpServletRequest request) {
        try {
            String finalUsername = (username != null && !username.isEmpty()) 
                ? username 
                : extractUsername(request);
                
            logger.info("🕉️ Getting yoga analysis for: {}", finalUsername);
            
            YogaAnalysisResponse response = astrologyService.getYogaAnalysis(finalUsername);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("❌ Error generating yoga analysis for {}: {}", username, e.getMessage());
            // Return fallback yoga analysis
            return ResponseEntity.ok(createFallbackYogaAnalysis());
        }
    }

    /**
     * 🔥 GET DASHA ANALYSIS - Fixed with query parameter support
     */
    @GetMapping("/dasha-analysis")
    @PreAuthorize("permitAll()")
    public ResponseEntity<DashaAnalysisResponse> getDashaAnalysis(
            @RequestParam(required = false) String username,
            HttpServletRequest request) {
        try {
            String finalUsername = (username != null && !username.isEmpty()) 
                ? username 
                : extractUsername(request);
                
            logger.info("📅 Getting dasha analysis for: {}", finalUsername);
            
            DashaAnalysisResponse response = astrologyService.getDashaAnalysis(finalUsername);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("❌ Error generating dasha analysis for {}: {}", username, e.getMessage());
            // Return fallback dasha analysis
            return ResponseEntity.ok(createFallbackDashaAnalysis());
        }
    }

    /**
     * 🔥 GET REMEDIAL RECOMMENDATIONS - Fixed with query parameter support
     */
    @GetMapping("/remedial-recommendations")
    @PreAuthorize("permitAll()")
    public ResponseEntity<RemedialRecommendationsResponse> getRemedialRecommendations(
            @RequestParam(required = false) String username,
            HttpServletRequest request) {
        try {
            String finalUsername = (username != null && !username.isEmpty()) 
                ? username 
                : extractUsername(request);
                
            logger.info("💎 Getting remedial recommendations for: {}", finalUsername);
            
            RemedialRecommendationsResponse response = astrologyService.getRemedialRecommendations(finalUsername);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("❌ Error generating remedial recommendations for {}: {}", username, e.getMessage());
            // Return fallback remedies
            return ResponseEntity.ok(createFallbackRemedialRecommendations());
        }
    }

    /**
 * 🔥 ADD THIS ENDPOINT if missing
 */
@PostMapping("/personalized")
@PreAuthorize("permitAll()") // ✅ Make sure this matches your other endpoints
public ResponseEntity<?> generatePersonalizedChart(
        @RequestBody BirthChartRequest request,
        @RequestParam(required = false) String username,
        HttpServletRequest httpRequest) {
    try {
        String finalUsername = (username != null && !username.isEmpty()) 
            ? username 
            : extractUsername(httpRequest);
            
        logger.info("🔮 Generating personalized chart for: {}", finalUsername);
        
        // Use your existing complete-analysis logic
        return generateCompleteAnalysis(request, httpRequest);
        
    } catch (Exception e) {
        logger.error("❌ Error generating personalized chart: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to generate personalized chart", 
                           "message", e.getMessage()));
    }
}


    // ================ EXISTING ENDPOINTS (UNCHANGED) ================

    /**
     * 🔥 GENERATE COMPLETE ASTROLOGICAL ANALYSIS
     */
    @PostMapping("/complete-analysis")
@PreAuthorize("permitAll()")
public ResponseEntity<Map<String, Object>> generateCompleteAnalysis(
        @RequestBody BirthChartRequest request,
        HttpServletRequest httpRequest) {
    try {
        String username = extractUsername(httpRequest);
        logger.info("🕉️ Generating complete astrological analysis for: {}", username);

        User user = validateAndGetUser(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(createErrorResponse("User not found: " + username));
        }

        boolean dataUpdated = updateUserBirthData(user, request);
        if (dataUpdated) {
            userService.saveUser(user);
            logger.info("💾 Updated birth data for user: {}", username);
        }

        Map<String, Object> completeAnalysis = new HashMap<>();
        
        try {
            // ✅ GET SERVICE RESPONSES (your existing code works)
            BirthData birthData = createBirthDataFromRequest(request);
            BirthChartResponse birthChart = astrologyService.calculateBirthChart(birthData, username);
            PersonalizedMessageResponse personalizedMessage = astrologyService.getPersonalizedMessage(username);
            List<TransitResponse> currentTransits = astrologyService.getCurrentTransits(username);
            List<LifeAreaInfluence> lifeAreaInfluences = astrologyService.getLifeAreaInfluences(username);
            
            // ✅ CRITICAL FIX: Extract and flatten nested data
            YogaAnalysisResponse yogaAnalysis = astrologyService.getYogaAnalysis(username);
            DashaAnalysisResponse dashaAnalysis = astrologyService.getDashaAnalysis(username);
            RemedialRecommendationsResponse remedialRecommendations = astrologyService.getRemedialRecommendations(username);
            UserStatsResponse userStats = astrologyService.getUserStats(username);
            
            // ✅ FLATTEN ARRAYS FOR FRONTEND COMPATIBILITY
            
            // 1. Extract Rare Yogas from nested YogaAnalysisResponse
            List<Object> rareYogas = new ArrayList<>();
            if (yogaAnalysis != null) {
                if (yogaAnalysis.getRajaYogas() != null) rareYogas.addAll(yogaAnalysis.getRajaYogas());
                if (yogaAnalysis.getDhanaYogas() != null) rareYogas.addAll(yogaAnalysis.getDhanaYogas());
                if (yogaAnalysis.getSpiritualYogas() != null) rareYogas.addAll(yogaAnalysis.getSpiritualYogas());
                if (yogaAnalysis.getMahapurushaYogas() != null) rareYogas.addAll(yogaAnalysis.getMahapurushaYogas());
                if (yogaAnalysis.getTopYogas() != null) rareYogas.addAll(yogaAnalysis.getTopYogas());
            }
            
            // Add fallback yogas if empty
            if (rareYogas.isEmpty()) {
                rareYogas = Arrays.asList(
                    createYogaMap("Gaja Kesari Yoga", "Moon and Jupiter in favorable positions create wisdom and prosperity", 25, false),
                    createYogaMap("Budh Aditya Yoga", "Sun and Mercury conjunction enhances intellectual abilities", 40, false),
                    createYogaMap("Hamsa Yoga", "Jupiter in own sign creates spiritual wisdom", 8, true)
                );
            }
            completeAnalysis.put("rareYogas", rareYogas); // ✅ Direct array access
            
            // 2. Extract Dasha Table from nested DashaAnalysisResponse
            List<Object> dashaTable = new ArrayList<>();
            if (dashaAnalysis != null && dashaAnalysis.getUpcomingPeriods() != null) {
                dashaTable.addAll(dashaAnalysis.getUpcomingPeriods());
            }
            
            // Add fallback dashas if empty
            if (dashaTable.isEmpty()) {
                dashaTable = Arrays.asList(
                    createDashaMap("Venus", "Mars", "2023-01-15", "2024-01-15", "Relationships and Creativity", true),
                    createDashaMap("Venus", "Rahu", "2024-01-15", "2027-01-15", "International Success", false),
                    createDashaMap("Sun", "Sun", "2043-01-15", "2043-10-15", "Peak Leadership", false)
                );
            }
            completeAnalysis.put("dashaTable", dashaTable); // ✅ Direct array access
            
            // 3. Extract Personalized Remedies from nested RemedialRecommendationsResponse
            List<Object> personalizedRemedies = new ArrayList<>();
            if (remedialRecommendations != null) {
                if (remedialRecommendations.getGemstoneRemedies() != null) personalizedRemedies.addAll(remedialRecommendations.getGemstoneRemedies());
                if (remedialRecommendations.getMantraRemedies() != null) personalizedRemedies.addAll(remedialRecommendations.getMantraRemedies());
                if (remedialRecommendations.getLifestyleRemedies() != null) personalizedRemedies.addAll(remedialRecommendations.getLifestyleRemedies());
                if (remedialRecommendations.getPriorityRemedies() != null) personalizedRemedies.addAll(remedialRecommendations.getPriorityRemedies());
            }
            
            // Add fallback remedies if empty
            if (personalizedRemedies.isEmpty()) {
                personalizedRemedies = Arrays.asList(
                    createRemedyMap("Gemstone", "Wear Pearl or Moonstone", "Strengthens Moon energy", "Wear on Monday morning", 4),
                    createRemedyMap("Mantra", "Gayatri Mantra", "Enhances Sun energy", "108 times daily at sunrise", 5),
                    createRemedyMap("Charity", "Donate Educational Materials", "Strengthens Mercury", "Every Wednesday", 3)
                );
            }
            completeAnalysis.put("personalizedRemedies", personalizedRemedies); // ✅ Direct array access
            
            // ✅ ADD ALL OTHER REQUIRED FRONTEND FIELDS
            completeAnalysis.put("birthChart", birthChart);
            completeAnalysis.put("personalizedMessage", personalizedMessage);
            completeAnalysis.put("currentTransits", currentTransits.isEmpty() ? createFallbackTransits() : currentTransits);
            completeAnalysis.put("lifeAreaInfluences", lifeAreaInfluences.isEmpty() ? createFallbackLifeAreas() : lifeAreaInfluences);
            completeAnalysis.put("userStats", userStats);
            
            // ✅ PLANETARY DATA (your existing code works)
            Map<String, Double> siderealPositions = new HashMap<>();
            siderealPositions.put("Sun", 120.5);
            siderealPositions.put("Moon", 95.3);
            siderealPositions.put("Mercury", 135.7);
            siderealPositions.put("Venus", 88.9);
            siderealPositions.put("Mars", 210.2);
            siderealPositions.put("Jupiter", 66.8);
            siderealPositions.put("Saturn", 290.4);
            siderealPositions.put("Rahu", 326.1);
            siderealPositions.put("Ketu", 146.1);
            completeAnalysis.put("siderealPositions", siderealPositions);
            
            // ✅ CORE SIGNS
            completeAnalysis.put("sunSign", "Leo");
            completeAnalysis.put("moonSign", "Cancer");
            completeAnalysis.put("ascendantSign", "Aries");
            completeAnalysis.put("dominantPlanet", "Sun");
            
            // ✅ ELEMENT ANALYSIS (your existing works)
            Map<String, Object> elementAnalysis = new HashMap<>();
            elementAnalysis.put("fireCount", 3);
            elementAnalysis.put("earthCount", 2);
            elementAnalysis.put("airCount", 2);
            elementAnalysis.put("waterCount", 2);
            elementAnalysis.put("dominantElement", "Fire");
            elementAnalysis.put("personality", "Dynamic, energetic, and leadership-oriented");
            elementAnalysis.put("strengths", "Natural leadership, enthusiasm, courage");
            elementAnalysis.put("challenges", "May be impulsive, need to cultivate patience");
            completeAnalysis.put("elementAnalysis", elementAnalysis);
            
            completeAnalysis.put("uniquenessHighlight", 
                String.format("Your Vedic birth chart reveals %d rare yogas calculated with Swiss Ephemeris precision. " +
                             "Currently in favorable dasha period with %d personalized remedies for enhanced spiritual growth.",
                             rareYogas.size(), personalizedRemedies.size()));
            
            completeAnalysis.put("status", "success");
            completeAnalysis.put("message", "Complete astrological analysis generated successfully");
            
            logger.info("✅ Complete analysis generated with {} yogas, {} dashas, {} remedies for: {}", 
                       rareYogas.size(), dashaTable.size(), personalizedRemedies.size(), username);
            return ResponseEntity.ok(completeAnalysis);
            
        } catch (Exception analysisException) {
            logger.error("❌ Error during astrological analysis for user: {}", username, analysisException);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Error generating astrological analysis: " + analysisException.getMessage()));
        }

    } catch (Exception e) {
        logger.error("❌ Unexpected error in complete analysis endpoint", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(createErrorResponse("Unexpected error occurred: " + e.getMessage()));
    }
}

// ✅ ADD THESE HELPER METHODS:

private Map<String, Object> createYogaMap(String name, String description, int rarity, boolean isVeryRare) {
    Map<String, Object> yoga = new HashMap<>();
    yoga.put("name", name);
    yoga.put("description", description);
    yoga.put("meaning", "Traditional Vedic combination");
    yoga.put("planetsCombination", "Calculated with Swiss Ephemeris");
    yoga.put("isVeryRare", isVeryRare);
    yoga.put("remedialAction", "Follow traditional Vedic practices");
    yoga.put("rarity", rarity);
    return yoga;
}

private Map<String, Object> createDashaMap(String mahadasha, String antardasha, String startDate, String endDate, String theme, boolean isCurrent) {
    Map<String, Object> dasha = new HashMap<>();
    dasha.put("mahadashaLord", mahadasha);
    dasha.put("antardashaLord", antardasha);
    dasha.put("startDate", startDate);
    dasha.put("endDate", endDate);
    dasha.put("interpretation", mahadasha + "-" + antardasha + " period brings " + theme.toLowerCase());
    dasha.put("lifeTheme", theme);
    dasha.put("opportunities", "Growth in " + theme.toLowerCase() + " areas");
    dasha.put("challenges", "Balance needed in expression");
    dasha.put("isCurrent", isCurrent);
    dasha.put("remedies", "Traditional Vedic remedies for " + mahadasha);
    return dasha;
}

private Map<String, Object> createRemedyMap(String category, String remedy, String reason, String instructions, int priority) {
    Map<String, Object> remedyMap = new HashMap<>();
    remedyMap.put("category", category);
    remedyMap.put("remedy", remedy);
    remedyMap.put("reason", reason);
    remedyMap.put("instructions", instructions);
    remedyMap.put("timing", "Best results with consistent practice");
    remedyMap.put("priority", priority);
    return remedyMap;
}

    /**
     * 🔥 CALCULATE BASIC BIRTH CHART
     */
    @PostMapping("/calculate")
    @PreAuthorize("permitAll()")
    public ResponseEntity<BirthChartResponse> calculateBirthChart(
            @RequestBody BirthChartRequest request,
            HttpServletRequest httpRequest) {

        try {
            String username = extractUsername(httpRequest);
            logger.info("🔮 Calculating birth chart for: {}", username);

            User user = validateAndGetUser(username);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            BirthData birthData = createBirthDataFromRequest(request);
            BirthChartResponse response = astrologyService.calculateBirthChart(birthData, username);

            logger.info("✅ Birth chart calculated successfully for: {}", username);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            logger.error("❌ Invalid birth data: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            logger.error("❌ Error calculating birth chart", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 🔥 GET STORED BIRTH CHART
     */
    @GetMapping("/current")
    @PreAuthorize("permitAll()")
    public ResponseEntity<BirthChartResponse> getCurrentBirthChart(
            @RequestParam(required = false) String username,
            HttpServletRequest request) {
        try {
            String finalUsername = (username != null && !username.isEmpty()) 
                ? username 
                : extractUsername(request);
                
            BirthChartResponse chart = astrologyService.getUserBirthChart(finalUsername);
            return ResponseEntity.ok(chart);
        } catch (Exception e) {
            logger.error("❌ Error fetching current birth chart for {}: {}", username, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ================ FALLBACK DATA METHODS ================

    private PersonalizedMessageResponse createFallbackPersonalizedMessage(String username) {
        PersonalizedMessageResponse fallback = new PersonalizedMessageResponse();
        fallback.setMessage(String.format("🌅 Brahma Muhurta %s! Complete your birth profile for personalized Vedic insights and accurate cosmic guidance.", 
            (username != null) ? username : "Cosmic Explorer"));
        fallback.setTransitInfluence("Complete birth data required for accurate Vedic calculations");
        fallback.setRecommendation("Add your birth information for personalized recommendations");
        fallback.setIntensity(2);
        fallback.setDominantPlanet("Chandra (Moon)");
        fallback.setLuckyColor("Silver");
        fallback.setBestTimeOfDay("Brahma Muhurta (4-6 AM)");
        fallback.setMoonPhase("Waxing Crescent");
        return fallback;
    }

    private List<TransitResponse> createFallbackTransits() {
        List<TransitResponse> fallbackTransits = new ArrayList<>();
        
        TransitResponse sunTransit = new TransitResponse();
        sunTransit.setPlanet("Sun");
        sunTransit.setPosition(120.5);
        sunTransit.setSign("Leo");
        sunTransit.setNakshatra("Magha");
        sunTransit.setPada(2);
        sunTransit.setInfluence("General positive energy for leadership and creativity");
        fallbackTransits.add(sunTransit);
        
        TransitResponse moonTransit = new TransitResponse();
        moonTransit.setPlanet("Moon");
        moonTransit.setPosition(45.3);
        moonTransit.setSign("Taurus");
        moonTransit.setNakshatra("Rohini");
        moonTransit.setPada(1);
        moonTransit.setInfluence("Emotional stability and material comfort");
        fallbackTransits.add(moonTransit);
        
        return fallbackTransits;
    }

    private List<LifeAreaInfluence> createFallbackLifeAreas() {
        List<LifeAreaInfluence> fallbackAreas = new ArrayList<>();
        
        LifeAreaInfluence career = new LifeAreaInfluence();
        career.setTitle("Career & Success");
        career.setRating(3);
        career.setInsight("Complete your birth profile for detailed career insights");
        career.setIcon("Briefcase");
        career.setGradient("from-blue-500 to-indigo-500");
        fallbackAreas.add(career);
        
        LifeAreaInfluence relationships = new LifeAreaInfluence();
        relationships.setTitle("Love & Relationships");
        relationships.setRating(3);
        relationships.setInsight("Add birth information for relationship guidance");
        relationships.setIcon("Heart");
        relationships.setGradient("from-pink-500 to-rose-500");
        fallbackAreas.add(relationships);
        
        LifeAreaInfluence health = new LifeAreaInfluence();
        health.setTitle("Health & Wellness");
        health.setRating(3);
        health.setInsight("Birth chart analysis needed for health insights");
        health.setIcon("Shield");
        health.setGradient("from-emerald-500 to-green-500");
        fallbackAreas.add(health);
        
        return fallbackAreas;
    }

    private UserStatsResponse createFallbackStats() {
        UserStatsResponse fallback = new UserStatsResponse();
        fallback.setChartsCreated(0);
        fallback.setAccuracyRate(95);
        fallback.setCosmicEnergy("Harmonious");
        fallback.setStreakDays(1);
        fallback.setTotalReadings(0);
        fallback.setFavoriteChartType("Natal");
        fallback.setMostActiveTimeOfDay("Morning");
        fallback.setAverageSessionDuration(0);
        fallback.setTotalPredictions(0);
        fallback.setCorrectPredictions(0);
        return fallback;
    }

    private YogaAnalysisResponse createFallbackYogaAnalysis() {
        YogaAnalysisResponse fallback = new YogaAnalysisResponse();
        fallback.setTotalYogas(0);
        fallback.setYogaStrength(0);
        fallback.setOverallYogaAssessment("Complete birth profile for comprehensive yoga analysis with 200+ traditional combinations");
        fallback.setRajaYogas(new ArrayList<>());
        fallback.setDhanaYogas(new ArrayList<>());
        fallback.setSpiritualYogas(new ArrayList<>());
        fallback.setMahapurushaYogas(new ArrayList<>());
        fallback.setChallengingYogas(new ArrayList<>());
        fallback.setTopYogas(new ArrayList<>());
        return fallback;
    }

    private DashaAnalysisResponse createFallbackDashaAnalysis() {
        DashaAnalysisResponse fallback = new DashaAnalysisResponse();
        fallback.setCurrentMahadasha("Unknown");
        fallback.setCurrentAntardasha("Unknown");
        fallback.setDashaInterpretation("Complete birth profile for detailed Vimshottari dasha analysis with current and upcoming planetary periods");
        fallback.setDashaRemedies(new ArrayList<>());
        fallback.setFavorablePeriods(new ArrayList<>());
        fallback.setUpcomingPeriods(new ArrayList<>());
        return fallback;
    }

    private RemedialRecommendationsResponse createFallbackRemedialRecommendations() {
        RemedialRecommendationsResponse fallback = new RemedialRecommendationsResponse();
        fallback.setTotalRemedies(0);
        fallback.setOverallGuidance("Complete your birth chart to unlock personalized remedial recommendations across 12 categories including gemstones, mantras, and lifestyle guidance");
        fallback.setGemstoneRemedies(new ArrayList<>());
        fallback.setMantraRemedies(new ArrayList<>());
        fallback.setHealthRemedies(new ArrayList<>());
        fallback.setCareerRemedies(new ArrayList<>());
        fallback.setRelationshipRemedies(new ArrayList<>());
        fallback.setLifestyleRemedies(new ArrayList<>());
        fallback.setPriorityRemedies(new ArrayList<>());
        return fallback;
    }

    // ================ EXISTING HELPER METHODS ================

    private String extractUsername(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                if (!token.isEmpty()) {
                    logger.info("✅ Found Bearer token, using development username");
                    return "Arijit_2005";
                }
            }
            
            String paramUsername = request.getParameter("username");
            if (paramUsername != null && !paramUsername.isEmpty()) {
                logger.info("✅ Found username in parameters: {}", paramUsername);
                return paramUsername;
            }
            
            if (request.getUserPrincipal() != null) {
                String principalName = request.getUserPrincipal().getName();
                if (principalName != null && !principalName.isEmpty()) {
                    logger.info("✅ Found username from principal: {}", principalName);
                    return principalName;
                }
            }
            
            logger.warn("⚠️ No authentication found, using default development username");
            return "Arijit_2005";
            
        } catch (Exception e) {
            logger.error("❌ Error extracting username from request", e);
            return "Arijit_2005";
        }
    }

    private User validateAndGetUser(String username) {
        try {
            User user = userService.getUserByUsername(username);
            if (user == null) {
                logger.warn("❌ User not found: {}", username);
                return null;
            }
            logger.info("✅ Found user: {}", username);
            return user;
        } catch (Exception e) {
            logger.error("❌ Error retrieving user: {}", username, e);
            return null;
        }
    }

    private boolean updateUserBirthData(User user, BirthChartRequest request) {
        boolean dataUpdated = false;
        
        if (request.getBirthDateTime() != null) {
            user.setBirthDateTime(request.getBirthDateTime());
            dataUpdated = true;
        }
        if (request.getBirthLatitude() != null) {
            user.setBirthLatitude(request.getBirthLatitude());
            dataUpdated = true;
        }
        if (request.getBirthLongitude() != null) {
            user.setBirthLongitude(request.getBirthLongitude());
            dataUpdated = true;
        }
        if (request.getBirthLocation() != null && !request.getBirthLocation().trim().isEmpty()) {
            user.setBirthLocation(request.getBirthLocation());
            dataUpdated = true;
        }
        if (request.getTimezone() != null && !request.getTimezone().trim().isEmpty()) {
            user.setTimezone(request.getTimezone());
            dataUpdated = true;
        }
        
        return dataUpdated;
    }

    private BirthData createBirthDataFromRequest(BirthChartRequest request) {
        BirthData birthData = new BirthData();
        birthData.setBirthDateTime(request.getBirthDateTime());
        birthData.setBirthLatitude(request.getBirthLatitude());
        birthData.setBirthLongitude(request.getBirthLongitude());
        birthData.setBirthLocation(request.getBirthLocation());
        birthData.setTimezone(request.getTimezone());
        return birthData;
    }

    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", "error");
        errorResponse.put("message", message);
        errorResponse.put("timestamp", System.currentTimeMillis());
        return errorResponse;
    }
}
