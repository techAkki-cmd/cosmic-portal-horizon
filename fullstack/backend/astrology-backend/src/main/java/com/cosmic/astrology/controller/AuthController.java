package com.cosmic.astrology.controller;

import com.cosmic.astrology.dto.*;
import com.cosmic.astrology.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult) {
        System.out.println("🔍 Login request received for username: " + loginRequest.getUsername());
        
        // Check for validation errors
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            System.out.println("❌ Validation errors: " + errors);
            return ResponseEntity.badRequest().body(new MessageResponse("Validation failed: " + errors));
        }
        
        try {
            JwtResponse response = authService.authenticateUser(loginRequest);
            System.out.println("✅ Login successful for username: " + loginRequest.getUsername());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("❌ Login failed for username: " + loginRequest.getUsername() + " - " + e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }
    
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest, BindingResult bindingResult) {
        System.out.println("📝 Signup request received for username: " + signUpRequest.getUsername() + 
                          ", email: " + signUpRequest.getEmail());
        
        // Check for validation errors
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            System.out.println("❌ Validation errors: " + errors);
            return ResponseEntity.badRequest().body(new MessageResponse("Validation failed: " + errors));
        }
        
        try {
            MessageResponse response = authService.registerUser(signUpRequest);
            System.out.println("✅ Signup successful for username: " + signUpRequest.getUsername());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("❌ Signup failed for username: " + signUpRequest.getUsername() + " - " + e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }
    
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String token) {
        try {
            String refreshedToken = authService.refreshToken(token.substring(7)); // Remove "Bearer "
            return ResponseEntity.ok(new JwtResponse(refreshedToken, null, null, null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Token refresh failed: " + e.getMessage()));
        }
    }
    
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        return ResponseEntity.ok(new MessageResponse("User logged out successfully!"));
    }
}
