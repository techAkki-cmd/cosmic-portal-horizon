package com.cosmic.astrology.controller;

import com.cosmic.astrology.entity.User;
import com.cosmic.astrology.dto.UserProfileRequest;
import com.cosmic.astrology.dto.BirthData;
import com.cosmic.astrology.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
@PreAuthorize("hasRole('CLIENT') or hasRole('ASTROLOGER')")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(Principal principal) {
        try {
            User user = userService.findByUsername(principal.getName());
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching profile: " + e.getMessage());
        }
    }
    
    @PutMapping("/profile")
    public ResponseEntity<?> updateUserProfile(
            @RequestBody UserProfileRequest request, 
            Principal principal) {
        try {
            User updatedUser = userService.updateProfile(principal.getName(), request);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating profile: " + e.getMessage());
        }
    }
    
    @GetMapping("/birth-data")
    public ResponseEntity<?> getBirthData(Principal principal) {
        try {
            User user = userService.findByUsername(principal.getName());
            BirthData birthData = new BirthData(
                user.getBirthDateTime(),
                user.getBirthLocation(),
                user.getBirthLatitude(),
                user.getBirthLongitude(),
                user.getTimezone()
            );
            return ResponseEntity.ok(birthData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching birth data: " + e.getMessage());
        }
    }
    
    @PostMapping("/birth-data")
    public ResponseEntity<?> updateBirthData(
            @RequestBody BirthData birthData, 
            Principal principal) {
        try {
            User updatedUser = userService.updateBirthData(principal.getName(), birthData);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating birth data: " + e.getMessage());
        }
    }
}
