package com.cosmic.astrology.dto;

public class PlanetaryAspect {
    private String fromPlanet;
    private String toPlanet;
    private String aspectType; // conjunction, opposition, trine, square, sextile
    private double orb;
    private String strength; // strong, medium, weak
    private String meaning;
    private boolean isApplying;

    public PlanetaryAspect() { }

    // Getters and Setters
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

    public String getMeaning() { return meaning; }
    public void setMeaning(String meaning) { this.meaning = meaning; }

    public boolean isApplying() { return isApplying; }
    public void setApplying(boolean applying) { isApplying = applying; }
}
