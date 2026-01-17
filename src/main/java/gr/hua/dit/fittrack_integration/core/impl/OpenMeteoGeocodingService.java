package gr.hua.dit.fittrack_integration.core.impl;

import com.fasterxml.jackson.databind.JsonNode;
import gr.hua.dit.fittrack_integration.core.GeocodingService;
import gr.hua.dit.fittrack_integration.core.model.GeocodingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Geocoding service using Open-Meteo Geocoding API.
 */
@Service
public class OpenMeteoGeocodingService implements GeocodingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenMeteoGeocodingService.class);

    private final WebClient geocodingWebClient;

    public OpenMeteoGeocodingService(WebClient geocodingWebClient) {
        this.geocodingWebClient = geocodingWebClient;
    }

    @Override
    public GeocodingResult searchByPostalCode(String postalCode, String country) {
        if (postalCode == null || postalCode.isBlank()) {
            return GeocodingResult.notFound("Postal code is required");
        }

        try {
            // Open-Meteo Geocoding API: https://geocoding-api.open-meteo.com/v1/search
            JsonNode root = geocodingWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("name", postalCode)
                            .queryParam("count", 5)
                            .queryParam("language", "el")
                            .queryParam("format", "json")
                            .build())
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            if (root == null || !root.has("results") || root.path("results").isEmpty()) {
                LOGGER.warn("No results found for location: {}", postalCode);
                return GeocodingResult.notFound("Δεν βρέθηκε τοποθεσία με αυτό το όνομα");
            }

            // Find first result from Greece (GR)
            JsonNode results = root.path("results");
            JsonNode selectedResult = null;

            for (JsonNode result : results) {
                String countryCode = result.path("country_code").asText("");
                if ("GR".equalsIgnoreCase(countryCode)) {
                    selectedResult = result;
                    break;
                }
            }

            // If no Greek result found, use the first result
            if (selectedResult == null) {
                selectedResult = results.get(0);
            }

            String name = selectedResult.path("name").asText();
            double latitude = selectedResult.path("latitude").asDouble();
            double longitude = selectedResult.path("longitude").asDouble();
            String countryCode = selectedResult.path("country_code").asText();

            LOGGER.info("Geocoding success: {} -> {}, {} ({}, {})", postalCode, name, countryCode, latitude, longitude);

            return GeocodingResult.success(name, latitude, longitude, countryCode);

        } catch (Exception e) {
            LOGGER.error("Geocoding error for postal code {}: {}", postalCode, e.getMessage());
            return GeocodingResult.notFound("Σφάλμα κατά την αναζήτηση: " + e.getMessage());
        }
    }
}
