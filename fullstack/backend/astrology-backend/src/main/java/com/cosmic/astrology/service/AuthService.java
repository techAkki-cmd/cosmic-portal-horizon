package com.cosmic.astrology.service;

import com.cosmic.astrology.dto.*;
import com.cosmic.astrology.entity.User;
import com.cosmic.astrology.entity.UserRole;
import com.cosmic.astrology.repository.UserRepository;
import com.cosmic.astrology.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;

@Service
public class AuthService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    // ✅ Create test user on startup
    @PostConstruct
    public void createTestUser() {
        try {
            if (!userRepository.existsByUsername("testuser")) {
                User testUser = new User();
                testUser.setUsername("testuser");
                testUser.setEmail("test@example.com");
                testUser.setPassword(encoder.encode("password123"));
                testUser.setFirstName("Test");
                testUser.setLastName("User");
                testUser.setRole(UserRole.CLIENT);
                testUser.setEnabled(true);
                
                userRepository.save(testUser);
                System.out.println("✅ Test user created successfully:");
                System.out.println("   Username: testuser");
                System.out.println("   Password: password123");
                System.out.println("   Email: test@example.com");
            } else {
                System.out.println("ℹ️ Test user already exists");
                
                // Verify test user details
                User existingUser = userRepository.findByUsername("testuser").orElse(null);
                if (existingUser != null) {
                    System.out.println("   Existing user details:");
                    System.out.println("   - Username: " + existingUser.getUsername());
                    System.out.println("   - Email: " + existingUser.getEmail());
                    System.out.println("   - Enabled: " + existingUser.isEnabled());
                    System.out.println("   - Role: " + existingUser.getRole());
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Failed to create/verify test user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        System.out.println("🔍 Authentication attempt for username: " + loginRequest.getUsername());
        
        try {
            // Check if user exists before authentication
            User user = userRepository.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> {
                        System.out.println("❌ User not found: " + loginRequest.getUsername());
                        return new RuntimeException("Bad credentials");
                    });
            
            System.out.println("✓ User found: " + user.getUsername() + " (enabled: " + user.isEnabled() + ")");
            
            if (!user.isEnabled()) {
                System.out.println("❌ User account is disabled: " + loginRequest.getUsername());
                throw new RuntimeException("User account is disabled");
            }

            // Attempt authentication
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(loginRequest.getUsername());

            // Update last login time
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);

            System.out.println("✅ Authentication successful for user: " + loginRequest.getUsername());
            return new JwtResponse(jwt, user.getUsername(), user.getEmail(), "Bearer");
            
        } catch (BadCredentialsException e) {
            System.out.println("❌ Bad credentials for username: " + loginRequest.getUsername());
            throw new RuntimeException("Bad credentials");
        } catch (Exception e) {
            System.out.println("❌ Authentication error: " + e.getMessage());
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        }
    }

    public MessageResponse registerUser(SignupRequest signUpRequest) {
        System.out.println("📝 Registration attempt for username: " + signUpRequest.getUsername() + 
                          ", email: " + signUpRequest.getEmail());
        
        // Validate required fields
        if (signUpRequest.getUsername() == null || signUpRequest.getUsername().trim().isEmpty()) {
            throw new RuntimeException("Username is required");
        }
        if (signUpRequest.getEmail() == null || signUpRequest.getEmail().trim().isEmpty()) {
            throw new RuntimeException("Email is required");
        }
        if (signUpRequest.getPassword() == null || signUpRequest.getPassword().trim().isEmpty()) {
            throw new RuntimeException("Password is required");
        }

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            System.out.println("❌ Username already exists: " + signUpRequest.getUsername());
            throw new RuntimeException("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            System.out.println("❌ Email already exists: " + signUpRequest.getEmail());
            throw new RuntimeException("Error: Email is already in use!");
        }

        try {
            // Create new user's account
            User user = new User();
            user.setUsername(signUpRequest.getUsername().trim());
            user.setEmail(signUpRequest.getEmail().trim());
            user.setPassword(encoder.encode(signUpRequest.getPassword()));
            user.setFirstName(signUpRequest.getFirstName());
            user.setLastName(signUpRequest.getLastName());
            user.setBirthDateTime(signUpRequest.getBirthDateTime());
            user.setBirthLocation(signUpRequest.getBirthLocation());
            user.setBirthLatitude(signUpRequest.getBirthLatitude());
            user.setBirthLongitude(signUpRequest.getBirthLongitude());
            user.setTimezone(signUpRequest.getTimezone());
            user.setRole(UserRole.CLIENT);
            user.setEnabled(true);

            User savedUser = userRepository.save(user);
            System.out.println("✅ User registered successfully: " + savedUser.getUsername() + 
                             " (ID: " + savedUser.getId() + ")");

            return new MessageResponse("User registered successfully!");
            
        } catch (Exception e) {
            System.out.println("❌ Registration failed: " + e.getMessage());
            throw new RuntimeException("Registration failed: " + e.getMessage());
        }
    }

    public String refreshToken(String token) {
        try {
            if (jwtUtils.validateJwtToken(token)) {
                String username = jwtUtils.getUserNameFromJwtToken(token);
                System.out.println("✅ Token refreshed for user: " + username);
                return jwtUtils.generateJwtToken(username);
            }
        } catch (Exception e) {
            System.out.println("❌ Token refresh failed: " + e.getMessage());
        }
        throw new RuntimeException("Invalid refresh token");
    }
}
