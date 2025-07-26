package com.cosmic.astrology.service;

import com.cosmic.astrology.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Service
public class AstrologyCalculationService {
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Calculate natal chart positions for a user
     */
    public Map<String, Object> calculateNatalChart(User user) {
        if (user.getBirthDateTime() == null || user.getBirthLatitude() == null || user.getBirthLongitude() == null) {
            throw new IllegalArgumentException("Complete birth data required");
        }
        
        Map<String, Object> natalChart = new HashMap<>();
        
        // Calculate planetary positions (simplified calculations)
        Map<String, Double> planetaryPositions = calculatePlanetaryPositions(
            user.getBirthDateTime(), 
            user.getBirthLatitude(), 
            user.getBirthLongitude()
        );
        
        // Calculate zodiac signs
        String sunSign = getZodiacSign(planetaryPositions.get("Sun"));
        String moonSign = getZodiacSign(planetaryPositions.get("Moon"));
        String risingSign = getZodiacSign(planetaryPositions.get("Ascendant"));
        
        natalChart.put("planetaryPositions", planetaryPositions);
        natalChart.put("sunSign", sunSign);
        natalChart.put("moonSign", moonSign);
        natalChart.put("risingSign", risingSign);
        natalChart.put("dominantElement", calculateDominantElement(planetaryPositions));
        natalChart.put("calculatedAt", LocalDateTime.now());
        
        return natalChart;
    }
    
    /**
     * Get current planetary transits (no parameters needed - global transits)
     */
    public Map<String, Double> getCurrentTransits() {
        return calculatePlanetaryPositions(LocalDateTime.now(), 0.0, 0.0);
    }
    
    /**
     * Analyze transits against natal chart to find influences
     */
    public List<String> analyzeTransits(Map<String, Object> natalChart, Map<String, Double> currentTransits) {
        List<String> influences = new ArrayList<>();
        
        @SuppressWarnings("unchecked")
        Map<String, Double> natalPositions = (Map<String, Double>) natalChart.get("planetaryPositions");
        
        if (natalPositions == null) {
            return influences; // Return empty list if no natal positions
        }
        
        // Check major aspects between current transits and natal positions
        for (Map.Entry<String, Double> transit : currentTransits.entrySet()) {
            String transitPlanet = transit.getKey();
            Double transitPosition = transit.getValue();
            
            for (Map.Entry<String, Double> natal : natalPositions.entrySet()) {
                String natalPlanet = natal.getKey();
                Double natalPosition = natal.getValue();
                
                if (transitPosition != null && natalPosition != null) {
                    double orb = Math.abs(transitPosition - natalPosition);
                    if (orb > 180) orb = 360 - orb; // Handle wrap-around
                    
                    // Check for major aspects (within 5 degree orb)
                    if (orb <= 5) { // Conjunction
                        influences.add(transitPlanet + " conjunct natal " + natalPlanet);
                    } else if (Math.abs(orb - 90) <= 5) { // Square
                        influences.add(transitPlanet + " square natal " + natalPlanet);
                    } else if (Math.abs(orb - 120) <= 5) { // Trine
                        influences.add(transitPlanet + " trine natal " + natalPlanet);
                    } else if (Math.abs(orb - 180) <= 5) { // Opposition
                        influences.add(transitPlanet + " opposite natal " + natalPlanet);
                    } else if (Math.abs(orb - 60) <= 5) { // Sextile
                        influences.add(transitPlanet + " sextile natal " + natalPlanet);
                    }
                }
            }
        }
        
        return influences;
    }
    
    /**
     * Calculate planetary positions (simplified algorithm)
     */
    private Map<String, Double> calculatePlanetaryPositions(LocalDateTime dateTime, Double latitude, Double longitude) {
        Map<String, Double> positions = new HashMap<>();
        
        // Simplified calculation - in production, use proper ephemeris
        long daysSinceEpoch = dateTime.toEpochSecond(ZoneOffset.UTC) / 86400;
        
        // Sun position (approximate)
        double sunPosition = (daysSinceEpoch * 0.9856) % 360;
        positions.put("Sun", sunPosition);
        
        // Moon position (approximate)
        double moonPosition = (daysSinceEpoch * 13.1763) % 360;
        positions.put("Moon", moonPosition);
        
        // Other planets (simplified)
        positions.put("Mercury", (sunPosition + (daysSinceEpoch * 4.1) % 360) % 360);
        positions.put("Venus", (sunPosition + (daysSinceEpoch * 1.6) % 360) % 360);
        positions.put("Mars", (sunPosition + (daysSinceEpoch * 0.5) % 360) % 360);
        positions.put("Jupiter", (sunPosition + (daysSinceEpoch * 0.08) % 360) % 360);
        positions.put("Saturn", (sunPosition + (daysSinceEpoch * 0.03) % 360) % 360);
        
        // Ascendant (simplified - needs local sidereal time)
        double lst = calculateLocalSiderealTime(dateTime, longitude);
        positions.put("Ascendant", lst % 360);
        
        return positions;
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
    // Add this method to AstrologyCalculationService
public Map<String,Double> calculatePlanetaryPositions(LocalDateTime dt, double lat, double lon) {
    long days = dt.toEpochSecond(ZoneOffset.UTC) / 86_400;
    double sun = (days * 0.9856) % 360;

    Map<String,Double> p = new HashMap<>();
    p.put("Sun", sun);
    p.put("Moon", (days * 13.1763) % 360);
    p.put("Mercury", (sun + days * 4.1) % 360);
    p.put("Venus", (sun + days * 1.6) % 360);
    p.put("Mars", (sun + days * 0.5) % 360);
    p.put("Jupiter", (sun + days * 0.08) % 360);
    p.put("Saturn", (sun + days * 0.03) % 360);
    return p;
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
    
    /**
     * Calculate local sidereal time (simplified)
     */
    private double calculateLocalSiderealTime(LocalDateTime dateTime, Double longitude) {
        // Simplified calculation
        long daysSinceJ2000 = dateTime.toEpochSecond(ZoneOffset.UTC) / 86400 - 10957;
        double gst = (18.697374558 + 24.06570982441908 * daysSinceJ2000) % 24;
        return ((gst + longitude / 15.0) % 24) * 15; // Convert to degrees
    }
}
