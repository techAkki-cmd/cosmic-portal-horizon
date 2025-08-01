package com.cosmic.astrology.dto;

public class PersonalizedRemedy {
    private String category; // Gemstone, Mantra, Ritual, Diet, etc.
    private String remedy;
    private String reason;
    private String instructions;
    private String timing;
    private int priority; // 1-5

    // Default constructor
    public PersonalizedRemedy() { }
     public PersonalizedRemedy(String category, String remedy, String reason, 
                             String instructions, String timing, int priority) {
        this.category = category;
        this.remedy = remedy;
        this.reason = reason;
        this.instructions = instructions;
        this.timing = timing;
        this.priority = priority;
    }

    // Getters and Setters
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getRemedy() { return remedy; }
    public void setRemedy(String remedy) { this.remedy = remedy; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }

    public String getTiming() { return timing; }
    public void setTiming(String timing) { this.timing = timing; }

    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
}
