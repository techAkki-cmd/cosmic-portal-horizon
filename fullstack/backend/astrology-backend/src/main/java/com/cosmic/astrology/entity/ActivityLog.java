package com.cosmic.astrology.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "activity_logs", indexes = {
    @Index(name = "idx_activity_username", columnList = "username"),
    @Index(name = "idx_activity_timestamp", columnList = "timestamp"),
    @Index(name = "idx_activity_type", columnList = "activity_type"),
    @Index(name = "idx_activity_status", columnList = "status"),
    @Index(name = "idx_activity_user_timestamp", columnList = "username, timestamp"),
    @Index(name = "idx_activity_session", columnList = "session_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @NotBlank(message = "Username cannot be blank")
    @Size(max = 50, message = "Username cannot exceed 50 characters")
    @Column(name = "username", nullable = false, length = 50)
    private String username;
    
    @NotBlank(message = "Activity type cannot be blank")
    @Size(max = 50, message = "Activity type cannot exceed 50 characters")
    @Column(name = "activity_type", nullable = false, length = 50)
    private String activityType;
    
    @NotBlank(message = "Description cannot be blank")
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    @Column(name = "description", nullable = false, length = 1000)
    private String description;
    
    @Column(name = "timestamp", nullable = false)
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
    
    @Size(max = 45, message = "IP address cannot exceed 45 characters")
    @Column(name = "ip_address", length = 45)
    private String ipAddress;
    
    @Size(max = 20, message = "Device type cannot exceed 20 characters")
    @Column(name = "device_type", length = 20)
    private String deviceType;
    
    @Size(max = 20, message = "Status cannot exceed 20 characters")
    @Column(name = "status", length = 20, nullable = false)
    private String status = "SUCCESS";
    
    @Size(max = 100, message = "Session ID cannot exceed 100 characters")
    @Column(name = "session_id", length = 100)
    private String sessionId;
    
    @Size(max = 500, message = "User agent cannot exceed 500 characters")
    @Column(name = "user_agent", length = 500)
    private String userAgent;
    
    @Size(max = 200, message = "Location cannot exceed 200 characters")
    @Column(name = "location", length = 200)
    private String location;
    
    @Min(value = 0, message = "Duration cannot be negative")
    @Column(name = "duration_ms")
    private Long durationMs;
    
    @Size(max = 30, message = "Category cannot exceed 30 characters")
    @Column(name = "category", length = 30)
    private String category;
    
    @Size(max = 20, message = "Priority cannot exceed 20 characters")
    @Column(name = "priority", length = 20)
    private String priority = "NORMAL";
    
    @Size(max = 1000, message = "Error details cannot exceed 1000 characters")
    @Column(name = "error_details", length = 1000)
    private String errorDetails;
    
    @Column(name = "metadata", columnDefinition = "JSON")
    @Convert(converter = JsonConverter.class)
    private Map<String, Object> metadata = new HashMap<>();
    
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @Size(max = 50, message = "Module cannot exceed 50 characters")
    @Column(name = "module", length = 50)
    private String module;
    
    @Size(max = 50, message = "Action cannot exceed 50 characters")
    @Column(name = "action", length = 50)
    private String action;
    
    @Size(max = 100, message = "Resource cannot exceed 100 characters")
    @Column(name = "resource", length = 100)
    private String resource;
    
    @Column(name = "request_id", length = 100)
    private String requestId;
    
    @Column(name = "correlation_id", length = 100)
    private String correlationId;
    
    public ActivityLog(String username, String activityType, String description) {
        this.username = username;
        this.activityType = activityType;
        this.description = description;
        this.timestamp = LocalDateTime.now();
        this.status = "SUCCESS";
        this.priority = "NORMAL";
        this.metadata = new HashMap<>();
    }
    
    public ActivityLog(String username, String activityType, String description, String ipAddress, String deviceType) {
        this(username, activityType, description);
        this.ipAddress = ipAddress;
        this.deviceType = deviceType;
    }
    
    public ActivityLog(String username, String activityType, String description, String ipAddress, 
                       String deviceType, String sessionId) {
        this(username, activityType, description, ipAddress, deviceType);
        this.sessionId = sessionId;
    }
    
    public void addMetadata(String key, Object value) {
        if (this.metadata == null) {
            this.metadata = new HashMap<>();
        }
        this.metadata.put(key, value);
    }
    
    public Object getMetadata(String key) {
        return this.metadata != null ? this.metadata.get(key) : null;
    }
    
    public boolean isSuccessful() {
        return "SUCCESS".equalsIgnoreCase(this.status);
    }
    
    public boolean isFailed() {
        return "FAILED".equalsIgnoreCase(this.status) || "ERROR".equalsIgnoreCase(this.status);
    }
    
    public void setFailed(String errorMessage) {
        this.status = "FAILED";
        this.errorDetails = errorMessage;
    }
    
    public void setDurationFromStart(LocalDateTime startTime) {
        if (startTime != null && this.timestamp != null) {
            this.durationMs = java.time.Duration.between(startTime, this.timestamp).toMillis();
        }
    }
    
    public String getFormattedTimestamp() {
        return timestamp != null ? timestamp.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null;
    }
    
    public long getAgeInMinutes() {
        return timestamp != null ? 
            java.time.Duration.between(timestamp, LocalDateTime.now()).toMinutes() : 0;
    }
    
    public boolean isRecent() {
        return getAgeInMinutes() <= 60;
    }
    
    public static ActivityLogBuilder builder() {
        return new ActivityLogBuilder();
    }
    
    public static class ActivityLogBuilder {
        private final ActivityLog activityLog = new ActivityLog();
        
        public ActivityLogBuilder username(String username) {
            activityLog.setUsername(username);
            return this;
        }
        
        public ActivityLogBuilder activityType(String activityType) {
            activityLog.setActivityType(activityType);
            return this;
        }
        
        public ActivityLogBuilder description(String description) {
            activityLog.setDescription(description);
            return this;
        }
        
        public ActivityLogBuilder ipAddress(String ipAddress) {
            activityLog.setIpAddress(ipAddress);
            return this;
        }
        
        public ActivityLogBuilder deviceType(String deviceType) {
            activityLog.setDeviceType(deviceType);
            return this;
        }
        
        public ActivityLogBuilder sessionId(String sessionId) {
            activityLog.setSessionId(sessionId);
            return this;
        }
        
        public ActivityLogBuilder userAgent(String userAgent) {
            activityLog.setUserAgent(userAgent);
            return this;
        }
        
        public ActivityLogBuilder location(String location) {
            activityLog.setLocation(location);
            return this;
        }
        
        public ActivityLogBuilder status(String status) {
            activityLog.setStatus(status);
            return this;
        }
        
        public ActivityLogBuilder category(String category) {
            activityLog.setCategory(category);
            return this;
        }
        
        public ActivityLogBuilder priority(String priority) {
            activityLog.setPriority(priority);
            return this;
        }
        
        public ActivityLogBuilder module(String module) {
            activityLog.setModule(module);
            return this;
        }
        
        public ActivityLogBuilder action(String action) {
            activityLog.setAction(action);
            return this;
        }
        
        public ActivityLogBuilder resource(String resource) {
            activityLog.setResource(resource);
            return this;
        }
        
        public ActivityLogBuilder requestId(String requestId) {
            activityLog.setRequestId(requestId);
            return this;
        }
        
        public ActivityLogBuilder correlationId(String correlationId) {
            activityLog.setCorrelationId(correlationId);
            return this;
        }
        
        public ActivityLogBuilder metadata(Map<String, Object> metadata) {
            activityLog.setMetadata(metadata);
            return this;
        }
        
        public ActivityLogBuilder addMetadata(String key, Object value) {
            activityLog.addMetadata(key, value);
            return this;
        }
        
        public ActivityLog build() {
            if (activityLog.getTimestamp() == null) {
                activityLog.setTimestamp(LocalDateTime.now());
            }
            if (activityLog.getStatus() == null) {
                activityLog.setStatus("SUCCESS");
            }
            if (activityLog.getPriority() == null) {
                activityLog.setPriority("NORMAL");
            }
            return activityLog;
        }
    }
}
