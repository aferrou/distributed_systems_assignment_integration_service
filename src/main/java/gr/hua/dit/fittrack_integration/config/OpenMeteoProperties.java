package gr.hua.dit.fittrack_integration.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "open-meteo")
public class OpenMeteoProperties {
    private String baseUrl = "https://api.open-meteo.com/v1/forecast";
    private String geocodingUrl = "https://geocoding-api.open-meteo.com/v1/search";
    private String timezone = "Europe/Athens";

    public String getBaseUrl() { return baseUrl; }
    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }

    public String getGeocodingUrl() { return geocodingUrl; }
    public void setGeocodingUrl(String geocodingUrl) { this.geocodingUrl = geocodingUrl; }

    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }
}