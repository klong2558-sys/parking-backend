package edu.sdsu.parking_backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Collection;

@RestController
@RequestMapping("/api") // sets the base url for all endpoints
public class ParkingController {

    private final ParkingLotService lotService;
    private static final Logger log = LoggerFactory.getLogger(ParkingController.class);

    public ParkingController(ParkingLotService lotService, ParkingLotRepo parkingLotRepo) 
    {this.lotService = lotService;}

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

    //new home for the "viewLots" logic 
    @GetMapping("/lots/studentview")
    public Collection<ParkingLot> viewLotsForStudent() {
        // Here, we'd also get the logged-in student's ID from security
        log.info("Student is viewing all lots...");
        return lotService.findAll();
    }

    @PostMapping("/lots/{id}/report")
    public ResponseEntity<?> updateLotStatus(@PathVariable int id, @RequestBody Report newReport) {
        // Here, we would get the logged-in student's info
        // and attach it to the new report.
        
        // This is a simplified version of your teammate's logic
        boolean success = lotService.updateOccupied(id, newReport.getReportedOccupanacy());
        
        if (!success) {
            log.warn("Student update for lot {} failed.", id);
            return ResponseEntity.badRequest().body("Update failed.");
        }

        log.info("Student updated Lot {}", id);
        return ResponseEntity.ok().build();
    }

}

