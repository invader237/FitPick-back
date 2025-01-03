package com.example.project.controller.Weather;

import com.example.project.service.Weather.WeatherService;
import com.example.project.model.Weather.WeatherDataDisplay;
import com.example.project.model.Weather.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/weather")
@CrossOrigin(origins = "http://localhost:3000") 
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    /**
     * Endpoint to retrieve full weather data.
     * 
     * @param lat Latitude of the geographical location.
     * @param lon Longitude of the geographical location.
     * @return An instance of {@link WeatherResponse} containing complete weather data
     *         for the specified coordinates.
     */
    @GetMapping("/")
    public WeatherResponse getWeather(@RequestParam double lat, @RequestParam double lon) {
        return weatherService.getWeather(lat, lon);
    }

    /**
     * Endpoint to retrieve simplified weather data for display purposes.
     * 
     * @param lat Latitude of the geographical location.
     * @param lon Longitude of the geographical location.
     * @return An instance of {@link WeatherDataDisplay} containing simplified weather data
     *         for the specified coordinates.
     */
    @GetMapping("/display")
    public WeatherDataDisplay getWeatherDisplayData(@RequestParam double lat, @RequestParam double lon) {
        //return new WeatherDataDisplay(7.01, "few clouds", 81, 5.14);
        return weatherService.getWeatherDisplayData(lat, lon);  
    }
}
