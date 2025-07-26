package com.cosmic.astrology.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String username;
    private String email;
    
    public JwtResponse(String accessToken, String username, String email, String type) {
        this.token = accessToken;
        this.username = username;
        this.email = email;
        if (type != null) {
            this.type = type;
        }
    }
}
