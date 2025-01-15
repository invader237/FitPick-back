package com.example.project.dto.ClothingReco;

public class WeatherRecoDTO {

    private int humidity;
    private int rain;
    private int temperature;
    private int wind;

    public WeatherRecoDTO(int humidity, int rain, int temperature, int wind) {
        this.humidity = humidity;
        this.rain = rain;
        this.temperature = temperature;
        this.wind = wind;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getRain() {
        return rain;
    }

    public void setRain(int rain) {
        this.rain = rain;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getWind() {
        return wind;
    }

    public void setWind(int wind) {
        this.wind = wind;
    }

    @Override
    public String toString() {
        return "WeatherRecoDTO{" +
                "humidity=" + humidity +
                ", rain=" + rain +
                ", temperature=" + temperature +
                ", wind=" + wind +
                '}';
    }

}
