package com.cosmic.astrology.controller;

import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class TestController {
    
    @GetMapping("/test")
    public String test() {
        return "Backend is connected successfully!";
    }
    
    @GetMapping("/hello")
    public String hello() {
        return "Hello from Cosmic Astrology Backend!";
    }
}
