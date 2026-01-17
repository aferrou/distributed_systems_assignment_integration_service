package gr.hua.dit.fittrack_integration.web.rest;

import gr.hua.dit.fittrack_integration.core.GeocodingService;
import gr.hua.dit.fittrack_integration.core.model.GeocodingResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST API for geocoding (postal code to coordinates).
 */
@RestController
@RequestMapping("/api/integration/geocoding")
public class GeocodingResource {

    private final GeocodingService geocodingService;

    public GeocodingResource(GeocodingService geocodingService) {
        this.geocodingService = geocodingService;
    }

    /**
     * Lookup location by postal code.
     *
     * Example: GET /api/integration/geocoding?postalCode=10431&country=GR
     */
    @GetMapping
    public ResponseEntity<GeocodingResult> geocode(
            @RequestParam String postalCode,
            @RequestParam(defaultValue = "GR") String country) {

        GeocodingResult result = geocodingService.searchByPostalCode(postalCode, country);

        if (result.found()) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }
}
