package edu.sdsu.parking_backend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * MapService provides simple stubs for loading a map, highlighting a lot,
 * and navigating to a lot's gate. Replace the placeholder bodies with real
 * provider integrations when ready.
 */
@Service
public class MapService {

    // API key is injected from configuration; leave blank for local dev
    private final String apiKey;

    // Placeholder for map state / data returned by provider
    private Object mapData;

    public MapService(@Value("${maps.api-key:}") String apiKey) {
        this.apiKey = apiKey;
        this.loadMap();
    }

    /**
     * Initialize or refresh map state. In a real integration, call your maps provider here.
     */
    private void loadMap() {
        // Example: verify API key presence for prod-like environments
        if (!StringUtils.hasText(apiKey)) {
            // No API key provided; continue with mock data
            this.mapData = "MOCK_MAP_DATA";
        } else {
            // TODO: fetch/initialize mapData using apiKey
            this.mapData = "LIVE_MAP_DATA_INITIALIZED";
        }
    }

    /**
     * Accessor for current map data (mock/live).
     */
    public Object getMapData() {
        return this.mapData;
    }

    /**
     * Highlights a given parking lot on the map.
     * @param lot parking lot to highlight
     */
    public void highlightLot(ParkingLot lot) {
        if (lot == null) {
            throw new IllegalArgumentException("lot cannot be null");
        }
        // Placeholder: log/print to indicate which lot is being highlighted
        System.out.println("Highlighting " + lot.getLotName() + " on the map!");
        // TODO: integrate with maps SDK to draw marker/poly/overlay
    }

    /**
     * Simulates navigation instructions to a parking lot's gate.
     * @param lot parking lot to navigate to
     */
    public void navigateGate(ParkingLot lot) {
        if (lot == null) {
            throw new IllegalArgumentException("lot cannot be null");
        }
        // Placeholder: print a message indicating a route is generated
        System.out.println("Generating navigation route to " + lot.getLotName());
        // TODO: integrate with routing API to compute/display directions
    }
}
