package com.cosmic.astrology.dto;

import java.util.List;

public class ChartVisualizationData {
    private List<PlanetPosition> planets;
    private List<HouseInfo> houses;
    private List<AspectLine> aspects;

    // Default constructor
    public ChartVisualizationData() { }

    // Getters and Setters
    public List<PlanetPosition> getPlanets() { return planets; }
    public void setPlanets(List<PlanetPosition> planets) { this.planets = planets; }

    public List<HouseInfo> getHouses() { return houses; }
    public void setHouses(List<HouseInfo> houses) { this.houses = houses; }

    public List<AspectLine> getAspects() { return aspects; }
    public void setAspects(List<AspectLine> aspects) { this.aspects = aspects; }

    // Inner class for Planet Position
    public static class PlanetPosition {
        private String planet;
        private double degree;
        private String sign;
        private int house;
        private String color;
        private boolean isRetrograde;

        public PlanetPosition() { }

        public String getPlanet() { return planet; }
        public void setPlanet(String planet) { this.planet = planet; }

        public double getDegree() { return degree; }
        public void setDegree(double degree) { this.degree = degree; }

        public String getSign() { return sign; }
        public void setSign(String sign) { this.sign = sign; }

        public int getHouse() { return house; }
        public void setHouse(int house) { this.house = house; }

        public String getColor() { return color; }
        public void setColor(String color) { this.color = color; }

        public boolean isRetrograde() { return isRetrograde; }
        public void setRetrograde(boolean retrograde) { isRetrograde = retrograde; }
    }

    // Inner class for House Information
    public static class HouseInfo {
        private int houseNumber;
        private String sign;
        private double startDegree;
        private String lord;
        private String meaning;

        public HouseInfo() { }

        public int getHouseNumber() { return houseNumber; }
        public void setHouseNumber(int houseNumber) { this.houseNumber = houseNumber; }

        public String getSign() { return sign; }
        public void setSign(String sign) { this.sign = sign; }

        public double getStartDegree() { return startDegree; }
        public void setStartDegree(double startDegree) { this.startDegree = startDegree; }

        public String getLord() { return lord; }
        public void setLord(String lord) { this.lord = lord; }

        public String getMeaning() { return meaning; }
        public void setMeaning(String meaning) { this.meaning = meaning; }
    }

    // Inner class for Aspect Lines
    public static class AspectLine {
        private String fromPlanet;
        private String toPlanet;
        private String aspectType;
        private double orb;
        private String strength;

        public AspectLine() { }

        public String getFromPlanet() { return fromPlanet; }
        public void setFromPlanet(String fromPlanet) { this.fromPlanet = fromPlanet; }

        public String getToPlanet() { return toPlanet; }
        public void setToPlanet(String toPlanet) { this.toPlanet = toPlanet; }

        public String getAspectType() { return aspectType; }
        public void setAspectType(String aspectType) { this.aspectType = aspectType; }

        public double getOrb() { return orb; }
        public void setOrb(double orb) { this.orb = orb; }

        public String getStrength() { return strength; }
        public void setStrength(String strength) { this.strength = strength; }
    }
}
