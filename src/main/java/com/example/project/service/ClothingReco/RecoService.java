package com.example.project.service.ClothingReco;

import com.example.project.dto.ClothingReco.WeatherRecoDTO;
import com.example.project.model.Weather.WeatherResponse;
import com.example.project.service.Weather.WeatherService;
import com.example.project.dto.clothingLib.ClothingDTO;
import org.springframework.stereotype.Service;
import com.example.project.repository.clothingLib.ClothingRepository;
import com.example.project.repository.clothingLib.TagRepository;
import com.example.project.model.clothingLib.Tag;
import com.example.project.model.clothingLib.Clothing;
import com.example.project.dto.clothingLib.TagDTO;
import com.example.project.dto.outfitLib.OutfitDTO;
import com.example.project.dto.outfitLib.OutfitDisplay;
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
    private WeatherService weatherService;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ClothingRepository clothingRepository;

    private WeatherRecoDTO weatherRecoData;

    private List<Long> tshirtTags = new ArrayList<>(List.of(9L)); 
    private List<Long> bottomTags = new ArrayList<>(List.of(10L)); 
    private List<Long> layerTags = new ArrayList<>(List.of(6L, 12L, 13L)); 

    private int minTemp = -30;
    private int maxTemp = 70;
    private int alignTemp = 20;
    private double CorrectionRate = 1.5;

    public void setTshirtTags(List<Long> tshirtTags) {
        this.tshirtTags = tshirtTags;
    }

    public void setBottomTags(List<Long> bottomTags) {
        this.bottomTags = bottomTags;
    }

    public void setLayerTags(List<Long> layerTags) {
        this.layerTags = layerTags;
    }

    public void getWeatherRecoData(WeatherResponse weather) {

        int humidity = weather.getMain().getHumidity(); 
        int rain = 0;//getRainIndex(weather.getRain().getOneHour()); 
        int temperature = getTempIndex(weather.getMain().getTemp()); 
        int wind = getWindIndex(weather.getWind().getSpeed()); 

        System.out.println("temp: " + temperature + ", rain: " + rain + ", wind: " + wind + ", humidity: " + humidity);

        this.weatherRecoData = new WeatherRecoDTO(humidity, rain, temperature, wind);
    }

    public int getTempIndex(double temperature) {
        int t = (int) Math.round(temperature); // Arrondi pour éviter une perte de précision
        double tIndex = 100 * (
            (Math.pow(t - alignTemp, 3) - Math.pow(minTemp - alignTemp, 3)) / 
            (Math.pow(maxTemp - alignTemp, 3) - Math.pow(minTemp - alignTemp, 3))
        );
        return (int) Math.round(tIndex);
    }

    public int getRainIndex(double rain) {
        return (int) (rain * 10);
    }

    public int getWindIndex(double wind) {
        return (int) (wind * 10);
    }

    public OutfitDTO getReco(Long userId) {
        // Exemple de données météorologiques simulées
        WeatherResponse weather = weatherService.getWeather(49.119308, 6.175715);

        getWeatherRecoData(weather); // Récupérer les données de recommandation météorologique

        List<ClothingDTO> outfit = new ArrayList<>();
        System.out.println("-------------------------------------");
        System.out.println("Début des recommandations");
        System.out.println("-------------------------------------");

        outfit.addAll(getTshirtRecommendation(userId));
        outfit.addAll(getBottomRecommendation(userId));
        outfit.addAll(getLayerRecommendation(userId));


        System.out.println("\n-------------------------------------");
        System.out.println("Recommandations complètes générées.");
        System.out.println("-------------------------------------");

        OutfitDTO recoOutfit = new OutfitDTO( null, "Recommandation météo", outfit);
        return recoOutfit;
    }

    // Étape 1 : Recommander un t-shirt
    private List<ClothingDTO> getTshirtRecommendation(Long userId) {
        System.out.println("\nÉtape 1 : Récupérer un t-shirt");
        // Récupérer les t-shirts
        List<Clothing> tshirts = clothingRepository.findByTagAndUserId(tshirtTags.get(0), userId);

        if (tshirts.isEmpty()) {
            System.out.println("Aucun t-shirt trouvé.");
            throw new RuntimeException("Aucun t-shirt trouvé.");
        } else {
            Clothing baseTshirt = tshirts.get(0);
            // Afficher le t-shirt recommandé
            System.out.println("T-shirt recommandé : " + baseTshirt.getClo_lib());
            return List.of(new ClothingDTO(
                baseTshirt.getClo_id(),
                baseTshirt.getClo_lib(),
                baseTshirt.getTags().stream()
                    .map(tag -> new TagDTO(tag.getTag_id(), tag.getTag_lib()))
                    .collect(Collectors.toList()),
                baseTshirt.getCloImageUrl()
            ));
        }
    }

    // Étape 2 : Recommander un pantalon
    private List<ClothingDTO> getBottomRecommendation(Long userId) {
        System.out.println("\nÉtape 2 : Récupérer un pantalon");

        // Récupérer les pantalons
        List<Clothing> bottoms = tagRepository.findByTemperature(
            weatherRecoData.getTemperature() - 100,
            weatherRecoData.getTemperature() + 100
        ).stream()
        .flatMap(tag -> clothingRepository.findByTagAndUserId(tag.getTag_id(), userId).stream())
        .filter(clothing -> clothing.getTags().stream().anyMatch(tag -> bottomTags.contains(tag.getTag_id())))
        .collect(Collectors.toList());

        if (bottoms.isEmpty()) {
            System.out.println("Aucun pantalon trouvé.");
            return new ArrayList<>();
        } else {
            Clothing bottom = bottoms.get(0);
            // Afficher le pantalon recommandé
            System.out.println("Pantalon recommandé : " + bottom.getClo_lib());
            return List.of(new ClothingDTO(
                bottom.getClo_id(),
                bottom.getClo_lib(),
                bottom.getTags().stream()
                    .map(tag -> new TagDTO(tag.getTag_id(), tag.getTag_lib()))
                    .collect(Collectors.toList()),
                bottom.getCloImageUrl()
            ));
        }
    }

    // Étape 3 : Ajouter des couches supérieures
    private List<ClothingDTO> getLayerRecommendation(Long userId) {
        System.out.println("\nÉtape 3 : Ajouter des couches supérieures");
        int layersNeeded = calculateLayers(weatherRecoData.getTemperature(), weatherRecoData.getWind(), weatherRecoData.getRain());

        // Récupérer les couches supérieures
        List<Clothing> layers = tagRepository.findByTemperature(
            weatherRecoData.getTemperature() - 100,
            weatherRecoData.getTemperature() + 100
        ).stream()
        .flatMap(tag -> clothingRepository.findByTagAndUserId(tag.getTag_id(), userId).stream())
        .filter(clothing -> clothing.getTags().stream().anyMatch(tag -> layerTags.contains(tag.getTag_id())))
        .limit(layersNeeded)
        .collect(Collectors.toList());

        if (layers.isEmpty()) {
            System.out.println("Aucune couche supérieure trouvée.");
            return new ArrayList<>();
        } else {
            List<ClothingDTO> layerRecommendations = layers.stream()
                .map(layer -> new ClothingDTO(
                    layer.getClo_id(),
                    layer.getClo_lib(),
                    layer.getTags().stream()
                        .map(tag -> new TagDTO(tag.getTag_id(), tag.getTag_lib()))
                        .collect(Collectors.toList()),
                    layer.getCloImageUrl()
                ))
                .collect(Collectors.toList());

            // Afficher les couches supérieures recommandées
            layerRecommendations.forEach(layer -> System.out.println("Couche supérieure ajoutée : " + layer.getCloLib()));
            return layerRecommendations;
        }
    }

    // Calculer le nombre de couches nécessaires
    private int calculateLayers(int temperature, int wind, int rain) {
        int baseLayers = (temperature < 20) ? 3 : (temperature < 40) ? 2 : (temperature < 50) ? 1 : 0;
        if (wind > 50) baseLayers += 1;
        if (rain > 5) baseLayers += 1;
        return baseLayers;
    }
}
