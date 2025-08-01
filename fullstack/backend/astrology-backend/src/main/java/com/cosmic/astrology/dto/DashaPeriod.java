package com.cosmic.astrology.dto;

import java.time.LocalDate;

public class DashaPeriod {
    private String mahadashaLord;
    private String antardashaLord;
    private LocalDate startDate;
    private LocalDate endDate;
    private String interpretation;
    private String lifeTheme;
    private String opportunities;
    private String challenges;
    private boolean isCurrent;
    private String remedies;

    // Default constructor
    public DashaPeriod() { }

    // Getters and Setters
    public String getMahadashaLord() { return mahadashaLord; }
    public void setMahadashaLord(String mahadashaLord) { this.mahadashaLord = mahadashaLord; }

    public String getAntardashaLord() { return antardashaLord; }
    public void setAntardashaLord(String antardashaLord) { this.antardashaLord = antardashaLord; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getInterpretation() { return interpretation; }
    public void setInterpretation(String interpretation) { this.interpretation = interpretation; }

    public String getLifeTheme() { return lifeTheme; }
    public void setLifeTheme(String lifeTheme) { this.lifeTheme = lifeTheme; }

    public String getOpportunities() { return opportunities; }
    public void setOpportunities(String opportunities) { this.opportunities = opportunities; }

    public String getChallenges() { return challenges; }
    public void setChallenges(String challenges) { this.challenges = challenges; }

    public boolean isCurrent() { return isCurrent; }
    public void setCurrent(boolean current) { isCurrent = current; }

    public String getRemedies() { return remedies; }
    public void setRemedies(String remedies) { this.remedies = remedies; }
}
