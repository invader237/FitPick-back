package com.example.project.model;

/**
 * Represents simplified weather data for display purposes.
 */
public class WeatherDataDisplay {
    private double temperature;   // Température actuelle
    private String main;          // Contiendra "main" de l'API OpenWeatherMap (ex: "Rain")
    private int humidity;         // Niveau d'humidité
    private double windSpeed;     // Vitesse du vent

    // Constructeurs
    public WeatherDataDisplay() {
    }

    public WeatherDataDisplay(double temperature, String main, int humidity, double windSpeed) {
        this.temperature = temperature;
        this.main = main;  // Utilisation de "main" au lieu de "description"
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

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
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
