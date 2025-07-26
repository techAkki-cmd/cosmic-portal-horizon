package com.cosmic.astrology.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "birth_charts")
public class BirthChart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "birth_date_time", nullable = false)
    private LocalDateTime birthDateTime;
    
    @Column(name = "birth_location", nullable = false)
    private String birthLocation;
    
    @Column(name = "birth_latitude", nullable = false)
    private Double birthLatitude;
    
    @Column(name = "birth_longitude", nullable = false)
    private Double birthLongitude;
    
    @Column(name = "timezone", nullable = false)
    private String timezone;
    
    @Lob
    @Column(name = "chart_data", columnDefinition = "TEXT")
    private String chartData; // JSON string of planetary positions
    
    @Column(name = "house_system", length = 50)
    private String houseSystem = "PLACIDUS"; // Default house system
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    // Constructors
    public BirthChart() {}
    
    public BirthChart(User user, LocalDateTime birthDateTime, String birthLocation, 
                     Double birthLatitude, Double birthLongitude, String timezone) {
        this.user = user;
        this.birthDateTime = birthDateTime;
        this.birthLocation = birthLocation;
        this.birthLatitude = birthLatitude;
        this.birthLongitude = birthLongitude;
        this.timezone = timezone;
        this.createdAt = LocalDateTime.now();
    }
    
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
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
    
    public String getChartData() { return chartData; }
    public void setChartData(String chartData) { this.chartData = chartData; }
    
    public String getHouseSystem() { return houseSystem; }
    public void setHouseSystem(String houseSystem) { this.houseSystem = houseSystem; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}
