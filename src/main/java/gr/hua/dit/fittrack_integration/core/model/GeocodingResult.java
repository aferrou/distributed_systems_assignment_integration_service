package gr.hua.dit.fittrack_integration.core.model;

/**
 * Result of geocoding lookup.
 */
public record GeocodingResult(
        boolean found,
        String name,
        Double latitude,
        Double longitude,
        String country,
        String error
) {
    public static GeocodingResult notFound(String error) {
        return new GeocodingResult(false, null, null, null, null, error);
    }

    public static GeocodingResult success(String name, double latitude, double longitude, String country) {
        return new GeocodingResult(true, name, latitude, longitude, country, null);
    }
}
