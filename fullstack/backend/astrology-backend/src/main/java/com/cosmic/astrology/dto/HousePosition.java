package com.cosmic.astrology.dto;

public class HousePosition {
    private int house;
    private double cusp;
    private String sign;
    private String ruler;
    private double degree;
    private int minutes;
    private int seconds;
    
    public HousePosition() {}
    
    public HousePosition(int house, double cusp, String sign, String ruler) {
        this.house = house;
        this.cusp = cusp;
        this.sign = sign;
        this.ruler = ruler;
        this.calculateDegreeMinutesSeconds(cusp);
    }
    
    private void calculateDegreeMinutesSeconds(double cusp) {
        double adjustedCusp = cusp % 30;
        this.degree = (int) adjustedCusp;
        double minutesDouble = (adjustedCusp - degree) * 60;
        this.minutes = (int) minutesDouble;
        this.seconds = (int) ((minutesDouble - minutes) * 60);
    }
    
    public int getHouse() { return house; }
    public void setHouse(int house) { this.house = house; }
    
    public double getCusp() { return cusp; }
    public void setCusp(double cusp) { 
        this.cusp = cusp;
        calculateDegreeMinutesSeconds(cusp);
    }
    
    public String getSign() { return sign; }
    public void setSign(String sign) { this.sign = sign; }
    
    public String getRuler() { return ruler; }
    public void setRuler(String ruler) { this.ruler = ruler; }
    
    public double getDegree() { return degree; }
    public void setDegree(double degree) { this.degree = degree; }
    
    public int getMinutes() { return minutes; }
    public void setMinutes(int minutes) { this.minutes = minutes; }
    
    public int getSeconds() { return seconds; }
    public void setSeconds(int seconds) { this.seconds = seconds; }
}
