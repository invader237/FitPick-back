package com.example.project.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

@Service
public class WeatherService {

    // Clé API OpenWeatherMap (tu peux la stocker dans application.properties pour la sécurité)
    @Value("92789fe44817b705760ab8ee7e2672c0")
    private String apiKey;

    // URL de l'API OpenWeatherMap pour la météo actuelle
    private final String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=Metz&units=metric&appid=";

    public String getWeather() {
        String url = apiUrl + apiKey;
        
        // Utilisation de RestTemplate pour envoyer la requête GET
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        
        return response; // Retourne la réponse JSON de l'API
    }
}
