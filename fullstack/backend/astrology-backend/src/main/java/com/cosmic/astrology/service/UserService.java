package com.cosmic.astrology.service;

import com.cosmic.astrology.entity.User;
import com.cosmic.astrology.dto.UserProfileRequest;
import com.cosmic.astrology.dto.BirthData;
import com.cosmic.astrology.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }
    
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
    }
    
    public User updateProfile(String username, UserProfileRequest request) {
        User user = findByUsername(username);
        
        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            // Verify email is not already taken
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new RuntimeException("Email is already in use!");
            }
            user.setEmail(request.getEmail());
        }
        
        return userRepository.save(user);
    }
    
    public User updateBirthData(String username, BirthData birthData) {
        User user = findByUsername(username);
        
        user.setBirthDateTime(birthData.getBirthDateTime());
        user.setBirthLocation(birthData.getBirthLocation());
        user.setBirthLatitude(birthData.getBirthLatitude());
        user.setBirthLongitude(birthData.getBirthLongitude());
        user.setTimezone(birthData.getTimezone());
        
        return userRepository.save(user);
    }
    
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
