package com.example.project.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

@Service
public class WeatherService {

    @Value("${openweathermap.api.key}")
    private String apiKey;

    public String getWeather(double lat, double lon) {
        String url = String.format(
            "https://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&units=metric&appid=%s",
            lat, lon, apiKey
        );

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, String.class);
    }

    public String getWeatherDisplayData(double lat, double lon) {
        // To do: Parse the JSON response and return only the data you want to display
        return getWeather(lat,lon);
    }
    
    
}
