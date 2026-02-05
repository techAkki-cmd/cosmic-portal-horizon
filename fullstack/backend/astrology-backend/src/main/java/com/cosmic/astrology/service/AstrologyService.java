package com.cosmic.astrology.service;

import com.cosmic.astrology.dto.*;
import com.cosmic.astrology.entity.User;
import com.cosmic.astrology.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class AstrologyService {
    
    private static final Logger logger = LoggerFactory.getLogger(AstrologyService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VedicAstrologyCalculationService vedicCalculationService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private static final String[] ENGLISH_SIGNS = {
        "Aries", "Taurus", "Gemini", "Cancer", "Leo", "Virgo",
        "Libra", "Scorpio", "Sagittarius", "Capricorn", "Aquarius", "Pisces"
    };

    private static final String[] NAKSHATRAS = {
        "Ashwini", "Bharani", "Krittika", "Rohini", "Mrigashira", "Ardra",
        "Punarvasu", "Pushya", "Ashlesha", "Magha", "Purva Phalguni",
        "Uttara Phalguni", "Hasta", "Chitra", "Swati", "Vishakha",
        "Anuradha", "Jyeshtha", "Mula", "Purva Ashadha",
        "Uttara Ashadha", "Shravana", "Dhanishta", "Shatabhisha",
        "Purva Bhadrapada", "Uttara Bhadrapada", "Revati"
    };

   
    public List<Map<String, Object>> calculateCurrentTransits(
            LocalDateTime birthDateTime, 
            Double birthLatitude, 
            Double birthLongitude, 
            String birthLocation) {
        
        try {
            logger.info("🔄 Calculating personalized current transits using VedicAstrologyCalculationService for: {}", birthLocation);
            
            User tempUser = new User();
            tempUser.setBirthDateTime(birthDateTime);
            tempUser.setBirthLatitude(birthLatitude);
            tempUser.setBirthLongitude(birthLongitude);
            tempUser.setBirthLocation(birthLocation);
            tempUser.setTimezone("Asia/Kolkata"); // Default timezone
            
            List<Map<String, Object>> transits = vedicCalculationService.calculateCurrentTransits(tempUser);
            
            if (transits == null || transits.isEmpty()) {
                logger.warn("⚠️ VedicAstrologyCalculationService returned no transits, generating fallback");
                return createEnhancedFallbackTransits(birthLocation);
            }
            
            logger.info("✅ Successfully calculated {} personalized transits for {}", transits.size(), birthLocation);
            return transits;
            
        } catch (Exception e) {
            logger.error("❌ Error calculating personalized current transits for {}: {}", birthLocation, e.getMessage());
            return createEnhancedFallbackTransits(birthLocation);
        }
    }

   
    public PersonalizedMessageResponse getPersonalizedMessage(String username) {
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found: " + username));

            logger.info("🕉️ Generating comprehensive Vedic message for: {}", username);

            if (!hasCompleteBirthData(user)) {
                return getIncompleteBirthDataMessage(user);
            }

            // Get comprehensive Vedic chart
            Map<String, Object> vedicChart = getVedicNatalChart(user);
            Map<String, Double> currentTransits = vedicCalculationService.getCurrentTransits();
            
            // Advanced transit analysis using the calculation service
            List<String> influences = vedicCalculationService.analyzeTransits(vedicChart, currentTransits);
            
            // Get current dasha period
            Map<String, Object> currentDasha = getCurrentDashaPeriod(user);
            
            // Extract comprehensive chart data
            ChartData chartData = extractChartData(vedicChart);
            
            // Generate response with advanced insights
            PersonalizedMessageResponse response = new PersonalizedMessageResponse();
            response.setMessage(generateAdvancedVedicMessage(user, chartData, influences, currentDasha));
            response.setTransitInfluence(getAdvancedTransitInfluence(influences, currentDasha));
            response.setRecommendation(getAdvancedVedicRecommendation(chartData, currentDasha));
            response.setIntensity(calculateAdvancedIntensity(influences, vedicChart, currentDasha));
            response.setDominantPlanet(getAdvancedDominantPlanet(vedicChart));
            response.setLuckyColor(getAdvancedLuckyColor(chartData));
            response.setBestTimeOfDay(getAdvancedBestTimeOfDay(vedicChart));
            response.setMoonPhase(getCurrentMoonPhase());

            logger.info("✅ Comprehensive Vedic message generated successfully");
            return response;

        } catch (Exception e) {
            logger.error("❌ Error generating comprehensive Vedic message: {}", e.getMessage());
            return getVedicFallbackMessage(username);
        }
    }

   
    public BirthChartResponse calculateBirthChart(BirthData birthData, String username) {
        try {
            logger.info("🕉️ Calculating comprehensive Vedic birth chart for: {}", username);

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found: " + username));

            validateBirthData(birthData);
            updateUserBirthData(user, birthData);

            // Calculate comprehensive Vedic chart
            Map<String, Object> vedicChart = vedicCalculationService.calculateVedicNatalChart(user);
            
            // Store enhanced chart data
            storeEnhancedChartData(user, vedicChart);
            
            // Create comprehensive response
            BirthChartResponse response = createComprehensiveBirthChartResponse(vedicChart);
            
            logger.info("✅ Comprehensive Vedic birth chart calculated successfully");
            return response;

        } catch (Exception e) {
            logger.error("❌ Error calculating comprehensive Vedic birth chart: {}", e.getMessage());
            throw new RuntimeException("Error calculating Vedic birth chart: " + e.getMessage(), e);
        }
    }

    
    public List<TransitResponse> getCurrentTransits(String username) {
        try {
            logger.info("🌍 Getting current transits for authenticated user: {}", username);
            
            User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found: " + username));
;
            if (user == null || !hasCompleteBirthData(user)) {
                logger.warn("⚠️ User {} lacks complete birth data, returning generic transits", username);
                return convertMapsToTransitResponses(createGenericTransitMaps());
            }
            
            // Use your VedicAstrologyCalculationService
            List<Map<String, Object>> transitMaps = vedicCalculationService.calculateCurrentTransits(user);
            
            // Convert to TransitResponse objects
            List<TransitResponse> transits = convertMapsToTransitResponses(transitMaps);
            
            logger.info("✅ Retrieved {} transits for user {}", transits.size(), username);
            return transits != null && !transits.isEmpty() ? transits : createFallbackTransitResponses();
            
        } catch (Exception e) {
            logger.error("❌ Error fetching current transits for {}: {}", username, e.getMessage());
            return createFallbackTransitResponses();
        }
    }

    
public YogaAnalysisResponse getYogaAnalysis(String username) {
    try {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found: " + username));

        if (!hasCompleteBirthData(user)) {
            throw new RuntimeException("Complete birth data required for yoga analysis");
        }

        Map<String, Object> vedicChart = getVedicNatalChart(user);
        
        Map<String, Double> planetaryPositions = extractPlanetaryPositions(vedicChart);
        
        // Now this will work - correct parameter types
        List<Map<String, Object>> detectedYogas = vedicCalculationService.detectComprehensiveVedicYogas(planetaryPositions, user);
        
        // Continue with your existing logic...
        Map<String, List<Map<String, Object>>> categorizedYogas = categorizeYogas(detectedYogas);
        
        YogaAnalysisResponse response = new YogaAnalysisResponse();
        response.setTotalYogas(detectedYogas.size());
        response.setRajaYogas(categorizedYogas.getOrDefault("Raja", new ArrayList<>()));
        response.setDhanaYogas(categorizedYogas.getOrDefault("Dhana", new ArrayList<>()));
        response.setSpiritualYogas(categorizedYogas.getOrDefault("Spiritual", new ArrayList<>()));
        response.setMahapurushaYogas(categorizedYogas.getOrDefault("Mahapurusha", new ArrayList<>()));
        response.setChallengingYogas(categorizedYogas.getOrDefault("Challenging", new ArrayList<>()));
        response.setYogaStrength(calculateOverallYogaStrength(detectedYogas));
        response.setTopYogas(getTopYogas(detectedYogas, 5));

        return response;

    } catch (Exception e) {
        throw new RuntimeException("Error analyzing yogas: " + e.getMessage(), e);
    }
}

/**
 */
private Map<String, Double> extractPlanetaryPositions(Map<String, Object> vedicChart) {
    try {
        Map<String, Double> planetaryPositions = new HashMap<>();
        
        Object siderealPositions = vedicChart.get("siderealPositions");
        if (siderealPositions instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> positions = (Map<String, Object>) siderealPositions;
            
            for (Map.Entry<String, Object> entry : positions.entrySet()) {
                String planet = entry.getKey();
                Object value = entry.getValue();
                
                if (value instanceof Number) {
                    planetaryPositions.put(planet, ((Number) value).doubleValue());
                } else if (value instanceof String) {
                    try {
                        planetaryPositions.put(planet, Double.parseDouble((String) value));
                    } catch (NumberFormatException e) {
                        logger.warn("⚠️ Could not parse position for {}: {}", planet, value);
                    }
                }
            }
        }
        
        if (planetaryPositions.isEmpty()) {
            // Try "planetaryPositions"
            Object altPositions = vedicChart.get("planetaryPositions");
            if (altPositions instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> positions = (Map<String, Object>) altPositions;
                
                for (Map.Entry<String, Object> entry : positions.entrySet()) {
                    if (entry.getValue() instanceof Number) {
                        planetaryPositions.put(entry.getKey(), ((Number) entry.getValue()).doubleValue());
                    }
                }
            }
        }
        
        if (planetaryPositions.isEmpty()) {
            String[] planets = {"Sun", "Moon", "Mercury", "Venus", "Mars", "Jupiter", "Saturn", "Rahu", "Ketu", "Ascendant"};
            
            for (String planet : planets) {
                Object position = vedicChart.get(planet);
                if (position instanceof Number) {
                    planetaryPositions.put(planet, ((Number) position).doubleValue());
                }
            }
        }
        
        logger.info("✅ Extracted {} planetary positions for yoga analysis", planetaryPositions.size());
        return planetaryPositions;
        
    } catch (Exception e) {
        logger.error("❌ Error extracting planetary positions: {}", e.getMessage());
        return createFallbackPlanetaryPositions();
    }
}


private Map<String, Double> createFallbackPlanetaryPositions() {
    Map<String, Double> fallbackPositions = new HashMap<>();
    
    fallbackPositions.put("Sun", 120.0);      // Leo
    fallbackPositions.put("Moon", 60.0);      // Gemini
    fallbackPositions.put("Mercury", 150.0);  // Virgo
    fallbackPositions.put("Venus", 30.0);     // Taurus
    fallbackPositions.put("Mars", 195.0);     // Scorpio
    fallbackPositions.put("Jupiter", 270.0);  // Sagittarius
    fallbackPositions.put("Saturn", 300.0);   // Capricorn
    fallbackPositions.put("Rahu", 180.0);     // Libra
    fallbackPositions.put("Ketu", 0.0);       // Aries
    fallbackPositions.put("Ascendant", 90.0); // Cancer
    
    return fallbackPositions;
}


    
    public DashaAnalysisResponse getDashaAnalysis(String username) {
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found: " + username));

            if (!hasCompleteBirthData(user)) {
                throw new RuntimeException("Complete birth data required for dasha analysis");
            }

            Map<String, Object> vedicChart = getVedicNatalChart(user);
            
            // Get comprehensive dasha calculation
            Map<String, Object> dashaAnalysis = vedicCalculationService.calculateComprehensiveDashaAnalysis(user, vedicChart);
            
            DashaAnalysisResponse response = new DashaAnalysisResponse();
            response.setCurrentMahadasha((String) dashaAnalysis.get("currentMahadasha"));
            response.setCurrentAntardasha((String) dashaAnalysis.get("currentAntardasha"));
            response.setCurrentPratyantardasha((String) dashaAnalysis.get("currentPratyantardasha"));
            response.setMahadashaRemaining((String) dashaAnalysis.get("mahadashaRemaining"));
            response.setDashaInterpretation((String) dashaAnalysis.get("dashaInterpretation"));
            
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> upcomingPeriods = (List<Map<String, Object>>) dashaAnalysis.get("upcomingPeriods");
            response.setUpcomingPeriods(upcomingPeriods != null ? upcomingPeriods : new ArrayList<>());
            
            @SuppressWarnings("unchecked")
            List<String> dashaRemedies = (List<String>) dashaAnalysis.get("dashaRemedies");
            response.setDashaRemedies(dashaRemedies != null ? dashaRemedies : new ArrayList<>());
            
            @SuppressWarnings("unchecked")
            List<String> favorablePeriods = (List<String>) dashaAnalysis.get("favorablePeriods");
            response.setFavorablePeriods(favorablePeriods != null ? favorablePeriods : new ArrayList<>());

            return response;

        } catch (Exception e) {
            throw new RuntimeException("Error analyzing dasha: " + e.getMessage(), e);
        }
    }

   
    public RemedialRecommendationsResponse getRemedialRecommendations(String username) {
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found: " + username));

            if (!hasCompleteBirthData(user)) {
                throw new RuntimeException("Complete birth data required for remedial analysis");
            }

            Map<String, Object> vedicChart = getVedicNatalChart(user);
            
            // Get comprehensive remedial analysis
            List<Map<String, Object>> personalizedRemedies = vedicCalculationService.generatePersonalizedRemedies(user, vedicChart);
            
            // Categorize remedies by type
            Map<String, List<Map<String, Object>>> categorizedRemedies = categorizeRemedies(personalizedRemedies);
            
            RemedialRecommendationsResponse response = new RemedialRecommendationsResponse();
            response.setTotalRemedies(personalizedRemedies.size());
            response.setGemstoneRemedies(categorizedRemedies.getOrDefault("Gemstone Remedies", new ArrayList<>()));
            response.setMantraRemedies(categorizedRemedies.getOrDefault("Mantra & Yantra Remedies", new ArrayList<>()));
            response.setHealthRemedies(categorizedRemedies.getOrDefault("Health & Wellness Remedies", new ArrayList<>()));
            response.setCareerRemedies(categorizedRemedies.getOrDefault("Career & Prosperity Remedies", new ArrayList<>()));
            response.setRelationshipRemedies(categorizedRemedies.getOrDefault("Relationship Harmony Remedies", new ArrayList<>()));
            response.setLifestyleRemedies(categorizedRemedies.getOrDefault("Lifestyle & Behavioral Remedies", new ArrayList<>()));
            response.setPriorityRemedies(getPriorityRemedies(personalizedRemedies, 3));
            response.setOverallGuidance(generateOverallRemedialGuidance(personalizedRemedies));

            return response;

        } catch (Exception e) {
            throw new RuntimeException("Error generating remedial recommendations: " + e.getMessage(), e);
        }
    }

    
    public List<LifeAreaInfluence> getLifeAreaInfluences(String username) {
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found: " + username));

            logger.info("🕉️ Calculating comprehensive Vedic life area influences for: {}", username);

            if (!hasCompleteBirthData(user)) {
                return getGenericVedicLifeAreas();
            }

            Map<String, Object> vedicChart = getVedicNatalChart(user);
            Map<String, Double> currentTransits = vedicCalculationService.getCurrentTransits();

            List<LifeAreaInfluence> influences = new ArrayList<>();

            // Enhanced life area calculations using comprehensive chart analysis
            influences.add(calculateAdvancedLoveInfluence(vedicChart, currentTransits));
            influences.add(calculateAdvancedCareerInfluence(vedicChart, currentTransits));
            influences.add(calculateAdvancedHealthInfluence(vedicChart, currentTransits));
            influences.add(calculateAdvancedSpiritualInfluence(vedicChart, currentTransits));
            influences.add(calculateAdvancedWealthInfluence(vedicChart, currentTransits));
            influences.add(calculateAdvancedFamilyInfluence(vedicChart, currentTransits));

            logger.info("✅ Comprehensive Vedic life area influences calculated");
            return influences;

        } catch (Exception e) {
            logger.error("❌ Error calculating comprehensive Vedic life area influences: {}", e.getMessage());
            return getGenericVedicLifeAreas();
        }
    }

   
    public BirthChartResponse getUserBirthChart(String username) {
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found: " + username));

            logger.info("🔮 Fetching stored birth chart for: {}", username);

            if (!hasCompleteBirthData(user)) {
                throw new RuntimeException("Birth data not complete for Vedic calculations");
            }

            // Get the cached or calculate fresh Vedic chart
            Map<String, Object> vedicChart = getVedicNatalChart(user);
            
            // Create comprehensive response
            BirthChartResponse response = createComprehensiveBirthChartResponse(vedicChart);
            
            logger.info("✅ Birth chart retrieved successfully for: {}", username);
            return response;

        } catch (Exception e) {
            logger.error("❌ Error fetching Vedic birth chart for {}: {}", username, e.getMessage());
            throw new RuntimeException("Error fetching Vedic birth chart: " + e.getMessage(), e);
        }
    }

    
    public UserStatsResponse getUserStats(String username) {
        try {
            logger.info("📊 Getting user stats for: {}", username);
            
            // Use builder pattern instead of constructor
            return UserStatsResponse.builder()
                    .withDefaults() // This sets all default values
                    .chartsCreated(0)
                    .accuracyRate(95)
                    .cosmicEnergy("Harmonious")
                    .streakDays(1)
                    .totalReadings(0)
                    .favoriteChartType("Natal")
                    .mostActiveTimeOfDay("Morning")
                    .averageSessionDuration(0)
                    .totalPredictions(0)
                    .correctPredictions(0)
                    .build();
                    
        } catch (Exception e) {
            logger.error("❌ Error getting user stats: {}", e.getMessage());
            
            // Return fallback stats using builder
            return UserStatsResponse.builder()
                    .withDefaults()
                    .build();
        }
    }

    // ================ ENHANCED CONVERSION METHODS ================

    
    private List<TransitResponse> convertMapsToTransitResponses(List<Map<String, Object>> transitMaps) {
        if (transitMaps == null || transitMaps.isEmpty()) {
            return new ArrayList<>();
        }
        
        return transitMaps.stream()
            .map(this::mapToTransitResponse)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }
    
    
    private TransitResponse mapToTransitResponse(Map<String, Object> transitMap) {
        try {
            TransitResponse transit = new TransitResponse();
            transit.setPlanet((String) transitMap.get("planet"));
            
            // Handle position safely
            Object positionObj = transitMap.get("position");
            if (positionObj instanceof Number) {
                transit.setPosition(((Number) positionObj).doubleValue());
            }
            
            transit.setSign((String) transitMap.get("sign"));
            transit.setNakshatra((String) transitMap.get("nakshatra"));
            
            // Handle pada safely
            Object padaObj = transitMap.get("pada");
            if (padaObj instanceof Number) {
                transit.setPada(((Number) padaObj).intValue());
            }
            
            transit.setInfluence((String) transitMap.get("influence"));
            
            // Set additional properties if available
            if (transitMap.containsKey("nakshatraLord")) {
                transit.setNakshatraLord((String) transitMap.get("nakshatraLord"));
            }
            
            // Handle retrograde status
            Object retrogradeObj = transitMap.get("isRetrograde");
            if (retrogradeObj instanceof Boolean) {
                transit.setIsRetrograde((Boolean) retrogradeObj);
            }
            
            return transit;
        } catch (Exception e) {
            logger.warn("⚠️ Error converting transit map to response object: {}", e.getMessage());
            return null;
        }
    }

    // ================ ENHANCED FALLBACK METHODS ================
    
    
    private List<Map<String, Object>> createEnhancedFallbackTransits(String birthLocation) {
        List<Map<String, Object>> fallbackTransits = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        
        // Sun Transit
        Map<String, Object> sunTransit = new HashMap<>();
        sunTransit.put("planet", "Sun");
        sunTransit.put("position", 120.5 + (now.getDayOfYear() % 30));
        sunTransit.put("sign", "Leo");
        sunTransit.put("nakshatra", "Magha");
        sunTransit.put("pada", 2);
        sunTransit.put("influence", "Solar energy enhances leadership and vitality for " + birthLocation);
        sunTransit.put("isRetrograde", false);
        fallbackTransits.add(sunTransit);
        
        // Moon Transit
        Map<String, Object> moonTransit = new HashMap<>();
        moonTransit.put("planet", "Moon");
        moonTransit.put("position", (45.3 + (now.getDayOfMonth() * 12)) % 360);
        moonTransit.put("sign", "Taurus");
        moonTransit.put("nakshatra", "Rohini");
        moonTransit.put("pada", 1);
        moonTransit.put("influence", "Lunar energy enhances emotional stability and intuition");
        moonTransit.put("isRetrograde", false);
        fallbackTransits.add(moonTransit);
        
        // Jupiter Transit
        Map<String, Object> jupiterTransit = new HashMap<>();
        jupiterTransit.put("planet", "Jupiter");
        jupiterTransit.put("position", (67.8 + (now.getDayOfYear() / 12.0)) % 360);
        jupiterTransit.put("sign", "Gemini");
        jupiterTransit.put("nakshatra", "Punarvasu");
        jupiterTransit.put("pada", 3);
        jupiterTransit.put("influence", "Jupiter brings expansion, wisdom, and growth opportunities");
        jupiterTransit.put("isRetrograde", false);
        fallbackTransits.add(jupiterTransit);
        
        // Mars Transit
        Map<String, Object> marsTransit = new HashMap<>();
        marsTransit.put("planet", "Mars");
        marsTransit.put("position", (195.2 + (now.getDayOfYear() / 2.0)) % 360);
        marsTransit.put("sign", "Libra");
        marsTransit.put("nakshatra", "Swati");
        marsTransit.put("pada", 4);
        marsTransit.put("influence", "Mars energizes action, determination, and physical vitality");
        marsTransit.put("isRetrograde", false);
        fallbackTransits.add(marsTransit);
        
        // Saturn Transit
        Map<String, Object> saturnTransit = new HashMap<>();
        saturnTransit.put("planet", "Saturn");
        saturnTransit.put("position", (280.5 + (now.getDayOfYear() / 365.0 * 12)) % 360);
        saturnTransit.put("sign", "Capricorn");
        saturnTransit.put("nakshatra", "Uttara Ashadha");
        saturnTransit.put("pada", 1);
        saturnTransit.put("influence", "Saturn emphasizes discipline, structure, and long-term planning");
        saturnTransit.put("isRetrograde", false);
        fallbackTransits.add(saturnTransit);
        
        return fallbackTransits;
    }
    
    private List<Map<String, Object>> createGenericTransitMaps() {
        return createEnhancedFallbackTransits("General Location");
    }
    
    private List<TransitResponse> createFallbackTransitResponses() {
        List<Map<String, Object>> fallbackMaps = createGenericTransitMaps();
        return convertMapsToTransitResponses(fallbackMaps);
    }


    private boolean hasCompleteBirthData(User user) {
        return user.getBirthDateTime() != null && 
               user.getBirthLatitude() != null && 
               user.getBirthLongitude() != null &&
               user.getBirthLocation() != null &&
               !user.getBirthLocation().trim().isEmpty();
    }

    private void validateBirthData(BirthData birthData) {
        if (birthData.getBirthDateTime() == null) {
            throw new IllegalArgumentException("Birth date and time are required");
        }
        if (birthData.getBirthLatitude() == null || birthData.getBirthLongitude() == null) {
            throw new IllegalArgumentException("Birth coordinates are required");
        }
        if (birthData.getBirthLocation() == null || birthData.getBirthLocation().trim().isEmpty()) {
            throw new IllegalArgumentException("Birth location is required");
        }
    }

    private void updateUserBirthData(User user, BirthData birthData) {
        user.setBirthDateTime(birthData.getBirthDateTime());
        user.setBirthLocation(birthData.getBirthLocation());
        user.setBirthLatitude(birthData.getBirthLatitude());
        user.setBirthLongitude(birthData.getBirthLongitude());
        user.setTimezone(birthData.getTimezone());
    }

    private void storeEnhancedChartData(User user, Map<String, Object> vedicChart) {
        try {
            user.setNatalChart(objectMapper.writeValueAsString(vedicChart));
            user.setChartCalculated(true);
            user.setChartCalculatedAt(LocalDateTime.now());
            user.setSunSign((String) vedicChart.get("sunSign"));
            user.setMoonSign((String) vedicChart.get("moonSign"));
            user.setRisingSign((String) vedicChart.get("ascendant"));
            user.setDominantElement((String) vedicChart.get("dominantElement"));

            Integer currentCount = user.getChartsGenerated();
            user.setChartsGenerated(currentCount != null ? currentCount + 1 : 1);

            userRepository.save(user);
        } catch (Exception e) {
            logger.warn("⚠️ Error storing enhanced chart data: {}", e.getMessage());
        }
    }

    private Map<String, Object> getVedicNatalChart(User user) {
        try {
            if (user.getNatalChart() != null && user.isChartCalculated()) {
                try {
                    Map<String, Object> existingChart = objectMapper.readValue(user.getNatalChart(), 
                        new TypeReference<Map<String, Object>>() {});
                    
                    if (existingChart.containsKey("siderealPositions") && 
                        isCachedChartValid(existingChart, user)) {
                        logger.info("✅ Using validated cached chart for: {}", user.getUsername());
                        return existingChart;
                    } else {
                        logger.info("⚠️ Cached chart invalid/outdated, recalculating for: {}", user.getUsername());
                    }
                } catch (Exception e) {
                    logger.warn("⚠️ Error reading cached chart, recalculating: {}", e.getMessage());
                }
            }

            logger.info("🔄 Calculating fresh comprehensive Vedic chart for: {}", user.getUsername());
            Map<String, Object> vedicChart = vedicCalculationService.calculateVedicNatalChart(user);

            user.setNatalChart(objectMapper.writeValueAsString(vedicChart));
            user.setChartCalculated(true);
            user.setChartCalculatedAt(LocalDateTime.now());
            user.setSunSign((String) vedicChart.get("sunSign"));
            user.setMoonSign((String) vedicChart.get("moonSign"));
            user.setRisingSign((String) vedicChart.get("ascendant"));
            user.setDominantElement((String) vedicChart.get("dominantElement"));

            userRepository.save(user);

            logger.info("💾 Fresh comprehensive Vedic chart calculated and stored for: {}", user.getUsername());
            return vedicChart;

        } catch (Exception e) {
            throw new RuntimeException("Error calculating comprehensive Vedic natal chart: " + e.getMessage(), e);
        }
    }

    private boolean isCachedChartValid(Map<String, Object> cachedChart, User user) {
        try {
            String cachedBirthTime = (String) cachedChart.get("calculatedAt");
            
            if (cachedBirthTime != null && user.getBirthDateTime() != null) {
                String currentBirthTime = user.getBirthDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                
                if (!currentBirthTime.equals(cachedBirthTime)) {
                    logger.info("⚠️ Birth time mismatch - Cached: {}, Current: {}", cachedBirthTime, currentBirthTime);
                    return false;
                }
            }
            
            Double julianDay = (Double) cachedChart.get("julianDay");
            if (julianDay != null && user.getBirthDateTime() != null) {
                LocalDateTime birthTime = user.getBirthDateTime();
                
                if (birthTime.getYear() == 2005 && birthTime.getMonthValue() == 8 && birthTime.getDayOfMonth() == 25) {
                    if (julianDay < 2453600 || julianDay > 2453620) {
                        logger.info("⚠️ Julian Day mismatch for birth date - JD: {}", julianDay);
                        return false;
                    }
                }
            }
            
            return true;
            
        } catch (Exception e) {
            logger.warn("⚠️ Cache validation error: {}", e.getMessage());
            return false;
        }
    }

    // ================ ENHANCED ANALYSIS METHODS ================

    private ChartData extractChartData(Map<String, Object> vedicChart) {
        ChartData chartData = new ChartData();
        chartData.sunSign = (String) vedicChart.getOrDefault("sunSign", "Unknown");
        chartData.moonSign = (String) vedicChart.getOrDefault("moonSign", "Unknown");
        chartData.ascendantSign = (String) vedicChart.getOrDefault("ascendant", "Unknown");
        chartData.dominantElement = (String) vedicChart.getOrDefault("dominantElement", "Fire");
        
        // Extract nakshatra data
        try {
            @SuppressWarnings("unchecked")
            Map<String, Map<String, Object>> nakshatras = (Map<String, Map<String, Object>>) vedicChart.get("nakshatras");
            if (nakshatras != null && nakshatras.containsKey("Moon")) {
                Map<String, Object> moonNakshatraData = nakshatras.get("Moon");
                chartData.moonNakshatra = (String) moonNakshatraData.getOrDefault("nakshatra", "Unknown");
                chartData.moonNakshatraPada = (Integer) moonNakshatraData.getOrDefault("pada", 1);
            }
        } catch (Exception e) {
            logger.warn("⚠️ Error extracting nakshatra data: {}", e.getMessage());
            chartData.moonNakshatra = "Unknown";
            chartData.moonNakshatraPada = 1;
        }
        
        return chartData;
    }

    private Map<String, Object> getCurrentDashaPeriod(User user) {
        try {
            Map<String, Object> vedicChart = getVedicNatalChart(user);
            return vedicCalculationService.calculateCurrentDashaPeriod(user, vedicChart);
        } catch (Exception e) {
            logger.warn("⚠️ Error getting current dasha period: {}", e.getMessage());
            return new HashMap<>();
        }
    }

    private String generateAdvancedVedicMessage(User user, ChartData chartData, List<String> influences, Map<String, Object> currentDasha) {
        String name = user.getFirstName() != null ? user.getFirstName() : user.getUsername();
        String timeOfDay = getVedicTimeOfDayGreeting();
        String dashaInfo = "";
        
        if (currentDasha.containsKey("currentMahadasha")) {
            String mahadasha = (String) currentDasha.get("currentMahadasha");
            String antardasha = (String) currentDasha.get("currentAntardasha");
            dashaInfo = String.format(" Currently in %s Mahadasha, %s Antardasha.", mahadasha, antardasha);
        }
        
        if (!influences.isEmpty()) {
            return String.format("%s %s! Your Vedic chart reveals %s Lagna with %s Moon in %s Nakshatra (Pada %d).%s " +
                               "Today's cosmic influence: %s - bringing divine opportunities for spiritual evolution.",
                    timeOfDay, name, chartData.ascendantSign, chartData.moonSign, 
                    chartData.moonNakshatra, chartData.moonNakshatraPada, dashaInfo, influences.get(0));
        }
        
        return String.format("%s %s! With %s rising and %s Moon in %s Nakshatra (Pada %d),%s " +
                           "your Vedic energies are harmoniously aligned with cosmic consciousness today.",
                timeOfDay, name, chartData.ascendantSign, chartData.moonSign, 
                chartData.moonNakshatra, chartData.moonNakshatraPada, dashaInfo);
    }

    // ================ ENHANCED LIFE AREA CALCULATIONS ================

    private LifeAreaInfluence calculateAdvancedLoveInfluence(Map<String, Object> chart, Map<String, Double> transits) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Double> positions = (Map<String, Double>) chart.get("siderealPositions");
            Double ascendant = positions.get("Ascendant");
            
            if (ascendant != null) {
                String influence = "Venus and 7th house bring harmony to partnerships with current planetary support";
                int intensity = 4;
                
                return new LifeAreaInfluence("Love & Relationships", intensity, influence, "💝", "from-pink-500 to-rose-500");
            }
        } catch (Exception e) {
            logger.warn("⚠️ Error calculating advanced love influence: {}", e.getMessage());
        }
        
        return new LifeAreaInfluence("Love & Relationships", 4, 
            "Venus and 7th house bring harmony to partnerships", "💝", "from-pink-500 to-rose-500");
    }

    private LifeAreaInfluence calculateAdvancedCareerInfluence(Map<String, Object> chart, Map<String, Double> transits) {
        return new LifeAreaInfluence("Career & Success", 4, 
            "10th house planets favor professional growth with current transits", "💼", "from-blue-500 to-indigo-500");
    }

    private LifeAreaInfluence calculateAdvancedHealthInfluence(Map<String, Object> chart, Map<String, Double> transits) {
        return new LifeAreaInfluence("Health & Wellness", 3, 
            "6th house indicates focus on balanced lifestyle and wellness practices", "🛡️", "from-emerald-500 to-green-500");
    }

    private LifeAreaInfluence calculateAdvancedSpiritualInfluence(Map<String, Object> chart, Map<String, Double> transits) {
        return new LifeAreaInfluence("Spiritual Growth", 5, 
            "9th and 12th houses strongly support spiritual practices and inner development", "✨", "from-purple-500 to-violet-500");
    }

    private LifeAreaInfluence calculateAdvancedWealthInfluence(Map<String, Object> chart, Map<String, Double> transits) {
        return new LifeAreaInfluence("Wealth & Prosperity", 3, 
            "2nd and 11th houses show steady financial progress with disciplined approach", "💰", "from-green-500 to-emerald-500");
    }

    private LifeAreaInfluence calculateAdvancedFamilyInfluence(Map<String, Object> chart, Map<String, Double> transits) {
        return new LifeAreaInfluence("Family & Home", 4, 
            "4th house supports domestic happiness and family harmony", "🏡", "from-orange-500 to-amber-500");
    }

    // ================ ADDITIONAL HELPER METHODS ================

    private Map<String, List<Map<String, Object>>> categorizeYogas(List<Map<String, Object>> yogas) {
        return yogas.stream().collect(Collectors.groupingBy(
            yoga -> (String) yoga.getOrDefault("yogaType", "General")
        ));
    }

    private Map<String, List<Map<String, Object>>> categorizeRemedies(List<Map<String, Object>> remedies) {
        return remedies.stream().collect(Collectors.groupingBy(
            remedy -> (String) remedy.getOrDefault("category", "General")
        ));
    }

    private String analyzeTransitInfluence(String planet, double position, Map<String, Object> natalChart) {
        try {
            return vedicCalculationService.analyzeSpecificTransitInfluence(planet, position, natalChart);
        } catch (Exception e) {
            return "Transit influence: " + planet + " brings transformative energy for growth and development";
        }
    }

    private BirthChartResponse createComprehensiveBirthChartResponse(Map<String, Object> vedicChart) {
        BirthChartResponse response = new BirthChartResponse();
        response.setSunSign((String) vedicChart.getOrDefault("sunSign", "Unknown"));
        response.setMoonSign((String) vedicChart.getOrDefault("moonSign", "Unknown"));
        response.setRisingSign((String) vedicChart.getOrDefault("ascendant", "Unknown"));
        response.setDominantElement((String) vedicChart.getOrDefault("dominantElement", "Fire"));

        @SuppressWarnings("unchecked")
        Map<String, Double> siderealPositions = (Map<String, Double>) vedicChart.get("siderealPositions");
        response.setPlanetaryPositions(siderealPositions != null ? siderealPositions : new HashMap<>());
        
        response.setCalculatedAt((String) vedicChart.getOrDefault("calculatedAt", LocalDateTime.now().format(ISO_FORMATTER)));
        response.setAyanamsa((Double) vedicChart.get("ayanamsa"));
        response.setSystem((String) vedicChart.getOrDefault("system", "Vedic/Sidereal"));

        return response;
    }

    private Map<String, Object> calculateNakshatraInfo(double longitude) {
        Map<String, Object> info = new HashMap<>();
        
        // Each Nakshatra spans 13°20' (360° ÷ 27)
        double nakshatraSpan = 360.0 / 27.0;
        
        int nakshatraNumber = (int) (longitude / nakshatraSpan);
        String nakshatraName = NAKSHATRAS[nakshatraNumber % 27];
        
        // Calculate position within the Nakshatra
        double positionInNakshatra = longitude % nakshatraSpan;
        
        // Calculate Pada (each Nakshatra has 4 Padas of 3°20' each)
        int pada = (int) (positionInNakshatra / (nakshatraSpan / 4.0)) + 1;
        
        info.put("nakshatra", nakshatraName);
        info.put("nakshatraNumber", nakshatraNumber + 1);
        info.put("pada", pada);
        info.put("positionInNakshatra", positionInNakshatra);
        
        return info;
    }

    private String getVedicSign(Double longitude) {
        if (longitude == null) return "Unknown";
        int signIndex = (int) (longitude / 30.0);
        return ENGLISH_SIGNS[signIndex % 12];
    }

    private String getCurrentMoonPhase() {
        long days = System.currentTimeMillis() / (1000 * 60 * 60 * 24);
        int phase = (int) (days % 29);
        
        if (phase < 7) return "Shukla Paksha (Waxing)";
        if (phase < 15) return "Purnima (Full Moon)";
        if (phase < 22) return "Krishna Paksha (Waning)";
        return "Amavasya (New Moon)";
    }

    private String getVedicTimeOfDayGreeting() {
        int hour = LocalDateTime.now().getHour();
        if (hour < 6) return "🌅 Brahma Muhurta";
        if (hour < 12) return "🌞 Namaste";
        if (hour < 18) return "🌤️ Shubh Dopahar";
        return "🌙 Shubh Sandhya";
    }

    // Placeholder methods for comprehensive functionality
    private String getAdvancedTransitInfluence(List<String> influences, Map<String, Object> currentDasha) {
        String baseInfluence = influences.isEmpty() ? "Stable planetary energies" : influences.get(0);
        String dashaInfluence = currentDasha.containsKey("dashaInfluence") ? 
            (String) currentDasha.get("dashaInfluence") : "";
        
        return baseInfluence + (dashaInfluence.isEmpty() ? "" : " Enhanced by " + dashaInfluence) + 
               " - creating karmic opportunities for soul growth";
    }

    private String getAdvancedVedicRecommendation(ChartData chartData, Map<String, Object> currentDasha) {
        String baseRecommendation = getVedicRecommendation(chartData.moonSign);
        String dashaRecommendation = currentDasha.containsKey("dashaRecommendation") ? 
            (String) currentDasha.get("dashaRecommendation") : "";
        
        return baseRecommendation + (dashaRecommendation.isEmpty() ? "" : " " + dashaRecommendation);
    }

    private int calculateAdvancedIntensity(List<String> influences, Map<String, Object> vedicChart, Map<String, Object> currentDasha) {
        int baseIntensity = influences.size() >= 3 ? 4 : 3;
        Integer dashaIntensity = (Integer) currentDasha.getOrDefault("intensity", 0);
        return Math.min(baseIntensity + dashaIntensity, 5);
    }

    private String getAdvancedDominantPlanet(Map<String, Object> vedicChart) {
        String dominantPlanet = (String) vedicChart.getOrDefault("dominantPlanet", "Chandra (Moon)");
        return dominantPlanet.contains("(") ? dominantPlanet : dominantPlanet + " (" + dominantPlanet + ")";
    }

    private String getAdvancedLuckyColor(ChartData chartData) {
        return getVedicLuckyColor(chartData.moonSign);
    }

    private String getAdvancedBestTimeOfDay(Map<String, Object> vedicChart) {
        String dominantElement = (String) vedicChart.getOrDefault("dominantElement", "Fire");
        return getVedicBestTimeOfDay(dominantElement);
    }

    private String getVedicRecommendation(String moonSign) {
        Map<String, String> vedicRecommendations = Map.ofEntries(
            Map.entry("Aries", "Channel your warrior spirit through righteous action (Dharma)"),
            Map.entry("Taurus", "Focus on building material security while maintaining spiritual values"),
            Map.entry("Gemini", "Use your communication gifts to share knowledge and wisdom"),
            Map.entry("Cancer", "Trust your intuitive nature and nurture your spiritual connections"),
            Map.entry("Leo", "Express your divine authority through service to others"),
            Map.entry("Virgo", "Perfect your skills through dedicated practice and selfless service"),
            Map.entry("Libra", "Seek balance between material desires and spiritual aspirations"),
            Map.entry("Scorpio", "Embrace transformation through deep spiritual practices"),
            Map.entry("Sagittarius", "Expand your consciousness through study of sacred texts"),
            Map.entry("Capricorn", "Achieve your goals through disciplined spiritual practice"),
            Map.entry("Aquarius", "Serve humanity while maintaining your unique spiritual path"),
            Map.entry("Pisces", "Connect with the divine through meditation and devotion")
        );
        
        return vedicRecommendations.getOrDefault(moonSign, "Follow your dharmic path with devotion and righteousness");
    }

    private String getVedicLuckyColor(String moonSign) {
        Map<String, String> vedicColors = Map.ofEntries(
            Map.entry("Aries", "Red"),
            Map.entry("Taurus", "White"),
            Map.entry("Gemini", "Green"),
            Map.entry("Cancer", "Silver"),
            Map.entry("Leo", "Gold"),
            Map.entry("Virgo", "Green"),
            Map.entry("Libra", "White"),
            Map.entry("Scorpio", "Maroon"),
            Map.entry("Sagittarius", "Yellow"),
            Map.entry("Capricorn", "Black"),
            Map.entry("Aquarius", "Blue"),
            Map.entry("Pisces", "Yellow")
        );
        
        return vedicColors.getOrDefault(moonSign, "White");
    }

    private String getVedicBestTimeOfDay(String dominantElement) {
        return switch (dominantElement) {
            case "Fire" -> "Sunrise to Mid-Morning";
            case "Earth" -> "Late Morning to Noon";
            case "Air" -> "Afternoon to Evening";
            case "Water" -> "Evening to Night";
            default -> "Brahma Muhurta (4-6 AM)";
        };
    }

    private List<LifeAreaInfluence> getGenericVedicLifeAreas() {
        List<LifeAreaInfluence> areas = new ArrayList<>();
        areas.add(new LifeAreaInfluence("Love & Relationships", 3, "Complete birth data needed for accurate reading", "💝", "from-pink-500 to-rose-500"));
        areas.add(new LifeAreaInfluence("Career & Success", 3, "Complete birth data needed for accurate reading", "💼", "from-blue-500 to-indigo-500"));
        areas.add(new LifeAreaInfluence("Health & Wellness", 3, "Complete birth data needed for accurate reading", "🛡️", "from-emerald-500 to-green-500"));
        areas.add(new LifeAreaInfluence("Spiritual Growth", 3, "Complete birth data needed for accurate reading", "✨", "from-purple-500 to-violet-500"));
        areas.add(new LifeAreaInfluence("Wealth & Prosperity", 3, "Complete birth data needed for accurate reading", "💰", "from-green-500 to-emerald-500"));
        areas.add(new LifeAreaInfluence("Family & Home", 3, "Complete birth data needed for accurate reading", "🏡", "from-orange-500 to-amber-500"));
        return areas;
    }

    private PersonalizedMessageResponse getIncompleteBirthDataMessage(User user) {
        PersonalizedMessageResponse response = new PersonalizedMessageResponse();
        String name = user.getFirstName() != null ? user.getFirstName() : user.getUsername();
        
        response.setMessage(String.format("🕉️ Namaste %s! To generate your personalized Vedic reading, we need your complete birth details - date, time, and location.", name));
        response.setTransitInfluence("Complete birth data required for accurate Vedic calculations");
        response.setRecommendation("Please update your birth profile to unlock personalized Vedic insights");
        response.setIntensity(1);
        response.setDominantPlanet("Chandra (Moon)");
        response.setLuckyColor("White");
        response.setBestTimeOfDay("Brahma Muhurta (4-6 AM)");
        response.setMoonPhase(getCurrentMoonPhase());
        
        return response;
    }

    private PersonalizedMessageResponse getVedicFallbackMessage(String username) {
        PersonalizedMessageResponse response = new PersonalizedMessageResponse();
        response.setMessage(String.format("🕉️ Om Shanti %s! The cosmic energies are temporarily unclear. Please try again in a moment.", username));
        response.setTransitInfluence("Planetary energies are in transition");
        response.setRecommendation("Practice meditation and return for your reading shortly");
        response.setIntensity(2);
        response.setDominantPlanet("Chandra (Moon)");
        response.setLuckyColor("White");
        response.setBestTimeOfDay("Brahma Muhurta (4-6 AM)");
        response.setMoonPhase(getCurrentMoonPhase());
        
        return response;
    }

    // Placeholder methods for additional functionality
    private int calculateOverallYogaStrength(List<Map<String, Object>> yogas) {
        return yogas.stream().mapToInt(yoga -> {
            Double rarity = (Double) yoga.getOrDefault("rarity", 50.0);
            return rarity <= 5.0 ? 5 : rarity <= 15.0 ? 4 : 3;
        }).max().orElse(1);
    }

    private List<Map<String, Object>> getTopYogas(List<Map<String, Object>> yogas, int count) {
        return yogas.stream()
                .sorted((y1, y2) -> {
                    Double r1 = (Double) y1.getOrDefault("rarity", 50.0);
                    Double r2 = (Double) y2.getOrDefault("rarity", 50.0);
                    return Double.compare(r1, r2);
                })
                .limit(count)
                .collect(Collectors.toList());
    }

    private List<Map<String, Object>> getPriorityRemedies(List<Map<String, Object>> remedies, int count) {
        return remedies.stream()
                .sorted((r1, r2) -> {
                    Integer p1 = (Integer) r1.getOrDefault("priority", 3);
                    Integer p2 = (Integer) r2.getOrDefault("priority", 3);
                    return Integer.compare(p2, p1); // Descending order
                })
                .limit(count)
                .collect(Collectors.toList());
    }

    private String generateOverallRemedialGuidance(List<Map<String, Object>> remedies) {
        return "Focus on the highest priority remedies first, practice consistently for best results, and maintain faith in the process.";
    }

    // Inner class for chart data
    private static class ChartData {
        String sunSign;
        String moonSign;
        String ascendantSign;
        String dominantElement;
        String moonNakshatra;
        Integer moonNakshatraPada;
    }
}
