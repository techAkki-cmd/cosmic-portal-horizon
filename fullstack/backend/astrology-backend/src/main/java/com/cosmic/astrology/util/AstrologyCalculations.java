package com.cosmic.astrology.util;

import com.cosmic.astrology.dto.PlanetPosition;
import com.cosmic.astrology.dto.HousePosition;
import com.cosmic.astrology.dto.AspectData;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;

@Component
public class AstrologyCalculations {
    
    // Your existing constants remain the same
    private static final String[] ZODIAC_SIGNS = {
        "Aries", "Taurus", "Gemini", "Cancer", "Leo", "Virgo",
        "Libra", "Scorpio", "Sagittarius", "Capricorn", "Aquarius", "Pisces"
    };
    
    private static final Map<String, Double> MAJOR_ASPECTS = Map.of(
        "Conjunction", 0.0,
        "Sextile", 60.0,
        "Square", 90.0,
        "Trine", 120.0,
        "Opposition", 180.0
    );
    
    private static final Map<String, Double> ASPECT_ORBS = Map.of(
        "Conjunction", 8.0,
        "Sextile", 6.0,
        "Square", 8.0,
        "Trine", 8.0,
        "Opposition", 8.0
    );

    /**
     * Calculate Julian Day using pure mathematical approach
     */
    public double calculateJulianDay(LocalDateTime dateTime, String timezone) {
        try {
            ZoneId zoneId = ZoneId.of(timezone);
            ZoneOffset offset = zoneId.getRules().getOffset(dateTime);
            LocalDateTime utcDateTime = dateTime.minusSeconds(offset.getTotalSeconds());
            
            int year = utcDateTime.getYear();
            int month = utcDateTime.getMonthValue();
            int day = utcDateTime.getDayOfMonth();
            double hour = utcDateTime.getHour() + 
                         utcDateTime.getMinute() / 60.0 + 
                         utcDateTime.getSecond() / 3600.0;
            
            // Julian Day calculation using standard algorithm
            if (month <= 2) {
                year--;
                month += 12;
            }
            
            int a = year / 100;
            int b = 2 - a + (a / 4);
            
            double jd = Math.floor(365.25 * (year + 4716)) + 
                       Math.floor(30.6001 * (month + 1)) + 
                       day + hour / 24.0 + b - 1524.5;
            
            return jd;
        } catch (Exception e) {
            throw new RuntimeException("Error calculating Julian Day: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get current Julian Day
     */
    public double getCurrentJulianDay() {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        return calculateJulianDay(now, "UTC");
    }
    
    /**
     * Calculate planetary positions using VSOP87 simplified formulas
     */
    public Map<String, PlanetPosition> calculatePlanetaryPositions(double julianDay) {
        Map<String, PlanetPosition> planets = new HashMap<>();
        double T = (julianDay - 2451545.0) / 36525.0; // Centuries since J2000.0
        
        // Sun position (actually Earth's position around Sun)
        double sunLongitude = calculateSunLongitude(T);
        planets.put("Sun", new PlanetPosition("Sun", sunLongitude, 0.0, 1.0, getZodiacSign(sunLongitude), 0));
        
        // Moon position
        double moonLongitude = calculateMoonLongitude(T);
        planets.put("Moon", new PlanetPosition("Moon", moonLongitude, 0.0, 13.2, getZodiacSign(moonLongitude), 0));
        
        // Mercury
        double mercuryLongitude = calculateMercuryLongitude(T);
        planets.put("Mercury", new PlanetPosition("Mercury", mercuryLongitude, 0.0, 4.1, getZodiacSign(mercuryLongitude), 0));
        
        // Venus
        double venusLongitude = calculateVenusLongitude(T);
        planets.put("Venus", new PlanetPosition("Venus", venusLongitude, 0.0, 1.6, getZodiacSign(venusLongitude), 0));
        
        // Mars
        double marsLongitude = calculateMarsLongitude(T);
        planets.put("Mars", new PlanetPosition("Mars", marsLongitude, 0.0, 0.5, getZodiacSign(marsLongitude), 0));
        
        // Jupiter
        double jupiterLongitude = calculateJupiterLongitude(T);
        planets.put("Jupiter", new PlanetPosition("Jupiter", jupiterLongitude, 0.0, 0.08, getZodiacSign(jupiterLongitude), 0));
        
        // Saturn
        double saturnLongitude = calculateSaturnLongitude(T);
        planets.put("Saturn", new PlanetPosition("Saturn", saturnLongitude, 0.0, 0.03, getZodiacSign(saturnLongitude), 0));
        
        // Outer planets (simplified positions)
        planets.put("Uranus", new PlanetPosition("Uranus", calculateUranusLongitude(T), 0.0, 0.01, getZodiacSign(calculateUranusLongitude(T)), 0));
        planets.put("Neptune", new PlanetPosition("Neptune", calculateNeptuneLongitude(T), 0.0, 0.006, getZodiacSign(calculateNeptuneLongitude(T)), 0));
        planets.put("Pluto", new PlanetPosition("Pluto", calculatePlutoLongitude(T), 0.0, 0.004, getZodiacSign(calculatePlutoLongitude(T)), 0));
        
        return planets;
    }
    
    // Simplified planetary position calculations
    private double calculateSunLongitude(double T) {
        double L0 = 280.46646 + 36000.76983 * T + 0.0003032 * T * T;
        double M = 357.52911 + 35999.05029 * T - 0.0001537 * T * T;
        double C = (1.914602 - 0.004817 * T - 0.000014 * T * T) * Math.sin(Math.toRadians(M))
                 + (0.019993 - 0.000101 * T) * Math.sin(Math.toRadians(2 * M))
                 + 0.000289 * Math.sin(Math.toRadians(3 * M));
        return normalizeAngle(L0 + C);
    }
    
    private double calculateMoonLongitude(double T) {
        double L = 218.3164477 + 481267.88123421 * T - 0.0015786 * T * T;
        double D = 297.8501921 + 445267.1114034 * T - 0.0018819 * T * T;
        double M = 357.5291092 + 35999.0502909 * T - 0.0001536 * T * T;
        double Mp = 134.9633964 + 477198.8675055 * T + 0.0087414 * T * T;
        double F = 93.2720950 + 483202.0175233 * T - 0.0036539 * T * T;
        
        // Simplified lunar longitude calculation
        double longitude = L + 6.289 * Math.sin(Math.toRadians(Mp))
                             - 1.274 * Math.sin(Math.toRadians(Mp - 2 * D))
                             + 0.658 * Math.sin(Math.toRadians(2 * D))
                             - 0.186 * Math.sin(Math.toRadians(M))
                             - 0.059 * Math.sin(Math.toRadians(2 * Mp - 2 * D))
                             - 0.057 * Math.sin(Math.toRadians(Mp - 2 * D + M));
        
        return normalizeAngle(longitude);
    }
    
    private double calculateMercuryLongitude(double T) {
        return normalizeAngle(252.250906 + 149474.0722491 * T);
    }
    
    private double calculateVenusLongitude(double T) {
        return normalizeAngle(181.979801 + 58519.2130302 * T);
    }
    
    private double calculateMarsLongitude(double T) {
        return normalizeAngle(355.433 + 19141.6964746 * T);
    }
    
    private double calculateJupiterLongitude(double T) {
        return normalizeAngle(34.351519 + 3036.3027748 * T);
    }
    
    private double calculateSaturnLongitude(double T) {
        return normalizeAngle(50.077444 + 1223.5110686 * T);
    }
    
    private double calculateUranusLongitude(double T) {
        return normalizeAngle(314.055005 + 429.8640561 * T);
    }
    
    private double calculateNeptuneLongitude(double T) {
        return normalizeAngle(304.348665 + 219.8833092 * T);
    }
    
    private double calculatePlutoLongitude(double T) {
        return normalizeAngle(238.958116 + 146.9176442 * T);
    }
    
    /**
     * Normalize angle to 0-360 degrees
     */
    private double normalizeAngle(double angle) {
        angle = angle % 360.0;
        if (angle < 0) angle += 360.0;
        return angle;
    }
    
    /**
     * Calculate house cusps using Equal House system (simpler alternative to Placidus)
     */
    public List<HousePosition> calculateHouses(double julianDay, double latitude, double longitude) {
        List<HousePosition> houses = new ArrayList<>();
        
        // Calculate Ascendant (simplified)
        double siderealTime = calculateSiderealTime(julianDay, longitude);
        double ascendant = calculateAscendant(siderealTime, latitude);
        
        // Equal House system: each house is exactly 30 degrees
        for (int i = 1; i <= 12; i++) {
            double cuspLongitude = normalizeAngle(ascendant + (i - 1) * 30);
            String sign = getZodiacSign(cuspLongitude);
            String ruler = getSignRuler(sign);
            
            HousePosition house = new HousePosition(i, cuspLongitude, sign, ruler);
            houses.add(house);
        }
        
        return houses;
    }
    
    private double calculateSiderealTime(double julianDay, double longitude) {
        double T = (julianDay - 2451545.0) / 36525.0;
        double theta0 = 280.46061837 + 360.98564736629 * (julianDay - 2451545.0) + 0.000387933 * T * T - T * T * T / 38710000.0;
        return normalizeAngle(theta0 + longitude);
    }
    
    private double calculateAscendant(double siderealTime, double latitude) {
        // Simplified ascendant calculation
        double obliquity = 23.4393 - 0.0130 * ((System.currentTimeMillis() / 1000.0 - 946684800) / 31557600.0);
        return Math.toDegrees(Math.atan2(Math.cos(Math.toRadians(siderealTime)), 
                                        -Math.sin(Math.toRadians(siderealTime)) * Math.cos(Math.toRadians(obliquity))));
    }
    
    /**
     * Calculate house position for a planet (simplified)
     */
    public int getHouse(double longitude, double latitude, double julianDay) {
        List<HousePosition> houses = calculateHouses(julianDay, latitude, longitude);
        
        for (int i = 0; i < houses.size(); i++) {
            double currentCusp = houses.get(i).getCusp();
            double nextCusp = (i == 11) ? houses.get(0).getCusp() + 360 : houses.get(i + 1).getCusp();
            
            if (nextCusp < currentCusp) {
                if (longitude >= currentCusp || longitude < nextCusp) {
                    return i + 1;
                }
            } else {
                if (longitude >= currentCusp && longitude < nextCusp) {
                    return i + 1;
                }
            }
        }
        return 1;
    }
    
    // Keep all your existing methods for zodiac signs, aspects, interpretations, etc.
    public String getZodiacSign(double longitude) {
        int signIndex = (int) (longitude / 30);
        return ZODIAC_SIGNS[signIndex % 12];
    }
    
    private String getSignRuler(String sign) {
        Map<String, String> rulers = Map.ofEntries(
            Map.entry("Aries", "Mars"),
            Map.entry("Taurus", "Venus"),
            Map.entry("Gemini", "Mercury"),
            Map.entry("Cancer", "Moon"),
            Map.entry("Leo", "Sun"),
            Map.entry("Virgo", "Mercury"),
            Map.entry("Libra", "Venus"),
            Map.entry("Scorpio", "Pluto"),
            Map.entry("Sagittarius", "Jupiter"),
            Map.entry("Capricorn", "Saturn"),
            Map.entry("Aquarius", "Uranus"),
            Map.entry("Pisces", "Neptune")
        );
        return rulers.getOrDefault(sign, "Unknown");
    }
    
    // All your existing aspect calculation methods remain the same...
    public List<AspectData> calculateAspects(Map<String, PlanetPosition> planets) {
        List<AspectData> aspects = new ArrayList<>();
        List<String> planetNames = new ArrayList<>(planets.keySet());
        
        for (int i = 0; i < planetNames.size(); i++) {
            for (int j = i + 1; j < planetNames.size(); j++) {
                String planet1 = planetNames.get(i);
                String planet2 = planetNames.get(j);
                
                PlanetPosition pos1 = planets.get(planet1);
                PlanetPosition pos2 = planets.get(planet2);
                
                double aspectAngle = calculateAspectAngle(pos1.getLongitude(), pos2.getLongitude());
                String aspectType = getAspectType(aspectAngle);
                
                if (aspectType != null) {
                    double orb = calculateOrb(aspectAngle, MAJOR_ASPECTS.get(aspectType));
                    boolean applying = isAspectApplying(pos1, pos2, aspectAngle);
                    
                    AspectData aspect = new AspectData(planet1, planet2, aspectType, aspectAngle, orb, applying);
                    aspect.setInterpretation(generateAspectInterpretation(planet1, planet2, aspectType));
                    aspects.add(aspect);
                }
            }
        }
        
        return aspects;
    }
    
    public double calculateAspectAngle(double longitude1, double longitude2) {
        double diff = Math.abs(longitude1 - longitude2);
        return Math.min(diff, 360 - diff);
    }
    
    public String getAspectType(double angle) {
        for (Map.Entry<String, Double> aspect : MAJOR_ASPECTS.entrySet()) {
            double expectedAngle = aspect.getValue();
            double orb = ASPECT_ORBS.get(aspect.getKey());
            
            if (Math.abs(angle - expectedAngle) <= orb) {
                return aspect.getKey();
            }
        }
        return null;
    }
    
    private double calculateOrb(double actualAngle, double expectedAngle) {
        return Math.abs(actualAngle - expectedAngle);
    }
    
    private boolean isAspectApplying(PlanetPosition planet1, PlanetPosition planet2, double currentAngle) {
        return Math.abs(planet1.getSpeed()) > Math.abs(planet2.getSpeed());
    }
    
    private String generateAspectInterpretation(String planet1, String planet2, String aspectType) {
        return String.format("%s %s %s: %s", 
            planet1, aspectType.toLowerCase(), planet2, 
            getBasicAspectMeaning(aspectType));
    }
    
    private String getBasicAspectMeaning(String aspectType) {
        Map<String, String> meanings = Map.of(
            "Conjunction", "Blending and amplification of energies",
            "Sextile", "Harmonious opportunity and cooperation",
            "Square", "Tension and challenge requiring action",
            "Trine", "Natural harmony and flowing energy",
            "Opposition", "Balance and integration of opposing forces"
        );
        return meanings.getOrDefault(aspectType, "Astrological influence");
    }
    
    public String getTransitInterpretation(String planet, String aspectType) {
        Map<String, String> planetMeanings = Map.of(
            "Sun", "vitality and core identity",
            "Moon", "emotions and instincts",
            "Mercury", "communication and thinking",
            "Venus", "love and values",
            "Mars", "energy and action",
            "Jupiter", "growth and expansion",
            "Saturn", "structure and discipline",
            "Uranus", "change and innovation",
            "Neptune", "spirituality and dreams",
            "Pluto", "transformation and power"
        );
        
        String planetMeaning = planetMeanings.getOrDefault(planet, "planetary energy");
        String aspectMeaning = getBasicAspectMeaning(aspectType);
        
        return String.format("%s transit creating %s affecting %s", 
            planet, aspectMeaning.toLowerCase(), planetMeaning);
    }
}
