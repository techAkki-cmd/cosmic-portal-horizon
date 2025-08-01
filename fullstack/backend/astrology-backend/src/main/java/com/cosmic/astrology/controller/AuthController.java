package com.cosmic.astrology.controller;

import com.cosmic.astrology.dto.*;
import com.cosmic.astrology.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Comprehensive Authentication Controller for Vedic Astrology Application
 * Handles user authentication, authorization, and account management
 */
@CrossOrigin(
    origins = {"http://localhost:3000", "http://localhost:8081", "https://yourdomain.com"}, 
    maxAge = 3600, 
    allowCredentials = "true",
    allowedHeaders = {"*"},
    methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}
)
@RestController
@RequestMapping("/api/auth")
@Validated
@Tag(name = "Authentication", description = "User authentication and account management endpoints")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    // ================ AUTHENTICATION ENDPOINTS ================
    
    /**
     * ✅ FIXED: Authenticate user with username/email and password
     * Returns JWT token, refresh token, and user info in proper structure for frontend
     */
    @PostMapping("/login")
    @Operation(summary = "User Login", 
               description = "Authenticate user with credentials and return JWT token")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login successful"),
        @ApiResponse(responseCode = "400", description = "Invalid credentials or validation errors"),
        @ApiResponse(responseCode = "401", description = "Authentication failed"),
        @ApiResponse(responseCode = "423", description = "Account locked due to multiple failed attempts"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> authenticateUser(
            @Valid @RequestBody LoginRequest loginRequest, 
            BindingResult bindingResult,
            HttpServletRequest request) {
        
        long startTime = System.currentTimeMillis();
        String clientIp = getClientIpAddress(request);
        String userAgent = request.getHeader("User-Agent");
        
        System.out.printf("🔐 Login attempt from IP: %s for username: %s%n", clientIp, loginRequest.getUsername());
        
        try {
            // Validate request
            if (bindingResult.hasErrors()) {
                Map<String, String> validationErrors = extractValidationErrors(bindingResult);
                System.out.printf("❌ Login validation errors for %s: %s%n", loginRequest.getUsername(), validationErrors);
                
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("VALIDATION_ERROR", "Invalid input data", validationErrors));
            }
            
            // Attempt authentication
            JwtResponse jwtResponse = authService.authenticateUser(loginRequest, clientIp, userAgent);
            
            // ✅ CRITICAL FIX: Ensure token exists and is valid
            if (jwtResponse == null || jwtResponse.getToken() == null || jwtResponse.getToken().trim().isEmpty()) {
                System.err.println("❌ Authentication succeeded but no token was generated for user: " + loginRequest.getUsername());
                throw new IllegalStateException("Authentication succeeded but no token was issued");
            }
            
            // ✅ FIXED: Extract and structure the response data properly for frontend
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("token", jwtResponse.getToken());
            responseData.put("refreshToken", jwtResponse.getRefreshToken());
            responseData.put("user", jwtResponse.getUser());
            
            // Add token metadata for debugging
            responseData.put("tokenType", "Bearer");
            responseData.put("expiresIn", jwtResponse.getExpiresIn()); // if available
            
            long processingTime = System.currentTimeMillis() - startTime;
            System.out.printf("✅ Login successful for %s from IP: %s (Processing time: %d ms)%n", 
                loginRequest.getUsername(), clientIp, processingTime);
            
            // Add security headers
            HttpHeaders headers = createSecurityHeaders();
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(createSuccessResponse("LOGIN_SUCCESS", "Authentication successful", responseData));
            
        } catch (SecurityException e) {
            System.out.printf("🔒 Security violation for %s from IP %s: %s%n", loginRequest.getUsername(), clientIp, e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(createErrorResponse("AUTHENTICATION_FAILED", e.getMessage(), null));
                    
        } catch (IllegalStateException e) {
            System.out.printf("🚫 Account locked for %s: %s%n", loginRequest.getUsername(), e.getMessage());
            return ResponseEntity.status(HttpStatus.LOCKED)
                    .body(createErrorResponse("ACCOUNT_LOCKED", e.getMessage(), null));
                    
        } catch (Exception e) {
            System.err.printf("❌ Login error for %s from IP %s: %s%n", loginRequest.getUsername(), clientIp, e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("LOGIN_ERROR", "Authentication service temporarily unavailable", null));
        }
    }
    
    /**
     * ✅ ENHANCED: Register new user account
     */
    @PostMapping("/signup")
    @Operation(summary = "User Registration", 
               description = "Register a new user account for the Vedic astrology platform")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Account created successfully"),
        @ApiResponse(responseCode = "400", description = "Validation errors or account exists"),
        @ApiResponse(responseCode = "409", description = "Username or email already exists"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> registerUser(
            @Valid @RequestBody SignupRequest signUpRequest, 
            BindingResult bindingResult,
            HttpServletRequest request) {
        
        String clientIp = getClientIpAddress(request);
        String userAgent = request.getHeader("User-Agent");
        
        System.out.printf("📝 Registration attempt from IP: %s for username: %s, email: %s%n", 
            clientIp, signUpRequest.getUsername(), signUpRequest.getEmail());
        
        try {
            // Validate request
            if (bindingResult.hasErrors()) {
                Map<String, String> validationErrors = extractValidationErrors(bindingResult);
                System.out.printf("❌ Registration validation errors: %s%n", validationErrors);
                
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("VALIDATION_ERROR", "Invalid registration data", validationErrors));
            }
            
            // Attempt registration
            MessageResponse response = authService.registerUser(signUpRequest, clientIp, userAgent);
            
            System.out.printf("✅ Registration successful for username: %s from IP: %s%n", 
                signUpRequest.getUsername(), clientIp);
            
            Map<String, Object> responseData = Map.of(
                "username", signUpRequest.getUsername(),
                "email", signUpRequest.getEmail(),
                "registrationTime", LocalDateTime.now()
            );
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .headers(createSecurityHeaders())
                    .body(createSuccessResponse("REGISTRATION_SUCCESS", response.getMessage(), responseData));
            
        } catch (IllegalArgumentException e) {
            System.out.printf("⚠️ Registration conflict for %s: %s%n", signUpRequest.getUsername(), e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(createErrorResponse("ACCOUNT_EXISTS", e.getMessage(), null));
                    
        } catch (Exception e) {
            System.err.printf("❌ Registration error for %s: %s%n", signUpRequest.getUsername(), e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("REGISTRATION_ERROR", "Registration service temporarily unavailable", null));
        }
    }
    
    /**
     * ✅ ENHANCED: Refresh JWT token
     */
    @PostMapping("/refresh")
    @Operation(summary = "Refresh JWT Token", 
               description = "Refresh an existing JWT token to extend session")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Token refreshed successfully"),
        @ApiResponse(responseCode = "401", description = "Invalid or expired token"),
        @ApiResponse(responseCode = "500", description = "Token refresh failed")
    })
    public ResponseEntity<?> refreshToken(
            @Parameter(description = "Authorization header with Bearer token") 
            @RequestHeader("Authorization") String authHeader,
            HttpServletRequest request) {
        
        String clientIp = getClientIpAddress(request);
        
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(createErrorResponse("INVALID_TOKEN", "Invalid authorization header format", null));
            }
            
            String token = authHeader.substring(7); // Remove "Bearer "
            System.out.printf("🔄 Token refresh request from IP: %s%n", clientIp);
            
            JwtResponse refreshedResponse = authService.refreshToken(token, clientIp);
            
            // ✅ FIXED: Structure refresh response properly
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("token", refreshedResponse.getToken());
            responseData.put("refreshToken", refreshedResponse.getRefreshToken());
            responseData.put("user", refreshedResponse.getUser());
            responseData.put("tokenType", "Bearer");
            
            System.out.printf("✅ Token refreshed successfully from IP: %s%n", clientIp);
            return ResponseEntity.ok()
                    .headers(createSecurityHeaders())
                    .body(createSuccessResponse("TOKEN_REFRESHED", "Token refreshed successfully", responseData));
            
        } catch (SecurityException e) {
            System.out.printf("🔒 Token refresh denied from IP %s: %s%n", clientIp, e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(createErrorResponse("TOKEN_INVALID", e.getMessage(), null));
                    
        } catch (Exception e) {
            System.err.printf("❌ Token refresh error from IP %s: %s%n", clientIp, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("REFRESH_ERROR", "Token refresh service temporarily unavailable", null));
        }
    }
    
    /**
     * Logout user and invalidate token
     */
    @PostMapping("/logout")
    @PreAuthorize("hasRole('USER') or hasRole('CLIENT') or hasRole('ASTROLOGER')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "User Logout", 
               description = "Logout user and invalidate current JWT token")
    public ResponseEntity<?> logoutUser(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            Principal principal,
            HttpServletRequest request) {
        
        String clientIp = getClientIpAddress(request);
        String username = principal != null ? principal.getName() : "anonymous";
        
        try {
            System.out.printf("🚪 Logout request from %s at IP: %s%n", username, clientIp);
            
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                authService.invalidateToken(token, username, clientIp);
            }
            
            System.out.printf("✅ Logout successful for %s from IP: %s%n", username, clientIp);
            
            Map<String, Object> responseData = Map.of(
                "username", username, 
                "logoutTime", LocalDateTime.now()
            );
            
            return ResponseEntity.ok()
                    .headers(createSecurityHeaders())
                    .body(createSuccessResponse("LOGOUT_SUCCESS", "User logged out successfully", responseData));
            
        } catch (Exception e) {
            System.err.printf("❌ Logout error for %s: %s%n", username, e.getMessage());
            return ResponseEntity.ok()
                    .body(createSuccessResponse("LOGOUT_PARTIAL", "Session ended (with warnings)", null));
        }
    }
    
    // ================ ACCOUNT MANAGEMENT ENDPOINTS ================
    
    /**
     * Request password reset
     */
    @PostMapping("/forgot-password")
    @Operation(summary = "Forgot Password", 
               description = "Request password reset link via email")
    public ResponseEntity<?> forgotPassword(
            @Valid @RequestBody @Parameter(description = "User email address") 
            Map<String, @Email @NotBlank String> request,
            HttpServletRequest httpRequest) {
        
        String email = request.get("email");
        String clientIp = getClientIpAddress(httpRequest);
        
        try {
            System.out.printf("🔑 Password reset requested for email: %s from IP: %s%n", email, clientIp);
            
            MessageResponse response = authService.initiatePasswordReset(email, clientIp);
            
            System.out.printf("✅ Password reset email sent to: %s%n", email);
            
            Map<String, Object> responseData = Map.of(
                "email", email, 
                "requestTime", LocalDateTime.now()
            );
            
            return ResponseEntity.ok()
                    .body(createSuccessResponse("RESET_INITIATED", response.getMessage(), responseData));
            
        } catch (Exception e) {
            System.err.printf("❌ Password reset error for %s: %s%n", email, e.getMessage());
            // Always return success for security (don't reveal if email exists)
            return ResponseEntity.ok()
                    .body(createSuccessResponse("RESET_PROCESSED", 
                          "If the email exists, a reset link has been sent", null));
        }
    }
    
    /**
     * Reset password with token
     */
    @PostMapping("/reset-password")
    @Operation(summary = "Reset Password", 
               description = "Reset password using reset token")
    public ResponseEntity<?> resetPassword(
            @Valid @RequestBody PasswordResetRequest resetRequest,
            BindingResult bindingResult,
            HttpServletRequest request) {
        
        String clientIp = getClientIpAddress(request);
        
        try {
            if (bindingResult.hasErrors()) {
                Map<String, String> validationErrors = extractValidationErrors(bindingResult);
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("VALIDATION_ERROR", "Invalid reset data", validationErrors));
            }
            
            System.out.printf("🔐 Password reset attempt from IP: %s%n", clientIp);
            
            MessageResponse response = authService.resetPassword(resetRequest, clientIp);
            
            System.out.printf("✅ Password reset successful from IP: %s%n", clientIp);
            
            return ResponseEntity.ok()
                    .body(createSuccessResponse("PASSWORD_RESET", response.getMessage(), null));
            
        } catch (SecurityException e) {
            System.out.printf("🔒 Invalid password reset attempt from IP %s: %s%n", clientIp, e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(createErrorResponse("INVALID_TOKEN", e.getMessage(), null));
                    
        } catch (Exception e) {
            System.err.printf("❌ Password reset error from IP %s: %s%n", clientIp, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("RESET_ERROR", "Password reset service temporarily unavailable", null));
        }
    }
    
    /**
     * Verify email address
     */
    @PostMapping("/verify-email")
    @Operation(summary = "Verify Email", 
               description = "Verify user email address with verification token")
    public ResponseEntity<?> verifyEmail(
            @RequestParam String token,
            HttpServletRequest request) {
        
        String clientIp = getClientIpAddress(request);
        
        try {
            System.out.printf("📧 Email verification attempt from IP: %s%n", clientIp);
            
            MessageResponse response = authService.verifyEmail(token, clientIp);
            
            System.out.printf("✅ Email verification successful from IP: %s%n", clientIp);
            
            return ResponseEntity.ok()
                    .body(createSuccessResponse("EMAIL_VERIFIED", response.getMessage(), null));
            
        } catch (SecurityException e) {
            System.out.printf("🔒 Invalid email verification from IP %s: %s%n", clientIp, e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(createErrorResponse("INVALID_TOKEN", e.getMessage(), null));
                    
        } catch (Exception e) {
            System.err.printf("❌ Email verification error from IP %s: %s%n", clientIp, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("VERIFICATION_ERROR", "Email verification service temporarily unavailable", null));
        }
    }
    
    /**
     * Change password for authenticated user
     */
    @PostMapping("/change-password")
    @PreAuthorize("hasRole('USER') or hasRole('CLIENT') or hasRole('ASTROLOGER')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Change Password", 
               description = "Change password for authenticated user")
    public ResponseEntity<?> changePassword(
            @Valid @RequestBody ChangePasswordRequest changeRequest,
            BindingResult bindingResult,
            Principal principal,
            HttpServletRequest request) {
        
        String username = principal.getName();
        String clientIp = getClientIpAddress(request);
        
        try {
            if (bindingResult.hasErrors()) {
                Map<String, String> validationErrors = extractValidationErrors(bindingResult);
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("VALIDATION_ERROR", "Invalid password data", validationErrors));
            }
            
            System.out.printf("🔐 Password change request from %s at IP: %s%n", username, clientIp);
            
            MessageResponse response = authService.changePassword(username, changeRequest, clientIp);
            
            System.out.printf("✅ Password changed successfully for %s%n", username);
            
            return ResponseEntity.ok()
                    .body(createSuccessResponse("PASSWORD_CHANGED", response.getMessage(), null));
            
        } catch (SecurityException e) {
            System.out.printf("🔒 Password change denied for %s: %s%n", username, e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(createErrorResponse("INVALID_CREDENTIALS", e.getMessage(), null));
                    
        } catch (Exception e) {
            System.err.printf("❌ Password change error for %s: %s%n", username, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("CHANGE_ERROR", "Password change service temporarily unavailable", null));
        }
    }
    
    // ================ UTILITY ENDPOINTS ================
    
    /**
     * Validate token endpoint
     */
    @PostMapping("/validate")
    @Operation(summary = "Validate Token", 
               description = "Validate JWT token and return user info")
    public ResponseEntity<?> validateToken(
            @RequestHeader("Authorization") String authHeader,
            HttpServletRequest request) {
        
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(createErrorResponse("INVALID_TOKEN", "Invalid authorization header", null));
            }
            
            String token = authHeader.substring(7);
            Map<String, Object> validation = authService.validateToken(token);
            
            return ResponseEntity.ok()
                    .body(createSuccessResponse("TOKEN_VALID", "Token is valid", validation));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(createErrorResponse("TOKEN_INVALID", "Token validation failed", null));
        }
    }
    
    /**
     * Get current user info
     */
    @GetMapping("/me")
    @PreAuthorize("hasRole('USER') or hasRole('CLIENT') or hasRole('ASTROLOGER')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get Current User", 
               description = "Get current authenticated user information")
    public ResponseEntity<?> getCurrentUser(Principal principal) {
        try {
            String username = principal.getName();
            Map<String, Object> userInfo = authService.getCurrentUserInfo(username);
            
            return ResponseEntity.ok()
                    .body(createSuccessResponse("USER_INFO", "User information retrieved", userInfo));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("USER_INFO_ERROR", "Unable to retrieve user information", null));
        }
    }
    
    /**
     * Authentication health check
     */
    @GetMapping("/health")
    @Operation(summary = "Auth Health Check", description = "Check authentication service health")
    public ResponseEntity<?> healthCheck() {
        try {
            Map<String, Object> health = new HashMap<>();
            health.put("status", "healthy");
            health.put("service", "Authentication Service");
            health.put("timestamp", LocalDateTime.now());
            health.put("version", "2.0.0");
            health.put("features", List.of("JWT Authentication", "Token Refresh", "Password Reset", 
                "Email Verification", "Rate Limiting", "Account Management"));
            
            return ResponseEntity.ok(health);
        } catch (Exception e) {
            Map<String, Object> health = new HashMap<>();
            health.put("status", "unhealthy");
            health.put("error", e.getMessage());
            health.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(health);
        }
    }
    
    // ================ PRIVATE HELPER METHODS ================
    
    /**
     * ✅ ENHANCED: Extract validation errors from BindingResult
     */
    private Map<String, String> extractValidationErrors(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .collect(Collectors.toMap(
                    FieldError::getField,
                    error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "Invalid value",
                    (existing, replacement) -> existing // Keep first error for duplicate fields
                ));
    }
    
    /**
     * ✅ ENHANCED: Create standardized success response
     */
    private Map<String, Object> createSuccessResponse(String code, String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("code", code);
        response.put("message", message);
        response.put("timestamp", LocalDateTime.now());
        if (data != null) {
            response.put("data", data);
        }
        return response;
    }
    
    /**
     * ✅ ENHANCED: Create standardized error response
     */
    private Map<String, Object> createErrorResponse(String code, String message, Object errors) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("code", code);
        response.put("message", message);
        response.put("timestamp", LocalDateTime.now());
        if (errors != null) {
            response.put("errors", errors);
        }
        return response;
    }
    
    /**
     * ✅ ENHANCED: Create comprehensive security headers
     */
    private HttpHeaders createSecurityHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Content-Type-Options", "nosniff");
        headers.add("X-Frame-Options", "DENY");
        headers.add("X-XSS-Protection", "1; mode=block");
        headers.add("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Referrer-Policy", "strict-origin-when-cross-origin");
        return headers;
    }
    
    /**
     * ✅ ENHANCED: Extract client IP address with comprehensive proxy support
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String[] headerNames = {
            "X-Forwarded-For",
            "Proxy-Client-IP", 
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
        };
        
        for (String header : headerNames) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                // Handle comma-separated IPs (X-Forwarded-For can contain multiple IPs)
                return ip.split(",")[0].trim();
            }
        }
        
        return request.getRemoteAddr();
    }
}
