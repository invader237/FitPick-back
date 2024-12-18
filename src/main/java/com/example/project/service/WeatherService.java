package com.example.project.service;

import com.example.project.model.WeatherResponse;
import com.example.project.model.WeatherDataDisplay;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

/**
 * Service class that handles communication with OpenWeatherMap API to retrieve weather data.
 */
@Service
public class WeatherService {

    @Value("${openweathermap.api.key}")
    private String apiKey;

    /**
     * Fetches the current weather data from OpenWeatherMap API for the given coordinates.
     */
    public WeatherResponse getWeather(double lat, double lon) {
        String url = String.format(
            "https://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&units=metric&appid=%s",
            lat, lon, apiKey
        );

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, WeatherResponse.class);  
    }

    /**
     * Retrieves simplified weather data for display purposes.
     */
    public WeatherDataDisplay getWeatherDisplayData(double lat, double lon) {
        WeatherResponse weatherResponse = getWeather(lat, lon);

        double temperature = weatherResponse.getMain().getTemp();
        String main = weatherResponse.getWeather()[0].getMain();  // Utilisation de "main"
        int humidity = weatherResponse.getMain().getHumidity();
        double windSpeed = weatherResponse.getWind().getSpeed();
        System.out.println(main);
        return new WeatherDataDisplay(temperature, main, humidity, windSpeed);
    }
}
