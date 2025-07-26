package com.cosmic.astrology.dto;

public class AspectData {
    private String planet1;
    private String planet2;
    private String aspectType;
    private double angle;
    private double orb;
    private boolean applying;
    private int strength;
    private String interpretation;
    
    public AspectData() {}
    
    public AspectData(String planet1, String planet2, String aspectType, 
                     double angle, double orb, boolean applying) {
        this.planet1 = planet1;
        this.planet2 = planet2;
        this.aspectType = aspectType;
        this.angle = angle;
        this.orb = orb;
        this.applying = applying;
        this.strength = calculateStrength(aspectType, orb);
    }
    
    private int calculateStrength(String aspectType, double orb) {
        int baseStrength;
        switch (aspectType.toLowerCase()) {
            case "conjunction": baseStrength = 10; break;
            case "opposition": baseStrength = 9; break;
            case "square": baseStrength = 8; break;
            case "trine": baseStrength = 7; break;
            case "sextile": baseStrength = 6; break;
            default: baseStrength = 5;
        }
        
        if (orb > 3) baseStrength -= 2;
        else if (orb > 1) baseStrength -= 1;
        
        return Math.max(1, Math.min(10, baseStrength));
    }
    
    public String getPlanet1() { return planet1; }
    public void setPlanet1(String planet1) { this.planet1 = planet1; }
    
    public String getPlanet2() { return planet2; }
    public void setPlanet2(String planet2) { this.planet2 = planet2; }
    
    public String getAspectType() { return aspectType; }
    public void setAspectType(String aspectType) { 
        this.aspectType = aspectType;
        this.strength = calculateStrength(aspectType, this.orb);
    }
    
    public double getAngle() { return angle; }
    public void setAngle(double angle) { this.angle = angle; }
    
    public double getOrb() { return orb; }
    public void setOrb(double orb) { 
        this.orb = orb;
        this.strength = calculateStrength(this.aspectType, orb);
    }
    
    public boolean isApplying() { return applying; }
    public void setApplying(boolean applying) { this.applying = applying; }
    
    public int getStrength() { return strength; }
    public void setStrength(int strength) { this.strength = strength; }
    
    public String getInterpretation() { return interpretation; }
    public void setInterpretation(String interpretation) { this.interpretation = interpretation; }
}
