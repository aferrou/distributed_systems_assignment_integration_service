package gr.hua.dit.fittrack_integration.web.rest;

import gr.hua.dit.fittrack_integration.core.WeatherService;
import gr.hua.dit.fittrack_integration.core.model.WeatherForecast;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class WeatherResource {

    private final WeatherService weatherService;

    public WeatherResource(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    /**
     * Original endpoint for compatibility.
     */
    @GetMapping("/integration/weather")
    public ResponseEntity<WeatherForecast> forecast(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam(defaultValue = "4") int days) {

        WeatherForecast wf = weatherService.getDailyForecast(lat, lon, days);
        return ResponseEntity.ok(wf);
    }

    /**
     * FitTrack backend compatible endpoint.
     * Returns weather for a specific date.
     */
    @GetMapping("/v1/weather/forecast")
    public ResponseEntity<Map<String, Object>> getForecastForDate(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam String date) {

        LocalDate targetDate = LocalDate.parse(date);

        // Get forecast for next 7 days to ensure we have the target date
        WeatherForecast forecast = weatherService.getDailyForecast(latitude, longitude, 7);

        // Find the specific day
        WeatherForecast.Daily targetDay = forecast.getDays().stream()
                .filter(day -> day.getDate().equals(targetDate))
                .findFirst()
                .orElse(null);

        Map<String, Object> response = new HashMap<>();

        if (targetDay != null) {
            response.put("date", targetDay.getDate());
            response.put("temperature_max", targetDay.getTempMax());
            response.put("temperature_min", targetDay.getTempMin());
            response.put("precipitation_sum", targetDay.getPrecipitationSum());
            response.put("weather_description", getWeatherDescription(targetDay.getWeatherCode()));
        } else {
            // Fallback
            response.put("date", targetDate);
            response.put("temperature_max", 20.0);
            response.put("temperature_min", 15.0);
            response.put("precipitation_sum", 0.0);
            response.put("weather_description", "Δεν υπάρχουν διαθέσιμα δεδομένα για αυτή την ημερομηνία");
        }

        return ResponseEntity.ok(response);
    }

    private String getWeatherDescription(Integer weatherCode) {
        if (weatherCode == null) return "Άγνωστος";

        // WMO Weather codes: https://open-meteo.com/en/docs
        return switch (weatherCode) {
            case 0 -> "Αίθριος ουρανός";
            case 1, 2, 3 -> "Κυρίως αίθριος";
            case 45, 48 -> "Ομίχλη";
            case 51, 53, 55 -> "Ψιλόβροχο";
            case 61, 63, 65 -> "Βροχή";
            case 71, 73, 75 -> "Χιονόπτωση";
            case 80, 81, 82 -> "Καταιγίδες";
            case 95, 96, 99 -> "Ισχυρές καταιγίδες";
            default -> "Διάφορες καιρικές συνθήκες";
        };
    }
}
