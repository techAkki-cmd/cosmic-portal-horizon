package com.cosmic.astrology.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Password Reset Request DTO for resetting password with token
 */
@Schema(description = "Password reset request with token and new password")
public class PasswordResetRequest {
    
    @NotBlank(message = "Reset token is required")
    @Schema(description = "Password reset token received via email", example = "550e8400-e29b-41d4-a716-446655440000")
    private String token;
    
    @NotBlank(message = "New password is required")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    @Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
        message = "Password must contain at least 8 characters with uppercase, lowercase, number, and special character"
    )
    @Schema(description = "New password meeting security requirements", example = "NewSecurePass123!")
    private String newPassword;
    
    @NotBlank(message = "Password confirmation is required")
    @Schema(description = "Confirmation of new password", example = "NewSecurePass123!")
    private String confirmPassword;
    
    // Constructors
    public PasswordResetRequest() {}
    
    public PasswordResetRequest(String token, String newPassword, String confirmPassword) {
        this.token = token;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }
    
    // Getters and Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    
    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    
    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
    
    // Validation helper
    public boolean isPasswordMatching() {
        return newPassword != null && newPassword.equals(confirmPassword);
    }
    
    @Override
    public String toString() {
        return String.format("PasswordResetRequest{token='%s', passwordsMatch=%s}", 
                           token != null ? token.substring(0, Math.min(8, token.length())) + "..." : "null",
                           isPasswordMatching());
    }
}
