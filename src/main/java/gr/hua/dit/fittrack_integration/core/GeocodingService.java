package gr.hua.dit.fittrack_integration.core;

import gr.hua.dit.fittrack_integration.core.model.GeocodingResult;

/**
 * Service for geocoding (converting postal codes/addresses to coordinates).
 */
public interface GeocodingService {

    /**
     * Search for location by postal code and country.
     *
     * @param postalCode the postal code (e.g., "10431")
     * @param country the country code (e.g., "GR")
     * @return geocoding result with coordinates
     */
    GeocodingResult searchByPostalCode(String postalCode, String country);
}
