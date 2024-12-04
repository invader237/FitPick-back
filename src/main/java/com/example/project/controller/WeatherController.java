package com.example.project.controller;

import com.example.project.service.WeatherService;
import com.example.project.model.WeatherDataDisplay;
import com.example.project.model.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    // Endpoint pour obtenir la météo
    @GetMapping("/weather")
    public WeatherResponse getWeather(@RequestParam double lat, @RequestParam double lon) {
        return weatherService.getWeather(lat, lon);
    }
    
    @GetMapping("/weather/display")
    public WeatherDataDisplay getWeatherDisplayData(@RequestParam double lat, @RequestParam double lon) {
        //return weatherService.getWeatherDisplayData(lat, lon);  
        return new WeatherDataDisplay(7.01, "few clouds", 81, 5.14);
    }
}
