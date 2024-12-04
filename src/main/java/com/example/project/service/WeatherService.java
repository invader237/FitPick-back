package com.example.project.service;

import com.example.project.model.WeatherResponse;
import com.example.project.model.WeatherDataDisplay;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

@Service
public class WeatherService {

    @Value("${openweathermap.api.key}")
    private String apiKey;

    public WeatherResponse getWeather(double lat, double lon) {
        String url = String.format(
            "https://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&units=metric&appid=%s",
            lat, lon, apiKey
        );

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, WeatherResponse.class);  
    }

    public WeatherDataDisplay getWeatherDisplayData(double lat, double lon) {
        WeatherResponse weatherResponse = getWeather(lat, lon);

        double temperature = weatherResponse.getMain().getTemp();
        String description = weatherResponse.getWeather()[0].getDescription();
        int humidity = weatherResponse.getMain().getHumidity();
        double windSpeed = weatherResponse.getWind().getSpeed();

        return new WeatherDataDisplay(temperature, description, humidity, windSpeed);
    }
}
