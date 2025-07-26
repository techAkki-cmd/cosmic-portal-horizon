// src/main/java/com/cosmic/astrology/entity/User.java
package com.cosmic.astrology.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Column(unique = true)
    private String username;
    
    @NotBlank
    @Email
    @Column(unique = true)
    private String email;
    
    @NotBlank
    private String password;
    
    private String firstName;
    private String lastName;
    
    // Enhanced Birth Information
    private LocalDateTime birthDateTime;
    private String birthLocation;
    private Double birthLatitude;
    private Double birthLongitude;
    private String timezone;
    
    // Calculated Astrology Data (cached for performance)
    private String sunSign;
    private String moonSign;
    private String risingSign;
    private String dominantElement; // Fire, Earth, Air, Water
    
    @Column(columnDefinition = "TEXT")
    private String natalChart; // JSON of calculated positions
    
    private LocalDateTime chartCalculatedAt;
    private boolean chartCalculated = false;
    
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.CLIENT;
    
    private boolean enabled = true;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
    
    // Statistics
    private Integer chartsGenerated = 0;
    private Integer loginStreak = 0;
    private LocalDateTime lastActiveDate;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    // Constructors
    public User() {}
    
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
    
    // All getters and setters (including new fields)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public LocalDateTime getBirthDateTime() { return birthDateTime; }
    public void setBirthDateTime(LocalDateTime birthDateTime) { this.birthDateTime = birthDateTime; }
    
    public String getBirthLocation() { return birthLocation; }
    public void setBirthLocation(String birthLocation) { this.birthLocation = birthLocation; }
    
    public Double getBirthLatitude() { return birthLatitude; }
    public void setBirthLatitude(Double birthLatitude) { this.birthLatitude = birthLatitude; }
    
    public Double getBirthLongitude() { return birthLongitude; }
    public void setBirthLongitude(Double birthLongitude) { this.birthLongitude = birthLongitude; }
    
    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }
    
    public String getSunSign() { return sunSign; }
    public void setSunSign(String sunSign) { this.sunSign = sunSign; }
    
    public String getMoonSign() { return moonSign; }
    public void setMoonSign(String moonSign) { this.moonSign = moonSign; }
    
    public String getRisingSign() { return risingSign; }
    public void setRisingSign(String risingSign) { this.risingSign = risingSign; }
    
    public String getDominantElement() { return dominantElement; }
    public void setDominantElement(String dominantElement) { this.dominantElement = dominantElement; }
    
    public String getNatalChart() { return natalChart; }
    public void setNatalChart(String natalChart) { this.natalChart = natalChart; }
    
    public LocalDateTime getChartCalculatedAt() { return chartCalculatedAt; }
    public void setChartCalculatedAt(LocalDateTime chartCalculatedAt) { this.chartCalculatedAt = chartCalculatedAt; }
    
    public boolean isChartCalculated() { return chartCalculated; }
    public void setChartCalculated(boolean chartCalculated) { this.chartCalculated = chartCalculated; }
    
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
    
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }
    
    public Integer getChartsGenerated() { return chartsGenerated; }
    public void setChartsGenerated(Integer chartsGenerated) { this.chartsGenerated = chartsGenerated; }
    
    public Integer getLoginStreak() { return loginStreak; }
    public void setLoginStreak(Integer loginStreak) { this.loginStreak = loginStreak; }
    
    public LocalDateTime getLastActiveDate() { return lastActiveDate; }
    public void setLastActiveDate(LocalDateTime lastActiveDate) { this.lastActiveDate = lastActiveDate; }
}
