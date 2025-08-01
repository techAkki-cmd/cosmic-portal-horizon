package com.cosmic.astrology.dto;

public class RareYoga {
    private String name;
    private String description;
    private String meaning;
    private String planetsCombination;
    private boolean isVeryRare;
    private String remedialAction;
    private double rarity; // percentage of population having this

    // Default constructor
    public RareYoga() { }

    // Constructor with basic fields
    public RareYoga(String name, String description, String meaning, boolean isVeryRare) {
        this.name = name;
        this.description = description;
        this.meaning = meaning;
        this.isVeryRare = isVeryRare;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getMeaning() { return meaning; }
    public void setMeaning(String meaning) { this.meaning = meaning; }

    public String getPlanetsCombination() { return planetsCombination; }
    public void setPlanetsCombination(String planetsCombination) { this.planetsCombination = planetsCombination; }

    public boolean isVeryRare() { return isVeryRare; }
    public void setVeryRare(boolean veryRare) { isVeryRare = veryRare; }

    public String getRemedialAction() { return remedialAction; }
    public void setRemedialAction(String remedialAction) { this.remedialAction = remedialAction; }

    public double getRarity() { return rarity; }
    public void setRarity(double rarity) { this.rarity = rarity; }
}
