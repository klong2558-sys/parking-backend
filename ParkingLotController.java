package edu.sdsu.parking_backend;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController         // handle web req
@RequestMapping("/api") // every endpoint url start with "/api"

public class ParkingLotController 
{
    private final ParkingLotRepo parkingLotRepo;

    public ParkingLotController(ParkingLotRepo parkingLotRepo)
    {this.parkingLotRepo = parkingLotRepo;}

    // ENDPOINTS:

    @GetMapping("/lots")
    public List<ParkingLot> getAllLots()
    {return parkingLotRepo.findAll();} // get a list of all parking lots from the database

    @GetMapping("/lots/{id}")
    public ParkingLot getLotById(@PathVariable int id) 
    {return parkingLotRepo.findById(id).orElse(null);} // search for a parking lot with the specific id#
}
