package com.example.project.service;

import com.example.project.model.WeatherResponse;
import com.example.project.model.WeatherDataDisplay;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

/**
 * Service class that handles the communication with the OpenWeatherMap API to retrieve weather data.
 * Provides methods to fetch weather data and return it in a simplified format for display purposes.
 */
@Service
public class WeatherService {

    /**
     * The API key required to authenticate requests to the OpenWeatherMap API.
     * The value is injected from the application's properties file.
     */
    @Value("${openweathermap.api.key}")
    private String apiKey;

    /**
     * Fetches the current weather data from the OpenWeatherMap API for the given coordinates.
     *
     * @param lat The latitude of the location.
     * @param lon The longitude of the location.
     * @return A {@link WeatherResponse} object containing the weather data.
     */
    public WeatherResponse getWeather(double lat, double lon) {
        // Construct the API request URL using the provided latitude, longitude, and API key
        String url = String.format(
            "https://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&units=metric&appid=%s",
            lat, lon, apiKey
        );

        // Create a RestTemplate instance to make the HTTP request
        RestTemplate restTemplate = new RestTemplate();
        
        // Make the request and map the response to a WeatherResponse object
        return restTemplate.getForObject(url, WeatherResponse.class);  
    }

    /**
     * Retrieves simplified weather data suitable for display purposes.
     * This method calls {@link #getWeather(double, double)} to get the raw weather data
     * and then extracts the necessary details to populate a {@link WeatherDataDisplay} object.
     *
     * @param lat The latitude of the location.
     * @param lon The longitude of the location.
     * @return A {@link WeatherDataDisplay} object containing temperature, weather description,
     *         humidity, and wind speed.
     */
    public WeatherDataDisplay getWeatherDisplayData(double lat, double lon) {
        // Get the weather data response
        WeatherResponse weatherResponse = getWeather(lat, lon);

        // Extract relevant information from the response
        double temperature = weatherResponse.getMain().getTemp();
        String description = weatherResponse.getWeather()[0].getDescription();
        int humidity = weatherResponse.getMain().getHumidity();
        double windSpeed = weatherResponse.getWind().getSpeed();

        // Return a new WeatherDataDisplay object with the extracted data
        return new WeatherDataDisplay(temperature, description, humidity, windSpeed);
    }
}
