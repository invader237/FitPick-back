package com.example.project.controller;

import com.example.project.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    // Endpoint pour obtenir la météo
    @GetMapping("/weather")
    public String getWeather() {
        return weatherService.getWeather();
    }
}
