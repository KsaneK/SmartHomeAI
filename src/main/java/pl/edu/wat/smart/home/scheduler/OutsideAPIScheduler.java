package pl.edu.wat.smart.home.scheduler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pl.edu.wat.smart.home.entity.AirQualityStatus;
import pl.edu.wat.smart.home.entity.WeatherStatus;
import pl.edu.wat.smart.home.repository.AirQualityStatusRepo;
import pl.edu.wat.smart.home.repository.WeatherStatusRepo;

import java.io.IOException;
import java.util.Date;

@Component
@EnableScheduling
public class OutsideAPIScheduler {

    private static final long WEATHER_UPDATE_RATE = 15*60*1000;
    private static final long AIR_QUALITY_UPDATE_RATE = 15*60*1000;
    private static final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather?q=Warsaw,pl&appid=" + "58c9714d666a67296e85053ae157eaf4&units=metric&lang=en";
    private static final String AIR_QUALITY_URL = "http://api.waqi.info/feed/here/?token=" + "9dd8927814e49c73d5091db03b7363fe678195ba";

    @Autowired
    AirQualityStatusRepo airQualityStatusRepo;

    @Autowired
    WeatherStatusRepo weatherStatusRepo;

    @Scheduled(fixedDelay = WEATHER_UPDATE_RATE)
    private void updateWeather() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(WEATHER_URL, String.class);

        if(response.getStatusCode() != HttpStatus.OK)
            return;

        try {
            parseWeather(response.getBody());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseWeather(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);

        String description = root.path("weather").path(0).path("description").asText();
        String city = root.path("name").asText();
        double temp = root.path("main").path("temp").asDouble();
        double windSpeed = root.path("wind").path("speed").asDouble();

        WeatherStatus status = new WeatherStatus();
        status.setDescription(description);
        status.setCity(city);
        status.setTemp(temp);
        status.setWindSpeed(windSpeed);
        status.setTime(new Date());

        weatherStatusRepo.save(status);
    }

    @Scheduled(fixedDelay = AIR_QUALITY_UPDATE_RATE)
    private void updateAirQuality() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(AIR_QUALITY_URL, String.class);

        if(response.getStatusCode() != HttpStatus.OK)
            return;

        try {
            parseAirQuality(response.getBody());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseAirQuality(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);

        double aqi = root.path("data").path("aqi").asDouble();
        String city = root.path("data").path("city").path("name").asText();

        AirQualityStatus status = new AirQualityStatus();
        status.setAqi(aqi);
        status.setCity(city);
        status.setTime(new Date());

        airQualityStatusRepo.save(status);
    }
}
