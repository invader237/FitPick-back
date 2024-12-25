package com.example.project.service.ClothingReco;

import com.example.project.dto.ClothingReco.WeatherRecoDTO;
import com.example.project.model.Weather.WeatherResponse;
import com.example.project.dto.clothingLib.ClothingDTO;
import org.springframework.stereotype.Service;
import com.example.project.repository.clothingLib.ClothingRepository;
import com.example.project.repository.clothingLib.TagRepository;
import com.example.project.model.clothingLib.Tag;
import com.example.project.model.clothingLib.Clothing;
import com.example.project.dto.clothingLib.TagDTO;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;

import com.example.project.model.Weather.WeatherResponse.Main;
import com.example.project.model.Weather.WeatherResponse.Rain;
import com.example.project.model.Weather.WeatherResponse.Wind;

import java.util.stream.Collectors;

import java.util.List;

@Service
public class RecoService {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ClothingRepository clothingRepository;

    private WeatherRecoDTO weatherRecoData;

    public void getWeatherRecoData(WeatherResponse weather) {
        int humidity = weather.getMain().getHumidity(); 
        int rain = getRainIndex(weather.getRain().getOneHour()); 
        int temperature = getTempIndex(weather.getMain().getTemp()); 
        int wind = getWindIndex(weather.getWind().getSpeed()); 

        System.out.println("temperature: " + temperature);

        this.weatherRecoData = new WeatherRecoDTO(humidity, rain, temperature, wind);
    }

    public int getTempIndex(double temperature) {
        return (int) (((int) temperature + 30) * (70 / 100.0));
    }

    public int getRainIndex(double rain) {
        return (int) (rain * 10);
    }

    public int getWindIndex(double wind) {
        return (int) (wind * 10);
    }

    public List<ClothingDTO> getReco(Long userId) {
        // Exemple de données météorologiques simulées
        WeatherResponse weather = new WeatherResponse();
        weather.setMain(new Main() {{
            setHumidity(100);
            setTemp(0);
            //41 pour t-shirt
        }});
        weather.setRain(new Rain() {{
            setOneHour(0.0);
        }});
        weather.setWind(new Wind() {{
            setSpeed(5.14);
        }});

        getWeatherRecoData(weather); // Récupérer les données de recommandation météorologique

        System.out.println("Début des recommandations");

        // Récupérer les tags en fonction de la température
        List<Tag> tags = tagRepository.findByTemperature(weatherRecoData.getTemperature() - 10, weatherRecoData.getTemperature() + 10);

        List<ClothingDTO> clothes = new ArrayList<>(); // Initialisation correcte de la liste

        for (Tag t : tags) {
            System.out.println("ID: " + t.getTag_id() + ", Name: " + t.getTag_lib());
            
            // Ajouter les vêtements dans la liste 'clothes'
            List<Clothing> clothingList = clothingRepository.findByTagAndUserId(t.getTag_id(), userId);
            for (Clothing clothing : clothingList) {
                ClothingDTO clothingDTO = new ClothingDTO(
                    clothing.getClo_id(),
                    clothing.getClo_lib(),
                    clothing.getTags().stream()
                        .map(tag -> new TagDTO(tag.getTag_id(), tag.getTag_lib()))
                        .collect(Collectors.toList())
                );
                clothes.add(clothingDTO);
            }
        }

        // Afficher les vêtements dans la console pour le débogage
        for (ClothingDTO c : clothes) {
            System.out.println(c.toString());
        }

        return clothes; // Retourner les vêtements recommandés
    }
}
