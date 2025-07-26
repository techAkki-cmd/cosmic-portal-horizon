package com.cosmic.astrology.dto;

public class TransitResponse {
    private String planet;
    private Double position;
    private String sign;
    private String aspect;
    
    public TransitResponse() {}
    
    public TransitResponse(String planet, Double position, String sign) {
        this.planet = planet;
        this.position = position;
        this.sign = sign;
    }
    
    // Getters and Setters
    public String getPlanet() {
        return planet;
    }
    
    public void setPlanet(String planet) {
        this.planet = planet;
    }
    
    public Double getPosition() {
        return position;
    }
    
    public void setPosition(Double position) {
        this.position = position;
    }
    
    public String getSign() {
        return sign;
    }
    
    public void setSign(String sign) {
        this.sign = sign;
    }
    
    public String getAspect() {
        return aspect;
    }
    
    public void setAspect(String aspect) {
        this.aspect = aspect;
    }
}
