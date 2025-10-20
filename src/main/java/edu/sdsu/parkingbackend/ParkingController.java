package edu.sdsu.parkingbackend;

import org.springframework.web.bind.annotation.*;
import java.util.Collection;

public class ParkingController {

    private final ParkingLotService lotService;

    public ParkingController(ParkingLotService lotService) {
        this.lotService = lotService;
    }

    @GetMapping("/lots")
    public Collection<ParkingLot> allLots() {
        return lotService.findAll();
    }

    @GetMapping("/lots/{id}")
    public ParkingLot oneLot(@PathVariable int id) {
        return lotService.findById(id);
    }

    // Update occupied spaces; flips FULL/NOT FULL and records a timestamp
    @PostMapping("/lots/{id}/occupied")
    public java.util.Map<String, Object> updateOccupied(@PathVariable int id, @RequestParam int occupied) {
        boolean ok = lotService.updateOccupied(id, occupied);
        return java.util.Map.of("ok", ok);
    }

}
