package gr.hua.dit.fittrack_integration.core.impl;

import com.fasterxml.jackson.databind.JsonNode;
import gr.hua.dit.fittrack_integration.config.OpenMeteoProperties;
import gr.hua.dit.fittrack_integration.core.WeatherService;
import gr.hua.dit.fittrack_integration.core.model.WeatherForecast;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OpenMeteoWeatherService implements WeatherService {

    private final WebClient webClient;
    private final OpenMeteoProperties props;

    public OpenMeteoWeatherService(WebClient openMeteoWebClient, OpenMeteoProperties props) {
        this.webClient = openMeteoWebClient;
        this.props = props;
    }

    @Override
    public WeatherForecast getDailyForecast(double latitude, double longitude, int days) {
        // Build comma-separated daily variables we care about
        String dailyVars = "temperature_2m_max,temperature_2m_min,precipitation_sum,weathercode";

        // Use WebClient synchronously (block) for simplicity in this integration service
        JsonNode root = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("latitude", latitude)
                        .queryParam("longitude", longitude)
                        .queryParam("daily", dailyVars)
                        .queryParam("forecast_days", days)
                        .queryParam("timezone", props.getTimezone())
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        if (root == null) {
            throw new RuntimeException("OpenMeteo returned empty response");
        }

        JsonNode daily = root.path("daily");
        JsonNode dates = daily.path("time");
        JsonNode tMax = daily.path("temperature_2m_max");
        JsonNode tMin = daily.path("temperature_2m_min");
        JsonNode precip = daily.path("precipitation_sum");
        JsonNode codes = daily.path("weathercode");

        List<WeatherForecast.Daily> list = new ArrayList<>();
        int len = dates.size();
        for (int i = 0; i < len; i++) {
            LocalDate date = LocalDate.parse(dates.get(i).asText());
            Double max = tMax.has(i) && !tMax.get(i).isNull() ? tMax.get(i).asDouble() : null;
            Double min = tMin.has(i) && !tMin.get(i).isNull() ? tMin.get(i).asDouble() : null;
            Double psum = precip.has(i) && !precip.get(i).isNull() ? precip.get(i).asDouble() : null;
            Integer code = codes.has(i) && !codes.get(i).isNull() ? codes.get(i).asInt() : null;

            WeatherForecast.Daily d = new WeatherForecast.Daily(date, min, max, psum, code);
            list.add(d);

        }

        return new WeatherForecast(latitude, longitude, list);
    }
}
