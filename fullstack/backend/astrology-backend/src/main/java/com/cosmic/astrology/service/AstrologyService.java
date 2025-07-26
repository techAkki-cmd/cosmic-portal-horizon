package com.cosmic.astrology.service;

import com.cosmic.astrology.dto.*;
import com.cosmic.astrology.entity.User;
import com.cosmic.astrology.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AstrologyService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AstrologyCalculationService calculationService;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Get personalized message based on user's birth chart and current transits
     */
    public PersonalizedMessageResponse getPersonalizedMessage(String username) {
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            // Check if birth data is complete
            if (!hasCompleteBirthData(user)) {
                return getIncompleteBirthDataMessage(user);
            }
            
            // Calculate or retrieve natal chart
            Map<String, Object> natalChart = getNatalChart(user);
            
            // Get current transits
            Map<String, Double> currentTransits = calculationService.getCurrentTransits();
            
            // Analyze transits
            List<String> influences = calculationService.analyzeTransits(natalChart, currentTransits);
            
            // Generate personalized content
            String sunSign = (String) natalChart.get("sunSign");
            String moonSign = (String) natalChart.get("moonSign");
            String dominantElement = (String) natalChart.get("dominantElement");
            
            PersonalizedMessageResponse response = new PersonalizedMessageResponse();
            response.setMessage(generatePersonalizedMessage(user, sunSign, influences));
            response.setTransitInfluence(getTransitInfluence(influences));
            response.setRecommendation(getPersonalizedRecommendation(sunSign, dominantElement));
            response.setIntensity(calculateIntensity(influences));
            response.setDominantPlanet(getDominantPlanet(influences));
            response.setLuckyColor(getLuckyColor(sunSign));
            response.setBestTimeOfDay(getBestTimeOfDay(dominantElement));
            response.setMoonPhase(getCurrentMoonPhase());
            
            return response;
            
        } catch (Exception e) {
            System.err.println("Error generating personalized message: " + e.getMessage());
            return getFallbackMessage(username);
        }
    }
    
    /**
     * Calculate birth chart from provided data
     */
    public BirthChartResponse calculateBirthChart(BirthData birthData, String username) {
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            // Update user with new birth data
            user.setBirthDateTime(birthData.getBirthDateTime());
            user.setBirthLocation(birthData.getBirthLocation());
            user.setBirthLatitude(birthData.getBirthLatitude());
            user.setBirthLongitude(birthData.getBirthLongitude());
            user.setTimezone(birthData.getTimezone());
            
            // Clear cached chart so it gets recalculated
            user.setChartCalculated(false);
            user.setNatalChart(null);
            
            userRepository.save(user);
            
            // Calculate new chart
            Map<String, Object> natalChart = getNatalChart(user);
            
            // Convert to response format
            BirthChartResponse response = new BirthChartResponse();
            response.setSunSign((String) natalChart.get("sunSign"));
            response.setMoonSign((String) natalChart.get("moonSign"));
            response.setRisingSign((String) natalChart.get("risingSign"));
            response.setDominantElement((String) natalChart.get("dominantElement"));
            
            @SuppressWarnings("unchecked")
            Map<String, Double> positions = (Map<String, Double>) natalChart.get("planetaryPositions");
            response.setPlanetaryPositions(positions);
            
            return response;
            
        } catch (Exception e) {
            throw new RuntimeException("Error calculating birth chart: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get user's stored birth chart
     */
    public BirthChartResponse getUserBirthChart(String username) {
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            if (!hasCompleteBirthData(user)) {
                throw new RuntimeException("Birth data not complete");
            }
            
            Map<String, Object> natalChart = getNatalChart(user);
            
            // Convert to BirthChartResponse format
            BirthChartResponse response = new BirthChartResponse();
            response.setSunSign((String) natalChart.get("sunSign"));
            response.setMoonSign((String) natalChart.get("moonSign"));
            response.setRisingSign((String) natalChart.get("risingSign"));
            response.setDominantElement((String) natalChart.get("dominantElement"));
            
            @SuppressWarnings("unchecked")
            Map<String, Double> positions = (Map<String, Double>) natalChart.get("planetaryPositions");
            response.setPlanetaryPositions(positions);
            
            return response;
            
        } catch (Exception e) {
            throw new RuntimeException("Error fetching birth chart: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get current transits for user
     */
    public List<TransitResponse> getCurrentTransits(String username) {
        try {
            Map<String, Double> transits = calculationService.getCurrentTransits();
            List<TransitResponse> transitList = new ArrayList<>();
            
            for (Map.Entry<String, Double> entry : transits.entrySet()) {
                TransitResponse transit = new TransitResponse();
                transit.setPlanet(entry.getKey());
                transit.setPosition(entry.getValue());
                transit.setSign(getZodiacSign(entry.getValue()));
                transitList.add(transit);
            }
            
            return transitList;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching transits: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get current planetary positions
     */
    public Map<String, Double> getCurrentPlanetaryPositions() {
        return calculationService.getCurrentTransits();
    }
    
    /**
     * Get personalized life area influences
     */
    public List<LifeAreaInfluence> getLifeAreaInfluences(String username) {
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            if (!hasCompleteBirthData(user)) {
                return getGenericLifeAreas();
            }
            
            Map<String, Object> natalChart = getNatalChart(user);
            Map<String, Double> currentTransits = calculationService.getCurrentTransits();
            
            List<LifeAreaInfluence> influences = new ArrayList<>();
            
            // Calculate personalized ratings based on transits and natal positions
            influences.add(calculateLoveInfluence(natalChart, currentTransits));
            influences.add(calculateCareerInfluence(natalChart, currentTransits));
            influences.add(calculateHealthInfluence(natalChart, currentTransits));
            influences.add(calculateSpiritualInfluence(natalChart, currentTransits));
            
            return influences;
            
        } catch (Exception e) {
            System.err.println("Error calculating life area influences: " + e.getMessage());
            return getGenericLifeAreas();
        }
    }
    
    /**
     * Get user statistics
     */
    public UserStatsResponse getUserStats(String username) {
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            // Calculate accuracy rate based on user's dominant element and current energy
            int accuracyRate = calculateAccuracyRate(user);
            String cosmicEnergy = calculateCosmicEnergy(user);
            
            return new UserStatsResponse(
                user.getChartsGenerated() != null ? user.getChartsGenerated() : 0,
                accuracyRate,
                cosmicEnergy,
                user.getLoginStreak() != null ? user.getLoginStreak() : 0
            );
            
        } catch (Exception e) {
            System.err.println("Error fetching user stats: " + e.getMessage());
            return new UserStatsResponse(0, 0, "Low", 0);
        }
    }
    
    // ================ HELPER METHODS ================
    
    private boolean hasCompleteBirthData(User user) {
        return user.getBirthDateTime() != null && 
               user.getBirthLatitude() != null && 
               user.getBirthLongitude() != null;
    }
    
    private Map<String, Object> getNatalChart(User user) {
        try {
            if (user.getNatalChart() != null && user.isChartCalculated()) {
                return objectMapper.readValue(user.getNatalChart(), Map.class);
            }
            
            // Calculate new chart using the calculation service
            Map<String, Object> natalChart = buildNatalChartForUser(user);
            
            // Store in user record
            user.setNatalChart(objectMapper.writeValueAsString(natalChart));
            user.setChartCalculated(true);
            user.setChartCalculatedAt(LocalDateTime.now());
            user.setSunSign((String) natalChart.get("sunSign"));
            user.setMoonSign((String) natalChart.get("moonSign"));
            user.setRisingSign((String) natalChart.get("risingSign"));
            user.setDominantElement((String) natalChart.get("dominantElement"));
            
            userRepository.save(user);
            
            return natalChart;
            
        } catch (Exception e) {
            throw new RuntimeException("Error calculating natal chart: " + e.getMessage(), e);
        }
    }
    
    /**
     * Build natal chart for user (since AstrologyCalculationService.calculateNatalChart doesn't exist)
     */
    private Map<String, Object> buildNatalChartForUser(User user) {
        if (user.getBirthDateTime() == null || user.getBirthLatitude() == null || user.getBirthLongitude() == null) {
            throw new IllegalArgumentException("Complete birth data required");
        }
        
        Map<String, Object> natalChart = new HashMap<>();
        
        // Get planetary positions from calculation service
        Map<String, Double> planetaryPositions = calculationService.calculatePlanetaryPositions(
            user.getBirthDateTime(), 
            user.getBirthLatitude(), 
            user.getBirthLongitude()
        );
        
        // Calculate zodiac signs
        String sunSign = getZodiacSign(planetaryPositions.get("Sun"));
        String moonSign = getZodiacSign(planetaryPositions.get("Moon"));
        String risingSign = sunSign; // Simplified - should calculate actual ascendant
        
        natalChart.put("planetaryPositions", planetaryPositions);
        natalChart.put("sunSign", sunSign);
        natalChart.put("moonSign", moonSign);
        natalChart.put("risingSign", risingSign);
        natalChart.put("dominantElement", calculateDominantElement(planetaryPositions));
        natalChart.put("calculatedAt", LocalDateTime.now());
        
        return natalChart;
    }
    
    /**
     * Get zodiac sign from ecliptic longitude
     */
    private String getZodiacSign(Double longitude) {
        if (longitude == null) return "Unknown";
        
        String[] signs = {"Aries", "Taurus", "Gemini", "Cancer", "Leo", "Virgo",
                         "Libra", "Scorpio", "Sagittarius", "Capricorn", "Aquarius", "Pisces"};
        
        int signIndex = (int) (longitude / 30);
        return signs[signIndex % 12];
    }
    
    /**
     * Calculate dominant element
     */
    private String calculateDominantElement(Map<String, Double> positions) {
        Map<String, Integer> elements = new HashMap<>();
        elements.put("Fire", 0);
        elements.put("Earth", 0);
        elements.put("Air", 0);
        elements.put("Water", 0);
        
        for (Double position : positions.values()) {
            String element = getElement(getZodiacSign(position));
            elements.put(element, elements.get(element) + 1);
        }
        
        return elements.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Fire");
    }
    
    /**
     * Get element for zodiac sign
     */
    private String getElement(String sign) {
        switch (sign) {
            case "Aries": case "Leo": case "Sagittarius": return "Fire";
            case "Taurus": case "Virgo": case "Capricorn": return "Earth";
            case "Gemini": case "Libra": case "Aquarius": return "Air";
            case "Cancer": case "Scorpio": case "Pisces": return "Water";
            default: return "Fire";
        }
    }
    
    private String generatePersonalizedMessage(User user, String sunSign, List<String> influences) {
        String name = user.getFirstName() != null ? user.getFirstName() : user.getUsername();
        String timeOfDay = getTimeOfDayGreeting();
        
        if (!influences.isEmpty()) {
            return String.format("%s %s! As a %s, today's %s creates powerful opportunities for growth and transformation.",
                    timeOfDay, name, sunSign, influences.get(0));
        }
        
        return String.format("%s %s! Your %s energy is particularly strong today, bringing clarity and purpose to your endeavors.",
                timeOfDay, name, sunSign);
    }
    
    private String getTimeOfDayGreeting() {
        int hour = LocalDateTime.now().getHour();
        if (hour < 12) return "Good morning";
        if (hour < 17) return "Good afternoon";
        return "Good evening";
    }
    
    private String getTransitInfluence(List<String> influences) {
        if (influences.isEmpty()) {
            return "Stable planetary energies support steady progress";
        }
        return influences.get(0) + " brings dynamic energy to your life";
    }
    
    private String getPersonalizedRecommendation(String sunSign, String dominantElement) {
        Map<String, String> recommendations = new HashMap<>();
        recommendations.put("Aries", "Channel your natural leadership into new projects");
        recommendations.put("Taurus", "Focus on building lasting foundations today");
        recommendations.put("Gemini", "Communication and learning are highlighted");
        recommendations.put("Cancer", "Trust your intuition and nurture relationships");
        recommendations.put("Leo", "Express your creativity and natural charisma");
        recommendations.put("Virgo", "Attention to detail brings success");
        recommendations.put("Libra", "Seek balance and harmony in all endeavors");
        recommendations.put("Scorpio", "Deep transformation and healing are possible");
        recommendations.put("Sagittarius", "Expand your horizons through adventure");
        recommendations.put("Capricorn", "Disciplined effort leads to achievement");
        recommendations.put("Aquarius", "Innovation and unique perspectives shine");
        recommendations.put("Pisces", "Connect with your spiritual and artistic nature");
        
        return recommendations.getOrDefault(sunSign, "Follow your natural instincts");
    }
    
    private int calculateIntensity(List<String> influences) {
        if (influences.isEmpty()) return 3;
        if (influences.size() >= 3) return 5;
        if (influences.size() >= 2) return 4;
        return 3;
    }
    
    private String getDominantPlanet(List<String> influences) {
        if (influences.isEmpty()) return "Sun";
        
        String firstInfluence = influences.get(0);
        if (firstInfluence.contains("Sun")) return "Sun";
        if (firstInfluence.contains("Moon")) return "Moon";
        if (firstInfluence.contains("Mercury")) return "Mercury";
        if (firstInfluence.contains("Venus")) return "Venus";
        if (firstInfluence.contains("Mars")) return "Mars";
        if (firstInfluence.contains("Jupiter")) return "Jupiter";
        if (firstInfluence.contains("Saturn")) return "Saturn";
        
        return "Sun";
    }
    
    private String getLuckyColor(String sunSign) {
        Map<String, String> colors = new HashMap<>();
        colors.put("Aries", "Red");
        colors.put("Taurus", "Green");
        colors.put("Gemini", "Yellow");
        colors.put("Cancer", "Silver");
        colors.put("Leo", "Gold");
        colors.put("Virgo", "Navy");
        colors.put("Libra", "Pink");
        colors.put("Scorpio", "Maroon");
        colors.put("Sagittarius", "Purple");
        colors.put("Capricorn", "Brown");
        colors.put("Aquarius", "Blue");
        colors.put("Pisces", "Turquoise");
        
        return colors.getOrDefault(sunSign, "White");
    }
    
    private String getBestTimeOfDay(String dominantElement) {
        switch (dominantElement) {
            case "Fire": return "Morning";
            case "Earth": return "Afternoon";
            case "Air": return "Evening";
            case "Water": return "Night";
            default: return "Morning";
        }
    }
    
    private String getCurrentMoonPhase() {
        // Simplified moon phase calculation
        long daysSinceNewMoon = (System.currentTimeMillis() / 86400000) % 29;
        if (daysSinceNewMoon < 7) return "Waxing Crescent";
        if (daysSinceNewMoon < 14) return "First Quarter";
        if (daysSinceNewMoon < 21) return "Waning Gibbous";
        return "Last Quarter";
    }
    
    private LifeAreaInfluence calculateLoveInfluence(Map<String, Object> natalChart, Map<String, Double> transits) {
        // Calculate based on Venus position and aspects
        int rating = 3 + (int) (Math.random() * 3); // Simplified
        String insight = "Venus influences bring harmony to relationships";
        return new LifeAreaInfluence("Love & Relationships", rating, insight, "Heart", "from-pink-500 to-rose-500");
    }
    
    private LifeAreaInfluence calculateCareerInfluence(Map<String, Object> natalChart, Map<String, Double> transits) {
        int rating = 2 + (int) (Math.random() * 4); // Simplified
        String insight = "Mercury enhances communication and professional growth";
        return new LifeAreaInfluence("Career & Success", rating, insight, "Briefcase", "from-blue-500 to-indigo-500");
    }
    
    private LifeAreaInfluence calculateHealthInfluence(Map<String, Object> natalChart, Map<String, Double> transits) {
        int rating = 3 + (int) (Math.random() * 3); // Simplified
        String insight = "Mars energizes vitality and physical strength";
        return new LifeAreaInfluence("Health & Wellness", rating, insight, "Shield", "from-emerald-500 to-green-500");
    }
    
    private LifeAreaInfluence calculateSpiritualInfluence(Map<String, Object> natalChart, Map<String, Double> transits) {
        int rating = 3 + (int) (Math.random() * 3); // Simplified
        String insight = "Jupiter expands consciousness and spiritual awareness";
        return new LifeAreaInfluence("Spiritual Growth", rating, insight, "Sparkles", "from-purple-500 to-violet-500");
    }
    
    private int calculateAccuracyRate(User user) {
        // Base accuracy on user's experience and astrological factors
        int baseRate = 70;
        if (user.getChartsGenerated() != null && user.getChartsGenerated() > 5) {
            baseRate += 15;
        }
        if (user.isChartCalculated()) {
            baseRate += 10;
        }
        return Math.min(baseRate + (int) (Math.random() * 10), 99);
    }
    
    private String calculateCosmicEnergy(User user) {
        if (user.isChartCalculated()) {
            String[] energies = {"High", "Very High", "Peak"};
            return energies[(int) (Math.random() * energies.length)];
        }
        return "Medium";
    }
    
    private List<LifeAreaInfluence> getGenericLifeAreas() {
        List<LifeAreaInfluence> areas = new ArrayList<>();
        areas.add(new LifeAreaInfluence("Love & Relationships", 3, "Complete your birth profile for personalized insights", "Heart", "from-pink-500 to-rose-500"));
        areas.add(new LifeAreaInfluence("Career & Success", 3, "Complete your birth profile for personalized insights", "Briefcase", "from-blue-500 to-indigo-500"));
        areas.add(new LifeAreaInfluence("Health & Wellness", 3, "Complete your birth profile for personalized insights", "Shield", "from-emerald-500 to-green-500"));
        areas.add(new LifeAreaInfluence("Spiritual Growth", 3, "Complete your birth profile for personalized insights", "Sparkles", "from-purple-500 to-violet-500"));
        return areas;
    }
    
    private PersonalizedMessageResponse getIncompleteBirthDataMessage(User user) {
        String name = user.getFirstName() != null ? user.getFirstName() : user.getUsername();
        
        PersonalizedMessageResponse response = new PersonalizedMessageResponse();
        response.setMessage(String.format("Welcome %s! Complete your birth profile to unlock personalized cosmic insights tailored just for you.", name));
        response.setTransitInfluence("Complete birth data needed for accurate analysis");
        response.setRecommendation("Add your birth date, time, and location for full personalization");
        response.setIntensity(2);
        response.setDominantPlanet("Sun");
        response.setLuckyColor("White");
        response.setBestTimeOfDay("Morning");
        response.setMoonPhase(getCurrentMoonPhase());
        
        return response;
    }
    
    private PersonalizedMessageResponse getFallbackMessage(String username) {
        PersonalizedMessageResponse response = new PersonalizedMessageResponse();
        response.setMessage("Welcome back! The cosmic energies are flowing harmoniously today.");
        response.setTransitInfluence("Stable planetary alignments support growth");
        response.setRecommendation("Focus on inner reflection and creative pursuits");
        response.setIntensity(3);
        response.setDominantPlanet("Sun");
        response.setLuckyColor("White");
        response.setBestTimeOfDay("Morning");
        response.setMoonPhase(getCurrentMoonPhase());
        
        return response;
    }
}
