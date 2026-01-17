package gr.hua.dit.fittrack_integration.core;

import gr.hua.dit.fittrack_integration.core.model.WeatherForecast;

public interface WeatherService {
    /**
     * Retrieve daily forecast for the given coordinates.
     * @param latitude  decimal latitude
     * @param longitude decimal longitude
     * @param days number of forecast days (max supported by API; you may limit)
     */
    WeatherForecast getDailyForecast(double latitude, double longitude, int days);
}

