package com.cosmic.astrology.dto;

public class NakshatraInfo {
    private String nakshatra;
    private int nakshatraNumber;
    private int pada;
    private String deity;
    private String symbol;
    private String quality;
    private String meaning;
    private String lifeLesson;
    private String mantra;

    // Default constructor
    public NakshatraInfo() { }

    // Getters and Setters
    public String getNakshatra() { return nakshatra; }
    public void setNakshatra(String nakshatra) { this.nakshatra = nakshatra; }

    public int getNakshatraNumber() { return nakshatraNumber; }
    public void setNakshatraNumber(int nakshatraNumber) { this.nakshatraNumber = nakshatraNumber; }

    public int getPada() { return pada; }
    public void setPada(int pada) { this.pada = pada; }

    public String getDeity() { return deity; }
    public void setDeity(String deity) { this.deity = deity; }

    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public String getQuality() { return quality; }
    public void setQuality(String quality) { this.quality = quality; }

    public String getMeaning() { return meaning; }
    public void setMeaning(String meaning) { this.meaning = meaning; }

    public String getLifeLesson() { return lifeLesson; }
    public void setLifeLesson(String lifeLesson) { this.lifeLesson = lifeLesson; }

    public String getMantra() { return mantra; }
    public void setMantra(String mantra) { this.mantra = mantra; }
}
