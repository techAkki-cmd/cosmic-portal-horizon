package com.cosmic.astrology.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Change Password Request DTO for authenticated users changing their password
 */
@Schema(description = "Password change request for authenticated users")
public class ChangePasswordRequest {
    
    @NotBlank(message = "Current password is required")
    @Schema(description = "User's current password", example = "CurrentPass123!")
    private String currentPassword;
    
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
    
    // Optional: reason for password change
    @Size(max = 200, message = "Reason cannot exceed 200 characters")
    @Schema(description = "Optional reason for password change", example = "Security update")
    private String reason;
    
    // Constructors
    public ChangePasswordRequest() {}
    
    public ChangePasswordRequest(String currentPassword, String newPassword, String confirmPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }
    
    public ChangePasswordRequest(String currentPassword, String newPassword, String confirmPassword, String reason) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
        this.reason = reason;
    }
    
    // Getters and Setters
    public String getCurrentPassword() { return currentPassword; }
    public void setCurrentPassword(String currentPassword) { this.currentPassword = currentPassword; }
    
    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    
    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
    
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    
    // Validation helpers
    public boolean isPasswordMatching() {
        return newPassword != null && newPassword.equals(confirmPassword);
    }
    
    public boolean isNewPasswordDifferent() {
        return currentPassword != null && newPassword != null && !currentPassword.equals(newPassword);
    }
    
    public boolean isValid() {
        return isPasswordMatching() && isNewPasswordDifferent();
    }
    
    @Override
    public String toString() {
        return String.format("ChangePasswordRequest{passwordsMatch=%s, newPasswordDifferent=%s, reason='%s'}", 
                           isPasswordMatching(), isNewPasswordDifferent(), reason);
    }
}
