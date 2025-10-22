package edu.sdsu.parking_backend;

import org.springframework.stereotype.Service;

@Service
public class MapService 
{
    // Attributes
    private String apiKey = "yourAPIKEYhere";
    private Object mapdata;

    // Constructor
    public MapService()
    {
        this.loadMap();
    }

    // Load map data w/ placeholder logic
    private void loadMap()
    {
        // LATER: replace with real map-loading logic using apiKey (i.e., HTTP request to a maps API)
        this.mapdata = "Map data for SDSU loaded successfully!";
        System.out.println(this.mapdata);
    }

    /*
     * Simulates highlighting a specific parking lot on the map 
     * @param lot : The parkinglot obj to be highlighted
     */
    public void highlightLot(ParkingLot lot)
    {
        // Place holder logic: print msg to indicate which lot is being highlighted
        System.out.println("Highlighting " + lot.getLotName() + " on the map!");
    }

    /* 
     * simulates navigation instructions to parking lot
     * @param lot : the arkinglot obj to navi to 
     */
    public void navigateGate(ParkingLot lot)
    {
         // Placeholder logic to stimulate gen a route
        System.out.println("Generating navigation route to " + lot.getLotName());
    }
}
