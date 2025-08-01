// src/main/java/com/cosmic/astrology/exception/UserNotFoundException.java
package com.cosmic.astrology.exception;

/**
 * Custom exception for when a user is not found in the Cosmic Astrology system
 */
public class UserNotFoundException extends RuntimeException {
    
    private String username;
    private String userId;
    
    public UserNotFoundException(String message) {
        super(message);
    }
    
    public UserNotFoundException(String message, String username) {
        super(message);
        this.username = username;
    }
    
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public UserNotFoundException(String message, String username, Throwable cause) {
        super(message, cause);
        this.username = username;
    }
    
    // Static factory methods for convenience
    public static UserNotFoundException byUsername(String username) {
        return new UserNotFoundException("User not found with username: " + username, username);
    }
    
    public static UserNotFoundException byUserId(String userId) {
        UserNotFoundException ex = new UserNotFoundException("User not found with ID: " + userId);
        ex.userId = userId;
        return ex;
    }
    
    public static UserNotFoundException withMessage(String message) {
        return new UserNotFoundException(message);
    }
    
    // Getters
    public String getUsername() {
        return username;
    }
    
    public String getUserId() {
        return userId;
    }
    
    @Override
    public String toString() {
        return "UserNotFoundException{" +
                "message='" + getMessage() + '\'' +
                ", username='" + username + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
