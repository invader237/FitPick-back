package com.example.project.controller.ClothingReco;

import com.example.project.dto.ClothingReco.WeatherRecoDTO;
import com.example.project.model.Weather.WeatherResponse;
import com.example.project.service.ClothingReco.RecoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reco")
@CrossOrigin(origins = "http://localhost:3000")
public class RecoController {

    @Autowired
    private RecoService recoService;

    @GetMapping("/{userId}")
    public void getReco(@PathVariable Long userId) {
        recoService.getReco(userId);
    }

    /**
     * Endpoint to retrieve weather recommendation data.
     * 
     * @param weather An instance of {@link WeatherResponse} containing weather data.
     */
    @GetMapping("/weather")
    public void getWeatherReco() {
        WeatherResponse weather = new WeatherResponse() {{
            setMain(new Main() {{
                setHumidity(100);
                setTemp(0.82);
            }});
            setRain(new Rain() {{
                setOneHour(0.0);
            }});
            setWind(new Wind() {{
                setSpeed(5.14);
            }});
        }};

        recoService.getWeatherRecoData(weather);
    }
}
