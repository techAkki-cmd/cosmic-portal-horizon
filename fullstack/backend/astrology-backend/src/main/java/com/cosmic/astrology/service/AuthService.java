package com.cosmic.astrology.service;

import com.cosmic.astrology.dto.*;
import com.cosmic.astrology.entity.User;
import com.cosmic.astrology.entity.UserRole;
import com.cosmic.astrology.repository.UserRepository;
import com.cosmic.astrology.security.JwtUtils;
import com.cosmic.astrology.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * Comprehensive Authentication Service for Vedic Astrology Application
 * Handles all authentication, authorization, and security-related operations
 */
@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired(required = false)
    private JavaMailSender emailSender;

    // Security configuration
    @Value("${app.security.maxLoginAttempts:5}")
    private int maxLoginAttempts;

    @Value("${app.security.lockoutDurationMinutes:30}")
    private int lockoutDurationMinutes;

    @Value("${app.security.passwordResetExpiryHours:24}")
    private int passwordResetExpiryHours;

    @Value("${app.security.emailVerificationExpiryHours:48}")
    private int emailVerificationExpiryHours;

    @Value("${app.mail.from:noreply@cosmic-astrology.com}")
    private String fromEmail;

    @Value("${app.frontend.url:http://localhost:3000}")
    private String frontendUrl;

    // In-memory stores (in production, use Redis or database)
    private final Map<String, Integer> loginAttempts = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> accountLockouts = new ConcurrentHashMap<>();
    private final Set<String> invalidatedTokens = ConcurrentHashMap.newKeySet();
    private final Map<String, String> passwordResetTokens = new ConcurrentHashMap<>();
    private final Map<String, String> emailVerificationTokens = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> tokenExpiryTimes = new ConcurrentHashMap<>();

    // Password validation pattern
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"
    );

    // ================ INITIALIZATION ================

    @PostConstruct
    public void initializeService() {
        createTestUser();
        cleanupExpiredTokens();
        System.out.println("🔐 AuthService initialized with security features:");
        System.out.println("   - Max login attempts: " + maxLoginAttempts);
        System.out.println("   - Lockout duration: " + lockoutDurationMinutes + " minutes");
        System.out.println("   - Password reset expiry: " + passwordResetExpiryHours + " hours");
        System.out.println("   - Email verification expiry: " + emailVerificationExpiryHours + " hours");
    }

    private void createTestUser() {
        try {
            if (!userRepository.existsByUsername("testuser")) {
                User testUser = new User();
                testUser.setUsername("testuser");
                testUser.setEmail("test@cosmic-astrology.com");
                testUser.setPassword(encoder.encode("TestPass123!"));
                testUser.setFirstName("Test");
                testUser.setLastName("User");
                testUser.setRole(UserRole.CLIENT);
                testUser.setEnabled(true);
                testUser.setEmailVerified(true);
                testUser.setCreatedAt(LocalDateTime.now());
                testUser.setLoginStreak(0);
                testUser.setChartsGenerated(0);
                
                userRepository.save(testUser);
                System.out.println("✅ Test user created successfully:");
                System.out.println("   Username: testuser");
                System.out.println("   Password: TestPass123!");
                System.out.println("   Email: test@cosmic-astrology.com");
            } else {
                System.out.println("ℹ️ Test user already exists");
                
                User existingUser = userRepository.findByUsername("testuser").orElse(null);
                if (existingUser != null) {
                    System.out.println("   Existing user verified:");
                    System.out.println("   - Username: " + existingUser.getUsername());
                    System.out.println("   - Email: " + existingUser.getEmail());
                    System.out.println("   - Enabled: " + existingUser.isEnabled());
                    System.out.println("   - Role: " + existingUser.getRole());
                    System.out.println("   - Email Verified: " + existingUser.isEmailVerified());
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Failed to create/verify test user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ================ AUTHENTICATION METHODS ================

    /**
     * Authenticate user with enhanced security features
     */
    public JwtResponse authenticateUser(LoginRequest loginRequest, String clientIp, String userAgent) {
        String username = loginRequest.getUsername().toLowerCase().trim();
        
        System.out.println("🔐 Authentication attempt for: " + username + " from IP: " + clientIp);
        
        try {
            // Check account lockout
            checkAccountLockout(username, clientIp);
            
            // Find user
            User user = userRepository.findByUsername(username)
                    .or(() -> userRepository.findByEmail(username))
                    .orElseThrow(() -> {
                        recordFailedLoginAttempt(username, clientIp);
                        System.out.println("❌ User not found: " + username);
                        return new SecurityException("Invalid username or password");
                    });
            
            // Check if account is enabled
            if (!user.isEnabled()) {
                System.out.println("❌ Account disabled: " + username);
                throw new DisabledException("Account is disabled");
            }

            // Authenticate
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), loginRequest.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // Clear failed attempts on successful login
            clearFailedLoginAttempts(username);
            
            // Generate JWT token
            String jwt = jwtUtils.generateJwtToken(user.getUsername());
            
            // Update user login information
            updateUserLoginInfo(user, clientIp, userAgent);
            
            System.out.println("✅ Authentication successful for: " + username + " from IP: " + clientIp);
            
            return new JwtResponse(
                jwt,
                user.getUsername(),
                user.getEmail(),
                "Bearer",
                user.getRole().name(),
                user.getFirstName(),
                user.getLastName(),
                user.isEmailVerified()
            );
            
        } catch (BadCredentialsException e) {
            recordFailedLoginAttempt(username, clientIp);
            System.out.println("❌ Bad credentials for: " + username + " from IP: " + clientIp);
            throw new SecurityException("Invalid username or password");
            
        } catch (DisabledException e) {
            System.out.println("❌ Account disabled: " + username);
            throw new SecurityException("Account is disabled. Please contact support.");
            
        } catch (LockedException e) {
            System.out.println("❌ Account locked: " + username);
            throw new IllegalStateException("Account is temporarily locked due to multiple failed login attempts");
            
        } catch (Exception e) {
            recordFailedLoginAttempt(username, clientIp);
            System.err.println("❌ Authentication error for " + username + ": " + e.getMessage());
            throw new SecurityException("Authentication failed");
        }
    }

    /**
     * Register new user with comprehensive validation
     */
    public MessageResponse registerUser(SignupRequest signUpRequest, String clientIp, String userAgent) {
        System.out.println("📝 Registration attempt for username: " + signUpRequest.getUsername() + 
                          ", email: " + signUpRequest.getEmail() + " from IP: " + clientIp);
        
        try {
            // Comprehensive validation
            validateRegistrationRequest(signUpRequest);
            
            String username = signUpRequest.getUsername().toLowerCase().trim();
            String email = signUpRequest.getEmail().toLowerCase().trim();
            
            // Check for existing users
            if (userRepository.existsByUsername(username)) {
                System.out.println("❌ Username already exists: " + username);
                throw new IllegalArgumentException("Username is already taken");
            }

            if (userRepository.existsByEmail(email)) {
                System.out.println("❌ Email already exists: " + email);
                throw new IllegalArgumentException("Email is already registered");
            }

            // Validate password strength
            if (!isPasswordStrong(signUpRequest.getPassword())) {
                throw new IllegalArgumentException("Password must be at least 8 characters long and contain uppercase, lowercase, number, and special character");
            }

            // Create new user
            User user = createNewUser(signUpRequest);
            User savedUser = userRepository.save(user);
            
            // Generate email verification token if email service is available
            if (emailSender != null) {
                String verificationToken = generateEmailVerificationToken(savedUser.getEmail());
                sendEmailVerification(savedUser.getEmail(), verificationToken);
                System.out.println("📧 Email verification sent to: " + savedUser.getEmail());
            }

            System.out.println("✅ User registered successfully: " + savedUser.getUsername() + 
                             " (ID: " + savedUser.getId() + ") from IP: " + clientIp);

            return new MessageResponse("User registered successfully! " + 
                (emailSender != null ? "Please check your email for verification." : ""));
            
        } catch (IllegalArgumentException e) {
            System.out.println("⚠️ Registration validation error: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("❌ Registration failed: " + e.getMessage());
            throw new RuntimeException("Registration failed. Please try again.");
        }
    }

    /**
     * Refresh JWT token with security checks
     */
    public JwtResponse refreshToken(String token, String clientIp) {
        try {
            // Check if token is invalidated
            if (invalidatedTokens.contains(token)) {
                throw new SecurityException("Token has been invalidated");
            }
            
            if (!jwtUtils.validateJwtToken(token)) {
                throw new SecurityException("Invalid or expired token");
            }
            
            String username = jwtUtils.getUserNameFromJwtToken(token);
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new SecurityException("User not found"));
            
            if (!user.isEnabled()) {
                throw new SecurityException("Account is disabled");
            }
            
            // Generate new token
            String newToken = jwtUtils.generateJwtToken(username);
            
            // Invalidate old token
            invalidatedTokens.add(token);
            
            System.out.println("✅ Token refreshed for user: " + username + " from IP: " + clientIp);
            
            return new JwtResponse(
                newToken,
                user.getUsername(),
                user.getEmail(),
                "Bearer",
                user.getRole().name(),
                user.getFirstName(),
                user.getLastName(),
                user.isEmailVerified()
            );
            
        } catch (Exception e) {
            System.err.println("❌ Token refresh failed from IP " + clientIp + ": " + e.getMessage());
            throw new SecurityException("Token refresh failed");
        }
    }

    /**
     * Invalidate JWT token on logout
     */
    public void invalidateToken(String token, String username, String clientIp) {
        try {
            invalidatedTokens.add(token);
            
            // Update user last logout time
            userRepository.findByUsername(username).ifPresent(user -> {
                user.setLastLogout(LocalDateTime.now());
                userRepository.save(user);
            });
            
            System.out.println("🚪 Token invalidated for user: " + username + " from IP: " + clientIp);
            
        } catch (Exception e) {
            System.err.println("⚠️ Error invalidating token for " + username + ": " + e.getMessage());
        }
    }

    // ================ PASSWORD MANAGEMENT ================

    /**
     * Initiate password reset process
     */
    public MessageResponse initiatePasswordReset(String email, String clientIp) {
        try {
            email = email.toLowerCase().trim();
            
            User user = userRepository.findByEmail(email).orElse(null);
            
            // Don't reveal if email exists for security
            if (user == null) {
                System.out.println("⚠️ Password reset attempted for non-existent email: " + email + " from IP: " + clientIp);
                return new MessageResponse("If the email exists, a reset link has been sent");
            }
            
            // Generate reset token
            String resetToken = generatePasswordResetToken(email);
            
            // Send reset email if email service is available
            if (emailSender != null) {
                sendPasswordResetEmail(email, resetToken);
                System.out.println("📧 Password reset email sent to: " + email + " from request IP: " + clientIp);
            }
            
            return new MessageResponse("Password reset instructions have been sent to your email");
            
        } catch (Exception e) {
            System.err.println("❌ Password reset error for email " + email + ": " + e.getMessage());
            return new MessageResponse("Password reset service is temporarily unavailable");
        }
    }

    /**
     * Reset password with token
     */
    public MessageResponse resetPassword(PasswordResetRequest resetRequest, String clientIp) {
        try {
            String email = passwordResetTokens.get(resetRequest.getToken());
            
            if (email == null) {
                throw new SecurityException("Invalid or expired reset token");
            }
            
            // Check token expiry
            LocalDateTime expiryTime = tokenExpiryTimes.get(resetRequest.getToken());
            if (expiryTime == null || LocalDateTime.now().isAfter(expiryTime)) {
                passwordResetTokens.remove(resetRequest.getToken());
                tokenExpiryTimes.remove(resetRequest.getToken());
                throw new SecurityException("Reset token has expired");
            }
            
            // Validate new password
            if (!isPasswordStrong(resetRequest.getNewPassword())) {
                throw new IllegalArgumentException("Password must be at least 8 characters long and contain uppercase, lowercase, number, and special character");
            }
            
            // Update password
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new SecurityException("User not found"));
            
            user.setPassword(encoder.encode(resetRequest.getNewPassword()));
            user.setPasswordChangedAt(LocalDateTime.now());
            userRepository.save(user);
            
            // Clean up tokens
            passwordResetTokens.remove(resetRequest.getToken());
            tokenExpiryTimes.remove(resetRequest.getToken());
            
            System.out.println("✅ Password reset successful for email: " + email + " from IP: " + clientIp);
            
            return new MessageResponse("Password has been reset successfully");
            
        } catch (Exception e) {
            System.err.println("❌ Password reset failed from IP " + clientIp + ": " + e.getMessage());
            throw e;
        }
    }

    /**
     * Change password for authenticated user
     */
    public MessageResponse changePassword(String username, ChangePasswordRequest changeRequest, String clientIp) {
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new SecurityException("User not found"));
            
            // Verify current password
            if (!encoder.matches(changeRequest.getCurrentPassword(), user.getPassword())) {
                throw new SecurityException("Current password is incorrect");
            }
            
            // Validate new password
            if (!isPasswordStrong(changeRequest.getNewPassword())) {
                throw new IllegalArgumentException("Password must be at least 8 characters long and contain uppercase, lowercase, number, and special character");
            }
            
            // Check if new password is different from current
            if (encoder.matches(changeRequest.getNewPassword(), user.getPassword())) {
                throw new IllegalArgumentException("New password must be different from current password");
            }
            
            // Update password
            user.setPassword(encoder.encode(changeRequest.getNewPassword()));
            user.setPasswordChangedAt(LocalDateTime.now());
            userRepository.save(user);
            
            System.out.println("✅ Password changed successfully for user: " + username + " from IP: " + clientIp);
            
            return new MessageResponse("Password changed successfully");
            
        } catch (Exception e) {
            System.err.println("❌ Password change failed for " + username + " from IP " + clientIp + ": " + e.getMessage());
            throw e;
        }
    }

    // ================ EMAIL VERIFICATION ================

    /**
     * Verify email address with token
     */
    public MessageResponse verifyEmail(String token, String clientIp) {
        try {
            String email = emailVerificationTokens.get(token);
            
            if (email == null) {
                throw new SecurityException("Invalid or expired verification token");
            }
            
            // Check token expiry
            LocalDateTime expiryTime = tokenExpiryTimes.get(token);
            if (expiryTime == null || LocalDateTime.now().isAfter(expiryTime)) {
                emailVerificationTokens.remove(token);
                tokenExpiryTimes.remove(token);
                throw new SecurityException("Verification token has expired");
            }
            
            // Update user email verification status
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new SecurityException("User not found"));
            
            user.setEmailVerified(true);
            user.setEmailVerifiedAt(LocalDateTime.now());
            userRepository.save(user);
            
            // Clean up tokens
            emailVerificationTokens.remove(token);
            tokenExpiryTimes.remove(token);
            
            System.out.println("✅ Email verified successfully for: " + email + " from IP: " + clientIp);
            
            return new MessageResponse("Email verified successfully");
            
        } catch (Exception e) {
            System.err.println("❌ Email verification failed from IP " + clientIp + ": " + e.getMessage());
            throw e;
        }
    }

    // ================ TOKEN VALIDATION ================

    /**
     * Validate JWT token and return user info
     */
    public Map<String, Object> validateToken(String token) {
        try {
            if (invalidatedTokens.contains(token)) {
                throw new SecurityException("Token has been invalidated");
            }
            
            if (!jwtUtils.validateJwtToken(token)) {
                throw new SecurityException("Invalid or expired token");
            }
            
            String username = jwtUtils.getUserNameFromJwtToken(token);
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new SecurityException("User not found"));
            
            Map<String, Object> validation = new HashMap<>();
            validation.put("valid", true);
            validation.put("username", user.getUsername());
            validation.put("email", user.getEmail());
            validation.put("role", user.getRole().name());
            validation.put("emailVerified", user.isEmailVerified());
            validation.put("tokenExpiry", jwtUtils.getExpirationDateFromJwtToken(token));
            
            return validation;
            
        } catch (Exception e) {
            Map<String, Object> validation = new HashMap<>();
            validation.put("valid", false);
            validation.put("error", e.getMessage());
            return validation;
        }
    }

    /**
     * Get current user information
     */
    public Map<String, Object> getCurrentUserInfo(String username) {
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", user.getId());
            userInfo.put("username", user.getUsername());
            userInfo.put("email", user.getEmail());
            userInfo.put("firstName", user.getFirstName());
            userInfo.put("lastName", user.getLastName());
            userInfo.put("role", user.getRole().name());
            userInfo.put("enabled", user.isEnabled());
            userInfo.put("emailVerified", user.isEmailVerified());
            userInfo.put("lastLogin", user.getLastLogin());
            userInfo.put("createdAt", user.getCreatedAt());
            userInfo.put("chartsGenerated", user.getChartsGenerated());
            userInfo.put("loginStreak", user.getLoginStreak());
            userInfo.put("hasBirthData", user.getBirthDateTime() != null && 
                                        user.getBirthLatitude() != null && 
                                        user.getBirthLongitude() != null);
            
            return userInfo;
            
        } catch (Exception e) {
            throw new RuntimeException("Unable to retrieve user information");
        }
    }

    // ================ PRIVATE HELPER METHODS ================

    private void validateRegistrationRequest(SignupRequest request) {
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
        if (request.getFirstName() == null || request.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        
        // Validate email format
        if (!isValidEmail(request.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }
        
        // Validate username format
        if (!isValidUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username must be 3-20 characters long and contain only letters, numbers, and underscores");
        }
    }

    private User createNewUser(SignupRequest request) {
        User user = new User();
        user.setUsername(request.getUsername().toLowerCase().trim());
        user.setEmail(request.getEmail().toLowerCase().trim());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName().trim());
        user.setLastName(request.getLastName() != null ? request.getLastName().trim() : "");
        user.setBirthDateTime(request.getBirthDateTime());
        user.setBirthLocation(request.getBirthLocation());
        user.setBirthLatitude(request.getBirthLatitude());
        user.setBirthLongitude(request.getBirthLongitude());
        user.setTimezone(request.getTimezone());
        user.setRole(UserRole.CLIENT);
        user.setEnabled(true);
        user.setEmailVerified(emailSender == null); // Auto-verify if no email service
        user.setCreatedAt(LocalDateTime.now());
        user.setLoginStreak(0);
        user.setChartsGenerated(0);
        
        return user;
    }

    private void checkAccountLockout(String username, String clientIp) {
        LocalDateTime lockoutTime = accountLockouts.get(username);
        if (lockoutTime != null) {
            LocalDateTime unlockTime = lockoutTime.plusMinutes(lockoutDurationMinutes);
            if (LocalDateTime.now().isBefore(unlockTime)) {
                long minutesRemaining = ChronoUnit.MINUTES.between(LocalDateTime.now(), unlockTime);
                System.out.println("🔒 Account locked for " + username + " from IP: " + clientIp + 
                                 ", unlock in " + minutesRemaining + " minutes");
                throw new LockedException("Account is locked for " + minutesRemaining + " more minutes");
            } else {
                // Lockout period expired, remove lockout
                accountLockouts.remove(username);
                loginAttempts.remove(username);
            }
        }
    }

    private void recordFailedLoginAttempt(String username, String clientIp) {
        int attempts = loginAttempts.getOrDefault(username, 0) + 1;
        loginAttempts.put(username, attempts);
        
        System.out.println("⚠️ Failed login attempt " + attempts + "/" + maxLoginAttempts + 
                          " for " + username + " from IP: " + clientIp);
        
        if (attempts >= maxLoginAttempts) {
            accountLockouts.put(username, LocalDateTime.now());
            System.out.println("🔒 Account locked for " + username + " due to " + maxLoginAttempts + 
                             " failed attempts from IP: " + clientIp);
        }
    }

    private void clearFailedLoginAttempts(String username) {
        loginAttempts.remove(username);
        accountLockouts.remove(username);
    }

    private void updateUserLoginInfo(User user, String clientIp, String userAgent) {
        user.setLastLogin(LocalDateTime.now());
        user.setLastActiveDate(LocalDateTime.now());
        user.setLastLoginIp(clientIp);
        
        // Update login streak
        Integer currentStreak = user.getLoginStreak();
        user.setLoginStreak(currentStreak != null ? currentStreak + 1 : 1);
        
        userRepository.save(user);
    }

    private String generatePasswordResetToken(String email) {
        String token = UUID.randomUUID().toString();
        passwordResetTokens.put(token, email);
        tokenExpiryTimes.put(token, LocalDateTime.now().plusHours(passwordResetExpiryHours));
        return token;
    }

    private String generateEmailVerificationToken(String email) {
        String token = UUID.randomUUID().toString();
        emailVerificationTokens.put(token, email);
        tokenExpiryTimes.put(token, LocalDateTime.now().plusHours(emailVerificationExpiryHours));
        return token;
    }

    private void sendPasswordResetEmail(String email, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(email);
            message.setSubject("🔐 Reset Your Cosmic Astrology Password");
            message.setText("Hello,\n\n" +
                "You requested to reset your password for your Cosmic Astrology account.\n\n" +
                "Click the link below to reset your password:\n" +
                frontendUrl + "/reset-password?token=" + token + "\n\n" +
                "This link will expire in " + passwordResetExpiryHours + " hours.\n\n" +
                "If you didn't request this reset, please ignore this email.\n\n" +
                "Best regards,\n" +
                "Cosmic Astrology Team 🕉️");
            
            emailSender.send(message);
        } catch (Exception e) {
            System.err.println("❌ Failed to send password reset email: " + e.getMessage());
        }
    }

    private void sendEmailVerification(String email, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(email);
            message.setSubject("📧 Verify Your Cosmic Astrology Email");
            message.setText("Welcome to Cosmic Astrology! 🕉️\n\n" +
                "Please verify your email address by clicking the link below:\n" +
                frontendUrl + "/verify-email?token=" + token + "\n\n" +
                "This link will expire in " + emailVerificationExpiryHours + " hours.\n\n" +
                "Once verified, you can access your personalized Vedic birth chart and cosmic insights.\n\n" +
                "Namaste,\n" +
                "Cosmic Astrology Team");
            
            emailSender.send(message);
        } catch (Exception e) {
            System.err.println("❌ Failed to send verification email: " + e.getMessage());
        }
    }

    private boolean isPasswordStrong(String password) {
        return password != null && PASSWORD_PATTERN.matcher(password).matches();
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    private boolean isValidUsername(String username) {
        return username != null && username.matches("^[a-zA-Z0-9_]{3,20}$");
    }

    private void cleanupExpiredTokens() {
        // Schedule periodic cleanup of expired tokens
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                LocalDateTime now = LocalDateTime.now();
                tokenExpiryTimes.entrySet().removeIf(entry -> {
                    if (now.isAfter(entry.getValue())) {
                        passwordResetTokens.remove(entry.getKey());
                        emailVerificationTokens.remove(entry.getKey());
                        return true;
                    }
                    return false;
                });
                
                // Clean up old lockouts
                accountLockouts.entrySet().removeIf(entry -> 
                    now.isAfter(entry.getValue().plusMinutes(lockoutDurationMinutes))
                );
            }
        }, 0, 3600000); // Run every hour
    }
}
