package com.cosmic.astrology.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;
    
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;
    
    @NotBlank
    @Size(min = 6, max = 40)
    private String password;
    
    private String firstName;
    private String lastName;
    
    // Birth data for astrology
    private LocalDateTime birthDateTime;
    private String birthLocation;
    private Double birthLatitude;
    private Double birthLongitude;
    private String timezone;



    public String getUsername() { return username; }
    public void   setUsername(String username) { this.username = username; }

    public String getEmail()    { return email; }
    public void   setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void   setPassword(String password) { this.password = password; }
}

