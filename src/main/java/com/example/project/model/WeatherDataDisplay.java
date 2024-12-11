package com.example.project.model;

/**
 * Represents simplified weather data for display purposes.
 *
 * Attributes:
 * - `temperature`: The current temperature in degrees Celsius.
 * - `description`: A short textual description of the weather conditions (e.g., "clear sky").
 * - `humidity`: The humidity level as a percentage.
 * - `windSpeed`: The wind speed measured in meters per second.
 */
public class WeatherDataDisplay {
    private double temperature;
    private String description;
    private int humidity;
    private double windSpeed;

    /**
     * Default constructor for the {@code WeatherDataDisplay} class.
     * Initializes an empty weather data object.
     */
    public WeatherDataDisplay() {
    }

    /**
     * Constructs a {@code WeatherDataDisplay} object with specified weather details.
     *
     * @param temperature The temperature in degrees Celsius.
     * @param description A short description of the weather conditions.
     * @param humidity The humidity level as a percentage.
     * @param windSpeed The wind speed in meters per second.
     */
    public WeatherDataDisplay(double temperature, String description, int humidity, double windSpeed) {
        this.temperature = temperature;
        this.description = description;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
    }

    // Getters et Setters
    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }
}
