package com.example.project.controller;

import com.example.project.service.WeatherService;
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
    public String getWeather(@RequestParam double lat, @RequestParam double lon) {
        return weatherService.getWeather(lat, lon);
    }
    
    @GetMapping("/weather/display")
    public String getWeatherDisplayData(@RequestParam double lat, @RequestParam double lon) {
        return weatherService.getWeatherDisplayData(lat,lon);   
    }  
}
