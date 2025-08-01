package com.cosmic.astrology.dto;

public class UserPreferencesResponse {
    private String preferredLanguage;
    private String timeFormat;
    private String dateFormat;
    private Boolean emailNotifications;
    private Boolean dailyHoroscope;
    private Boolean transitAlerts;
    private String timezone;
    
    // Constructors, getters, and setters
    public UserPreferencesResponse() {}
    
    public String getPreferredLanguage() { return preferredLanguage; }
    public void setPreferredLanguage(String preferredLanguage) { this.preferredLanguage = preferredLanguage; }
    
    public String getTimeFormat() { return timeFormat; }
    public void setTimeFormat(String timeFormat) { this.timeFormat = timeFormat; }
    
    public String getDateFormat() { return dateFormat; }
    public void setDateFormat(String dateFormat) { this.dateFormat = dateFormat; }
    
    public Boolean getEmailNotifications() { return emailNotifications; }
    public void setEmailNotifications(Boolean emailNotifications) { this.emailNotifications = emailNotifications; }
    
    public Boolean getDailyHoroscope() { return dailyHoroscope; }
    public void setDailyHoroscope(Boolean dailyHoroscope) { this.dailyHoroscope = dailyHoroscope; }
    
    public Boolean getTransitAlerts() { return transitAlerts; }
    public void setTransitAlerts(Boolean transitAlerts) { this.transitAlerts = transitAlerts; }
    
    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }
}